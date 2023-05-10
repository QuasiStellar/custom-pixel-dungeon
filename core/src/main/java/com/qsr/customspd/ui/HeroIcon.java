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

package com.qsr.customspd.ui;

import com.qsr.customspd.actors.hero.HeroSubClass;
import com.qsr.customspd.actors.hero.abilities.ArmorAbility;
import com.qsr.customspd.assets.Asset;
import com.qsr.customspd.assets.GeneralAsset;
import com.watabou.noosa.Image;

//icons for hero subclasses and abilities atm, maybe add classes?
public class HeroIcon extends Image {

	//transparent icon
	public static final Asset NONE    = GeneralAsset.BLANK;

	//subclasses
	public static final Asset BERSERKER   = GeneralAsset.HERO_ICON_BERSERKER;
	public static final Asset GLADIATOR   = GeneralAsset.HERO_ICON_GLADIATOR;
	public static final Asset BATTLEMAGE  = GeneralAsset.HERO_ICON_BATTLEMAGE;
	public static final Asset WARLOCK     = GeneralAsset.HERO_ICON_WARLOCK;
	public static final Asset ASSASSIN    = GeneralAsset.HERO_ICON_ASSASSIN;
	public static final Asset FREERUNNER  = GeneralAsset.HERO_ICON_FREERUNNER;
	public static final Asset SNIPER      = GeneralAsset.HERO_ICON_SNIPER;
	public static final Asset WARDEN      = GeneralAsset.HERO_ICON_WARDEN;
	public static final Asset CHAMPION    = GeneralAsset.HERO_ICON_CHAMPION;
	public static final Asset MONK        = GeneralAsset.HERO_ICON_MONK;

	//abilities
	public static final Asset HEROIC_LEAP     = GeneralAsset.HERO_ICON_HEROIC_LEAP;
	public static final Asset SHOCKWAVE       = GeneralAsset.HERO_ICON_SHOCKWAVE;
	public static final Asset ENDURE          = GeneralAsset.HERO_ICON_ENDURE;
	public static final Asset ELEMENTAL_BLAST = GeneralAsset.HERO_ICON_ELEMENTAL_BLAST;
	public static final Asset WILD_MAGIC      = GeneralAsset.HERO_ICON_WILD_MAGIC;
	public static final Asset WARP_BEACON     = GeneralAsset.HERO_ICON_WARP_BEACON;
	public static final Asset SMOKE_BOMB      = GeneralAsset.HERO_ICON_SMOKE_BOMB;
	public static final Asset DEATH_MARK      = GeneralAsset.HERO_ICON_DEATH_MARK;
	public static final Asset SHADOW_CLONE    = GeneralAsset.HERO_ICON_SHADOW_CLONE;
	public static final Asset SPECTRAL_BLADES = GeneralAsset.HERO_ICON_SPECTRAL_BLADES;
	public static final Asset NATURES_POWER   = GeneralAsset.HERO_ICON_NATURES_POWER;
	public static final Asset SPIRIT_HAWK     = GeneralAsset.HERO_ICON_SPIRIT_HAWK;
	public static final Asset CHALLENGE       = GeneralAsset.HERO_ICON_CHALLENGE;
	public static final Asset ELEMENTAL_STRIKE= GeneralAsset.HERO_ICON_ELEMENTAL_STRIKE;
	public static final Asset FEINT           = GeneralAsset.HERO_ICON_FEINT;
	public static final Asset RATMOGRIFY      = GeneralAsset.HERO_ICON_RATMOGRIFY;

	//action indicator visuals
	public static final Asset BERSERK         = GeneralAsset.HERO_ICON_BERSERK;
	public static final Asset COMBO           = GeneralAsset.HERO_ICON_COMBO;
	public static final Asset PREPARATION     = GeneralAsset.HERO_ICON_PREPARATION;
	public static final Asset MOMENTUM        = GeneralAsset.HERO_ICON_MOMENTUM;
	public static final Asset SNIPERS_MARK    = GeneralAsset.HERO_ICON_SNIPERS_MARK;
	public static final Asset WEAPON_SWAP     = GeneralAsset.HERO_ICON_WEAPON_SWAP;
	public static final Asset MONK_ABILITIES  = GeneralAsset.HERO_ICON_MONK_ABILITIES;

	public HeroIcon(HeroSubClass subCls) {
		super( Asset.getAssetFilePath(subCls.icon()) );
	}

	public HeroIcon(ArmorAbility ability) {
		super( Asset.getAssetFilePath(ability.icon()) );
	}

	public HeroIcon(ActionIndicator.Action action){
		super( Asset.getAssetFilePath(action.actionIcon()) );
	}
}
