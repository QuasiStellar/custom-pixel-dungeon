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

package com.qsr.customspd.items.bags;

import com.qsr.customspd.items.ArcaneResin;
import com.qsr.customspd.items.Item;
import com.qsr.customspd.items.scrolls.Scroll;
import com.qsr.customspd.items.spells.BeaconOfReturning;
import com.qsr.customspd.items.spells.Spell;
import com.qsr.customspd.assets.GeneralAsset;

public class ScrollHolder extends Bag {

	{
		image = GeneralAsset.HOLDER;
	}

	@Override
	public boolean canHold( Item item ) {
		if (item instanceof Scroll || item instanceof Spell || item instanceof ArcaneResin){
			return super.canHold(item);
		} else {
			return false;
		}
	}

	public int capacity(){
		return 19;
	}
	
	@Override
	public void onDetach( ) {
		super.onDetach();
		for (Item item : items) {
			if (item instanceof BeaconOfReturning) {
				((BeaconOfReturning) item).returnLevel = "surface";
			}
		}
	}
	
	@Override
	public int value() {
		return 40;
	}

}
