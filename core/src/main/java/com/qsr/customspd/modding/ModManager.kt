package com.qsr.customspd.modding

import com.badlogic.gdx.Files
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.qsr.customspd.utils.Archiver
import com.qsr.customspd.utils.Network
import com.watabou.noosa.Game
import com.watabou.utils.Bundle
import com.watabou.utils.FileUtils
import io.ktor.client.request.get
import io.ktor.client.statement.readBytes
import io.ktor.http.appendPathSegments
import java.io.File
import java.net.UnknownHostException
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

object ModManager {

    const val STORAGE = "MODS"

    private const val MOD_INFO_FILE = "mod_info.json"

    private const val ENABLED_MODS = "enabled_mods"
    private const val ENABLED_MODS_FILE = "$ENABLED_MODS.dat"

    private val SLASH = File.separatorChar

    private const val SUMMARY_ZIP = "Summary.zip"
    private val SUMMARY_PATH = "${SLASH}Summary"
    private val MOD_LIST_PATH = "$SUMMARY_PATH${SLASH}mod_list.json"
    private val MOD_INFO_PATH = "$SUMMARY_PATH${SLASH}%s.json"
    private val MOD_ICON_PATH = "$SUMMARY_PATH${SLASH}%s.png"
    private val MOD_PREVIEW_PATH = "$SUMMARY_PATH${SLASH}%s %d.png"

    private const val MARKETPLACE = "https://d5dsgaub4mu915q7vd3p.apigw.yandexcloud.net"

    private val _marketplaceMods: MutableStateFlow<List<MarketplaceMod>> = MutableStateFlow(emptyList())
    val marketplaceMods: StateFlow<List<MarketplaceMod>> = _marketplaceMods

    private val _downloadedMods: MutableStateFlow<Set<String>> = MutableStateFlow(emptySet())
    val downloadedMods: StateFlow<Set<String>> = _downloadedMods

    fun getModdedAssetFilePath(assetPath: String): String {
        lazyEnabledModNames.forEach {
            val path = "$SLASH$it$SLASH${assetPath.replace('/', SLASH)}"
            if (getFileHandle(path).exists()) return "$STORAGE$SLASH$path"
        }
        return assetPath
    }

    fun getAllModdedAssetFilePaths(assetPath: String): List<String> = lazyEnabledModNames.mapNotNull {
        val path = "$SLASH$it$SLASH${assetPath.replace('/', SLASH)}"
        if (getFileHandle(path).exists()) "$STORAGE$SLASH$path"
        else null
    }

    fun getAssetFileHandle(assetPath: String): FileHandle {
        val internal = Gdx.files.internal(assetPath)
        if (internal.exists()) return internal

        val modded = FileUtils.getFileHandle(Files.FileType.External, FileUtils.defaultPath, assetPath)
        if (modded.exists()) return modded

        return FileUtils.getFileHandle(assetPath)
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun receiveMarketplaceInfo() = GlobalScope.launch {

        if (_marketplaceMods.value.isNotEmpty()) return@launch

        Archiver.unzip(try {
            Network.access {
                get(MARKETPLACE) {
                    url { appendPathSegments(SUMMARY_ZIP) }
                }.readBytes()
            }
        } catch (e: UnknownHostException) {
            return@launch
        }, storage.file().absolutePath)

        delay(100)

        Json.decodeFromString<List<String>>(
            getFileHandle(MOD_LIST_PATH).readString()
        ).map { name ->
            MarketplaceMod(
                Json.decodeFromString(getFileHandle(MOD_INFO_PATH.format(name)).readString()),
                getFileHandle(MOD_ICON_PATH.format(name)).readBytes(),
                (1..5).mapNotNull {
                    with(getFileHandle(MOD_PREVIEW_PATH.format(name, it))) {
                        if (exists()) readBytes()
                        else null
                    }
                },
            )
        }
            .filter { it.info.minCpdVersion <= Game.versionCode }
            .also {
                getFileHandle(SUMMARY_PATH).deleteDirectory()
                _marketplaceMods.emit(it)
            }
    }

    private fun getFileHandle(path: String): FileHandle =
        FileUtils.getFileHandle(Files.FileType.Absolute, storage.file().absolutePath + path)

    @OptIn(DelicateCoroutinesApi::class)
    fun download(modName: String) = GlobalScope.launch {
        Archiver.unzip(try {
            Network.access {
                get(MARKETPLACE) {
                    url { appendPathSegments("$modName.zip") }
                }.readBytes()
            }
        } catch (e: UnknownHostException) {
            return@launch
        }, storage.file().absolutePath)
        _downloadedMods.value += modName
    }

    fun updateMarketplaceMods() = marketplaceMods.value.forEach {
        if (it.info.name in getInstalledModNames() &&
            it.info.version > (getInstalledMods().find { m ->
                m.info.name == it.info.name
            }?.info?.version ?: 0)) {
            delete(it.info.name)
            download(it.info.name)
        }
    }

    fun getInstalledMods(): List<Mod> = storage.list().mapNotNull { modFolder ->
        modFolder.child(MOD_INFO_FILE).takeIf { it.exists() }?.let {
            try {
                Mod(modFolder, Json.decodeFromString(it.readString()))
            } catch (e: SerializationException) {
                null
            }
        }
    }.filter { it.info.minCpdVersion <= Game.versionCode }.sortedBy { it.info.name }

    fun getInstalledModNames(): List<String> = storage.list().map { it.name() }.sortedBy { it }

    fun delete(modName: String) {
        storage.child(modName).deleteDirectory()
        _downloadedMods.value -= modName
    }

    fun getEnabledMods(): List<Mod> = getEnabledModsWithoutSaving().also { saveToEnabled(it) }

    fun getEnabledGameplayModNames(): List<String> = getEnabledModsWithoutSaving().filter { it.info.gameplayMod }.map { it.info.name }

    fun isEnabled(mod: Mod): Boolean = mod.info.name in lazyEnabledModNames.map { it }

    fun isEnabled(mod: String): Boolean = mod in lazyEnabledModNames.map { it }

    fun enableMod(modName: String) = saveToEnabled(arrayOf(modName) + enabledModNames())

    fun disableMod(modName: String) = saveToEnabled((enabledModNames().toList() - modName).toTypedArray())

    private fun saveToEnabled(mods: List<Mod>) = saveToEnabled(
        mods.map { it.info.name }.toTypedArray()
    )

    private fun saveToEnabled(modNames: Array<String>) = FileUtils.bundleToFile(
        ENABLED_MODS_FILE,
        Bundle().apply { put(ENABLED_MODS, modNames) }
    ).also { _lazyEnabledModNames = null }

    private fun getEnabledModsWithoutSaving(): List<Mod> =
        if (FileUtils.getFileHandle(ENABLED_MODS_FILE).exists())
            getInstalledMods().filter {
                it.info.name in lazyEnabledModNames
            }.sortedBy { lazyEnabledModNames.indexOf(it.info.name) }
        else emptyList()

    private var _lazyEnabledModNames: Array<String>? = null
    private val lazyEnabledModNames get() = _lazyEnabledModNames ?: enabledModNames().also { _lazyEnabledModNames = it }

    private fun enabledModNames() = if (FileUtils.getFileHandle(ENABLED_MODS_FILE).exists())
        FileUtils.bundleFromFile(ENABLED_MODS_FILE).getStringArray(ENABLED_MODS)
    else
        emptyArray<String>().also { saveToEnabled(emptyArray()) }

    private val storage: FileHandle
        get() = FileUtils.getFileHandle(Files.FileType.External, FileUtils.defaultPath, STORAGE).apply { if (!exists()) mkdirs() }
}
