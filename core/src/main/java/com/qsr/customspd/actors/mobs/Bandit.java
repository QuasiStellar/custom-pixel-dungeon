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

package com.qsr.customspd.actors.mobs;

import com.qsr.customspd.Dungeon;
import com.qsr.customspd.actors.buffs.Blindness;
import com.qsr.customspd.actors.buffs.Buff;
import com.qsr.customspd.actors.buffs.Cripple;
import com.qsr.customspd.actors.buffs.Poison;
import com.qsr.customspd.actors.hero.Hero;
import com.qsr.customspd.items.Item;
import com.qsr.customspd.sprites.BanditSprite;
import com.watabou.utils.Random;

public class Bandit extends Thief {
	
	public Item item;
	
	{
		spriteClass = BanditSprite.class;

		//guaranteed first drop, then 1/3, 1/9, etc.
		lootChance = 1f;
	}
	
	@Override
	protected boolean steal( Hero hero ) {
		if (super.steal( hero )) {
			
			Buff.prolong( hero, Blindness.class, Blindness.DURATION/2f );
			Buff.affect( hero, Poison.class ).set(Random.IntRange(5, 6) );
			Buff.prolong( hero, Cripple.class, Cripple.DURATION/2f );
			Dungeon.observe();
			
			return true;
		} else {
			return false;
		}
	}
	
}
