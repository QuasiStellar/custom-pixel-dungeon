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

package com.qsr.customspd.levels;

import com.qsr.customspd.Assets;
import com.qsr.customspd.Challenges;
import com.qsr.customspd.Dungeon;
import com.qsr.customspd.Statistics;
import com.qsr.customspd.actors.Actor;
import com.qsr.customspd.actors.Char;
import com.qsr.customspd.actors.mobs.Goo;
import com.qsr.customspd.actors.mobs.Mob;
import com.qsr.customspd.assets.Asset;
import com.qsr.customspd.assets.GeneralAsset;
import com.qsr.customspd.dungeon.CustomLevelLayout;
import com.qsr.customspd.dungeon.ItemSpawn;
import com.qsr.customspd.dungeon.MobSpawn;
import com.qsr.customspd.dungeon.Position;
import com.qsr.customspd.items.Amulet;
import com.qsr.customspd.items.Generator;
import com.qsr.customspd.items.Item;
import com.qsr.customspd.items.KindOfWeapon;
import com.qsr.customspd.items.Stylus;
import com.qsr.customspd.items.Torch;
import com.qsr.customspd.items.armor.Armor;
import com.qsr.customspd.items.potions.PotionOfStrength;
import com.qsr.customspd.items.scrolls.ScrollOfUpgrade;
import com.qsr.customspd.items.stones.StoneOfEnchantment;
import com.qsr.customspd.items.stones.StoneOfIntuition;
import com.qsr.customspd.items.weapon.Weapon;
import com.qsr.customspd.levels.features.LevelTransition;
import com.qsr.customspd.levels.painters.Painter;
import com.qsr.customspd.messages.Messages;
import com.qsr.customspd.tiles.CustomTilemap;
import com.qsr.customspd.tiles.DungeonTileSheet;
import com.watabou.noosa.Group;
import com.watabou.noosa.Tilemap;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;
import com.watabou.utils.SparseArray;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

public class CustomLevel extends Level {

	CustomLevelLayout layout;
	String levelName;

	public CustomLevel() {

	}

	public CustomLevel(CustomLevelLayout layout, String levelName) {
		this.layout = layout;
		this.levelName = levelName;

		color1 = layout.getLeafColor1();
		color2 = layout.getLeafColor2();

		viewDistance = layout.getViewDistance();
	}

	@Override
	public void playLevelMusic() {

		List<Float> probs = layout.getMusic().getProbs();
		float[] probsArray = new float[probs.size()];
		int i = 0;
		for (Float f : probs) {
			probsArray[i++] = (f != null ? f : Float.NaN); // java is trash
		}

		Music.INSTANCE.playTracks(
			layout.getMusic().getTracks().toArray(new String[0]),
			probsArray,
			false
		);
	}

	@Override
	public String tilesTex() {
		switch (layout.getRegion()) {
			case 1 -> {
				return Assets.Environment.TILES_SEWERS;
			}
			case 2 -> {
				return Assets.Environment.TILES_PRISON;
			}
			case 3 -> {
				return Assets.Environment.TILES_CAVES;
			}
			case 4 -> {
				return Assets.Environment.TILES_CITY;
			}
			case 5 -> {
				return Assets.Environment.TILES_HALLS;
			}
		}
		return Assets.Environment.TILES_SEWERS;
	}

	@Override
	public String waterTex() {
		switch (layout.getRegion()) {
			case 1 -> {
				return Asset.getAssetFilePath(GeneralAsset.WATER_SEWERS);
			}
			case 2 -> {
				return Asset.getAssetFilePath(GeneralAsset.WATER_PRISON);
			}
			case 3 -> {
				return Asset.getAssetFilePath(GeneralAsset.WATER_CAVES);
			}
			case 4 -> {
				return Asset.getAssetFilePath(GeneralAsset.WATER_CITY);
			}
			case 5 -> {
				return Asset.getAssetFilePath(GeneralAsset.WATER_HALLS);
			}
		}
		return Asset.getAssetFilePath(GeneralAsset.WATER_SEWERS);
	}

	@Override
	public void create() {

		Random.pushGenerator( Dungeon.seedCurDepth() );

		do {
			width = height = length = 0;

			transitions = new ArrayList<>();

			mobs = new HashSet<>();
			heaps = new SparseArray<>();
			blobs = new HashMap<>();
			plants = new SparseArray<>();
			traps = new SparseArray<>();
			customTiles = new HashSet<>();
			customWalls = new HashSet<>();

		} while (!build());

		buildFlagMaps();
		cleanWalls();

		createMobs();
		createItems();

		Random.popGenerator();
	}

