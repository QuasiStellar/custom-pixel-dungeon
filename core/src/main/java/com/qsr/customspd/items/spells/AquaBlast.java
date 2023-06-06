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

package com.qsr.customspd.items.spells;

import com.qsr.customspd.actors.hero.Hero;
import com.qsr.customspd.assets.GeneralAsset;
import com.qsr.customspd.items.potions.exotic.PotionOfStormClouds;
import com.qsr.customspd.levels.traps.GeyserTrap;
import com.qsr.customspd.mechanics.Ballistica;
import com.qsr.customspd.assets.GeneralAsset;

public class AquaBlast extends TargetedSpell {
	
	{
		image = GeneralAsset.AQUA_BLAST;
		usesTargeting = true;
	}
	
	@Override
	protected void affectTarget(Ballistica bolt, Hero hero) {
		int cell = bolt.collisionPos;

		GeyserTrap geyser = new GeyserTrap();
		geyser.pos = cell;
		geyser.source = this;
		if (bolt.path.size() > bolt.dist+1) {
			geyser.centerKnockBackDirection = bolt.path.get(bolt.dist + 1);
		}
		geyser.activate();
	}
	
	@Override
	public int value() {
		//prices of ingredients, divided by output quantity, rounds down
		return (int)((60 + 40) * (quantity/8f));
	}
	
	public static class Recipe extends com.qsr.customspd.items.Recipe.SimpleRecipe {
		
		{
			inputs =  new Class[]{PotionOfStormClouds.class, ArcaneCatalyst.class};
			inQuantity = new int[]{1, 1};
			
			cost = 2;
			
			output = AquaBlast.class;
			outQuantity = 8;
		}
		
	}
}
