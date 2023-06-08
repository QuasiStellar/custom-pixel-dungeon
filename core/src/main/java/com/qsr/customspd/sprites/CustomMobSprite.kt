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
package com.qsr.customspd.sprites

import com.qsr.customspd.Dungeon
import com.qsr.customspd.items.Item
import com.qsr.customspd.modding.MobAnimationScheme
import com.qsr.customspd.modding.ModManager
import com.watabou.noosa.TextureFilm
import com.watabou.utils.Reflection

class CustomMobSprite(private val scheme: MobAnimationScheme) : MobSprite() {

    private var cellToAttack = 0

    init {
        texture(ModManager.getModdedAssetFilePath(scheme.texture))
        val frames = TextureFilm(texture, scheme.width, scheme.height)
        idle = Animation(scheme.idleFps, true)
        idle.frames(frames, *scheme.idleFrames.toTypedArray())
        run = Animation(scheme.runFps, true)
        run.frames(frames, *scheme.runFrames.toTypedArray())
        attack = Animation(scheme.attackFps, false)
        attack.frames(frames, *scheme.attackFrames.toTypedArray())
        zap = Animation(scheme.zapFps, false)
        zap.frames(frames, *scheme.zapFrames.toTypedArray())
        die = Animation(scheme.dieFps, false)
        die.frames(frames, *scheme.dieFrames.toTypedArray())
        play(idle)
    }

    override fun blood(): Int = scheme.bloodColor ?: super.blood()

    override fun attack(cell: Int) {
        if (!Dungeon.level.adjacent(cell, ch.pos)) {
            cellToAttack = cell
            zap(cell)
        } else {
            super.attack(cell)
        }
    }

    override fun onComplete(anim: Animation) {
        if (anim === zap) {
            idle()

            if (scheme.missile != null) {
                (parent.recycle(MissileSprite::class.java) as MissileSprite).reset(
                    this,
                    cellToAttack,
                    Reflection.newInstance(Reflection.forName("com.qsr.customspd.items." + scheme.missile)) as Item,
                ) { ch.onAttackComplete() }
            } else {
                ch.onAttackComplete()
                super.onComplete(anim)
            }
        } else {
            super.onComplete(anim)
        }
    }
}
