@file:JvmName("RandomGenUtils")
package com.qsr.customspd.dungeon

fun calculateLevels(distributions: List<ItemDistribution>) =
    distributions.flatMap { it.levels.asSequence().shuffled().take(it.quantity) }.toTypedArray()

fun calculateQuestLevel(distribution: List<String>) = distribution.random()
