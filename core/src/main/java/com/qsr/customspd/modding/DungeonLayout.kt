package com.qsr.customspd.modding

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DungeonLayout(
    val merge: Boolean = false,
    val start: String,
    val dungeon: Map<String, LevelScheme>,
    val gold: Int = 0,
    val energy: Int = 0,
    val bones: Boolean = true,
    @SerialName("rat_king_level") val ratKingLevel: String = "surface",
    @SerialName("ghost_spawn_levels") val ghostSpawnLevels: List<String> = emptyList(),
    @SerialName("wandmaker_spawn_levels") val wandmakerSpawnLevels: List<String> = emptyList(),
    @SerialName("blacksmith_spawn_levels") val blacksmithSpawnLevels: List<String> = emptyList(),
    @SerialName("imp_spawn_levels") val impSpawnLevels: List<String> = emptyList(),
    @SerialName("pos_distribution") val posDistribution: List<ItemDistribution> = emptyList(),
    @SerialName("sou_distribution") val souDistribution: List<ItemDistribution> = emptyList(),
    @SerialName("as_distribution") val asDistribution: List<ItemDistribution> = emptyList(),
)
