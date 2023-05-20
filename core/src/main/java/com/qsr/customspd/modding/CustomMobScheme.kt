package com.qsr.customspd.modding

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CustomMobScheme(
    val hp: Int,
    val exp: Int,
    @SerialName("max_lvl") val maxLvl: Int,
    val speed: Float = 1f,
    val evasion: Int,
    val accuracy: Int,
    @SerialName("min_damage") val minDamage: Int,
    @SerialName("max_damage") val maxDamage: Int,
    @SerialName("min_armor") val minArmor: Int,
    @SerialName("max_armor") val maxArmor: Int,
    val loot: ExtraItemSpawn,
    @SerialName("loot_chance") val lootChance: Float,
    val animation: MobAnimationScheme,
)
