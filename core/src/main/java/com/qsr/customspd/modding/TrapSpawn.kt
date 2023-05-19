package com.qsr.customspd.modding

import kotlinx.serialization.Serializable

@Serializable
data class TrapSpawn(
    val x: Int,
    val y: Int,
    val type: String,
)
