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

package com.qsr.customspd.items.weapon.melee;

import com.qsr.customspd.Assets;
import com.qsr.customspd.Dungeon;
import com.qsr.customspd.actors.Actor;
import com.qsr.customspd.actors.Char;
import com.qsr.customspd.actors.buffs.Buff;
import com.qsr.customspd.actors.buffs.FlavourBuff;
import com.qsr.customspd.actors.buffs.Invisibility;
import com.qsr.customspd.actors.hero.Hero;
import com.qsr.customspd.messages.Messages;
import com.qsr.customspd.sprites.ItemSpriteSheet;
import com.qsr.customspd.ui.AttackIndicator;
import com.qsr.customspd.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

import java.util.HashSet;

public class Sai extends MeleeWeapon {

	{
		image = ItemSpriteSheet.SAI;
		hitSound = Assets.Sounds.HIT_STAB;
		hitSoundPitch = 1.3f;

		tier = 3;
		DLY = 0.5f; //2x speed
	}

	@Override
	public int max(int lvl) {
		return  Math.round(2.5f*(tier+1)) +     //10 base, down from 20
				lvl*Math.round(0.5f*(tier+1));  //+2 per level, down from +4
	}

	@Override
	public String targetingPrompt() {
		return Messages.get(this, "prompt");
	}

	@Override
	protected void duelistAbility(Hero hero, Integer target) {
		Sai.comboStrikeAbility(hero, target, 0.25f, this);
	}

	public static void comboStrikeAbility(Hero hero, Integer target, float boostPerHit, MeleeWeapon wep){
		if (target == null) {
			return;
		}

		Char enemy = Actor.findChar(target);
		if (enemy == null || enemy == hero || hero.isCharmedBy(enemy) || !Dungeon.level.heroFOV[target]) {
			GLog.w(Messages.get(wep, "ability_no_target"));
			return;
		}

		hero.belongings.abilityWeapon = wep;
		if (!hero.canAttack(enemy)){
			GLog.w(Messages.get(wep, "ability_bad_position"));
			hero.belongings.abilityWeapon = null;
			return;
		}
		hero.belongings.abilityWeapon = null;

		hero.sprite.attack(enemy.pos, new Callback() {
			@Override
			public void call() {
				wep.beforeAbilityUsed(hero);
				AttackIndicator.target(enemy);

				HashSet<ComboStrikeTracker> buffs = hero.buffs(ComboStrikeTracker.class);
				int recentHits = buffs.size();
				for (Buff b : buffs){
					b.detach();
				}

				boolean hit = hero.attack(enemy, 1f + boostPerHit*recentHits, 0, Char.INFINITE_ACCURACY);
				if (hit && !enemy.isAlive()){
					wep.onAbilityKill(hero);
				}

				Invisibility.dispel();
				hero.spendAndNext(hero.attackDelay());
				if (recentHits >= 2 && hit){
					Sample.INSTANCE.play(Assets.Sounds.HIT_STRONG);
				}

				wep.afterAbilityUsed(hero);
			}
		});
	}

	public static class ComboStrikeTracker extends FlavourBuff{

		public static float DURATION = 5f;

	}

}
