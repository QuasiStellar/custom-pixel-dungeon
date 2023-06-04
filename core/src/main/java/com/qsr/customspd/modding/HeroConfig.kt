package com.qsr.customspd.modding

import kotlinx.serialization.Serializable

@Serializable
data class HeroConfig(
    val merge: Boolean = false,
    val weapon: ItemDescription? = null,
    val armor: ItemDescription? = null,
    val artifact: ItemDescription? = null,
    val misc: ItemDescription? = null,
    val ring: ItemDescription? = null,
    val items: List<ItemDescription> = emptyList(),
    val identified: List<String> = emptyList(),
)
