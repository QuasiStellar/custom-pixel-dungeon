package com.qsr.customspd.modding

import kotlinx.serialization.Serializable

@Serializable
data class CustomMobEnchantment(
    val type: String,
    val chance: Float = 1f,
    val strength: Int = 0,
)
