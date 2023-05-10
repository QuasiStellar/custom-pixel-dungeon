package com.qsr.customspd.dungeon

import kotlinx.serialization.Serializable

@Serializable
data class MobSpawn(
    val x: Int,
    val y: Int,
    val type: String,
    val hp: Int? = null,
    val alignment: String? = null,
)
