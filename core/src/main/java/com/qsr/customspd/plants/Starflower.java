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

package com.qsr.customspd.plants;

import com.qsr.customspd.actors.Char;
import com.qsr.customspd.actors.buffs.Bless;
import com.qsr.customspd.actors.buffs.Buff;
import com.qsr.customspd.actors.buffs.Recharging;
import com.qsr.customspd.actors.hero.Hero;
import com.qsr.customspd.actors.hero.HeroSubClass;
import com.qsr.customspd.effects.Flare;
import com.qsr.customspd.effects.SpellSprite;
import com.qsr.customspd.assets.GeneralAsset;
import com.qsr.customspd.assets.GeneralAsset;

public class Starflower extends Plant {

	{
		image = 9;
		seedClass = Seed.class;
	}

	@Override
	public void activate( Char ch ) {

		if (ch != null) {
			Buff.prolong(ch, Bless.class, Bless.DURATION);
			new Flare( 6, 32 ).color(0xFFFF00, true).show( ch.sprite, 2f );
			if (ch instanceof Hero && ((Hero) ch).subClass == HeroSubClass.WARDEN){
				Buff.prolong(ch, Recharging.class, Recharging.DURATION);
				SpellSprite.show( ch, GeneralAsset.CHARGE );
			}
		}

	}

	public static class Seed extends Plant.Seed{

		{
			image = GeneralAsset.SEED_STARFLOWER;

			plantClass = Starflower.class;
		}
		
		@Override
		public int value() {
			return 30 * quantity;
		}

		@Override
		public int energyVal() {
			return 3 * quantity;
		}
	}
}
