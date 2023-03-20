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

package com.qsr.customspd.items.scrolls.exotic;

import com.qsr.customspd.Assets;
import com.qsr.customspd.actors.buffs.Buff;
import com.qsr.customspd.actors.buffs.Foresight;
import com.qsr.customspd.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

public class ScrollOfForesight extends ExoticScroll {
	
	{
		icon = ItemSpriteSheet.Icons.SCROLL_FORESIGHT;
	}
	
	@Override
	public void doRead() {
		Sample.INSTANCE.play( Assets.Sounds.READ );
		
		Buff.affect(curUser, Foresight.class, Foresight.DURATION);

		identify();
		
		readAnimation();
	}
	
}
