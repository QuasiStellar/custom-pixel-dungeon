package com.qsr.customspd.dungeon

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class LevelVisibility {
    @SerialName("default") DEFAULT,
    @SerialName("only_visible") ONLY_VISIBLE,
    @SerialName("secrets") SECRETS,
}
