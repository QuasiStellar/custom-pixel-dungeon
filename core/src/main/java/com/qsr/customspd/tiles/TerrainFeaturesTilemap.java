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

package com.qsr.customspd.tiles;

import com.qsr.customspd.Assets;
import com.qsr.customspd.Dungeon;
import com.qsr.customspd.levels.CavesBossLevel;
import com.qsr.customspd.levels.CavesLevel;
import com.qsr.customspd.levels.CityBossLevel;
import com.qsr.customspd.levels.CityLevel;
import com.qsr.customspd.levels.CustomLevel;
import com.qsr.customspd.levels.HallsBossLevel;
import com.qsr.customspd.levels.HallsLevel;
import com.qsr.customspd.levels.LastLevel;
import com.qsr.customspd.levels.LastShopLevel;
import com.qsr.customspd.levels.PrisonBossLevel;
import com.qsr.customspd.levels.PrisonLevel;
import com.qsr.customspd.levels.SewerBossLevel;
import com.qsr.customspd.levels.SewerLevel;
import com.qsr.customspd.levels.Terrain;
import com.qsr.customspd.levels.traps.Trap;
import com.qsr.customspd.plants.Plant;
import com.watabou.noosa.Image;
import com.watabou.noosa.tweeners.ScaleTweener;
import com.watabou.utils.PointF;
import com.watabou.utils.RectF;
import com.watabou.utils.SparseArray;

public class TerrainFeaturesTilemap extends DungeonTilemap {

	private static TerrainFeaturesTilemap instance;

	private SparseArray<Plant> plants;
	private SparseArray<Trap> traps;

	public TerrainFeaturesTilemap(SparseArray<Plant> plants, SparseArray<Trap> traps) {
		super(Assets.Environment.TERRAIN_FEATURES);

		this.plants = plants;
		this.traps = traps;

		map( Dungeon.level.map, Dungeon.level.width() );

		instance = this;
	}

	protected int getTileVisual(int pos, int tile, boolean flat){
		if (traps.get(pos) != null){
			Trap trap = traps.get(pos);
			if (!trap.visible)
				return -1;
			else
				return (trap.active ? trap.color : Trap.BLACK) + (trap.shape * 16);
		}

		if (plants.get(pos) != null){
			return plants.get(pos).image + 7*16;
		}

		int stage;
		if (Dungeon.level instanceof CustomLevel) stage = Dungeon.layout.getDungeon().get(Dungeon.levelName).getCustomLayout().getRegion() - 1;
		else if (Dungeon.level instanceof SewerLevel || Dungeon.level instanceof SewerBossLevel) stage = 0;
		else if (Dungeon.level instanceof PrisonLevel || Dungeon.level instanceof PrisonBossLevel) stage = 1;
		else if (Dungeon.level instanceof CavesLevel || Dungeon.level instanceof CavesBossLevel) stage = 2;
		else if (Dungeon.level instanceof CityLevel || Dungeon.level instanceof CityBossLevel) stage = 3;
		else if (Dungeon.level instanceof HallsLevel || Dungeon.level instanceof HallsBossLevel || Dungeon.level instanceof LastLevel) stage = 4;
		else stage = 1;
		if (tile == Terrain.HIGH_GRASS){
			return 9 + 16*stage + (DungeonTileSheet.tileVariance[pos] >= 50 ? 1 : 0);
		} else if (tile == Terrain.FURROWED_GRASS){
			return 11 + 16*stage + (DungeonTileSheet.tileVariance[pos] >= 50 ? 1 : 0);
		} else if (tile == Terrain.GRASS) {
			return 13 + 16*stage + (DungeonTileSheet.tileVariance[pos] >= 50 ? 1 : 0);
		} else if (tile == Terrain.EMBERS) {
			return 9 * (16*5) + (DungeonTileSheet.tileVariance[pos] >= 50 ? 1 : 0);
		}

		return -1;
	}

	public static Image tile(int pos, int tile ) {
		RectF uv = instance.tileset.get( instance.getTileVisual( pos, tile, true ) );
		if (uv == null) return null;
		
		Image img = new Image( instance.texture );
		img.frame(uv);
		return img;
	}

	public void growPlant( final int pos ){
		final Image plant = tile( pos, map[pos] );
		if (plant == null) return;
		
		plant.origin.set( 8, 12 );
		plant.scale.set( 0 );
		plant.point( DungeonTilemap.tileToWorld( pos ) );

		parent.add( plant );

		parent.add( new ScaleTweener( plant, new PointF(1, 1), 0.2f ) {
			protected void onComplete() {
				plant.killAndErase();
				killAndErase();
				updateMapCell(pos);
			}
		} );
	}

}
