@file:JvmName("RandomGenUtils")
package com.qsr.customspd.modding

fun calculateLevels(distributions: List<ItemDistribution>) =
    distributions.flatMap { it.levels.asSequence().shuffled().take(it.quantity) }.toTypedArray()

fun calculateQuestLevel(distribution: List<String>) = if (distribution.isNotEmpty()) distribution.random() else null
