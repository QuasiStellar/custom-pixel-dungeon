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
import com.qsr.customspd.effects.Speck;
import com.qsr.customspd.modding.SpriteSizeConfig;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.particles.Emitter;

import java.util.List;

import kotlin.Pair;

public class RotHeartSprite extends MobSprite {

	private Emitter cloud;

	public RotHeartSprite(){
		super();

		perspectiveRaise = 0.2f;

		texture( Asset.getAssetFilePath(GeneralAsset.ROT_HEART) );

		List<Integer> frameSizes = SpriteSizeConfig.getSizes(GeneralAsset.ROT_HEART);
		int frameWidth = frameSizes.get(0);
		int frameHeight = frameSizes.get(1);

		TextureFilm frames = new TextureFilm( texture, frameWidth, frameHeight );

		idle = new MovieClip.Animation( 1, true );
		idle.frames( frames, 0);

		run = new MovieClip.Animation( 1, true );
		run.frames( frames, 0 );

		attack = new MovieClip.Animation( 1, false );
		attack.frames( frames, 0 );

		die = new MovieClip.Animation( 8, false );
		die.frames( frames, 1, 2, 3, 4, 5, 6, 7, 7, 7 );

		play( idle );
	}

	@Override
	public void link( Char ch ) {
		super.link( ch );

		renderShadow = false;

		if (cloud == null) {
			cloud = emitter();
			cloud.pour( Speck.factory(Speck.TOXIC), 0.7f );
		}
	}

	@Override
	public void turnTo(int from, int to) {
		//do nothing
	}

	@Override
	public void update() {

		super.update();

		if (cloud != null) {
			cloud.visible = visible;
		}
	}

	@Override
	public void die() {
		super.die();

		if (cloud != null) {
			cloud.on = false;
		}
	}

	@Override
	public int blood() {
		return 0xFF88CC44;
	}
}
