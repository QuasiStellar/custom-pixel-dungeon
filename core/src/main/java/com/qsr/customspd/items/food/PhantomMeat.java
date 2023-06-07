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

package com.qsr.customspd.items.food;

import com.qsr.customspd.actors.buffs.Barkskin;
import com.qsr.customspd.actors.buffs.Buff;
import com.qsr.customspd.actors.buffs.Hunger;
import com.qsr.customspd.actors.buffs.Invisibility;
import com.qsr.customspd.actors.hero.Hero;
import com.qsr.customspd.assets.GeneralAsset;
import com.qsr.customspd.effects.Speck;
import com.qsr.customspd.items.potions.PotionOfHealing;

public class PhantomMeat extends Food {

	{
		image = GeneralAsset.PHANTOM_MEAT;
		energy = Hunger.STARVING;
	}

	@Override
	protected void satisfy(Hero hero) {
		super.satisfy(hero);
		effect(hero);
	}

	public int value() {
		return 30 * quantity;
	}

	public static void effect(Hero hero){

		Buff.affect( hero, Barkskin.class ).set( hero.HT / 4, 1 );
		Buff.affect( hero, Invisibility.class, Invisibility.DURATION );
		if (hero.HP < hero.HT) {
			hero.HP = Math.min( hero.HP + hero.HT / 4, hero.HT );
		}
		hero.sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
		PotionOfHealing.cure(hero);

	}


}
