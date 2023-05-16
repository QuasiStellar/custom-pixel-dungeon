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
    val bestiary: List<String>? = null,
    @SerialName("rare_mob") val rareMob: String? = null,
    @SerialName("spawner_cooldown") val spawnerCooldown: Float = 60f,
    val boss: Boolean = false,
    val shop: Boolean = false,
    @SerialName("price_multiplier") val priceMultiplier: Int = 1,
    val visibility: LevelVisibility = LevelVisibility.DEFAULT,
    @SerialName("trap_detection") val trapDetection: DetectionLevel = DetectionLevel.DEFAULT,
    @SerialName("door_detection") val doorDetection: DetectionLevel = DetectionLevel.DEFAULT,
    val levelFeeling: String = "random",
)
