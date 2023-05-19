package com.qsr.customspd.modding

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class LevelType {
    @SerialName("regular") REGULAR,
    @SerialName("custom") CUSTOM,
}
