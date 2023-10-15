package com.qsr.customspd.modding

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.Json.Default.parseToJsonElement
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
object JsonConfigRetriever {
    fun customMobExists(name: String) = ModManager.getAssetFileHandle(ModManager.getModdedAssetFilePath("mobs/$name.json")).exists()

    fun retrieveMobScheme(name: String): CustomMobScheme =
        Json.decodeFromString(ModManager.getAssetFileHandle(ModManager.getModdedAssetFilePath("mobs/$name.json")).readString())

    fun retrieveSpriteSizes(): Map<String, List<Int>> =
        Json.decodeFromString(
            ModManager.getAllModdedAssetFilePaths("config/sprite_sizes.json").reversed().fold(
                parseToJsonElement(ModManager.getAssetFileHandle("config/sprite_sizes.json").readString()),
                ::mergeSpriteSizes
            ).toString()
        )

    fun retrieveDungeonLayout(): DungeonLayout =
        Json.decodeFromString(
            ModManager.getAllModdedAssetFilePaths("dungeon/dungeon.json").reversed().fold(
                parseToJsonElement(ModManager.getAssetFileHandle("dungeon/dungeon.json").readString()),
                ::mergeDungeonLayouts
            ).toString()
        )

    fun retrieveHeroConfig(type: String): HeroConfig =
        Json.decodeFromString(
            ModManager.getAllModdedAssetFilePaths("heroes/$type.json").reversed().fold(
                parseToJsonElement(ModManager.getAssetFileHandle("heroes/$type.json").readString()),
                ::mergeHeroConfigs
            ).toString()
        )

    private fun mergeDungeonLayouts(base: JsonElement, mod: String): JsonElement = mergeDungeonLayouts(
        base as JsonObject,
        parseToJsonElement(ModManager.getAssetFileHandle(mod).readString()) as JsonObject
    )

    private fun mergeDungeonLayouts(base: JsonObject, mod: JsonObject): JsonObject = buildJsonObject {
        if (mod["merge"] != JsonPrimitive(true)) return mod
        for (entry in base.entries) {
            if (!mod.containsKey(entry.key)) {
                put(entry.key, entry.value)
            } else {
                when (entry.key) {
                    "dungeon" -> put(entry.key, buildJsonObject {
                        for (level in entry.value.jsonObject.entries) {
                            if (!mod[entry.key]!!.jsonObject.containsKey(level.key)) {
                                put(level.key, level.value)
                            } else {
                                put(level.key, mod[entry.key]!!.jsonObject[level.key]!!)
                            }
                        }
                        for (level in mod[entry.key]!!.jsonObject.entries) {
                            if (!entry.value.jsonObject.containsKey(level.key)) {
                                put(level.key, mod[entry.key]!!.jsonObject[level.key]!!)
                            }
                        }
                    })

                    else -> put(entry.key, mod[entry.key]!!)
                }
            }
        }
        for (entry in mod.entries) {
            if (!base.containsKey(entry.key)) {
                put(entry.key, entry.value)
            }
        }
    }

    private fun mergeHeroConfigs(base: JsonElement, mod: String): JsonElement = mergeHeroConfigs(
        base as JsonObject,
        parseToJsonElement(ModManager.getAssetFileHandle(mod).readString()) as JsonObject
    )

    private fun mergeHeroConfigs(base: JsonObject, mod: JsonObject): JsonObject = buildJsonObject {
        if (mod["merge"] != JsonPrimitive(true)) return mod
        for (entry in base.entries) {
            if (!mod.containsKey(entry.key)) {
                put(entry.key, entry.value)
            } else {
                when (entry.value) {
                    is JsonArray -> put(entry.key, buildJsonArray {
                        for (item in entry.value.jsonArray) {
                            add(item)
                        }
                        for (item in mod[entry.key]!!.jsonArray) {
                            add(item)
                        }
                    })

                    else -> put(entry.key, mod[entry.key]!!)
                }
            }
        }
        for (entry in mod.entries) {
            if (!base.containsKey(entry.key)) {
                put(entry.key, entry.value)
            }
        }
    }

    private fun mergeSpriteSizes(base: JsonElement, mod: String): JsonElement = mergeSpriteSizes(
        base as JsonObject,
        parseToJsonElement(ModManager.getAssetFileHandle(mod).readString()) as JsonObject
    )

    private fun mergeSpriteSizes(base: JsonObject, mod: JsonObject): JsonObject = buildJsonObject {
        for (entry in base.entries) {
            if (!mod.containsKey(entry.key))
                put(entry.key, entry.value)
            else
                put(entry.key, mod[entry.key]!!)
        }
    }
}

