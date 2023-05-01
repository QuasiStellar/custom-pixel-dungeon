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

package com.qsr.customspd.items.potions.brews;

import com.qsr.customspd.Assets;
import com.qsr.customspd.Dungeon;
import com.qsr.customspd.actors.blobs.Blizzard;
import com.qsr.customspd.actors.blobs.Blob;
import com.qsr.customspd.items.potions.AlchemicalCatalyst;
import com.qsr.customspd.items.potions.PotionOfFrost;
import com.qsr.customspd.scenes.GameScene;
import com.qsr.customspd.assets.GeneralAsset;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

public class BlizzardBrew extends Brew {
	
	{
		image = GeneralAsset.BREW_BLIZZARD;
	}
	
	@Override
	public void shatter(int cell) {
		if (Dungeon.level.heroFOV[cell]) {
			splash( cell );
			Sample.INSTANCE.play( Assets.Sounds.SHATTER );
			Sample.INSTANCE.play( Assets.Sounds.GAS );
		}

		int centerVolume = 120;
		for (int i : PathFinder.NEIGHBOURS8){
			if (!Dungeon.level.solid[cell+i]){
				GameScene.add( Blob.seed( cell+i, 120, Blizzard.class ) );
			} else {
				centerVolume += 120;
			}
		}

		GameScene.add( Blob.seed( cell, centerVolume, Blizzard.class ) );
	}
	
	@Override
	public int value() {
		//prices of ingredients
		return quantity * (30 + 40);
	}
	
	public static class Recipe extends com.qsr.customspd.items.Recipe.SimpleRecipe {
		
		{
			inputs =  new Class[]{PotionOfFrost.class, AlchemicalCatalyst.class};
			inQuantity = new int[]{1, 1};
			
			cost = 3;
			
			output = BlizzardBrew.class;
			outQuantity = 1;
		}
		
	}
}
