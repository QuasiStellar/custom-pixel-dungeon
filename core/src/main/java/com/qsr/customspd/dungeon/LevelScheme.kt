package com.qsr.customspd.dungeon

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LevelScheme(
    val depth: Int,
    val type: LevelType,
    val layout: String? = null,
    @SerialName("custom_layout") val customLayout: CustomLevelLayout? = null,
    val entrances: List<String>,
    val exits: List<String>,
    val chasm: String,
    val passage: String,
    @SerialName("spawner_cooldown") val spawnerCooldown: Float = 60f,
    val boss: Boolean = false,
    val shop: Boolean = false,
    @SerialName("price_multiplier") val priceMultiplier: Int = 1,
)
