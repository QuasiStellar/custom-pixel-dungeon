package com.qsr.customspd.modding

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemDescription(
    val type: String,
    val quantity: Int = 1,
    val level: Int = 0,
    val enchantment: String? = null,
    val identified: Boolean = false,
    val cursed: Boolean = false,
    @SerialName("level_name") val levelName: String? = null,
    val quickslot: Boolean = false,
    val seal: Boolean = false,
    @SerialName("core_wand") val coreWand: String? = null,
)
