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
import com.qsr.customspd.Bones;
import com.qsr.customspd.actors.Actor;
import com.qsr.customspd.actors.Char;
import com.qsr.customspd.actors.mobs.Mob;
import com.qsr.customspd.assets.Asset;
import com.qsr.customspd.assets.GeneralAsset;
import com.qsr.customspd.items.Heap;
import com.qsr.customspd.items.Item;
import com.qsr.customspd.levels.builders.Builder;
import com.qsr.customspd.levels.builders.LineBuilder;
import com.qsr.customspd.levels.painters.CityPainter;
import com.qsr.customspd.levels.painters.Painter;
import com.qsr.customspd.levels.rooms.Room;
import com.qsr.customspd.levels.rooms.standard.EntranceRoom;
import com.qsr.customspd.levels.rooms.standard.ExitRoom;
import com.qsr.customspd.levels.rooms.standard.ImpShopRoom;
import com.qsr.customspd.messages.Messages;
import com.watabou.noosa.Group;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class LastShopLevel extends RegularLevel {
	
	{
		color1 = 0x4b6636;
		color2 = 0xf2f2f2;
	}
	
	@Override
	public String tilesTex() {
		return Assets.Environment.TILES_CITY;
	}
	
	@Override
	public String waterTex() {
		return Asset.getAssetFilePath(GeneralAsset.WATER_CITY);
	}
	
	@Override
	protected boolean build() {
		feeling = Feeling.CHASM;
		if (super.build()){
			
			for (int i=0; i < length(); i++) {
				if (map[i] == Terrain.SECRET_DOOR) {
					map[i] = Terrain.DOOR;
				}
			}
			
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	protected ArrayList<Room> initRooms() {
		ArrayList<Room> rooms = new ArrayList<>();
		
		rooms.add ( roomEntrance = new EntranceRoom());
		rooms.add( new ImpShopRoom() );
		rooms.add( roomExit = new ExitRoom());
		
		return rooms;
	}
	
	@Override
	protected Builder builder() {
		return new LineBuilder()
				.setPathVariance(0f)
				.setPathLength(1f, new float[]{1})
				.setTunnelLength(new float[]{0, 0, 1}, new float[]{1});
	}
	
	@Override
	protected Painter painter() {
		return new CityPainter()
				.setWater( 0.10f, 4 )
				.setGrass( 0.10f, 3 );
	}
	
	@Override
	public Mob createMob() {
		return null;
	}
	
	@Override
	protected void createMobs() {
	}
	
	public Actor addRespawner() {
		return null;
	}
	
	@Override
	protected void createItems() {
		Item item = Bones.get();
		if (item != null) {
			int pos;
			do {
				pos = pointToCell(roomEntrance.random());
			} while (pos == entrance());
			drop( item, pos ).setHauntedIfCursed().type = Heap.Type.REMAINS;
		}
	}
	
	@Override
	public int randomRespawnCell( Char ch ) {
		ArrayList<Integer> candidates = new ArrayList<>();
		for (Point p : roomEntrance.getPoints()){
			int cell = pointToCell(p);
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
	public String tileName( int tile ) {
		switch (tile) {
			case Terrain.WATER:
				return Messages.get(CityLevel.class, "water_name");
			case Terrain.HIGH_GRASS:
				return Messages.get(CityLevel.class, "high_grass_name");
			default:
				return super.tileName( tile );
		}
	}
	
	@Override
	public String tileDesc(int tile) {
		switch (tile) {
			case Terrain.ENTRANCE:
				return Messages.get(CityLevel.class, "entrance_desc");
			case Terrain.EXIT:
				return Messages.get(CityLevel.class, "exit_desc");
			case Terrain.WALL_DECO:
			case Terrain.EMPTY_DECO:
				return Messages.get(CityLevel.class, "deco_desc");
			case Terrain.EMPTY_SP:
				return Messages.get(CityLevel.class, "sp_desc");
			case Terrain.STATUE:
			case Terrain.STATUE_SP:
				return Messages.get(CityLevel.class, "statue_desc");
			case Terrain.BOOKSHELF:
				return Messages.get(CityLevel.class, "bookshelf_desc");
			default:
				return super.tileDesc( tile );
		}
	}

	@Override
	public Group addVisuals( ) {
		super.addVisuals();
		CityLevel.addCityVisuals(this, visuals);
		return visuals;
	}
}
