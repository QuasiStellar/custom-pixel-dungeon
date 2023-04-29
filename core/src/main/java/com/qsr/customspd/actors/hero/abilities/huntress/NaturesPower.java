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

package com.qsr.customspd.actors.hero.abilities.huntress;

import com.qsr.customspd.Assets;
import com.qsr.customspd.actors.Actor;
import com.qsr.customspd.actors.buffs.Buff;
import com.qsr.customspd.actors.buffs.FlavourBuff;
import com.qsr.customspd.actors.buffs.Invisibility;
import com.qsr.customspd.actors.hero.Hero;
import com.qsr.customspd.actors.hero.Talent;
import com.qsr.customspd.actors.hero.abilities.ArmorAbility;
import com.qsr.customspd.assets.Asset;
import com.qsr.customspd.effects.particles.LeafParticle;
import com.qsr.customspd.items.armor.ClassArmor;
import com.qsr.customspd.ui.BuffIndicator;
import com.qsr.customspd.ui.HeroIcon;
import com.watabou.noosa.audio.Sample;

import kotlin.Pair;

public class NaturesPower extends ArmorAbility {

	{
		baseChargeUse = 35f;
	}

	@Override
	protected void activate(ClassArmor armor, Hero hero, Integer target) {

		Buff.prolong(hero, naturesPowerTracker.class, naturesPowerTracker.DURATION);
		hero.buff(naturesPowerTracker.class).extensionsLeft = 2;
		hero.sprite.operate(hero.pos);
		Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);
		hero.sprite.emitter().burst(LeafParticle.GENERAL, 10);

		armor.charge -= chargeUse(hero);
		armor.updateQuickslot();
		Invisibility.dispel();
		hero.spendAndNext(Actor.TICK);

	}

	@Override
	public Asset icon() {
		return HeroIcon.NATURES_POWER;
	}

	@Override
	public Talent[] talents() {
		return new Talent[]{Talent.GROWING_POWER, Talent.NATURES_WRATH, Talent.WILD_MOMENTUM, Talent.HEROIC_ENERGY};
	}

	public static class naturesPowerTracker extends FlavourBuff{

		{
			type = buffType.POSITIVE;
		}

		public static final float DURATION = 8f;

		public int extensionsLeft = 2;

		public void extend( int turns ){
			if (extensionsLeft > 0 && turns > 0) {
				spend(turns);
				extensionsLeft--;
			}
		}

		@Override
		public Pair<Asset, Asset> icon() {
			return BuffIndicator.NATURE_POWER;
		}

		@Override
		public float iconFadePercent() {
			return Math.max(0, (DURATION - visualcooldown()) / DURATION);
		}

	}
}
