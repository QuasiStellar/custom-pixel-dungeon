package com.qsr.customspd.modding

import com.qsr.customspd.actors.hero.HeroAction.Attack
import kotlinx.serialization.Serializable

@Serializable
data class MobAnimationScheme(
    val width: Int,
    val height: Int,
    val idle: List<Int>,
    val run: List<Int>,
    val attack: List<Int>,
    val die: List<Int>,
)
