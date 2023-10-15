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
import com.qsr.customspd.Dungeon;
import com.qsr.customspd.actors.Actor;
import com.qsr.customspd.actors.Char;
import com.qsr.customspd.actors.buffs.Buff;
import com.qsr.customspd.actors.mobs.CustomMob;
import com.qsr.customspd.actors.mobs.Mimic;
import com.qsr.customspd.actors.mobs.Mob;
import com.qsr.customspd.assets.Asset;
import com.qsr.customspd.assets.GeneralAsset;
import com.qsr.customspd.items.BrokenSeal;
import com.qsr.customspd.items.wands.Wand;
import com.qsr.customspd.items.weapon.melee.MagesStaff;
import com.qsr.customspd.modding.CustomLevelLayout;
import com.qsr.customspd.modding.ItemSpawn;
import com.qsr.customspd.modding.JsonConfigRetriever;
import com.qsr.customspd.modding.MobSpawn;
import com.qsr.customspd.modding.PlantSpawn;
import com.qsr.customspd.modding.TrapSpawn;
import com.qsr.customspd.items.Generator;
import com.qsr.customspd.items.Heap;
import com.qsr.customspd.items.Item;
import com.qsr.customspd.items.armor.Armor;
import com.qsr.customspd.items.keys.Key;
import com.qsr.customspd.items.weapon.Weapon;
import com.qsr.customspd.items.weapon.missiles.darts.TippedDart;
import com.qsr.customspd.levels.features.LevelTransition;
import com.qsr.customspd.levels.painters.Painter;
import com.qsr.customspd.levels.traps.Trap;
import com.qsr.customspd.modding.ModManager;
import com.qsr.customspd.plants.Plant;
import com.qsr.customspd.scenes.GameScene;
import com.qsr.customspd.tiles.CustomTilemap;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.Group;
import com.watabou.noosa.Tilemap;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;
import com.watabou.utils.SparseArray;
import java.util.ArrayList;
import java.util.Collections;
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

		setLeafColors();

		viewDistance = layout.getViewDistance();
	}

	private void setLeafColors() {
		switch (layout.getRegion()) {
			case 1:
				color1 = 0x48763c;
				color2 = 0x59994a;
				break;
			case 2:
				color1 = 0x6a723d;
				color2 = 0x88924c;
				break;
			case 3:
				color1 = 0x534f3e;
				color2 = 0xb9d661;
				break;
			case 4:
				color1 = 0x4b6636;
				color2 = 0xf2f2f2;
				break;
			case 5:
				color1 = 0x801500;
				color2 = 0xa68521;
				break;
		}
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
			case 1:
				return Assets.Environment.TILES_SEWERS;
			case 2:
				return Assets.Environment.TILES_PRISON;
			case 3:
				return Assets.Environment.TILES_CAVES;
			case 4:
				return Assets.Environment.TILES_CITY;
			case 5:
				return Assets.Environment.TILES_HALLS;
		}
		return Assets.Environment.TILES_SEWERS;
	}

	@Override
	public String waterTex() {
		switch (layout.getRegion()) {
			case 1:
				return Asset.getAssetFilePath(GeneralAsset.WATER_SEWERS);
			case 2:
				return Asset.getAssetFilePath(GeneralAsset.WATER_PRISON);
			case 3:
				return Asset.getAssetFilePath(GeneralAsset.WATER_CAVES);
			case 4:
				return Asset.getAssetFilePath(GeneralAsset.WATER_CITY);
			case 5:
				return Asset.getAssetFilePath(GeneralAsset.WATER_HALLS);
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

		switch (Dungeon.layout.getDungeon().get(Dungeon.levelName).getVisibility()) {
			case ONLY_VISIBLE:
				for (int i=0; i < length; i++) {
					if (discoverable[i]) {
						visited[i] = true;
					}
				}
				GameScene.updateFog();
				break;
			case SECRETS:
				for (int i=0; i < length; i++) {
					int terr = map[i];
					if (discoverable[i]) {
						visited[i] = true;
						if ((Terrain.flags[terr] & Terrain.SECRET) != 0) {
							discover( i );
						}
					}
				}
				GameScene.updateFog();
				break;
		}

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
			LevelTransition.Type type = Dungeon.layout.getDungeon().get(levelName).getEntrances().get(i).equals("surface")
				? LevelTransition.Type.SURFACE
				: LevelTransition.Type.REGULAR_ENTRANCE;
			transitions.add(new LevelTransition(this, cell, type, i));
		}

		for (int i = 0; i < layout.getExits().size(); i++) {
			int cell = layout.getExits().get(i).getX() + layout.getExits().get(i).getY() * width;
			LevelTransition.Type type = Dungeon.layout.getDungeon().get(levelName).getExits().get(i).equals("surface")
				? LevelTransition.Type.SURFACE
				: LevelTransition.Type.REGULAR_EXIT;
			transitions.add(new LevelTransition(this, cell, type, i));
		}

		if (TextureCache.getBitmap(ModManager.INSTANCE.getModdedAssetFilePath("dungeon/" + Dungeon.levelName + "_tiles.png")) != null) {
			CustomTilemap tiles = new CustomLevelTiles(Dungeon.levelName, width, height);
			tiles.pos(0, 0);
			customTiles.add(tiles);
		}

		if (TextureCache.getBitmap(ModManager.INSTANCE.getModdedAssetFilePath("dungeon/" + Dungeon.levelName + "_walls.png")) != null) {
			CustomTilemap walls = new CustomLevelWalls(Dungeon.levelName, width, height);
			walls.pos(0, 0);
			customWalls.add(walls);
		}

		for (TrapSpawn trap : layout.getOpenTraps()) {
			if (map[trap.getX() + trap.getY() * width] != Terrain.TRAP) continue;
			setTrap(((Trap) Reflection.newInstance(Reflection.forName("com.qsr.customspd.levels.traps." + trap.getType()))).reveal(), trap.getX() + trap.getY() * width);
		}

		for (TrapSpawn trap : layout.getHiddenTraps()) {
			if (map[trap.getX() + trap.getY() * width] != Terrain.SECRET_TRAP) continue;
			setTrap(((Trap) Reflection.newInstance(Reflection.forName("com.qsr.customspd.levels.traps." + trap.getType()))), trap.getX() + trap.getY() * width);
		}

		for (TrapSpawn trap : layout.getDisarmedTraps()) {
			if (map[trap.getX() + trap.getY() * width] != Terrain.INACTIVE_TRAP) continue;
			setTrap(((Trap) Reflection.newInstance(Reflection.forName("com.qsr.customspd.levels.traps." + trap.getType()))).reveal(), trap.getX() + trap.getY() * width)
				.active = false;
		}

		for (PlantSpawn plant : layout.getPlants()) {
			plant(((Plant.Seed) Reflection.newInstance(Reflection.forName("com.qsr.customspd.plants." + plant.getType() + "$Seed"))), plant.getX() + plant.getY() * width);
		}

		return true;
	}

	@Override
	protected void createMobs() {

		super.createMobs();

		for (MobSpawn mobSpawn : layout.getMobs()) {
			int pos = mobSpawn.getX() + mobSpawn.getY() * layout.getWidth();
			Mob mob;
			if (JsonConfigRetriever.INSTANCE.customMobExists(mobSpawn.getType())) {
				mob = new CustomMob(mobSpawn.getType());
			} else {
				mob = (Mob) Reflection.newInstance(Reflection.forName("com.qsr.customspd.actors.mobs." + mobSpawn.getType()));
			}
			mob.pos = pos;
			if (mobSpawn.getAlignment() != null)
				mob.alignment = Char.Alignment.valueOf(mobSpawn.getAlignment().toUpperCase(Locale.ENGLISH));
			if (mobSpawn.getHp() != null) {
				mob.HP = mobSpawn.getHp();
			}
			if (mobSpawn.getChampion() != null) {
				Buff.affect(mob, Reflection.forName("com.qsr.customspd.actors.buffs.ChampionEnemy$" + mobSpawn.getChampion()));
			}
			if (mobSpawn.getAiState() != null) {
				switch (mobSpawn.getAiState().toUpperCase()) {
					case "SLEEPING":
						mob.state = mob.SLEEPING;
						break;
					case "HUNTING":
						mob.state = mob.HUNTING;
						break;
					case "WANDERING":
						mob.state = mob.WANDERING;
						break;
					case "FLEEING":
						mob.state = mob.FLEEING;
						break;
					case "PASSIVE":
						mob.state = mob.PASSIVE;
						break;
				}
			}
			if (mob instanceof Mimic) {
				((Mimic) mob).setLevel( Dungeon.depth );
			}
			mobs.add(mob);
		}
	}

	public Actor addRespawner() {
		return null;
	}

	@Override
	protected void createItems() {
		List<ItemSpawn> itemsToSpawn = new ArrayList<>(layout.getItems());
		if (layout.getShuffleItems()) Collections.shuffle(itemsToSpawn);
		for (int i = 0; i < itemsToSpawn.size(); i++) {
			ItemSpawn itemSpawn = itemsToSpawn.get(i);
			int pos = layout.getItems().get(i).getX() + layout.getItems().get(i).getY() * layout.getWidth();
			Item item;
			try {
				Generator.Category category = Generator.Category.valueOf(itemSpawn.getCategory());
				if (itemSpawn.getIgnoreDeck()) {
					item = Generator.randomUsingDefaults(category);
				} else {
					item = Generator.random(category);
				}
			} catch (IllegalArgumentException | NullPointerException e) {
				if (itemSpawn.getType().equals("weapon.missiles.darts.TippedDart")) {
					item = TippedDart.randomTipped(1);
				} else if (itemSpawn.getType().startsWith("plants.")) {
					item = (Item) Reflection.newInstance(Reflection.forName("com.qsr.customspd." + itemSpawn.getType()));
				} else {
					item = (Item) Reflection.newInstance(Reflection.forName("com.qsr.customspd.items." + itemSpawn.getType()));
				}
			}
			if (itemSpawn.getSeal() && item instanceof Armor) ((Armor)item).affixSeal(new BrokenSeal());
			if (itemSpawn.getCoreWand() != null && item instanceof MagesStaff) item = new MagesStaff((Wand) Reflection.newInstance(Reflection.forName("com.qsr.customspd.items." + itemSpawn.getCoreWand())));
			item.quantity(itemSpawn.getQuantity());
			if (itemSpawn.getQuantityMin() != null && itemSpawn.getQuantityMax() != null) {
				item.quantity(Random.IntRange(itemSpawn.getQuantityMin(), itemSpawn.getQuantityMax()));
			}
			item.level(itemSpawn.getLevel());
			if (itemSpawn.getIdentified()) {
				item.identify(itemSpawn.getHeapType() == null || Heap.Type.valueOf(itemSpawn.getHeapType()) != Heap.Type.FOR_SALE);
			}
			if (Boolean.TRUE.equals(itemSpawn.getCursed())) item.cursed = true;
			if (Boolean.FALSE.equals(itemSpawn.getCursed())) item.cursed = false;
			if (itemSpawn.getEnchantment() != null) {
				if (item instanceof Armor) {
					if (itemSpawn.getEnchantment().equals("none")) {
						((Armor) item).inscribe(null);
					} else {
						((Armor) item).inscribe((Armor.Glyph) Reflection.newInstance(Reflection.forName("com.qsr.customspd.items.armor." + itemSpawn.getEnchantment())));
					}
				} else if (item instanceof Weapon) {
					if (itemSpawn.getEnchantment().equals("none")) {
						((Weapon) item).enchant(null);
					} else {
						((Weapon) item).enchant((Weapon.Enchantment) Reflection.newInstance(Reflection.forName("com.qsr.customspd.items.weapon." + itemSpawn.getEnchantment())));
					}
				}
			}
			if (item instanceof Key) {
				if (itemSpawn.getLevelName() == null) {
					((Key) item).levelName = Dungeon.levelName;
				} else {
					((Key) item).levelName = itemSpawn.getLevelName();
				}
			}
			Heap dropped = drop(item, pos);
			if (itemSpawn.getHeapType() != null) {
				Heap.Type type = Heap.Type.valueOf(itemSpawn.getHeapType());
				dropped.type = type;
				if (type == Heap.Type.SKELETON){
					dropped.setHauntedIfCursed();
				}
			}
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
		switch (layout.getRegion()) {
			case 1:
				SewerLevel.addSewerVisuals(this, visuals);
				break;
			case 2:
				PrisonLevel.addPrisonVisuals(this, visuals);
				break;
			case 3:
				CavesLevel.addCavesVisuals(this, visuals);
				break;
			case 4:
				CityLevel.addCityVisuals(this, visuals);
				break;
			case 5:
				HallsLevel.addHallsVisuals(this, visuals);
				break;
		}
		return visuals;
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		this.layout = Dungeon.layout.getDungeon().get(Dungeon.levelName).getCustomLayout();
		this.levelName = Dungeon.levelName;

		setLeafColors();

		viewDistance = layout.getViewDistance();
	}

	public static class CustomLevelTiles extends CustomTilemap {

		public CustomLevelTiles() {}

		public CustomLevelTiles(String name, int width, int height) {
			texture = ModManager.INSTANCE.getModdedAssetFilePath("dungeon/" + name + "_tiles.png");

			tileW = width;
			tileH = height;
		}

		@Override
		public Tilemap create() {

			Tilemap v = super.create();

			int[] data = mapSimpleImage(0, 0, tileW * 16);

			v.map(data, tileW);
			return v;
		}

		private static final String TEXTURE  = "texture";

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);

			texture = bundle.getString(TEXTURE);
		}

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);

			bundle.put(TEXTURE, texture.toString());
		}
	}

	public static class CustomLevelWalls extends CustomTilemap {

		public CustomLevelWalls() {}

		public CustomLevelWalls(String name, int width, int height) {
			texture = ModManager.INSTANCE.getModdedAssetFilePath("dungeon/" + name + "_walls.png");

			tileW = width;
			tileH = height;
		}

		@Override
		public Tilemap create() {

			Tilemap v = super.create();

			int[] data = mapSimpleImage(0, 0, tileW * 16);

			v.map(data, tileW);
			return v;
		}

		private static final String TEXTURE  = "texture";

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);

			texture = bundle.getString(TEXTURE);
		}

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);

			bundle.put(TEXTURE, texture.toString());
		}
	}
}