	@Override
	protected boolean build() {
		
		setSize(layout.getWidth(), layout.getHeight());

		for (int i = 0; i < width * height; i++) {
			Painter.set(this, i % width, i / width, layout.getMap().get(i));
		}

		for (int i = 0; i < layout.getEntrances().size(); i++) {
			int cell = layout.getEntrances().get(i).getX() + layout.getEntrances().get(i).getY() * width;
			LevelTransition.Type type = Dungeon.layout().getDungeon().get(levelName).getEntrances().get(i).equals("surface")
				? LevelTransition.Type.SURFACE
				: LevelTransition.Type.REGULAR_ENTRANCE;
			transitions.add(new LevelTransition(this, cell, type, i));
		}

		for (int i = 0; i < layout.getExits().size(); i++) {
			int cell = layout.getExits().get(i).getX() + layout.getExits().get(i).getY() * width;
			LevelTransition.Type type = Dungeon.layout().getDungeon().get(levelName).getExits().get(i).equals("surface")
				? LevelTransition.Type.SURFACE
				: LevelTransition.Type.REGULAR_EXIT;
			transitions.add(new LevelTransition(this, cell, type, i));
		}

		return true;
	}

	@Override
	protected void createMobs() {
		for (MobSpawn mobSpawn : layout.getMobs()) {
			int pos = mobSpawn.getX() + mobSpawn.getY() * layout.getWidth();
			Mob mob = (Mob) Reflection.newInstance(Reflection.forName("com.qsr.customspd.actors.mobs." + mobSpawn.getType()));
			mob.pos = pos;
			if (mobSpawn.getAlignment() != null) mob.alignment = Char.Alignment.valueOf(mobSpawn.getAlignment().toUpperCase(Locale.ENGLISH));
			if (mobSpawn.getHp() != null) {
				mob.HP = mobSpawn.getHp();
			}
			mobs.add(mob);
		}
	}

	public Actor addRespawner() {
		return null;
	}

	@Override
	protected void createItems() {
		for (ItemSpawn itemSpawn : layout.getItems()) {
			int pos = itemSpawn.getX() + itemSpawn.getY() * layout.getWidth();
			Item item = (Item) Reflection.newInstance(Reflection.forName("com.qsr.customspd.items." + itemSpawn.getType()));
			item.quantity(itemSpawn.getQuantity());
			item.level(itemSpawn.getLevel());
			if (itemSpawn.getIdentified()) item.identify();
			if (itemSpawn.getCursed()) item.cursed = true;
			if (itemSpawn.getEnchantment() != null) {
				if (item instanceof Armor) {
					((Armor) item).inscribe((Armor.Glyph) Reflection.newInstance(Reflection.forName("com.qsr.customspd.items.armor." + itemSpawn.getEnchantment())));
				} else if (item instanceof Weapon) {
					((Weapon) item).enchant((Weapon.Enchantment) Reflection.newInstance(Reflection.forName("com.qsr.customspd.items.weapon." + itemSpawn.getEnchantment())));
				}
			}
			drop(item, pos);
		}
	}

	@Override
	public int randomRespawnCell( Char ch ) {
		ArrayList<Integer> candidates = new ArrayList<>();
		for (int i : PathFinder.NEIGHBOURS8){
			int cell = entrance() + i;
			if (passable[cell]
					&& Actor.findChar(cell) == null
					&& (!Char.hasProp(ch, Char.Property.LARGE) || openSpace[cell])){
				candidates.add(cell);
			}
		}

		if (candidates.isEmpty()){
			return -1;
		} else {
			return Random.element(candidates);
		}
	}

	@Override
	public Group addVisuals () {
		super.addVisuals();
		HallsLevel.addHallsVisuals(this, visuals); // TODO
		return visuals;
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		this.layout = Dungeon.layout().getDungeon().get(Dungeon.levelName).getCustomLayout();
		this.levelName = Dungeon.levelName;

		color1 = layout.getLeafColor1();
		color2 = layout.getLeafColor2();

		viewDistance = layout.getViewDistance();
	}
}
