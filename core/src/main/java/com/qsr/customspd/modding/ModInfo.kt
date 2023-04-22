package com.qsr.customspd.modding

import kotlinx.serialization.Serializable

@Serializable
data class ModInfo(
    val name: String,
    val version: Int,
    val description: String,
    val author: String,
    val license: String,
)
