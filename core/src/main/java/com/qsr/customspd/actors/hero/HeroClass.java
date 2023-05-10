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

package com.qsr.customspd.actors.hero;

import com.qsr.customspd.Badges;
import com.qsr.customspd.Challenges;
import com.qsr.customspd.Dungeon;
import com.qsr.customspd.QuickSlot;
import com.qsr.customspd.SPDSettings;
import com.qsr.customspd.actors.hero.abilities.ArmorAbility;
import com.qsr.customspd.actors.hero.abilities.duelist.Challenge;
import com.qsr.customspd.actors.hero.abilities.duelist.ElementalStrike;
import com.qsr.customspd.actors.hero.abilities.duelist.Feint;
import com.qsr.customspd.actors.hero.abilities.huntress.NaturesPower;
import com.qsr.customspd.actors.hero.abilities.huntress.SpectralBlades;
import com.qsr.customspd.actors.hero.abilities.huntress.SpiritHawk;
import com.qsr.customspd.actors.hero.abilities.mage.ElementalBlast;
import com.qsr.customspd.actors.hero.abilities.mage.WarpBeacon;
import com.qsr.customspd.actors.hero.abilities.mage.WildMagic;
import com.qsr.customspd.actors.hero.abilities.rogue.DeathMark;
import com.qsr.customspd.actors.hero.abilities.rogue.ShadowClone;
import com.qsr.customspd.actors.hero.abilities.rogue.SmokeBomb;
import com.qsr.customspd.actors.hero.abilities.warrior.Endure;
import com.qsr.customspd.actors.hero.abilities.warrior.HeroicLeap;
import com.qsr.customspd.actors.hero.abilities.warrior.Shockwave;
import com.qsr.customspd.assets.Asset;
import com.qsr.customspd.assets.GeneralAsset;
import com.qsr.customspd.items.BrokenSeal;
import com.qsr.customspd.items.Item;
import com.qsr.customspd.items.Waterskin;
import com.qsr.customspd.items.armor.ClothArmor;
import com.qsr.customspd.items.artifacts.CloakOfShadows;
import com.qsr.customspd.items.bags.VelvetPouch;
import com.qsr.customspd.items.food.Food;
import com.qsr.customspd.items.potions.PotionOfHealing;
import com.qsr.customspd.items.potions.PotionOfInvisibility;
import com.qsr.customspd.items.potions.PotionOfLiquidFlame;
import com.qsr.customspd.items.potions.PotionOfMindVision;
import com.qsr.customspd.items.potions.PotionOfStrength;
import com.qsr.customspd.items.scrolls.ScrollOfIdentify;
import com.qsr.customspd.items.scrolls.ScrollOfLullaby;
import com.qsr.customspd.items.scrolls.ScrollOfMagicMapping;
import com.qsr.customspd.items.scrolls.ScrollOfMirrorImage;
import com.qsr.customspd.items.scrolls.ScrollOfRage;
import com.qsr.customspd.items.scrolls.ScrollOfUpgrade;
import com.qsr.customspd.items.wands.WandOfMagicMissile;
import com.qsr.customspd.items.weapon.SpiritBow;
import com.qsr.customspd.items.weapon.melee.Dagger;
import com.qsr.customspd.items.weapon.melee.Gloves;
import com.qsr.customspd.items.weapon.melee.MagesStaff;
import com.qsr.customspd.items.weapon.melee.Rapier;
import com.qsr.customspd.items.weapon.melee.WornShortsword;
import com.qsr.customspd.items.weapon.missiles.ThrowingKnife;
import com.qsr.customspd.items.weapon.missiles.ThrowingSpike;
import com.qsr.customspd.items.weapon.missiles.ThrowingStone;
import com.qsr.customspd.messages.Messages;
import com.watabou.utils.DeviceCompat;

public enum HeroClass {

	WARRIOR( HeroSubClass.BERSERKER, HeroSubClass.GLADIATOR ),
	MAGE( HeroSubClass.BATTLEMAGE, HeroSubClass.WARLOCK ),
	ROGUE( HeroSubClass.ASSASSIN, HeroSubClass.FREERUNNER ),
	HUNTRESS( HeroSubClass.SNIPER, HeroSubClass.WARDEN ),
	DUELIST( HeroSubClass.CHAMPION, HeroSubClass.MONK );

	private HeroSubClass[] subClasses;

	HeroClass( HeroSubClass...subClasses ) {
		this.subClasses = subClasses;
	}

	public void initHero( Hero hero ) {

		hero.heroClass = this;
		Talent.initClassTalents(hero);

		Item i = new ClothArmor().identify();
		if (!Challenges.isItemBlocked(i)) hero.belongings.armor = (ClothArmor)i;

		i = new Food();
		if (!Challenges.isItemBlocked(i)) i.collect();

		new VelvetPouch().collect();
		Dungeon.LimitedDrops.VELVET_POUCH.drop();

		Waterskin waterskin = new Waterskin();
		waterskin.collect();

		new ScrollOfIdentify().identify();

		switch (this) {
			case WARRIOR:
				initWarrior( hero );
				break;

			case MAGE:
				initMage( hero );
				break;

			case ROGUE:
				initRogue( hero );
				break;

			case HUNTRESS:
				initHuntress( hero );
				break;

			case DUELIST:
				initDuelist( hero );
				break;
		}

		if (SPDSettings.quickslotWaterskin()) {
			for (int s = 0; s < QuickSlot.SIZE; s++) {
				if (Dungeon.quickslot.getItem(s) == null) {
					Dungeon.quickslot.setSlot(s, waterskin);
					break;
				}
			}
		}

	}

