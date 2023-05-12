package com.qsr.customspd.modding

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ModInfo(
    val name: String,
    val version: Int,
    val description: String,
    val author: String,
    val languages: List<String>? = null,
    val license: String,
    @SerialName("gameplay_mod") val gameplayMod: Boolean = false,
    val link: String? = null,
)
