package com.qsr.customspd.modding

import kotlinx.serialization.Serializable

@Serializable
data class PlantSpawn(
    val x: Int,
    val y: Int,
    val type: String,
)
