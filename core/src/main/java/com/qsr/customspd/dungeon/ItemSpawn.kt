package com.qsr.customspd.dungeon

import kotlinx.serialization.Serializable

@Serializable
data class ItemSpawn(
    val x: Int,
    val y: Int,
    val type: String,
    val quantity: Int = 1,
    val level: Int = 0,
    val enchantment: String? = null,
    val identified: Boolean = false,
    val cursed: Boolean = false,
)
