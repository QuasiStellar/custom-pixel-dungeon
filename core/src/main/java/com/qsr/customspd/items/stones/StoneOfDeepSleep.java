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

package com.qsr.customspd.items.stones;

import com.qsr.customspd.Assets;
import com.qsr.customspd.actors.Actor;
import com.qsr.customspd.actors.Char;
import com.qsr.customspd.actors.buffs.Buff;
import com.qsr.customspd.actors.buffs.MagicalSleep;
import com.qsr.customspd.actors.mobs.Mob;
import com.qsr.customspd.effects.Speck;
import com.qsr.customspd.assets.GeneralAsset;
import com.watabou.noosa.audio.Sample;

public class StoneOfDeepSleep extends Runestone {
	
	{
		image = GeneralAsset.STONE_SLEEP;
	}
	
	@Override
	protected void activate(int cell) {

		if (Actor.findChar(cell) != null) {

			Char c = Actor.findChar(cell);

			if (c instanceof Mob){

				Buff.affect(c, MagicalSleep.class);
				c.sprite.centerEmitter().start( Speck.factory( Speck.NOTE ), 0.3f, 5 );

			}

		}
		
		Sample.INSTANCE.play( Assets.Sounds.LULLABY );
		
	}
}
