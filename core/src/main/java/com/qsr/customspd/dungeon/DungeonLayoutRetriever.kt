package com.qsr.customspd.dungeon

import com.qsr.customspd.modding.ModManager
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

object DungeonLayoutRetriever {
    fun retrieve(): DungeonLayout =
        Json.decodeFromString(ModManager.getAssetFileHandle(ModManager.getModdedAssetFilePath("dungeon/dungeon.json")).readString())
}
