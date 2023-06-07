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

import com.qsr.customspd.Badges;
import com.qsr.customspd.Dungeon;
import com.qsr.customspd.assets.Asset;
import com.qsr.customspd.effects.Splash;
import com.qsr.customspd.items.weapon.curses.Sacrificial;
import com.qsr.customspd.items.weapon.melee.MeleeWeapon;
import com.qsr.customspd.items.weapon.melee.Sickle;
import com.qsr.customspd.levels.features.Chasm;
import com.qsr.customspd.messages.Messages;
import com.qsr.customspd.ui.BuffIndicator;
import com.qsr.customspd.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.PointF;

import static com.watabou.utils.Random.NormalFloat;

import kotlin.Pair;

public class Bleeding extends Buff {

	{
		type = buffType.NEGATIVE;
		announced = true;
	}
	
	protected float level;

	//used in specific cases where the source of the bleed is important for death logic
	private Class source;

	public float level(){
		return level;
	}
	
	private static final String LEVEL	= "level";
	private static final String SOURCE	= "source";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( LEVEL, level );
		bundle.put( SOURCE, source );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		level = bundle.getFloat( LEVEL );
		source = bundle.getClass( SOURCE );
	}
	
	public void set( float level ) {
		set( level, null );
	}

	public void set( float level, Class source ){
		if (this.level < level) {
			this.level = Math.max(this.level, level);
			this.source = source;
		}
	}
	
	@Override
	public Pair<Asset, Asset> icon() {
		return BuffIndicator.BLEEDING;
	}

	@Override
	public String iconTextDisplay() {
		return Integer.toString(Math.round(level));
	}
	
	@Override
	public boolean act() {
		if (target.isAlive()) {
			
			level = NormalFloat(level / 2f, level);
			int dmg = Math.round(level);
			
			if (dmg > 0) {
				
				target.damage( dmg, this );
				if (target.sprite.visible) {
					Splash.at( target.sprite.center(), -PointF.PI / 2, PointF.PI / 6,
							target.sprite.blood(), Math.min( 10 * dmg / target.HT, 10 ) );
				}
				
				if (target == Dungeon.hero && !target.isAlive()) {
					if (source == Chasm.class){
						Badges.validateDeathFromFalling();
					} else if (source == Sacrificial.class){
						Badges.validateDeathFromFriendlyMagic();
					}
					Dungeon.fail( this );
					GLog.n( Messages.get(this, "ondeath") );
				}

				if (source == Sickle.HarvestBleedTracker.class && !target.isAlive()){
					MeleeWeapon.onAbilityKill(Dungeon.hero, target);
				}

				spend( TICK );
			} else {
				detach();
			}
			
		} else {
			
			detach();
			
		}
		
		return true;
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", Math.round(level));
	}
}
