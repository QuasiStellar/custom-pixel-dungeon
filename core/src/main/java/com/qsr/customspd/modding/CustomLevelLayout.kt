package com.qsr.customspd.modding

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CustomLevelLayout(
    val width: Int,
    val height: Int,
    val region: Int,
    @SerialName("view_distance") val viewDistance: Int = 8,
    val music: MusicPlayer,
    val map: List<Int>,
    val entrances: List<Position> = emptyList(),
    val exits: List<Position> = emptyList(),
    val items: List<ItemSpawn> = emptyList(),
    @SerialName("shuffle_items") val shuffleItems: Boolean = false,
    val mobs: List<MobSpawn> = emptyList(),
    @SerialName("open_traps") val openTraps: List<TrapSpawn> = emptyList(),
    @SerialName("hidden_traps") val hiddenTraps: List<TrapSpawn> = emptyList(),
    @SerialName("disarmed_traps") val disarmedTraps: List<TrapSpawn> = emptyList(),
    val plants: List<PlantSpawn> = emptyList(),
)
