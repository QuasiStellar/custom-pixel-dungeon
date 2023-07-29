package com.qsr.customspd.modding

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CustomMobScheme(
    val animation: MobAnimationScheme,
    val hp: Int = 1,
    val exp: Int = 0,
    @SerialName("max_lvl") val maxLvl: Int = 0,
    val speed: Float = 1f,
    val evasion: Int = 0,
    val accuracy: Int = 0,
    @SerialName("min_damage") val minDamage: Int = 0,
    @SerialName("max_damage") val maxDamage: Int = 0,
    @SerialName("min_armor") val minArmor: Int = 0,
    @SerialName("max_armor") val maxArmor: Int = 0,
    val ranged: Boolean = false,
    val loot: ExtraItemSpawn? = null,
    @SerialName("loot_chance") val lootChance: Float = 1f,
    val properties: List<String> = emptyList(),
    val enchantments: List<CustomMobEnchantment> = emptyList(),
    val flying: Boolean = false,
    val alignment: String = "enemy",
    val dialogues: Int = 0,
    @SerialName("talk_via_log") val talkViaLog: Boolean = false,
    @SerialName("vanish_on_damage") val vanishOnDamage: Boolean = false,
    @SerialName("anger_yell") val angerYell: Boolean = false,
    @SerialName("death_yell") val deathYell: Boolean = false,
)
