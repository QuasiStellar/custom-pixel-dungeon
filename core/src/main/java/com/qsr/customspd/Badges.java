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

package com.qsr.customspd;

import com.qsr.customspd.actors.hero.HeroClass;
import com.qsr.customspd.actors.hero.HeroSubClass;
import com.qsr.customspd.actors.mobs.DM300;
import com.qsr.customspd.actors.mobs.DwarfKing;
import com.qsr.customspd.actors.mobs.Goo;
import com.qsr.customspd.actors.mobs.Tengu;
import com.qsr.customspd.actors.mobs.YogDzewa;
import com.qsr.customspd.assets.Asset;
import com.qsr.customspd.assets.GeneralAsset;
import com.qsr.customspd.items.Item;
import com.qsr.customspd.items.artifacts.Artifact;
import com.qsr.customspd.items.bags.MagicalHolster;
import com.qsr.customspd.items.bags.PotionBandolier;
import com.qsr.customspd.items.bags.ScrollHolder;
import com.qsr.customspd.items.bags.VelvetPouch;
import com.qsr.customspd.items.weapon.melee.MeleeWeapon;
import com.qsr.customspd.journal.Catalog;
import com.qsr.customspd.messages.Messages;
import com.qsr.customspd.scenes.PixelScene;
import com.qsr.customspd.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.FileUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class Badges {

	public enum Badge {
		MASTERY_WARRIOR,
		MASTERY_MAGE,
		MASTERY_ROGUE,
		MASTERY_HUNTRESS,
		MASTERY_DUELIST,
		FOUND_RATMOGRIFY,

		//bronze
		UNLOCK_MAGE                 ( GeneralAsset.UNLOCK_MAGE ),
		UNLOCK_ROGUE                ( GeneralAsset.UNLOCK_ROGUE ),
		UNLOCK_HUNTRESS             ( GeneralAsset.UNLOCK_HUNTRESS ),
		UNLOCK_DUELIST              ( GeneralAsset.UNLOCK_DUELIST ),
		//UNLOCK_CLERIC             ( 5 ),
		MONSTERS_SLAIN_1            ( GeneralAsset.MONSTERS_SLAIN_1 ),
		MONSTERS_SLAIN_2            ( GeneralAsset.MONSTERS_SLAIN_2 ),
		GOLD_COLLECTED_1            ( GeneralAsset.GOLD_COLLECTED_1 ),
		GOLD_COLLECTED_2            ( GeneralAsset.GOLD_COLLECTED_2 ),
		ITEM_LEVEL_1                ( GeneralAsset.ITEM_LEVEL_1 ),
		LEVEL_REACHED_1             ( GeneralAsset.LEVEL_REACHED_1 ),
		STRENGTH_ATTAINED_1         ( GeneralAsset.STRENGTH_ATTAINED_1 ),
		FOOD_EATEN_1                ( GeneralAsset.FOOD_EATEN_1 ),
		ITEMS_CRAFTED_1             ( GeneralAsset.ITEMS_CRAFTED_1 ),
		BOSS_SLAIN_1                ( GeneralAsset.BOSS_SLAIN_1 ),
		DEATH_FROM_FIRE             ( GeneralAsset.DEATH_FROM_FIRE ),
		DEATH_FROM_POISON           ( GeneralAsset.DEATH_FROM_POISON ),
		DEATH_FROM_GAS              ( GeneralAsset.DEATH_FROM_GAS ),
		DEATH_FROM_HUNGER           ( GeneralAsset.DEATH_FROM_HUNGER ),
		DEATH_FROM_FALLING          ( GeneralAsset.DEATH_FROM_FALLING ),
		GAMES_PLAYED_1              ( GeneralAsset.GAMES_PLAYED_1, true ),
		HIGH_SCORE_1                ( GeneralAsset.HIGH_SCORE_1 ),

		//silver
		NO_MONSTERS_SLAIN           ( GeneralAsset.NO_MONSTERS_SLAIN ),
		MONSTERS_SLAIN_3            ( GeneralAsset.MONSTERS_SLAIN_3 ),
		MONSTERS_SLAIN_4            ( GeneralAsset.MONSTERS_SLAIN_4 ),
		GOLD_COLLECTED_3            ( GeneralAsset.GOLD_COLLECTED_3 ),
		GOLD_COLLECTED_4            ( GeneralAsset.GOLD_COLLECTED_4 ),
		ITEM_LEVEL_2                ( GeneralAsset.ITEM_LEVEL_2 ),
		ITEM_LEVEL_3                ( GeneralAsset.ITEM_LEVEL_3 ),
		LEVEL_REACHED_2             ( GeneralAsset.LEVEL_REACHED_2 ),
		LEVEL_REACHED_3             ( GeneralAsset.LEVEL_REACHED_3 ),
		STRENGTH_ATTAINED_2         ( GeneralAsset.STRENGTH_ATTAINED_2 ),
		STRENGTH_ATTAINED_3         ( GeneralAsset.STRENGTH_ATTAINED_3 ),
		FOOD_EATEN_2                ( GeneralAsset.FOOD_EATEN_2 ),
		FOOD_EATEN_3                ( GeneralAsset.FOOD_EATEN_3 ),
		ITEMS_CRAFTED_2             ( GeneralAsset.ITEMS_CRAFTED_2 ),
		ITEMS_CRAFTED_3             ( GeneralAsset.ITEMS_CRAFTED_3 ),
		BOSS_SLAIN_2                ( GeneralAsset.BOSS_SLAIN_2 ),
		BOSS_SLAIN_3                ( GeneralAsset.BOSS_SLAIN_3 ),
		ALL_POTIONS_IDENTIFIED      ( GeneralAsset.ALL_POTIONS_IDENTIFIED ),
		ALL_SCROLLS_IDENTIFIED      ( GeneralAsset.ALL_SCROLLS_IDENTIFIED ),
		DEATH_FROM_ENEMY_MAGIC      ( GeneralAsset.DEATH_FROM_ENEMY_MAGIC ),
		DEATH_FROM_FRIENDLY_MAGIC   ( GeneralAsset.DEATH_FROM_FRIENDLY_MAGIC ),
		DEATH_FROM_SACRIFICE        ( GeneralAsset.DEATH_FROM_SACRIFICE ),
		BOSS_SLAIN_1_WARRIOR,
		BOSS_SLAIN_1_MAGE,
		BOSS_SLAIN_1_ROGUE,
		BOSS_SLAIN_1_HUNTRESS,
		BOSS_SLAIN_1_DUELIST,
		BOSS_SLAIN_1_ALL_CLASSES    ( GeneralAsset.BOSS_SLAIN_1_ALL_CLASSES, true ),
		GAMES_PLAYED_2              ( GeneralAsset.GAMES_PLAYED_2, true ),
		HIGH_SCORE_2                ( GeneralAsset.HIGH_SCORE_2 ),

		//gold
		PIRANHAS                    ( GeneralAsset.PIRANHAS ),
		GRIM_WEAPON                 ( GeneralAsset.GRIM_WEAPON ),
		BAG_BOUGHT_VELVET_POUCH,
		BAG_BOUGHT_SCROLL_HOLDER,
		BAG_BOUGHT_POTION_BANDOLIER,
		BAG_BOUGHT_MAGICAL_HOLSTER,
		ALL_BAGS_BOUGHT             ( GeneralAsset.ALL_BAGS_BOUGHT ),
		MASTERY_COMBO               ( GeneralAsset.MASTERY_COMBO ),
		MONSTERS_SLAIN_5            ( GeneralAsset.MONSTERS_SLAIN_5 ),
		GOLD_COLLECTED_5            ( GeneralAsset.GOLD_COLLECTED_5 ),
		ITEM_LEVEL_4                ( GeneralAsset.ITEM_LEVEL_4 ),
		LEVEL_REACHED_4             ( GeneralAsset.LEVEL_REACHED_4 ),
		STRENGTH_ATTAINED_4         ( GeneralAsset.STRENGTH_ATTAINED_4 ),
		STRENGTH_ATTAINED_5         ( GeneralAsset.STRENGTH_ATTAINED_5 ),
		FOOD_EATEN_4                ( GeneralAsset.FOOD_EATEN_4 ),
		FOOD_EATEN_5                ( GeneralAsset.FOOD_EATEN_5 ),
		ITEMS_CRAFTED_4             ( GeneralAsset.ITEMS_CRAFTED_4 ),
		ITEMS_CRAFTED_5             ( GeneralAsset.ITEMS_CRAFTED_5 ),
		BOSS_SLAIN_4                ( GeneralAsset.BOSS_SLAIN_4 ),
		ALL_RINGS_IDENTIFIED        ( GeneralAsset.ALL_RINGS_IDENTIFIED ),
		ALL_ARTIFACTS_IDENTIFIED    ( GeneralAsset.ALL_ARTIFACTS_IDENTIFIED ),
		DEATH_FROM_GRIM_TRAP        ( GeneralAsset.DEATH_FROM_GRIM_TRAP ), //also disintegration traps
		VICTORY                     ( GeneralAsset.VICTORY ),
		BOSS_CHALLENGE_1            ( GeneralAsset.BOSS_CHALLENGE_1 ),
		BOSS_CHALLENGE_2            ( GeneralAsset.BOSS_CHALLENGE_2 ),
		GAMES_PLAYED_3              ( GeneralAsset.GAMES_PLAYED_3, true ),
		HIGH_SCORE_3                ( GeneralAsset.HIGH_SCORE_3 ),

		//platinum
		ITEM_LEVEL_5                ( GeneralAsset.ITEM_LEVEL_5 ),
		LEVEL_REACHED_5             ( GeneralAsset.LEVEL_REACHED_5 ),
		HAPPY_END                   ( GeneralAsset.HAPPY_END ),
		ALL_WEAPONS_IDENTIFIED      ( GeneralAsset.ALL_WEAPONS_IDENTIFIED ),
		ALL_ARMOR_IDENTIFIED        ( GeneralAsset.ALL_ARMOR_IDENTIFIED ),
		ALL_WANDS_IDENTIFIED        ( GeneralAsset.ALL_WANDS_IDENTIFIED ),
		ALL_ITEMS_IDENTIFIED        ( GeneralAsset.ALL_ITEMS_IDENTIFIED, true ),
		VICTORY_WARRIOR,
		VICTORY_MAGE,
		VICTORY_ROGUE,
		VICTORY_HUNTRESS,
		VICTORY_DUELIST,
		VICTORY_ALL_CLASSES         ( GeneralAsset.VICTORY_ALL_CLASSES, true ),
		DEATH_FROM_ALL              ( GeneralAsset.DEATH_FROM_ALL, true ),
		BOSS_SLAIN_3_GLADIATOR,
		BOSS_SLAIN_3_BERSERKER,
		BOSS_SLAIN_3_WARLOCK,
		BOSS_SLAIN_3_BATTLEMAGE,
		BOSS_SLAIN_3_FREERUNNER,
		BOSS_SLAIN_3_ASSASSIN,
		BOSS_SLAIN_3_SNIPER,
		BOSS_SLAIN_3_WARDEN,
		BOSS_SLAIN_3_CHAMPION,
		BOSS_SLAIN_3_MONK,
		BOSS_SLAIN_3_ALL_SUBCLASSES ( GeneralAsset.BOSS_SLAIN_3_ALL_SUBCLASSES, true ),
		BOSS_CHALLENGE_3            ( GeneralAsset.BOSS_CHALLENGE_3 ),
		BOSS_CHALLENGE_4            ( GeneralAsset.BOSS_CHALLENGE_4 ),
		GAMES_PLAYED_4              ( GeneralAsset.GAMES_PLAYED_4, true ),
		HIGH_SCORE_4                ( GeneralAsset.HIGH_SCORE_4 ),
		CHAMPION_1                  ( GeneralAsset.CHAMPION_1 ),

		//diamond
		BOSS_CHALLENGE_5            ( GeneralAsset.BOSS_CHALLENGE_5 ),
		GAMES_PLAYED_5              ( GeneralAsset.GAMES_PLAYED_5, true ),
		HIGH_SCORE_5                ( GeneralAsset.HIGH_SCORE_5 ),
		CHAMPION_2                  ( GeneralAsset.CHAMPION_2 ),
		CHAMPION_3                  ( GeneralAsset.CHAMPION_3 );

		public boolean meta;

		public Asset asset;

		Badge( Asset asset ) {
			this( asset, false );
		}

		Badge( Asset asset, boolean meta ) {
			this.asset = asset;
			this.meta = meta;
		}

		public String title(){
			return Messages.get(this, name()+".title");
		}

		public String desc(){
			return Messages.get(this, name()+".desc");
		}

		Badge() {
			this(null);
		}
	}

	private static HashSet<Badge> global;
	private static HashSet<Badge> local = new HashSet<>();

	private static boolean saveNeeded = false;

	public static void reset() {
		local.clear();
		loadGlobal();
	}

	public static final String BADGES_FILE	= "badges.dat";
	private static final String BADGES		= "badges";

	private static final HashSet<String> removedBadges = new HashSet<>();
	static{
		//v1.3.0 (These were removed and re-added internally as new unlock reqs were added)
		removedBadges.add("YASD");
		removedBadges.add("DEATH_FROM_GLYPH");
	}

	private static final HashMap<String, String> renamedBadges = new HashMap<>();
	static{
		//no renamed badges currently
	}

	public static HashSet<Badge> restore( Bundle bundle ) {
		HashSet<Badge> badges = new HashSet<>();
		if (bundle == null) return badges;

		String[] names = bundle.getStringArray( BADGES );
		if (names == null) return badges;

		for (int i=0; i < names.length; i++) {
			try {
				if (renamedBadges.containsKey(names[i])){
					names[i] = renamedBadges.get(names[i]);
				}
				if (!removedBadges.contains(names[i])){
					badges.add( Badge.valueOf( names[i] ) );
				}
			} catch (Exception e) {
				ShatteredPixelDungeon.reportException(e);
			}
		}

		addReplacedBadges(badges);

		return badges;
	}

	public static void store( Bundle bundle, HashSet<Badge> badges ) {
		addReplacedBadges(badges);

		int count = 0;
		String names[] = new String[badges.size()];

		for (Badge badge:badges) {
			names[count++] = badge.name();
		}
		bundle.put( BADGES, names );
	}

	public static void loadLocal( Bundle bundle ) {
		local = restore( bundle );
	}

	public static void saveLocal( Bundle bundle ) {
		store( bundle, local );
	}

	public static void loadGlobal() {
		if (global == null) {
			try {
				Bundle bundle = FileUtils.bundleFromFile( BADGES_FILE );
				global = restore( bundle );

			} catch (IOException e) {
				global = new HashSet<>();
			}
		}
	}

	public static void saveGlobal(){
		saveGlobal(false);
	}

	public static void saveGlobal(boolean force) {
		if (saveNeeded || force) {

			Bundle bundle = new Bundle();
			store( bundle, global );

			try {
				FileUtils.bundleToFile(BADGES_FILE, bundle);
				saveNeeded = false;
			} catch (IOException e) {
				ShatteredPixelDungeon.reportException(e);
			}
		}
	}

	public static int totalUnlocked(boolean global){
		if (global) return Badges.global.size();
		else        return Badges.local.size();
	}

	public static void validateMonstersSlain() {
		Badge badge = null;

		if (!local.contains( Badge.MONSTERS_SLAIN_1 ) && Statistics.enemiesSlain >= 10) {
			badge = Badge.MONSTERS_SLAIN_1;
			local.add( badge );
		}
		if (!local.contains( Badge.MONSTERS_SLAIN_2 ) && Statistics.enemiesSlain >= 50) {
			if (badge != null) unlock(badge);
			badge = Badge.MONSTERS_SLAIN_2;
			local.add( badge );
		}
		if (!local.contains( Badge.MONSTERS_SLAIN_3 ) && Statistics.enemiesSlain >= 100) {
			if (badge != null) unlock(badge);
			badge = Badge.MONSTERS_SLAIN_3;
			local.add( badge );
		}
		if (!local.contains( Badge.MONSTERS_SLAIN_4 ) && Statistics.enemiesSlain >= 250) {
			if (badge != null) unlock(badge);
			badge = Badge.MONSTERS_SLAIN_4;
			local.add( badge );
		}
		if (!local.contains( Badge.MONSTERS_SLAIN_5 ) && Statistics.enemiesSlain >= 500) {
			if (badge != null) unlock(badge);
			badge = Badge.MONSTERS_SLAIN_5;
			local.add( badge );
		}
		
		displayBadge( badge );
	}
	
	public static void validateGoldCollected() {
		Badge badge = null;
		
		if (!local.contains( Badge.GOLD_COLLECTED_1 ) && Statistics.goldCollected >= 250) {
			if (badge != null) unlock(badge);
			badge = Badge.GOLD_COLLECTED_1;
			local.add( badge );
		}
		if (!local.contains( Badge.GOLD_COLLECTED_2 ) && Statistics.goldCollected >= 1000) {
			if (badge != null) unlock(badge);
			badge = Badge.GOLD_COLLECTED_2;
			local.add( badge );
		}
		if (!local.contains( Badge.GOLD_COLLECTED_3 ) && Statistics.goldCollected >= 2500) {
			if (badge != null) unlock(badge);
			badge = Badge.GOLD_COLLECTED_3;
			local.add( badge );
		}
		if (!local.contains( Badge.GOLD_COLLECTED_4 ) && Statistics.goldCollected >= 7500) {
			if (badge != null) unlock(badge);
			badge = Badge.GOLD_COLLECTED_4;
			local.add( badge );
		}
		if (!local.contains( Badge.GOLD_COLLECTED_5 ) && Statistics.goldCollected >= 15_000) {
			if (badge != null) unlock(badge);
			badge = Badge.GOLD_COLLECTED_5;
			local.add( badge );
		}
		
		displayBadge( badge );
	}
	
	public static void validateLevelReached() {
		Badge badge = null;
		
		if (!local.contains( Badge.LEVEL_REACHED_1 ) && Dungeon.hero.lvl >= 6) {
			badge = Badge.LEVEL_REACHED_1;
			local.add( badge );
		}
		if (!local.contains( Badge.LEVEL_REACHED_2 ) && Dungeon.hero.lvl >= 12) {
			if (badge != null) unlock(badge);
			badge = Badge.LEVEL_REACHED_2;
			local.add( badge );
		}
		if (!local.contains( Badge.LEVEL_REACHED_3 ) && Dungeon.hero.lvl >= 18) {
			if (badge != null) unlock(badge);
			badge = Badge.LEVEL_REACHED_3;
			local.add( badge );
		}
		if (!local.contains( Badge.LEVEL_REACHED_4 ) && Dungeon.hero.lvl >= 24) {
			if (badge != null) unlock(badge);
			badge = Badge.LEVEL_REACHED_4;
			local.add( badge );
		}
		if (!local.contains( Badge.LEVEL_REACHED_5 ) && Dungeon.hero.lvl >= 30) {
			if (badge != null) unlock(badge);
			badge = Badge.LEVEL_REACHED_5;
			local.add( badge );
		}
		
		displayBadge( badge );
	}
	
	public static void validateStrengthAttained() {
		Badge badge = null;
		
		if (!local.contains( Badge.STRENGTH_ATTAINED_1 ) && Dungeon.hero.STR >= 12) {
			badge = Badge.STRENGTH_ATTAINED_1;
			local.add( badge );
		}
		if (!local.contains( Badge.STRENGTH_ATTAINED_2 ) && Dungeon.hero.STR >= 14) {
			if (badge != null) unlock(badge);
			badge = Badge.STRENGTH_ATTAINED_2;
			local.add( badge );
		}
		if (!local.contains( Badge.STRENGTH_ATTAINED_3 ) && Dungeon.hero.STR >= 16) {
			if (badge != null) unlock(badge);
			badge = Badge.STRENGTH_ATTAINED_3;
			local.add( badge );
		}
		if (!local.contains( Badge.STRENGTH_ATTAINED_4 ) && Dungeon.hero.STR >= 18) {
			if (badge != null) unlock(badge);
			badge = Badge.STRENGTH_ATTAINED_4;
			local.add( badge );
		}
		if (!local.contains( Badge.STRENGTH_ATTAINED_5 ) && Dungeon.hero.STR >= 20) {
			if (badge != null) unlock(badge);
			badge = Badge.STRENGTH_ATTAINED_5;
			local.add( badge );
		}
		
		displayBadge( badge );
	}
	
	public static void validateFoodEaten() {
		Badge badge = null;
		
		if (!local.contains( Badge.FOOD_EATEN_1 ) && Statistics.foodEaten >= 10) {
			badge = Badge.FOOD_EATEN_1;
			local.add( badge );
		}
		if (!local.contains( Badge.FOOD_EATEN_2 ) && Statistics.foodEaten >= 20) {
			if (badge != null) unlock(badge);
			badge = Badge.FOOD_EATEN_2;
			local.add( badge );
		}
		if (!local.contains( Badge.FOOD_EATEN_3 ) && Statistics.foodEaten >= 30) {
			if (badge != null) unlock(badge);
			badge = Badge.FOOD_EATEN_3;
			local.add( badge );
		}
		if (!local.contains( Badge.FOOD_EATEN_4 ) && Statistics.foodEaten >= 40) {
			if (badge != null) unlock(badge);
			badge = Badge.FOOD_EATEN_4;
			local.add( badge );
		}
		if (!local.contains( Badge.FOOD_EATEN_5 ) && Statistics.foodEaten >= 50) {
			if (badge != null) unlock(badge);
			badge = Badge.FOOD_EATEN_5;
			local.add( badge );
		}
		
		displayBadge( badge );
	}
	
	public static void validateItemsCrafted() {
		Badge badge = null;
		
		if (!local.contains( Badge.ITEMS_CRAFTED_1 ) && Statistics.itemsCrafted >= 3) {
			badge = Badge.ITEMS_CRAFTED_1;
			local.add( badge );
		}
		if (!local.contains( Badge.ITEMS_CRAFTED_2 ) && Statistics.itemsCrafted >= 8) {
			if (badge != null) unlock(badge);
			badge = Badge.ITEMS_CRAFTED_2;
			local.add( badge );
		}
		if (!local.contains( Badge.ITEMS_CRAFTED_3 ) && Statistics.itemsCrafted >= 15) {
			if (badge != null) unlock(badge);
			badge = Badge.ITEMS_CRAFTED_3;
			local.add( badge );
		}
		if (!local.contains( Badge.ITEMS_CRAFTED_4 ) && Statistics.itemsCrafted >= 24) {
			if (badge != null) unlock(badge);
			badge = Badge.ITEMS_CRAFTED_4;
			local.add( badge );
		}
		if (!local.contains( Badge.ITEMS_CRAFTED_5 ) && Statistics.itemsCrafted >= 35) {
			if (badge != null) unlock(badge);
			badge = Badge.ITEMS_CRAFTED_5;
			local.add( badge );
		}
		
		displayBadge( badge );
	}
	
	public static void validatePiranhasKilled() {
		Badge badge = null;
		
		if (!local.contains( Badge.PIRANHAS ) && Statistics.piranhasKilled >= 6) {
			badge = Badge.PIRANHAS;
			local.add( badge );
		}
		
		displayBadge( badge );
	}
	
	public static void validateItemLevelAquired( Item item ) {
		
		// This method should be called:
		// 1) When an item is obtained (Item.collect)
		// 2) When an item is upgraded (ScrollOfUpgrade, ScrollOfWeaponUpgrade, ShortSword, WandOfMagicMissile)
		// 3) When an item is identified

		// Note that artifacts should never trigger this badge as they are alternatively upgraded
		if (!item.levelKnown || item instanceof Artifact) {
			return;
		}

		if (item instanceof MeleeWeapon){
			validateDuelistUnlock();
		}
		
		Badge badge = null;
		if (!local.contains( Badge.ITEM_LEVEL_1 ) && item.level() >= 3) {
			badge = Badge.ITEM_LEVEL_1;
			local.add( badge );
		}
		if (!local.contains( Badge.ITEM_LEVEL_2 ) && item.level() >= 6) {
			if (badge != null) unlock(badge);
			badge = Badge.ITEM_LEVEL_2;
			local.add( badge );
		}
		if (!local.contains( Badge.ITEM_LEVEL_3 ) && item.level() >= 9) {
			if (badge != null) unlock(badge);
			badge = Badge.ITEM_LEVEL_3;
			local.add( badge );
		}
		if (!local.contains( Badge.ITEM_LEVEL_4 ) && item.level() >= 12) {
			if (badge != null) unlock(badge);
			badge = Badge.ITEM_LEVEL_4;
			local.add( badge );
		}
		if (!local.contains( Badge.ITEM_LEVEL_5 ) && item.level() >= 15) {
			if (badge != null) unlock(badge);
			badge = Badge.ITEM_LEVEL_5;
			local.add( badge );
		}
		
		displayBadge( badge );
	}
	
	public static void validateAllBagsBought( Item bag ) {
		
		Badge badge = null;
		if (bag instanceof VelvetPouch) {
			badge = Badge.BAG_BOUGHT_VELVET_POUCH;
		} else if (bag instanceof ScrollHolder) {
			badge = Badge.BAG_BOUGHT_SCROLL_HOLDER;
		} else if (bag instanceof PotionBandolier) {
			badge = Badge.BAG_BOUGHT_POTION_BANDOLIER;
		} else if (bag instanceof MagicalHolster) {
			badge = Badge.BAG_BOUGHT_MAGICAL_HOLSTER;
		}
		
		if (badge != null) {
			
			local.add( badge );
			
			if (!local.contains( Badge.ALL_BAGS_BOUGHT ) &&
				local.contains( Badge.BAG_BOUGHT_VELVET_POUCH ) &&
				local.contains( Badge.BAG_BOUGHT_SCROLL_HOLDER ) &&
				local.contains( Badge.BAG_BOUGHT_POTION_BANDOLIER ) &&
				local.contains( Badge.BAG_BOUGHT_MAGICAL_HOLSTER )) {
						
					badge = Badge.ALL_BAGS_BOUGHT;
					local.add( badge );
					displayBadge( badge );
			}
		}
	}
	
	public static void validateItemsIdentified() {
		
		for (Catalog cat : Catalog.values()){
			if (cat.allSeen()){
				Badge b = Catalog.catalogBadges.get(cat);
				if (!isUnlocked(b)){
					displayBadge(b);
				}
			}
		}
		
		if (isUnlocked( Badge.ALL_WEAPONS_IDENTIFIED ) &&
				isUnlocked( Badge.ALL_ARMOR_IDENTIFIED ) &&
				isUnlocked( Badge.ALL_WANDS_IDENTIFIED ) &&
				isUnlocked( Badge.ALL_RINGS_IDENTIFIED ) &&
				isUnlocked( Badge.ALL_ARTIFACTS_IDENTIFIED ) &&
				isUnlocked( Badge.ALL_POTIONS_IDENTIFIED ) &&
				isUnlocked( Badge.ALL_SCROLLS_IDENTIFIED )) {

			Badge badge = Badge.ALL_ITEMS_IDENTIFIED;
			if (!isUnlocked( badge )) {
				displayBadge( badge );
			}
		}
	}
	
	public static void validateDeathFromFire() {
		Badge badge = Badge.DEATH_FROM_FIRE;
		local.add( badge );
		displayBadge( badge );
		
		validateDeathFromAll();
	}
	
	public static void validateDeathFromPoison() {
		Badge badge = Badge.DEATH_FROM_POISON;
		local.add( badge );
		displayBadge( badge );
		
		validateDeathFromAll();
	}
	
	public static void validateDeathFromGas() {
		Badge badge = Badge.DEATH_FROM_GAS;
		local.add( badge );
		displayBadge( badge );
		
		validateDeathFromAll();
	}
	
	public static void validateDeathFromHunger() {
		Badge badge = Badge.DEATH_FROM_HUNGER;
		local.add( badge );
		displayBadge( badge );
		
		validateDeathFromAll();
	}

	public static void validateDeathFromFalling() {
		Badge badge = Badge.DEATH_FROM_FALLING;
		local.add( badge );
		displayBadge( badge );

		validateDeathFromAll();
	}

	public static void validateDeathFromEnemyMagic() {
		Badge badge = Badge.DEATH_FROM_ENEMY_MAGIC;
		local.add( badge );
		displayBadge( badge );

		validateDeathFromAll();
	}
	
	public static void validateDeathFromFriendlyMagic() {
		Badge badge = Badge.DEATH_FROM_FRIENDLY_MAGIC;
		local.add( badge );
		displayBadge( badge );

		validateDeathFromAll();
	}

	public static void validateDeathFromSacrifice() {
		Badge badge = Badge.DEATH_FROM_SACRIFICE;
		local.add( badge );
		displayBadge( badge );

		validateDeathFromAll();
	}

	public static void validateDeathFromGrimOrDisintTrap() {
		Badge badge = Badge.DEATH_FROM_GRIM_TRAP;
		local.add( badge );
		displayBadge( badge );

		validateDeathFromAll();
	}
	
	private static void validateDeathFromAll() {
		if (isUnlocked( Badge.DEATH_FROM_FIRE ) &&
				isUnlocked( Badge.DEATH_FROM_POISON ) &&
				isUnlocked( Badge.DEATH_FROM_GAS ) &&
				isUnlocked( Badge.DEATH_FROM_HUNGER) &&
				isUnlocked( Badge.DEATH_FROM_FALLING) &&
				isUnlocked( Badge.DEATH_FROM_ENEMY_MAGIC) &&
				isUnlocked( Badge.DEATH_FROM_FRIENDLY_MAGIC) &&
				isUnlocked( Badge.DEATH_FROM_SACRIFICE) &&
				isUnlocked( Badge.DEATH_FROM_GRIM_TRAP)) {

			Badge badge = Badge.DEATH_FROM_ALL;
			if (!isUnlocked( badge )) {
				displayBadge( badge );
			}
		}
	}

	private static LinkedHashMap<HeroClass, Badge> firstBossClassBadges = new LinkedHashMap<>();
	static {
		firstBossClassBadges.put(HeroClass.WARRIOR, Badge.BOSS_SLAIN_1_WARRIOR);
		firstBossClassBadges.put(HeroClass.MAGE, Badge.BOSS_SLAIN_1_MAGE);
		firstBossClassBadges.put(HeroClass.ROGUE, Badge.BOSS_SLAIN_1_ROGUE);
		firstBossClassBadges.put(HeroClass.HUNTRESS, Badge.BOSS_SLAIN_1_HUNTRESS);
		firstBossClassBadges.put(HeroClass.DUELIST, Badge.BOSS_SLAIN_1_DUELIST);
	}

	private static LinkedHashMap<HeroClass, Badge> victoryClassBadges = new LinkedHashMap<>();
	static {
		victoryClassBadges.put(HeroClass.WARRIOR, Badge.VICTORY_WARRIOR);
		victoryClassBadges.put(HeroClass.MAGE, Badge.VICTORY_MAGE);
		victoryClassBadges.put(HeroClass.ROGUE, Badge.VICTORY_ROGUE);
		victoryClassBadges.put(HeroClass.HUNTRESS, Badge.VICTORY_HUNTRESS);
		victoryClassBadges.put(HeroClass.DUELIST, Badge.VICTORY_DUELIST);
	}

	private static LinkedHashMap<HeroSubClass, Badge> thirdBossSubclassBadges = new LinkedHashMap<>();
	static {
		thirdBossSubclassBadges.put(HeroSubClass.BERSERKER, Badge.BOSS_SLAIN_3_BERSERKER);
		thirdBossSubclassBadges.put(HeroSubClass.GLADIATOR, Badge.BOSS_SLAIN_3_GLADIATOR);
		thirdBossSubclassBadges.put(HeroSubClass.BATTLEMAGE, Badge.BOSS_SLAIN_3_BATTLEMAGE);
		thirdBossSubclassBadges.put(HeroSubClass.WARLOCK, Badge.BOSS_SLAIN_3_WARLOCK);
		thirdBossSubclassBadges.put(HeroSubClass.ASSASSIN, Badge.BOSS_SLAIN_3_ASSASSIN);
		thirdBossSubclassBadges.put(HeroSubClass.FREERUNNER, Badge.BOSS_SLAIN_3_FREERUNNER);
		thirdBossSubclassBadges.put(HeroSubClass.SNIPER, Badge.BOSS_SLAIN_3_SNIPER);
		thirdBossSubclassBadges.put(HeroSubClass.WARDEN, Badge.BOSS_SLAIN_3_WARDEN);
		thirdBossSubclassBadges.put(HeroSubClass.CHAMPION, Badge.BOSS_SLAIN_3_CHAMPION);
		thirdBossSubclassBadges.put(HeroSubClass.MONK, Badge.BOSS_SLAIN_3_MONK);
	}

	public static void validateBossSlain(Class<?> boss) {
		Badge badge = null;
		if (boss.equals(Goo.class)) {
			badge = Badge.BOSS_SLAIN_1;
		} else if (boss.equals(Tengu.class)) {
			badge = Badge.BOSS_SLAIN_2;
		} else if (boss.equals(DM300.class)) {
			badge = Badge.BOSS_SLAIN_3;
		} else if (boss.equals(DwarfKing.class)) {
			badge = Badge.BOSS_SLAIN_4;
		}

		if (badge != null) {
			local.add( badge );
			displayBadge( badge );
			
			if (badge == Badge.BOSS_SLAIN_1) {
				badge = firstBossClassBadges.get(Dungeon.hero.heroClass);
				if (badge == null) return;
				local.add( badge );
				unlock(badge);

				boolean allUnlocked = true;
				for (Badge b : firstBossClassBadges.values()){
					if (!isUnlocked(b)){
						allUnlocked = false;
						break;
					}
				}
				if (allUnlocked) {
					
					badge = Badge.BOSS_SLAIN_1_ALL_CLASSES;
					if (!isUnlocked( badge )) {
						displayBadge( badge );
					}
				}
			} else if (badge == Badge.BOSS_SLAIN_3) {

				badge = thirdBossSubclassBadges.get(Dungeon.hero.subClass);
				if (badge == null) return;
				local.add( badge );
				unlock(badge);

				boolean allUnlocked = true;
				for (Badge b : thirdBossSubclassBadges.values()){
					if (!isUnlocked(b)){
						allUnlocked = false;
						break;
					}
				}
				if (allUnlocked) {
					badge = Badge.BOSS_SLAIN_3_ALL_SUBCLASSES;
					if (!isUnlocked( badge )) {
						displayBadge( badge );
					}
				}
			}
		}
	}

	public static void validateBossChallengeCompleted(Class<?> boss){
		Badge badge = null;
		if (boss.equals(Goo.class)) {
			badge = Badge.BOSS_CHALLENGE_1;
		} else if (boss.equals(Tengu.class)) {
			badge = Badge.BOSS_CHALLENGE_2;
		} else if (boss.equals(DM300.class)) {
			badge = Badge.BOSS_CHALLENGE_3;
		} else if (boss.equals(DwarfKing.class)) {
			badge = Badge.BOSS_CHALLENGE_4;
		} else if (boss.equals(YogDzewa.class)) {
			badge = Badge.BOSS_CHALLENGE_5;
		}

		if (badge != null) {
			local.add(badge);
			displayBadge(badge);
		}
	}
	
	public static void validateMastery() {
		
		Badge badge = null;
		switch (Dungeon.hero.heroClass) {
			case WARRIOR:
				badge = Badge.MASTERY_WARRIOR;
				break;
			case MAGE:
				badge = Badge.MASTERY_MAGE;
				break;
			case ROGUE:
				badge = Badge.MASTERY_ROGUE;
				break;
			case HUNTRESS:
				badge = Badge.MASTERY_HUNTRESS;
				break;
			case DUELIST:
				badge = Badge.MASTERY_DUELIST;
				break;
		}
		
		unlock(badge);
	}

	public static void validateRatmogrify(){
		unlock(Badge.FOUND_RATMOGRIFY);
	}
	
	public static void validateMageUnlock(){
		if (Statistics.upgradesUsed >= 1 && !isUnlocked(Badge.UNLOCK_MAGE)){
			displayBadge( Badge.UNLOCK_MAGE );
		}
	}
	
	public static void validateRogueUnlock(){
		if (Statistics.sneakAttacks >= 10 && !isUnlocked(Badge.UNLOCK_ROGUE)){
			displayBadge( Badge.UNLOCK_ROGUE );
		}
	}
	
	public static void validateHuntressUnlock(){
		if (Statistics.thrownAttacks >= 10 && !isUnlocked(Badge.UNLOCK_HUNTRESS)){
			displayBadge( Badge.UNLOCK_HUNTRESS );
		}
	}

	public static void validateDuelistUnlock(){
		if (!isUnlocked(Badge.UNLOCK_DUELIST) && Dungeon.hero != null
				&& Dungeon.hero.belongings.weapon instanceof MeleeWeapon
				&& ((MeleeWeapon) Dungeon.hero.belongings.weapon).tier >= 2
				&& ((MeleeWeapon) Dungeon.hero.belongings.weapon).STRReq() <= Dungeon.hero.STR()){

			if (Dungeon.hero.belongings.weapon.isIdentified() &&
					((MeleeWeapon) Dungeon.hero.belongings.weapon).STRReq() <= Dungeon.hero.STR()) {
				displayBadge(Badge.UNLOCK_DUELIST);

			} else if (!Dungeon.hero.belongings.weapon.isIdentified() &&
					((MeleeWeapon) Dungeon.hero.belongings.weapon).STRReq(0) <= Dungeon.hero.STR()){
				displayBadge(Badge.UNLOCK_DUELIST);
			}
		}
	}
	
	public static void validateMasteryCombo( int n ) {
		if (!local.contains( Badge.MASTERY_COMBO ) && n == 10) {
			Badge badge = Badge.MASTERY_COMBO;
			local.add( badge );
			displayBadge( badge );
		}
	}
	
	public static void validateVictory() {

		Badge badge = Badge.VICTORY;
		local.add( badge );
		displayBadge( badge );

		badge = victoryClassBadges.get(Dungeon.hero.heroClass);
		if (badge == null) return;
		local.add( badge );
		unlock(badge);

		boolean allUnlocked = true;
		for (Badge b : victoryClassBadges.values()){
			if (!isUnlocked(b)){
				allUnlocked = false;
				break;
			}
		}
		if (allUnlocked){
			badge = Badge.VICTORY_ALL_CLASSES;
			displayBadge( badge );
		}
	}

	public static void validateNoKilling() {
		if (!local.contains( Badge.NO_MONSTERS_SLAIN ) && Statistics.completedWithNoKilling) {
			Badge badge = Badge.NO_MONSTERS_SLAIN;
			local.add( badge );
			displayBadge( badge );
		}
	}
	
	public static void validateGrimWeapon() {
		if (!local.contains( Badge.GRIM_WEAPON )) {
			Badge badge = Badge.GRIM_WEAPON;
			local.add( badge );
			displayBadge( badge );
		}
	}
	
	public static void validateGamesPlayed() {
		Badge badge = null;
		if (Rankings.INSTANCE.totalNumber >= 10 || Rankings.INSTANCE.wonNumber >= 1) {
			badge = Badge.GAMES_PLAYED_1;
		}
		if (Rankings.INSTANCE.totalNumber >= 25 || Rankings.INSTANCE.wonNumber >= 3) {
			unlock(badge);
			badge = Badge.GAMES_PLAYED_2;
		}
		if (Rankings.INSTANCE.totalNumber >= 50 || Rankings.INSTANCE.wonNumber >= 5) {
			unlock(badge);
			badge = Badge.GAMES_PLAYED_3;
		}
		if (Rankings.INSTANCE.totalNumber >= 200 || Rankings.INSTANCE.wonNumber >= 10) {
			unlock(badge);
			badge = Badge.GAMES_PLAYED_4;
		}
		if (Rankings.INSTANCE.totalNumber >= 1000 || Rankings.INSTANCE.wonNumber >= 25) {
			unlock(badge);
			badge = Badge.GAMES_PLAYED_5;
		}
		
		displayBadge( badge );
	}

	public static void validateHighScore( int score ){
		Badge badge = null;
		if (score >= 5000) {
			badge = Badge.HIGH_SCORE_1;
			local.add( badge );
		}
		if (score >= 25_000) {
			unlock(badge);
			badge = Badge.HIGH_SCORE_2;
			local.add( badge );
		}
		if (score >= 100_000) {
			unlock(badge);
			badge = Badge.HIGH_SCORE_3;
			local.add( badge );
		}
		if (score >= 250_000) {
			unlock(badge);
			badge = Badge.HIGH_SCORE_4;
			local.add( badge );
		}
		if (score >= 1_000_000) {
			unlock(badge);
			badge = Badge.HIGH_SCORE_5;
			local.add( badge );
		}

		displayBadge( badge );
	}
	
	//necessary in order to display the happy end badge in the surface scene
	public static void silentValidateHappyEnd() {
		if (!local.contains( Badge.HAPPY_END )){
			local.add( Badge.HAPPY_END );
		}
	}
	
	public static void validateHappyEnd() {
		displayBadge( Badge.HAPPY_END );
	}

	public static void validateChampion( int challenges ) {
		if (challenges == 0) return;
		Badge badge = null;
		if (challenges >= 1) {
			badge = Badge.CHAMPION_1;
		}
		if (challenges >= 3){
			unlock(badge);
			badge = Badge.CHAMPION_2;
		}
		if (challenges >= 6){
			unlock(badge);
			badge = Badge.CHAMPION_3;
		}
		local.add(badge);
		displayBadge( badge );
	}
	
	private static void displayBadge( Badge badge ) {
		
		if (badge == null || !Dungeon.customSeedText.isEmpty()) {
			return;
		}
		
		if (isUnlocked( badge )) {
			
			if (!badge.meta) {
				GLog.h( Messages.get(Badges.class, "endorsed", badge.title()) );
				GLog.newLine();
			}
			
		} else {
			
			unlock(badge);
			
			GLog.h( Messages.get(Badges.class, "new", badge.title() + " (" + badge.desc() + ")") );
			GLog.newLine();
			PixelScene.showBadge( badge );
		}
	}
	
	public static boolean isUnlocked( Badge badge ) {
		return global.contains( badge );
	}
	
	public static HashSet<Badge> allUnlocked(){
		loadGlobal();
		return new HashSet<>(global);
	}
	
	public static void disown( Badge badge ) {
		loadGlobal();
		global.remove( badge );
		saveNeeded = true;
	}
	
	public static void unlock( Badge badge ){
		if (!isUnlocked(badge) && Dungeon.customSeedText.isEmpty()){
			global.add( badge );
			saveNeeded = true;
		}
	}

	public static List<Badge> filterReplacedBadges( boolean global ) {

		ArrayList<Badge> badges = new ArrayList<>(global ? Badges.global : Badges.local);

		Iterator<Badge> iterator = badges.iterator();
		while (iterator.hasNext()) {
			Badge badge = iterator.next();
			if ((!global && badge.meta) || badge.asset == null) {
				iterator.remove();
			}
		}

		Collections.sort(badges);

		return filterReplacedBadges(badges);

	}

	//only show the highest unlocked and the lowest locked
	private static final Badge[][] tierBadgeReplacements = new Badge[][]{
			{Badge.MONSTERS_SLAIN_1, Badge.MONSTERS_SLAIN_2, Badge.MONSTERS_SLAIN_3, Badge.MONSTERS_SLAIN_4, Badge.MONSTERS_SLAIN_5},
			{Badge.GOLD_COLLECTED_1, Badge.GOLD_COLLECTED_2, Badge.GOLD_COLLECTED_3, Badge.GOLD_COLLECTED_4, Badge.GOLD_COLLECTED_5},
			{Badge.ITEM_LEVEL_1, Badge.ITEM_LEVEL_2, Badge.ITEM_LEVEL_3, Badge.ITEM_LEVEL_4, Badge.ITEM_LEVEL_5},
			{Badge.LEVEL_REACHED_1, Badge.LEVEL_REACHED_2, Badge.LEVEL_REACHED_3, Badge.LEVEL_REACHED_4, Badge.LEVEL_REACHED_5},
			{Badge.STRENGTH_ATTAINED_1, Badge.STRENGTH_ATTAINED_2, Badge.STRENGTH_ATTAINED_3, Badge.STRENGTH_ATTAINED_4, Badge.STRENGTH_ATTAINED_5},
			{Badge.FOOD_EATEN_1, Badge.FOOD_EATEN_2, Badge.FOOD_EATEN_3, Badge.FOOD_EATEN_4, Badge.FOOD_EATEN_5},
			{Badge.ITEMS_CRAFTED_1, Badge.ITEMS_CRAFTED_2, Badge.ITEMS_CRAFTED_3, Badge.ITEMS_CRAFTED_4, Badge.ITEMS_CRAFTED_5},
			{Badge.BOSS_SLAIN_1, Badge.BOSS_SLAIN_2, Badge.BOSS_SLAIN_3, Badge.BOSS_SLAIN_4},
			{Badge.HIGH_SCORE_1, Badge.HIGH_SCORE_2, Badge.HIGH_SCORE_3, Badge.HIGH_SCORE_4, Badge.HIGH_SCORE_5},
			{Badge.GAMES_PLAYED_1, Badge.GAMES_PLAYED_2, Badge.GAMES_PLAYED_3, Badge.GAMES_PLAYED_4, Badge.GAMES_PLAYED_5},
			{Badge.CHAMPION_1, Badge.CHAMPION_2, Badge.CHAMPION_3}
	};

	//don't show the later badge if the earlier one isn't unlocked
	private static final Badge[][] prerequisiteBadges = new Badge[][]{
			{Badge.BOSS_SLAIN_1, Badge.BOSS_CHALLENGE_1},
			{Badge.BOSS_SLAIN_2, Badge.BOSS_CHALLENGE_2},
			{Badge.BOSS_SLAIN_3, Badge.BOSS_CHALLENGE_3},
			{Badge.BOSS_SLAIN_4, Badge.BOSS_CHALLENGE_4},
			{Badge.VICTORY,      Badge.BOSS_CHALLENGE_5},
	};

	//If the summary badge is unlocked, don't show the component badges
	private static final Badge[][] summaryBadgeReplacements = new Badge[][]{
			{Badge.DEATH_FROM_FIRE, Badge.DEATH_FROM_ALL},
			{Badge.DEATH_FROM_GAS, Badge.DEATH_FROM_ALL},
			{Badge.DEATH_FROM_HUNGER, Badge.DEATH_FROM_ALL},
			{Badge.DEATH_FROM_POISON, Badge.DEATH_FROM_ALL},
			{Badge.DEATH_FROM_FALLING, Badge.DEATH_FROM_ALL},
			{Badge.DEATH_FROM_ENEMY_MAGIC, Badge.DEATH_FROM_ALL},
			{Badge.DEATH_FROM_FRIENDLY_MAGIC, Badge.DEATH_FROM_ALL},
			{Badge.DEATH_FROM_SACRIFICE, Badge.DEATH_FROM_ALL},
			{Badge.DEATH_FROM_GRIM_TRAP, Badge.DEATH_FROM_ALL},

			{Badge.ALL_WEAPONS_IDENTIFIED, Badge.ALL_ITEMS_IDENTIFIED},
			{Badge.ALL_ARMOR_IDENTIFIED, Badge.ALL_ITEMS_IDENTIFIED},
			{Badge.ALL_WANDS_IDENTIFIED, Badge.ALL_ITEMS_IDENTIFIED},
			{Badge.ALL_RINGS_IDENTIFIED, Badge.ALL_ITEMS_IDENTIFIED},
			{Badge.ALL_ARTIFACTS_IDENTIFIED, Badge.ALL_ITEMS_IDENTIFIED},
			{Badge.ALL_POTIONS_IDENTIFIED, Badge.ALL_ITEMS_IDENTIFIED},
			{Badge.ALL_SCROLLS_IDENTIFIED, Badge.ALL_ITEMS_IDENTIFIED}
	};
	
	public static List<Badge> filterReplacedBadges( List<Badge> badges ) {

		for (Badge[] tierReplace : tierBadgeReplacements){
			leaveBest( badges, tierReplace );
		}

		for (Badge[] metaReplace : summaryBadgeReplacements){
			leaveBest( badges, metaReplace );
		}
		
		return badges;
	}
	
	private static void leaveBest( Collection<Badge> list, Badge...badges ) {
		for (int i=badges.length-1; i > 0; i--) {
			if (list.contains( badges[i])) {
				for (int j=0; j < i; j++) {
					list.remove( badges[j] );
				}
				break;
			}
		}
	}

	public static List<Badge> filterBadgesWithoutPrerequisites(List<Badges.Badge> badges ) {

		for (Badge[] prereqReplace : prerequisiteBadges){
			leaveWorst( badges, prereqReplace );
		}

		for (Badge[] tierReplace : tierBadgeReplacements){
			leaveWorst( badges, tierReplace );
		}

		Collections.sort( badges );

		return badges;
	}

	private static void leaveWorst( Collection<Badge> list, Badge...badges ) {
		for (int i=0; i < badges.length; i++) {
			if (list.contains( badges[i])) {
				for (int j=i+1; j < badges.length; j++) {
					list.remove( badges[j] );
				}
				break;
			}
		}
	}

	public static Collection<Badge> addReplacedBadges(Collection<Badges.Badge> badges ) {

		for (Badge[] tierReplace : tierBadgeReplacements){
			addLower( badges, tierReplace );
		}

		for (Badge[] metaReplace : summaryBadgeReplacements){
			addLower( badges, metaReplace );
		}

		return badges;
	}

	private static void addLower( Collection<Badge> list, Badge...badges ) {
		for (int i=badges.length-1; i > 0; i--) {
			if (list.contains( badges[i])) {
				for (int j=0; j < i; j++) {
					list.add( badges[j] );
				}
				break;
			}
		}
	}

	//used for badges with completion progress that would otherwise be hard to track
	public static String showCompletionProgress( Badge badge ){
		if (isUnlocked(badge)) return null;

		String result = "\n";

		if (badge == Badge.BOSS_SLAIN_1_ALL_CLASSES){
			for (HeroClass cls : HeroClass.values()){
				result += "\n";
				if (isUnlocked(firstBossClassBadges.get(cls)))  result += "_" + Messages.titleCase(cls.title()) + "_";
				else                                            result += Messages.titleCase(cls.title());
			}

			return result;

		} else if (badge == Badge.VICTORY_ALL_CLASSES) {

			for (HeroClass cls : HeroClass.values()){
				result += "\n";
				if (isUnlocked(victoryClassBadges.get(cls)))    result += "_" + Messages.titleCase(cls.title()) + "_";
				else                                            result += Messages.titleCase(cls.title());
			}

			return result;

		} else if (badge == Badge.BOSS_SLAIN_3_ALL_SUBCLASSES){

			for (HeroSubClass cls : HeroSubClass.values()){
				if (cls == HeroSubClass.NONE) continue;
				result += "\n";
				if (isUnlocked(thirdBossSubclassBadges.get(cls)))   result += "_" + Messages.titleCase(cls.title()) + "_";
				else                                                result += Messages.titleCase(cls.title()) ;
			}

			return result;
		}

		return null;
	}
}
