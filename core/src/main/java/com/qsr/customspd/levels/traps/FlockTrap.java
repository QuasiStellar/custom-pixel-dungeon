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

package com.qsr.customspd.levels.traps;

import com.qsr.customspd.Assets;
import com.qsr.customspd.Dungeon;
import com.qsr.customspd.actors.Actor;
import com.qsr.customspd.actors.mobs.npcs.Sheep;
import com.qsr.customspd.effects.CellEmitter;
import com.qsr.customspd.effects.Speck;
import com.qsr.customspd.scenes.GameScene;
import com.qsr.customspd.utils.BArray;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;

public class FlockTrap extends Trap {

	{
		color = WHITE;
		shape = WAVES;
	}


	@Override
	public void activate() {
		PathFinder.buildDistanceMap( pos, BArray.not( Dungeon.level.solid, null ), 2 );
		ArrayList<Integer> spawnPoints = new ArrayList<>();
		for (int i = 0; i < PathFinder.distance.length; i++) {
			if (PathFinder.distance[i] < Integer.MAX_VALUE) {
				spawnPoints.add(i);
			}
		}

		for (int i : spawnPoints){
			Trap t;
			if (Dungeon.level.insideMap(i)
					&& Actor.findChar(i) == null
					&& !(Dungeon.level.pit[i])) {
				Sheep sheep = new Sheep();
				sheep.lifespan = 6;
				sheep.pos = i;
				GameScene.add(sheep);
				CellEmitter.get(i).burst(Speck.factory(Speck.WOOL), 4);
				//before the tile is pressed, directly trigger traps to avoid sfx spam
				if ((t = Dungeon.level.traps.get(i)) != null && t.active){
					if (t.disarmedByActivation) t.disarm();
					t.reveal();
					t.activate();
				}
				Dungeon.level.occupyCell(sheep);
				Sample.INSTANCE.play(Assets.Sounds.PUFF);
				Sample.INSTANCE.play(Assets.Sounds.SHEEP);
			}
		}
	}

}
