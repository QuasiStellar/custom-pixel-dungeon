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

package com.qsr.customspd.items.potions.exotic;

import com.qsr.customspd.actors.buffs.Buff;
import com.qsr.customspd.actors.buffs.Stamina;
import com.qsr.customspd.actors.hero.Hero;
import com.qsr.customspd.effects.SpellSprite;
import com.qsr.customspd.sprites.ItemSpriteSheet;

public class PotionOfStamina extends ExoticPotion {
	
	{
		icon = ItemSpriteSheet.Icons.POTION_STAMINA;
	}
	
	@Override
	public void apply(Hero hero) {
		identify();
		
		Buff.affect(hero, Stamina.class, Stamina.DURATION);
		SpellSprite.show(hero, SpellSprite.HASTE, 0.5f, 1, 0.5f);
	}
	
}
