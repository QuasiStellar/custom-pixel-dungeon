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

import com.qsr.customspd.Dungeon;
import com.qsr.customspd.assets.Asset;
import com.qsr.customspd.assets.GeneralAsset;
import com.qsr.customspd.items.Item;
import com.qsr.customspd.modding.SpriteSizeConfig;
import com.qsr.customspd.scenes.GameScene;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Callback;
import java.util.List;

public class TenguSprite extends MobSprite {
	
	public TenguSprite() {
		super();
		
		texture( Asset.getAssetFilePath(GeneralAsset.TENGU) );

		List<Integer> frameSizes = SpriteSizeConfig.getSizes(GeneralAsset.TENGU);
		int frameWidth = frameSizes.get(0);
		int frameHeight = frameSizes.get(1);

		TextureFilm frames = new TextureFilm( texture, frameWidth, frameHeight );
		
		idle = new Animation( 2, true );
		idle.frames( frames, 0, 0, 0, 1 );
		
		run = new Animation( 15, false );
		run.frames( frames, 2, 3, 4, 5, 0 );
		
		attack = new Animation( 15, false );
		attack.frames( frames, 6, 7, 7, 0 );
		
		zap = attack.clone();
		
		die = new Animation( 8, false );
		die.frames( frames, 8, 9, 10, 10, 10, 10, 10, 10 );
		
		play( run.clone() );
	}

	@Override
	public void play(Animation anim) {
		if (isMoving && anim != run){
			synchronized (this){
				isMoving = false;
				notifyAll();
			}
		}
		super.play(anim);
	}

	@Override
	public void move( int from, int to ) {
		
		place( to );
		
		play( run );
		turnTo( from , to );

		isMoving = true;

		if (Dungeon.level.water[to]) {
			GameScene.ripple( to );
		}

	}

	@Override
	public void update() {
		if (paused) isMoving = false;
		super.update();
	}

	@Override
	public void attack( int cell ) {
		if (!Dungeon.level.adjacent( cell, ch.pos )) {

			((MissileSprite)parent.recycle( MissileSprite.class )).
				reset( this, cell, new TenguShuriken(), new Callback() {
					@Override
					public void call() {
						ch.onAttackComplete();
					}
				} );
			
			zap( ch.pos );
			
		} else {
			
			super.attack( cell );
			
		}
	}
	
	@Override
	public void onComplete( Animation anim ) {
		if (anim == run) {
			synchronized (this){
				isMoving = false;
				idle();

				notifyAll();
			}
		} else {
			super.onComplete( anim );
		}
	}
	
	public static class TenguShuriken extends Item {
		{
			image = GeneralAsset.SHURIKEN;
		}
	}
}
