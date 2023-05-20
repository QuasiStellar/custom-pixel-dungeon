package com.qsr.customspd.modding

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemSpawn(
    val x: Int,
    val y: Int,
    val type: String? = null,
    val category: String? = null,
    @SerialName("ignore_deck") val ignoreDeck: Boolean = false,
    @SerialName("heap_type") val heapType: String? = null,
    val quantity: Int = 1,
    @SerialName("quantity_min") val quantityMin: Int? = null,
    @SerialName("quantity_max") val quantityMax: Int? = null,
    val level: Int = 0,
    val enchantment: String? = null,
    val identified: Boolean = false,
    val cursed: Boolean? = null,
    @SerialName("level_name") val levelName: String? = null,
    val seal: Boolean = false,
    @SerialName("core_wand") val coreWand: String? = null,
)
