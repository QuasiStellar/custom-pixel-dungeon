package com.qsr.customspd.dungeon

import kotlinx.serialization.Serializable

@Serializable
data class MusicPlayer(
    val tracks: List<String>,
    val probs: List<Float>,
)
