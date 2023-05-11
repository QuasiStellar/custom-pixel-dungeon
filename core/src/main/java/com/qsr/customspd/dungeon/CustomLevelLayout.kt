package com.qsr.customspd.dungeon

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CustomLevelLayout(
    val width: Int,
    val height: Int,
    val region: Int,
    @SerialName("view_distance") val viewDistance: Int,
    val music: MusicPlayer,
    val map: List<Int>,
    val entrances: List<Position> = emptyList(),
    val exits: List<Position> = emptyList(),
    val items: List<ItemSpawn> = emptyList(),
    val mobs: List<MobSpawn> = emptyList(),
)
