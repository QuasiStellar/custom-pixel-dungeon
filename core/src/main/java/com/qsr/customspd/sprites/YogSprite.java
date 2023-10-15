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

import com.qsr.customspd.actors.Char;
import com.qsr.customspd.assets.Asset;
import com.qsr.customspd.assets.GeneralAsset;
import com.qsr.customspd.effects.Splash;
import com.qsr.customspd.modding.SpriteSizeConfig;
import com.watabou.noosa.TextureFilm;

import java.util.List;

import kotlin.Pair;

public class YogSprite extends MobSprite {
	
	public YogSprite() {
		super();

		perspectiveRaise = 5 / 16f;

		texture( Asset.getAssetFilePath(GeneralAsset.YOG) );

		List<Integer> frameSizes = SpriteSizeConfig.getSizes(GeneralAsset.YOG);
		int frameWidth = frameSizes.get(0);
		int frameHeight = frameSizes.get(1);
		
		TextureFilm frames = new TextureFilm( texture, frameWidth, frameHeight );
		
		idle = new Animation( 10, true );
		idle.frames( frames, 0, 1, 2, 2, 1, 0, 3, 4, 4, 3, 0, 5, 6, 6, 5 );
		
		run = new Animation( 12, true );
		run.frames( frames, 0 );
		
		attack = new Animation( 12, false );
		attack.frames( frames, 0 );
		
		die = new Animation( 10, false );
		die.frames( frames, 0, 7, 8, 9 );
		
		play( idle );
	}

	@Override
	public void link(Char ch) {
		super.link(ch);
		renderShadow = false;
	}

	@Override
	public void die() {
		super.die();
		
		Splash.at( center(), blood(), 12 );
	}
}
