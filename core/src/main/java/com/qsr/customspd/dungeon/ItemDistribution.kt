package com.qsr.customspd.dungeon

import kotlinx.serialization.Serializable

@Serializable
data class ItemDistribution(
    val quantity: Int,
    val levels: List<String>,
)
