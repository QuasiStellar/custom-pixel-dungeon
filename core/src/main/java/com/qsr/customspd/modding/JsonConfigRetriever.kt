package com.qsr.customspd.modding

import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

object JsonConfigRetriever {
    fun retrieveDungeonLayout(): DungeonLayout =
        Json.decodeFromString(ModManager.getAssetFileHandle(ModManager.getModdedAssetFilePath("dungeon/dungeon.json")).readString())

    fun retrieveHeroConfig(type: String): HeroConfig =
        Json.decodeFromString(ModManager.getAssetFileHandle(ModManager.getModdedAssetFilePath("heroes/$type.json")).readString())

    fun customMobExists(name: String) = ModManager.getAssetFileHandle(ModManager.getModdedAssetFilePath("mobs/$name.json")).exists()

    fun retrieveMobScheme(name: String): CustomMobScheme =
        Json.decodeFromString(ModManager.getAssetFileHandle(ModManager.getModdedAssetFilePath("mobs/$name.json")).readString())
}
