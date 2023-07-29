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

package com.qsr.customspd.items.weapon.curses;

import com.qsr.customspd.actors.Char;
import com.qsr.customspd.actors.buffs.Buff;
import com.qsr.customspd.actors.buffs.FlavourBuff;
import com.qsr.customspd.assets.Asset;
import com.qsr.customspd.items.weapon.Weapon;
import com.qsr.customspd.sprites.ItemSprite;
import com.qsr.customspd.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Random;

import kotlin.Pair;

public class Wayward extends Weapon.Enchantment {

	private static ItemSprite.Glowing BLACK = new ItemSprite.Glowing( 0x000000 );

	@Override
	public int proc(float probability, int strength, Char attacker, Char defender, int damage) {
		if (attacker.buff(WaywardBuff.class) != null){
			Buff.detach(attacker, WaywardBuff.class);
		} else if (Random.Float() < probability){
			Buff.prolong(attacker, WaywardBuff.class, WaywardBuff.DURATION);
		}

		return damage;
	}

	@Override
	public int proc( Weapon weapon, Char attacker, Char defender, int damage ) {
		return proc(procChanceMultiplier(attacker), 0, attacker, defender, damage);
	}

	@Override
	public boolean curse() {
		return true;
	}

	@Override
	public ItemSprite.Glowing glowing() {
		return BLACK;
	}

	//see weapon.accuracyFactor for effect
	public static class WaywardBuff extends FlavourBuff {

		{
			type = buffType.NEGATIVE;
			announced = true;
		}

		public static final float DURATION	= 10f;

		@Override
		public Pair<Asset, Asset> icon() {
			return BuffIndicator.WEAKNESS;
		}

		@Override
		public void tintIcon(Image icon) {
			icon.hardlight(1, 1, 0);
		}

		@Override
		public float iconFadePercent() {
			return Math.max(0, (DURATION - visualcooldown()) / DURATION);
		}

	}

}
