package com.qsr.customspd.dungeon

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemSpawn(
    val x: Int,
    val y: Int,
    val type: String? = null,
    val category: String? = null,
    @SerialName("heap_type") val heapType: String? = null,
    val quantity: Int = 1,
    val level: Int = 0,
    val enchantment: String? = null,
    val identified: Boolean = false,
    val cursed: Boolean = false,
    @SerialName("level_name") val levelName: String? = null,
)
