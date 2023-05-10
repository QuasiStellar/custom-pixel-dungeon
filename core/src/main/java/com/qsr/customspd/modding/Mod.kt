package com.qsr.customspd.modding

import com.badlogic.gdx.files.FileHandle
import com.qsr.customspd.assets.Asset.Companion.getAssetFilePath
import com.qsr.customspd.assets.GeneralAsset
import com.watabou.noosa.Image
import java.io.File

class Mod(
    private val folder: FileHandle,
    val info: ModInfo,
) {
    val isEnabled get() = ModManager.isEnabled(this)

    val icon: Image
        get() = with(folder.child("mod_icon.png")) {
            if (exists())
                Image("${File.separatorChar}${ModManager.STORAGE}${File.separatorChar}${folder.name()}${File.separatorChar}mod_icon.png")
            else Image(getAssetFilePath(GeneralAsset.ICON_TALENT))
        }

    fun getPreviews(): List<Image> = (1..5).mapNotNull {
        with(folder.child("mod_preview_$it.png")) {
            if (exists()) Image("${File.separatorChar}${ModManager.STORAGE}${File.separatorChar}${folder.name()}${File.separatorChar}mod_preview_$it.png")
            else null
        }
    }

    fun enable() = ModManager.enableMod(info.name)

    fun disable() = ModManager.disableMod(info.name)
}
