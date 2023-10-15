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

package com.qsr.customspd.sprites;

import com.qsr.customspd.assets.Asset;
import com.qsr.customspd.assets.GeneralAsset;
import com.qsr.customspd.modding.SpriteSizeConfig;
import com.watabou.noosa.TextureFilm;

import java.util.List;

import kotlin.Pair;

public class BatSprite extends MobSprite {
	
	public BatSprite() {
		super();
		
		texture( Asset.getAssetFilePath(GeneralAsset.BAT) );

		List<Integer> frameSizes = SpriteSizeConfig.getSizes(GeneralAsset.BAT);
		int frameWidth = frameSizes.get(0);
		int frameHeight = frameSizes.get(1);
		
		TextureFilm frames = new TextureFilm( texture, frameWidth, frameHeight );
		
		idle = new Animation( 8, true );
		idle.frames( frames, 0, 1 );
		
		run = new Animation( 12, true );
		run.frames( frames, 0, 1 );
		
		attack = new Animation( 12, false );
		attack.frames( frames, 2, 3, 0, 1 );
		
		die = new Animation( 12, false );
		die.frames( frames, 4, 5, 6 );
		
		play( idle );
	}
}
