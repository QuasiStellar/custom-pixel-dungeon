package com.qsr.customspd.modding

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MobAnimationScheme(
    val texture: String,
    val width: Int,
    val height: Int,
    @SerialName("idle_fps") val idleFps: Int = 1,
    @SerialName("idle_frames") val idleFrames: List<Int> = listOf(0),
    @SerialName("run_fps") val runFps: Int = 1,
    @SerialName("run_frames") val runFrames: List<Int> = listOf(0),
    @SerialName("attack_fps") val attackFps: Int = 1,
    @SerialName("attack_frames") val attackFrames: List<Int> = listOf(0),
    @SerialName("zap_fps") val zapFps: Int = 1,
    @SerialName("zap_frames") val zapFrames: List<Int> = listOf(0),
    @SerialName("die_fps") val dieFps: Int = 1,
    @SerialName("die_frames") val dieFrames: List<Int> = listOf(0),
    val missile: String? = null,
    @SerialName("blood_color") val bloodColor: Int? = null,
)
