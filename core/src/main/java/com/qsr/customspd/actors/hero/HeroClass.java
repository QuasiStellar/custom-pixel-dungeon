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
import com.qsr.customspd.Dungeon;
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
import com.qsr.customspd.items.KindofMisc;
import com.qsr.customspd.items.Waterskin;
import com.qsr.customspd.items.armor.Armor;
import com.qsr.customspd.items.artifacts.Artifact;
import com.qsr.customspd.items.bags.MagicalHolster;
import com.qsr.customspd.items.bags.PotionBandolier;
import com.qsr.customspd.items.bags.ScrollHolder;
import com.qsr.customspd.items.bags.VelvetPouch;
import com.qsr.customspd.items.keys.Key;
import com.qsr.customspd.items.rings.Ring;
import com.qsr.customspd.items.wands.Wand;
import com.qsr.customspd.items.weapon.Weapon;
import com.qsr.customspd.items.weapon.melee.MagesStaff;
import com.qsr.customspd.items.weapon.melee.MeleeWeapon;
import com.qsr.customspd.items.weapon.missiles.darts.TippedDart;
import com.qsr.customspd.messages.Messages;
import com.qsr.customspd.modding.HeroConfig;
import com.qsr.customspd.modding.ItemDescription;
import com.qsr.customspd.modding.JsonConfigRetriever;
import com.watabou.utils.DeviceCompat;
import com.watabou.utils.Reflection;

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

	private static int curQuickslot;

	public void initHero( Hero hero ) {

		hero.heroClass = this;
		Talent.initClassTalents(hero);

		HeroConfig any = JsonConfigRetriever.INSTANCE.retrieveHeroConfig("any");

		curQuickslot = 0;
		setUp(any, hero);

		switch (this) {
			case WARRIOR:
				setUp(JsonConfigRetriever.INSTANCE.retrieveHeroConfig("warrior"), hero);
				break;

			case MAGE:
				setUp(JsonConfigRetriever.INSTANCE.retrieveHeroConfig("mage"), hero);
				break;

			case ROGUE:
				setUp(JsonConfigRetriever.INSTANCE.retrieveHeroConfig("rogue"), hero);
				break;

			case HUNTRESS:
				setUp(JsonConfigRetriever.INSTANCE.retrieveHeroConfig("huntress"), hero);
				break;

			case DUELIST:
				setUp(JsonConfigRetriever.INSTANCE.retrieveHeroConfig("duelist"), hero);
				break;
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

	private static void setUp(HeroConfig config, Hero hero) {
		if (config.getArmor() != null) {
			Armor armor = (Armor) Reflection.newInstance(Reflection.forName("com.qsr.customspd.items." + config.getArmor().getType()));
			if (config.getArmor().getSeal()) armor.affixSeal(new BrokenSeal());
			armor.level(config.getArmor().getLevel());
			if (config.getArmor().getIdentified()) armor.identify();
			if (config.getArmor().getCursed()) armor.cursed = true;
			if (config.getArmor().getEnchantment() != null) {
				armor.inscribe((Armor.Glyph) Reflection.newInstance(Reflection.forName("com.qsr.customspd.items.armor." + config.getArmor().getEnchantment())));
			}
			hero.belongings.armor = armor;
			if (config.getArmor().getQuickslot()) Dungeon.quickslot.setSlot(curQuickslot++, armor);
		}

		if (config.getWeapon() != null) {
			MeleeWeapon weapon = (MeleeWeapon) Reflection.newInstance(Reflection.forName("com.qsr.customspd.items." + config.getWeapon().getType()));
			if (config.getWeapon().getCoreWand() != null && weapon instanceof MagesStaff) weapon = new MagesStaff((Wand) Reflection.newInstance(Reflection.forName("com.qsr.customspd.items." + config.getWeapon().getCoreWand())));
			weapon.level(config.getWeapon().getLevel());
			if (config.getWeapon().getIdentified()) weapon.identify();
			if (config.getWeapon().getCursed()) weapon.cursed = true;
			if (config.getWeapon().getEnchantment() != null) {
				weapon.enchant((Weapon.Enchantment) Reflection.newInstance(Reflection.forName("com.qsr.customspd.items.weapon." + config.getWeapon().getEnchantment())));
			}
			hero.belongings.weapon = weapon;
			hero.belongings.weapon.activate(hero);
			if (config.getWeapon().getQuickslot()) Dungeon.quickslot.setSlot(curQuickslot++, weapon);
		}

		if (config.getArtifact() != null) {
			Artifact artifact = (Artifact) Reflection.newInstance(Reflection.forName("com.qsr.customspd.items." + config.getArtifact().getType()));
			artifact.level(config.getArtifact().getLevel());
			if (config.getArtifact().getIdentified()) artifact.identify();
			if (config.getArtifact().getCursed()) artifact.cursed = true;
			hero.belongings.artifact = artifact;
			hero.belongings.artifact.activate(hero);
			if (config.getArtifact().getQuickslot()) Dungeon.quickslot.setSlot(curQuickslot++, artifact);
		}

		if (config.getMisc() != null) {
			KindofMisc misc = (KindofMisc) Reflection.newInstance(Reflection.forName("com.qsr.customspd.items." + config.getMisc().getType()));
			misc.level(config.getMisc().getLevel());
			if (config.getMisc().getIdentified()) misc.identify();
			if (config.getMisc().getCursed()) misc.cursed = true;
			hero.belongings.misc = misc;
			hero.belongings.misc.activate(hero);
			if (config.getMisc().getQuickslot()) Dungeon.quickslot.setSlot(curQuickslot++, misc);
		}

		if (config.getRing() != null) {
			Ring ring = (Ring) Reflection.newInstance(Reflection.forName("com.qsr.customspd.items." + config.getRing().getType()));
			ring.level(config.getRing().getLevel());
			if (config.getRing().getIdentified()) ring.identify();
			if (config.getRing().getCursed()) ring.cursed = true;
			hero.belongings.ring = ring;
			if (config.getRing().getQuickslot()) Dungeon.quickslot.setSlot(curQuickslot++, ring);
		}

		for (ItemDescription itemDesc : config.getItems()) {
			Item item;
			if (itemDesc.getType().equals("weapon.missiles.darts.TippedDart")) {
				item = TippedDart.randomTipped(1);
			} else {
				item = (Item) Reflection.newInstance(Reflection.forName("com.qsr.customspd.items." + itemDesc.getType()));
			}
			if (itemDesc.getSeal() && item instanceof Armor) ((Armor)item).affixSeal(new BrokenSeal());
			if (itemDesc.getCoreWand() != null && item instanceof MagesStaff) item = new MagesStaff((Wand) Reflection.newInstance(Reflection.forName("com.qsr.customspd.items." + itemDesc.getCoreWand())));
			item.quantity(itemDesc.getQuantity());
			item.level(itemDesc.getLevel());
			if (itemDesc.getIdentified()) item.identify();
			if (itemDesc.getCursed()) item.cursed = true;
			if (itemDesc.getEnchantment() != null) {
				if (item instanceof Armor) {
					if (itemDesc.getEnchantment().equals("none")) {
						((Armor) item).inscribe(null);
					} else {
						((Armor) item).inscribe((Armor.Glyph) Reflection.newInstance(Reflection.forName("com.qsr.customspd.items.armor." + itemDesc.getEnchantment())));
					}
				} else if (item instanceof Weapon) {
					if (itemDesc.getEnchantment().equals("none")) {
						((Weapon) item).enchant(null);
					} else {
						((Weapon) item).enchant((Weapon.Enchantment) Reflection.newInstance(Reflection.forName("com.qsr.customspd.items.weapon." + itemDesc.getEnchantment())));
					}
				}
			}
			if (item instanceof Key) {
				if (itemDesc.getLevelName() == null) {
					((Key) item).levelName = Dungeon.levelName;
				} else {
					((Key) item).levelName = itemDesc.getLevelName();
				}
			}
			item.collect();
			if (item instanceof VelvetPouch) Dungeon.LimitedDrops.VELVET_POUCH.drop();
			else if (item instanceof PotionBandolier) Dungeon.LimitedDrops.POTION_BANDOLIER.drop();
			else if (item instanceof ScrollHolder) Dungeon.LimitedDrops.SCROLL_HOLDER.drop();
			else if (item instanceof MagicalHolster) Dungeon.LimitedDrops.MAGICAL_HOLSTER.drop();
			if (itemDesc.getQuickslot()) Dungeon.quickslot.setSlot(curQuickslot++, item);
			else if (SPDSettings.quickslotWaterskin() && item instanceof Waterskin) {
				Dungeon.quickslot.setSlot(curQuickslot++, item);
			}
		}

		for (String itemType : config.getIdentified()) {
			((Item) Reflection.newInstance(Reflection.forName("com.qsr.customspd.items." + itemType))).identify();
		}
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

	public GeneralAsset asset() {
		switch (this) {
			case WARRIOR: default:
				return GeneralAsset.WARRIOR;
			case MAGE:
				return GeneralAsset.MAGE;
			case ROGUE:
				return GeneralAsset.ROGUE;
			case HUNTRESS:
				return GeneralAsset.HUNTRESS;
			case DUELIST:
				return GeneralAsset.DUELIST;
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
		// actually... always unlock!
		return true;

//		switch (this){
//			case WARRIOR: default:
//				return true;
//			case MAGE:
//				return Badges.isUnlocked(Badges.Badge.UNLOCK_MAGE);
//			case ROGUE:
//				return Badges.isUnlocked(Badges.Badge.UNLOCK_ROGUE);
//			case HUNTRESS:
//				return Badges.isUnlocked(Badges.Badge.UNLOCK_HUNTRESS);
//			case DUELIST:
//				return Badges.isUnlocked(Badges.Badge.UNLOCK_DUELIST);
//		}
	}
	
	public String unlockMsg() {
		return shortDesc() + "\n\n" + Messages.get(HeroClass.class, name()+"_unlock");
	}

}
