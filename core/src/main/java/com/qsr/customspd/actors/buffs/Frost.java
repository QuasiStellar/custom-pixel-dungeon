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

package com.qsr.customspd.actors.buffs;

import com.qsr.customspd.Dungeon;
import com.qsr.customspd.actors.Char;
import com.qsr.customspd.actors.hero.Hero;
import com.qsr.customspd.actors.mobs.Thief;
import com.qsr.customspd.assets.Asset;
import com.qsr.customspd.items.Item;
import com.qsr.customspd.items.food.FrozenCarpaccio;
import com.qsr.customspd.items.food.MysteryMeat;
import com.qsr.customspd.items.potions.Potion;
import com.qsr.customspd.messages.Messages;
import com.qsr.customspd.sprites.CharSprite;
import com.qsr.customspd.ui.BuffIndicator;
import com.qsr.customspd.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.utils.Random;

import java.util.ArrayList;

import kotlin.Pair;

public class Frost extends FlavourBuff {

	public static final float DURATION	= 10f;

	{
		type = buffType.NEGATIVE;
		announced = true;
	}
	
	@Override
	public boolean attachTo( Char target ) {
		Buff.detach( target, Burning.class );

		if (super.attachTo( target )) {
			
			target.paralysed++;
			Buff.detach( target, Chill.class );

			if (target instanceof Hero) {

				Hero hero = (Hero)target;
				ArrayList<Item> freezable = new ArrayList<>();
				//does not reach inside of containers
				if (hero.buff(LostInventory.class) == null) {
					for (Item i : hero.belongings.backpack.items) {
						if (!i.unique && (i instanceof Potion || i instanceof MysteryMeat)) {
							freezable.add(i);
						}
					}
				}
				
				if (!freezable.isEmpty()){
					Item toFreeze = Random.element(freezable).detach( hero.belongings.backpack );
					GLog.w( Messages.capitalize(Messages.get(this, "freezes", toFreeze.title())) );
					if (toFreeze instanceof Potion){
						((Potion) toFreeze).shatter(hero.pos);
					} else if (toFreeze instanceof MysteryMeat){
						FrozenCarpaccio carpaccio = new FrozenCarpaccio();
						if (!carpaccio.collect( hero.belongings.backpack )) {
							Dungeon.level.drop( carpaccio, target.pos ).sprite.drop();
						}
					}
				}
				
			} else if (target instanceof Thief) {

				Item item = ((Thief) target).item;

				if (item instanceof Potion && !item.unique) {
					((Potion) ((Thief) target).item).shatter(target.pos);
					((Thief) target).item = null;
				} else if (item instanceof MysteryMeat){
					((Thief) target).item = new FrozenCarpaccio();
				}

			}

			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void detach() {
		super.detach();
		if (target.paralysed > 0)
			target.paralysed--;
		if (Dungeon.level.water[target.pos])
			Buff.prolong(target, Chill.class, Chill.DURATION/2f);
	}
	
	@Override
	public Pair<Asset, Asset> icon() {
		return BuffIndicator.FROST;
	}

	@Override
	public void tintIcon(Image icon) {
		icon.hardlight(0f, 0.75f, 1f);
	}

	@Override
	public float iconFadePercent() {
		return Math.max(0, (DURATION - visualcooldown()) / DURATION);
	}

	@Override
	public void fx(boolean on) {
		if (on) {
			target.sprite.add(CharSprite.State.FROZEN);
			target.sprite.add(CharSprite.State.PARALYSED);
		} else {
			target.sprite.remove(CharSprite.State.FROZEN);
			if (target.paralysed <= 1) target.sprite.remove(CharSprite.State.PARALYSED);
		}
	}

	{
		//can't chill what's frozen!
		immunities.add( Chill.class );
	}

}
