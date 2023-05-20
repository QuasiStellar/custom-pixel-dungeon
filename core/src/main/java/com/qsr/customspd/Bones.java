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

import com.qsr.customspd.actors.hero.Hero;
import com.qsr.customspd.items.Generator;
import com.qsr.customspd.items.Gold;
import com.qsr.customspd.items.Item;
import com.qsr.customspd.items.artifacts.Artifact;
import com.qsr.customspd.items.weapon.missiles.MissileWeapon;
import com.watabou.utils.Bundle;
import com.watabou.utils.FileUtils;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class Bones {

	private static final String BONES_FILE	= "bones.dat";
	
	private static final String LEVEL	= "level";
	private static final String ITEM	= "item";

	private static String level = "surface";
	private static Item item;
	
	public static void leave() {

		level = Dungeon.levelName;

		//heroes drop no bones if they have the amulet, die far above their farthest depth, are challenged, or are playing with a custom seed.
		if (Statistics.amuletObtained || (Statistics.deepestFloor - 5) >= Dungeon.depth || Dungeon.challenges > 0 || !Dungeon.customSeedText.isEmpty()) {
			level = "surface";
			return;
		}

		item = pickItem(Dungeon.hero);

		Bundle bundle = new Bundle();
		bundle.put( LEVEL, level);
		bundle.put( ITEM, item );

		try {
			FileUtils.bundleToFile( BONES_FILE, bundle );
		} catch (IOException e) {
			ShatteredPixelDungeon.reportException(e);
		}
	}

	private static Item pickItem(Hero hero){
		Item item = null;
		if (Random.Int(3) != 0) {
			switch (Random.Int(7)) {
				case 0:
					item = hero.belongings.weapon;
					//if the hero has two weapons (champion), pick the stronger one
					if (hero.belongings.secondWep != null &&
							(item == null || hero.belongings.secondWep.trueLevel() > item.trueLevel())){
						item = hero.belongings.secondWep;
						break;
					}
					break;
				case 1:
					item = hero.belongings.armor;
					break;
				case 2:
					item = hero.belongings.artifact;
					break;
				case 3:
					item = hero.belongings.misc;
					break;
				case 4:
					item = hero.belongings.ring;
					break;
				case 5: case 6:
					item = Dungeon.quickslot.randomNonePlaceholder();
					break;
			}
			if (item == null || !item.bones) {
				return pickItem(hero);
			}
		} else {

			Iterator<Item> iterator = hero.belongings.backpack.iterator();
			Item curItem;
			ArrayList<Item> items = new ArrayList<>();
			while (iterator.hasNext()){
				curItem = iterator.next();
				if (curItem.bones)
					items.add(curItem);
			}

			if (Random.Int(3) < items.size()) {
				item = Random.element(items);
				if (item.stackable){
					item.quantity(Random.NormalIntRange(1, (item.quantity() + 1) / 2));
				}
			} else {
				if (Dungeon.gold > 100) {
					item = new Gold( Random.NormalIntRange( 50, Dungeon.gold/2 ) );
				} else {
					item = new Gold( 50 );
				}
			}
		}
		
		return item;
	}

	public static Item get() {
		if (level.equals("surface")) {

			try {
				Bundle bundle = FileUtils.bundleFromFile(BONES_FILE);

				level = bundle.getString( LEVEL );
				if (!level.equals("surface") && !level.equals("0")) {
					item = (Item) bundle.get(ITEM);
				}

				return get();

			} catch (IOException e) {
				return null;
			}

		} else {
			//heroes who are challenged or on a seeded run cannot find bones
			if (level.equals(Dungeon.levelName) && Dungeon.challenges == 0 && Dungeon.customSeedText.isEmpty() && Dungeon.layout.getBones()) {
				Bundle emptyBones = new Bundle();
				emptyBones.put(LEVEL, 0);
				try {
					FileUtils.bundleToFile( BONES_FILE, emptyBones );
				} catch (IOException e) {
					ShatteredPixelDungeon.reportException(e);
				}
				level = "0";
				
				if (item == null) return null;

				//Enforces artifact uniqueness
				if (item instanceof Artifact){
					if (Generator.removeArtifact(((Artifact)item).getClass())) {
						
						//generates a new artifact of the same type, always +0
						Artifact artifact = Reflection.newInstance(((Artifact)item).getClass());
						
						if (artifact == null){
							return new Gold(item.value());
						}

						artifact.cursed = true;
						artifact.cursedKnown = true;

						return artifact;
						
					} else {
						return new Gold(item.value());
					}
				}
				
				if (item.isUpgradable() && !(item instanceof MissileWeapon)) {
					item.cursed = true;
					item.cursedKnown = true;
				}
				
				if (item.isUpgradable()) {
					//caps at +3
					if (item.level() > 3) {
						item.degrade( item.level() - 3 );
					}
					//thrown weapons are always IDed, otherwise set unknown
					item.levelKnown = item instanceof MissileWeapon;
				}
				
				item.reset();
				
				return item;
			} else {
				return null;
			}
		}
	}
}
