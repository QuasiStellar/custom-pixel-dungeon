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

package com.qsr.customspd.items.weapon.enchantments;

import com.qsr.customspd.actors.Char;
import com.qsr.customspd.actors.buffs.Buff;
import com.qsr.customspd.actors.buffs.Chill;
import com.qsr.customspd.effects.Splash;
import com.qsr.customspd.items.weapon.Weapon;
import com.qsr.customspd.sprites.ItemSprite;
import com.qsr.customspd.sprites.ItemSprite.Glowing;
import com.watabou.utils.Random;

public class Chilling extends Weapon.Enchantment {

	private static ItemSprite.Glowing TEAL = new ItemSprite.Glowing( 0x00FFFF );

	@Override
	public int proc(float probability, int strength, Char attacker, Char defender, int damage) {
		if (Random.Float() < probability) {

			//adds 3 turns of chill per proc, with a cap of 6 turns
			float durationToAdd = strength;
			Chill existing = defender.buff(Chill.class);
			if (existing != null){
				durationToAdd = Math.min(durationToAdd, (2 * strength)-existing.cooldown());
			}

			Buff.affect( defender, Chill.class, durationToAdd );
			Splash.at( defender.sprite.center(), 0xFFB2D6FF, 5);

		}

		return damage;
	}

	@Override
	public int proc( Weapon weapon, Char attacker, Char defender, int damage ) {
		int level = Math.max( 0, weapon.buffedLvl() );

		// lvl 0 - 25%
		// lvl 1 - 40%
		// lvl 2 - 50%
		float procChance = (level+1f)/(level+4f) * procChanceMultiplier(attacker);
		if (Random.Float() < procChance) {
			float powerMulti = Math.max(1f, procChance);

			//adds 3 turns of chill per proc, with a cap of 6 turns
			float durationToAdd = 3f * powerMulti;
			Chill existing = defender.buff(Chill.class);
			if (existing != null){
				durationToAdd = Math.min(durationToAdd, (6f*powerMulti)-existing.cooldown());
			}

			Buff.affect( defender, Chill.class, durationToAdd );
			Splash.at( defender.sprite.center(), 0xFFB2D6FF, 5);

		}

		return damage;
	}
	
	@Override
	public Glowing glowing() {
		return TEAL;
	}

}
