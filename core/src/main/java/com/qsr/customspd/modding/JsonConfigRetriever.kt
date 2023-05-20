package com.qsr.customspd.modding

import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

object JsonConfigRetriever {
    fun retrieveDungeonLayout(): DungeonLayout =
        Json.decodeFromString(ModManager.getAssetFileHandle(ModManager.getModdedAssetFilePath("dungeon/dungeon.json")).readString())

    fun retrieveHeroesConfig(): HeroesConfig =
        Json.decodeFromString(ModManager.getAssetFileHandle(ModManager.getModdedAssetFilePath("heroes/heroes.json")).readString())
}
