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
package com.qsr.customspd.actors.mobs

import com.qsr.customspd.Dungeon
import com.qsr.customspd.actors.Char
import com.qsr.customspd.actors.buffs.Buff
import com.qsr.customspd.effects.CellEmitter
import com.qsr.customspd.effects.particles.ElmoParticle
import com.qsr.customspd.items.weapon.Weapon.Enchantment
import com.qsr.customspd.mechanics.Ballistica
import com.qsr.customspd.messages.Messages
import com.qsr.customspd.modding.CustomMobEnchantment
import com.qsr.customspd.modding.CustomMobScheme
import com.qsr.customspd.modding.JsonConfigRetriever
import com.qsr.customspd.scenes.GameScene
import com.qsr.customspd.sprites.CharSprite
import com.qsr.customspd.sprites.CustomMobSprite
import com.qsr.customspd.utils.GLog
import com.qsr.customspd.windows.WndTitledMessage
import com.watabou.noosa.Game
import com.watabou.utils.Bundle
import com.watabou.utils.Random
import com.watabou.utils.Reflection

class CustomMob : Mob {

    private var name: String? = null
    private var scheme: CustomMobScheme? = null
    private val sc get() = scheme!!

    private var angered: Boolean = false
    private var dialogueCount: Int = 0

    constructor(name: String) {
        this.name = name
        scheme = JsonConfigRetriever.retrieveMobScheme(name)
        initialize()
        HP = HT
    }

    constructor()

    private fun initialize() {
        HT = sc.hp
        baseSpeed = sc.speed
        defenseSkill = sc.evasion
        EXP = sc.exp
        maxLvl = sc.maxLvl
        loot = sc.loot
        lootChance = sc.lootChance
        alignment = Alignment.valueOf(sc.alignment.uppercase())
        if (angered) alignment = Alignment.ENEMY
        if (alignment == Alignment.NEUTRAL) state = PASSIVE
        flying = sc.flying

        for (property in sc.properties) {
            properties.add(Property.valueOf(property.uppercase()))
        }
    }

    override fun damage(dmg: Int, src: Any) {
        if (sc.vanishOnDamage) {
            destroy()
            if (sprite != null) {
                sprite.killAndErase()
                CellEmitter.get(pos).burst(ElmoParticle.FACTORY, 6)
            }
        } else {
            super.damage(dmg, src)
            if (sc.dialogues != 0 && !angered) {
                angered = true
                state = HUNTING
                if (sc.angerYell) yell(Messages.get("$name.anger_yell"))
                alignment = Alignment.ENEMY
            }
        }
    }

    override fun attackProc(enemy: Char, damage: Int): Int {
        var dmg = super.attackProc(enemy, damage)
        val random = Random.Float()
        var enchantment: CustomMobEnchantment? = null
        var counter = 0f
        for (enchant in sc.enchantments) {
            counter += enchant.chance
            if (counter > random) {
                enchantment = enchant
                break
            }
        }
        enchantment?.let {
            dmg = (Reflection.newInstance(Reflection.forName("com.qsr.customspd.items.weapon." + enchantment.type)) as Enchantment)
                .proc(enchantment.strength, this, enemy, damage)
        }
        if (!enemy.isAlive && enemy === Dungeon.hero) {
            Dungeon.fail(this)
            GLog.n(Messages.capitalize(Messages.get(Char::class.java, "kill", name())))
        }
        return dmg
    }

    override fun add(buff: Buff): Boolean {
        if (super.add(buff)) {
            if (sc.dialogues != 0 && !angered) {
                angered = true
                state = HUNTING
                if (sc.angerYell) yell(Messages.get("$name.anger_yell"))
                alignment = Alignment.ENEMY
            }
            return true
        }
        return false
    }

    override fun interact(c: Char): Boolean = if (alignment == Alignment.NEUTRAL && sc.dialogues != 0) {
        sprite.turnTo(pos, c.pos)
        dialogueCount++
        if (dialogueCount > sc.dialogues) dialogueCount--
        if (sc.talkViaLog) {
            yell(Messages.get("$name.dialogue_$dialogueCount"))
        } else {
            Game.runOnRenderThread {
                GameScene.show(WndTitledMessage(sprite(), Messages.titleCase(name()), Messages.get("$name.dialogue_$dialogueCount")))
            }
        }
        true
    } else {
        super.interact(c)
    }

    override fun getCloser(target: Int): Boolean =
        if (properties.contains(Property.IMMOVABLE)) true
        else super.getCloser(target)

    override fun getFurther(target: Int): Boolean =
        if (properties.contains(Property.IMMOVABLE)) true
        else super.getFurther(target)

    override fun damageRoll(): Int = Random.NormalIntRange(sc.minDamage, sc.maxDamage)

    override fun attackSkill(target: Char): Int = sc.accuracy

    override fun drRoll(): Int = super.drRoll() + Random.NormalIntRange(sc.minArmor, sc.maxArmor)

    override fun canAttack(enemy: Char): Boolean {
        return (super.canAttack(enemy)
            || (sc.ranged && Ballistica(pos, enemy.pos, Ballistica.PROJECTILE).collisionPos == enemy.pos))
    }

    override fun die(cause: Any?) {
        if (sc.deathYell) yell(Messages.get("$name.death_yell"))
        super.die(cause)
    }

    override fun sprite(): CharSprite = CustomMobSprite(sc.animation)

    override fun name(): String = Messages.get("$name.name")

    override fun description(): String = Messages.get("$name.desc")

    override fun storeInBundle(bundle: Bundle) {
        super.storeInBundle(bundle)
        bundle.put(NAME, name)
        bundle.put(ANGERED, angered)
        bundle.put(DIALOGUE_COUNT, dialogueCount)
    }

    override fun restoreFromBundle(bundle: Bundle) {
        name = bundle.getString(NAME)
        scheme = JsonConfigRetriever.retrieveMobScheme(name!!)
        super.restoreFromBundle(bundle)
        angered = bundle.getBoolean(ANGERED)
        dialogueCount = bundle.getInt(DIALOGUE_COUNT)
        initialize()
    }

    companion object {
        private const val NAME = "name"
        private const val ANGERED = "angered"
        private const val DIALOGUE_COUNT = "dialogue_count"
    }
}
