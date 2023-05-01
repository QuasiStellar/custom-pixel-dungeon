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

package com.qsr.customspd.items.weapon.missiles.darts;

import com.qsr.customspd.Dungeon;
import com.qsr.customspd.actors.Char;
import com.qsr.customspd.actors.buffs.Buff;
import com.qsr.customspd.actors.buffs.ChampionEnemy;
import com.qsr.customspd.actors.buffs.FlavourBuff;
import com.qsr.customspd.actors.mobs.Mob;
import com.qsr.customspd.items.potions.exotic.PotionOfCleansing;
import com.qsr.customspd.items.weapon.melee.Crossbow;
import com.qsr.customspd.assets.GeneralAsset;

public class CleansingDart extends TippedDart {
	
	{
		image = GeneralAsset.CLEANSING_DART;
	}
	
	@Override
	public int proc(Char attacker, final Char defender, int damage) {

		if (attacker.alignment == defender.alignment && defender != attacker){
			PotionOfCleansing.cleanse(defender, PotionOfCleansing.Cleanse.DURATION*2f);
			return 0;
		} else {
			for (Buff b : defender.buffs()){
				if (!(b instanceof ChampionEnemy)
						&& b.type == Buff.buffType.POSITIVE
						&& !(b instanceof Crossbow.ChargedShot)){
					b.detach();
				}
			}
			//for when cleansed effects were keeping defender alive (e.g. raging brutes)
			if (!defender.isAlive()){
				defender.die(attacker);
				return super.proc(attacker, defender, damage);
			}
			if (defender instanceof Mob) {
				//need to delay this so damage from the dart doesn't break wandering
				new FlavourBuff(){
					{actPriority = VFX_PRIO;}
					public boolean act() {
						if (((Mob) defender).state == ((Mob) defender).HUNTING || ((Mob) defender).state == ((Mob) defender).FLEEING){
							((Mob) defender).state = ((Mob) defender).WANDERING;
						}
						((Mob) defender).beckon(Dungeon.level.randomDestination(defender));
						defender.sprite.showLost();
						return super.act();
					}
				}.attachTo(defender);
			}
		}

		return super.proc(attacker, defender, damage);
	}
}
