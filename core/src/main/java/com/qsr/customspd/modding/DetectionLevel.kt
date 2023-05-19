package com.qsr.customspd.modding

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class DetectionLevel {
    @SerialName("default") DEFAULT,
    @SerialName("always_find") ALWAYS_FIND,
    @SerialName("only_search") ONLY_SEARCH,
    @SerialName("only_talisman") ONLY_TALISMAN,
    @SerialName("impossible") IMPOSSIBLE,
}
