package com.qsr.customspd.modding

import kotlinx.serialization.Serializable

@Serializable
data class HeroesConfig(
    val any: HeroConfig,
    val warrior: HeroConfig,
    val mage: HeroConfig,
    val rogue: HeroConfig,
    val huntress: HeroConfig,
    val duelist: HeroConfig,
)
