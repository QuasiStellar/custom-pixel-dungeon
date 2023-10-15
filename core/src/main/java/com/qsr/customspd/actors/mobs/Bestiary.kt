/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
@file:Suppress("UNCHECKED_CAST")

package com.qsr.customspd.actors.mobs

import com.qsr.customspd.Dungeon
import com.qsr.customspd.modding.JsonConfigRetriever.customMobExists
import com.watabou.utils.Random
import com.watabou.utils.Reflection
import java.util.*

object Bestiary {
    fun getMobRotation(): List<Class<out Mob?>> = standardMobRotation().also {
        addRareMobs(it)
        swapMobAlts(it)
        Random.shuffle(it)
    }

    fun getCustomMobs(): List<String> = Dungeon.layout.dungeon[Dungeon.levelName]!!.bestiary.filter {
        customMobExists(it)
    }

    //returns a rotation of standard mobs, unshuffled.
    private fun standardMobRotation(): MutableList<Class<out Mob?>> = Dungeon.layout.dungeon[Dungeon.levelName]!!.bestiary.map {
        if (customMobExists(it)) return@map CustomMob::class.java
        if (it == "Elemental") return@map Elemental.random()
        if (it == "Shaman") return@map Shaman.random()
        Reflection.forName("com.qsr.customspd.actors.mobs.$it") as Class<out Mob?>
    }.toMutableList()

    //has a chance to add a rarely spawned mobs to the rotation
    private fun addRareMobs(rotation: MutableList<Class<out Mob?>>) {
        val mobClass = Dungeon.layout.dungeon[Dungeon.levelName]!!.rareMob
        if (Random.Float() < 0.025f && mobClass != null) rotation.add(Reflection.forName("com.qsr.customspd.actors.mobs.$mobClass") as Class<out Mob?>)
    }

    //switches out regular mobs for their alt versions when appropriate
    private fun swapMobAlts(rotation: MutableList<Class<out Mob?>>) {
        for (i in rotation.indices) {
            if (Random.Int(50) == 0) {
                var cl = rotation[i]
                if (cl == Rat::class.java) {
                    cl = Albino::class.java
                } else if (cl == Slime::class.java) {
                    cl = CausticSlime::class.java
                } else if (cl == Thief::class.java) {
                    cl = Bandit::class.java
                } else if (cl == Necromancer::class.java) {
                    cl = SpectralNecromancer::class.java
                } else if (cl == Brute::class.java) {
                    cl = ArmoredBrute::class.java
                } else if (cl == DM200::class.java) {
                    cl = DM201::class.java
                } else if (cl == Monk::class.java) {
                    cl = Senior::class.java
                } else if (cl == Scorpio::class.java) {
                    cl = Acidic::class.java
                }
                rotation[i] = cl
            }
        }
    }
}
