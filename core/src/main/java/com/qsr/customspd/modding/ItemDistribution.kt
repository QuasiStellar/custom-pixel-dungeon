package com.qsr.customspd.modding

import kotlinx.serialization.Serializable

@Serializable
data class ItemDistribution(
    val quantity: Int,
    val levels: List<String>,
)
