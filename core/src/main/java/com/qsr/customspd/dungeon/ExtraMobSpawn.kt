package com.qsr.customspd.dungeon

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExtraMobSpawn(
    val type: String,
    val hp: Int? = null,
    val alignment: String? = null,
    @SerialName("ai_state") val aiState: String? = null,
    val champion: String? = null,
)