	public Badges.Badge masteryBadge() {
		switch (this) {
			case WARRIOR:
				return Badges.Badge.MASTERY_WARRIOR;
			case MAGE:
				return Badges.Badge.MASTERY_MAGE;
			case ROGUE:
				return Badges.Badge.MASTERY_ROGUE;
			case HUNTRESS:
				return Badges.Badge.MASTERY_HUNTRESS;
			case DUELIST:
				return Badges.Badge.MASTERY_DUELIST;
		}
		return null;
	}

	private static void initWarrior( Hero hero ) {
		(hero.belongings.weapon = new WornShortsword()).identify();
		ThrowingStone stones = new ThrowingStone();
		stones.quantity(3).collect();
		Dungeon.quickslot.setSlot(0, stones);

		if (hero.belongings.armor != null){
			hero.belongings.armor.affixSeal(new BrokenSeal());
		}

		new PotionOfHealing().identify();
		new ScrollOfRage().identify();
	}

	private static void initMage( Hero hero ) {
		MagesStaff staff;

		staff = new MagesStaff(new WandOfMagicMissile());

		(hero.belongings.weapon = staff).identify();
		hero.belongings.weapon.activate(hero);

		Dungeon.quickslot.setSlot(0, staff);

		new ScrollOfUpgrade().identify();
		new PotionOfLiquidFlame().identify();
	}

	private static void initRogue( Hero hero ) {
		(hero.belongings.weapon = new Dagger()).identify();

		CloakOfShadows cloak = new CloakOfShadows();
		(hero.belongings.artifact = cloak).identify();
		hero.belongings.artifact.activate( hero );

		ThrowingKnife knives = new ThrowingKnife();
		knives.quantity(3).collect();

		Dungeon.quickslot.setSlot(0, cloak);
		Dungeon.quickslot.setSlot(1, knives);

		new ScrollOfMagicMapping().identify();
		new PotionOfInvisibility().identify();
	}

	private static void initHuntress( Hero hero ) {

		(hero.belongings.weapon = new Gloves()).identify();
		SpiritBow bow = new SpiritBow();
		bow.identify().collect();

		Dungeon.quickslot.setSlot(0, bow);

		new PotionOfMindVision().identify();
		new ScrollOfLullaby().identify();
	}

	private static void initDuelist( Hero hero ) {

		(hero.belongings.weapon = new Rapier()).identify();
		hero.belongings.weapon.activate(hero);

		ThrowingSpike spikes = new ThrowingSpike();
		spikes.quantity(2).collect();

		Dungeon.quickslot.setSlot(0, hero.belongings.weapon);
		Dungeon.quickslot.setSlot(1, spikes);

		new PotionOfStrength().identify();
		new ScrollOfMirrorImage().identify();
	}

	public String title() {
		return Messages.get(HeroClass.class, name());
	}

	public String desc(){
		return Messages.get(HeroClass.class, name()+"_desc");
	}

	public String shortDesc(){
		return Messages.get(HeroClass.class, name()+"_desc_short");
	}

	public HeroSubClass[] subClasses() {
		return subClasses;
	}

	public ArmorAbility[] armorAbilities(){
		switch (this) {
			case WARRIOR: default:
				return new ArmorAbility[]{new HeroicLeap(), new Shockwave(), new Endure()};
			case MAGE:
				return new ArmorAbility[]{new ElementalBlast(), new WildMagic(), new WarpBeacon()};
			case ROGUE:
				return new ArmorAbility[]{new SmokeBomb(), new DeathMark(), new ShadowClone()};
			case HUNTRESS:
				return new ArmorAbility[]{new SpectralBlades(), new NaturesPower(), new SpiritHawk()};
			case DUELIST:
				return new ArmorAbility[]{new Challenge(), new ElementalStrike(), new Feint()};
		}
	}

	public String spritesheet() {
		switch (this) {
			case WARRIOR: default:
				return Asset.getAssetFilePath(GeneralAsset.WARRIOR);
			case MAGE:
				return Asset.getAssetFilePath(GeneralAsset.MAGE);
			case ROGUE:
				return Asset.getAssetFilePath(GeneralAsset.ROGUE);
			case HUNTRESS:
				return Asset.getAssetFilePath(GeneralAsset.HUNTRESS);
			case DUELIST:
				return Asset.getAssetFilePath(GeneralAsset.DUELIST);
		}
	}

	public String splashArt(){
		switch (this) {
			case WARRIOR: default:
				return Asset.getAssetFilePath(GeneralAsset.WARRIOR_SPLASH);
			case MAGE:
				return Asset.getAssetFilePath(GeneralAsset.MAGE_SPLASH);
			case ROGUE:
				return Asset.getAssetFilePath(GeneralAsset.ROGUE_SPLASH);
			case HUNTRESS:
				return Asset.getAssetFilePath(GeneralAsset.HUNTRESS_SPLASH);
			case DUELIST:
				return Asset.getAssetFilePath(GeneralAsset.DUELIST_SPLASH);
		}
	}
	
	public boolean isUnlocked(){
		//always unlock on debug builds
		if (DeviceCompat.isDebug()) return true;

		switch (this){
			case WARRIOR: default:
				return true;
			case MAGE:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_MAGE);
			case ROGUE:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_ROGUE);
			case HUNTRESS:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_HUNTRESS);
			case DUELIST:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_DUELIST);
		}
	}
	
	public String unlockMsg() {
		return shortDesc() + "\n\n" + Messages.get(HeroClass.class, name()+"_unlock");
	}

}
