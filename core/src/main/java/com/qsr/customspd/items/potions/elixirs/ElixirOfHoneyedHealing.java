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

package com.qsr.customspd.items.potions.elixirs;

import com.qsr.customspd.Assets;
import com.qsr.customspd.Dungeon;
import com.qsr.customspd.actors.Actor;
import com.qsr.customspd.actors.Char;
import com.qsr.customspd.actors.buffs.Buff;
import com.qsr.customspd.actors.buffs.Hunger;
import com.qsr.customspd.actors.hero.Hero;
import com.qsr.customspd.actors.hero.Talent;
import com.qsr.customspd.actors.mobs.Bee;
import com.qsr.customspd.items.Honeypot;
import com.qsr.customspd.items.potions.PotionOfHealing;
import com.qsr.customspd.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

public class ElixirOfHoneyedHealing extends Elixir {
	
	{
		image = ItemSpriteSheet.ELIXIR_HONEY;
	}
	
	@Override
	public void apply(Hero hero) {
		PotionOfHealing.cure(hero);
		PotionOfHealing.heal(hero);
		Talent.onHealingPotionUsed( hero );
		Buff.affect(hero, Hunger.class).satisfy(Hunger.HUNGRY/2f);
		Talent.onFoodEaten(hero, Hunger.HUNGRY/2f, this);
	}
	
	@Override
	public void shatter(int cell) {
		if (Dungeon.level.heroFOV[cell]) {
			Sample.INSTANCE.play( Assets.Sounds.SHATTER );
			splash( cell );
		}
		
		Char ch = Actor.findChar(cell);
		if (ch != null){
			PotionOfHealing.cure(ch);
			PotionOfHealing.heal(ch);
			if (ch instanceof Bee && ch.alignment != curUser.alignment){
				ch.alignment = Char.Alignment.ALLY;
				((Bee)ch).setPotInfo(-1, null);
			}
		}
	}
	
	@Override
	public int value() {
		//prices of ingredients
		return quantity * (30 + 5);
	}
	
	public static class Recipe extends com.qsr.customspd.items.Recipe.SimpleRecipe {
		
		{
			inputs =  new Class[]{PotionOfHealing.class, Honeypot.ShatteredPot.class};
			inQuantity = new int[]{1, 1};
			
			cost = 4;
			
			output = ElixirOfHoneyedHealing.class;
			outQuantity = 1;
		}
		
	}
}
