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

package com.qsr.customspd.levels.rooms.standard;

import com.qsr.customspd.Dungeon;
import com.qsr.customspd.SPDSettings;
import com.qsr.customspd.items.journal.GuidePage;
import com.qsr.customspd.items.journal.Guidebook;
import com.qsr.customspd.journal.Document;
import com.qsr.customspd.levels.Level;
import com.qsr.customspd.levels.Terrain;
import com.qsr.customspd.levels.features.LevelTransition;
import com.qsr.customspd.levels.painters.Painter;
import com.qsr.customspd.levels.rooms.Room;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class EntranceRoom extends StandardRoom {
	
	@Override
	public int minWidth() {
		return Math.max(super.minWidth(), 5);
	}
	
	@Override
	public int minHeight() {
		return Math.max(super.minHeight(), 5);
	}

	@Override
	public boolean canMerge(Level l, Point p, int mergeTerrain) {
		return false;
	}

	@Override
	public boolean canPlaceTrap(Point p) {
		if (Dungeon.depth == 1) {
			return false;
		} else {
			return super.canPlaceTrap(p);
		}
	}

	public void paint(Level level ) {
		
		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1, Terrain.EMPTY );
		
		for (Room.Door door : connected.values()) {
			door.set( Room.Door.Type.REGULAR );
		}

		int entrance;
		do {
			entrance = level.pointToCell(random(2));
		} while (level.findMob(entrance) != null);
		Painter.set( level, entrance, Terrain.ENTRANCE );

		if (Dungeon.layout.getDungeon().get(Dungeon.levelName).getEntrances().get(0).equals("surface")) {
			level.transitions.add(new LevelTransition(level, entrance, LevelTransition.Type.SURFACE, 0));
		} else {
			level.transitions.add(new LevelTransition(level, entrance, LevelTransition.Type.REGULAR_ENTRANCE, 0));
		}

		//use a separate generator here so meta progression doesn't affect levelgen
		Random.pushGenerator();

		//places the first guidebook page on floor 1
		if (Dungeon.depth == 1 &&
				(!Document.ADVENTURERS_GUIDE.isPageRead(Document.GUIDE_INTRO) || SPDSettings.intro() )){
			int pos;
			do {
				//can't be on bottom row of tiles
				pos = level.pointToCell(new Point( Random.IntRange( left + 1, right - 1 ),
						Random.IntRange( top + 1, bottom - 2 )));
			} while (pos == level.entrance() || level.findMob(level.entrance()) != null);
			level.drop( new Guidebook(), pos );
		}

		//places the third guidebook page on floor 2
		if (Dungeon.depth == 2 && !Document.ADVENTURERS_GUIDE.isPageFound(Document.GUIDE_SEARCHING)){
			int pos;
			do {
				//can't be on bottom row of tiles
				pos = level.pointToCell(new Point( Random.IntRange( left + 1, right - 1 ),
						Random.IntRange( top + 1, bottom - 2 )));
			} while (pos == level.entrance() || level.findMob(level.entrance()) != null);
			GuidePage p = new GuidePage();
			p.page(Document.GUIDE_SEARCHING);
			level.drop( p, pos );
		}

		Random.popGenerator();

	}

	@Override
	public boolean connect(Room room) {
		//cannot connect to exit, otherwise works normally
		if (room instanceof ExitRoom)   return false;
		else                            return super.connect(room);
	}
	
}
