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

package com.qsr.customspd.effects;

import com.qsr.customspd.Assets;
import com.qsr.customspd.Badges;
import com.qsr.customspd.assets.Asset;
import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Point;
import com.watabou.utils.PointF;

import java.util.ArrayList;
import java.util.HashMap;

public class BadgeBanner extends Image {

	private enum State {
		FADE_IN, STATIC, FADE_OUT
	}
	private State state;

	public static final float DEFAULT_SCALE	= 3;
	public static final int SIZE = 16;
	
	private static final float FADE_IN_TIME		= 0.25f;
	private static final float STATIC_TIME		= 1f;
	private static final float FADE_OUT_TIME	= 1.75f;
	
	private Asset asset;
	private float time;

	public static ArrayList<BadgeBanner> showing = new ArrayList<>();
	
	private BadgeBanner( Asset asset ) {
		
		super( Asset.getAssetFilePath(asset) );
		
		setup(asset);
	}
	
	public void setup( Asset asset ){
		this.asset = asset;

		origin.set( width / 2, height / 2 );
		
		alpha( 0 );
		scale.set( 2 * DEFAULT_SCALE );
		
		state = State.FADE_IN;
		time = FADE_IN_TIME;
		
		Sample.INSTANCE.play( Assets.Sounds.BADGE );
	}
	
	@Override
	public void update() {
		super.update();
		
		time -= Game.elapsed;
		if (time >= 0) {
			
			switch (state) {
			case FADE_IN:
				float p = time / FADE_IN_TIME;
				scale.set( (1 + p) * DEFAULT_SCALE );
				alpha( 1 - p );
				break;
			case STATIC:
				break;
			case FADE_OUT:
				alpha( time /  FADE_OUT_TIME );
				break;
			}
			
		} else {
			
			switch (state) {
			case FADE_IN:
				time = STATIC_TIME;
				state = State.STATIC;
				scale.set( DEFAULT_SCALE );
				alpha( 1 );
				highlight( this, asset );
				break;
			case STATIC:
				time = FADE_OUT_TIME;
				state = State.FADE_OUT;
				break;
			case FADE_OUT:
				killAndErase();
				break;
			}
			
		}
	}
	
	@Override
	public void kill() {
		showing.remove(this);
		super.kill();
	}

	@Override
	public void destroy() {
		showing.remove(this);
		super.destroy();
	}

	//map to cache highlight positions so we don't have to keep looking at texture pixels
	private static HashMap<Asset, Point> highlightPositions = new HashMap<>();

	//we also hardcode any special cases
	static {
		highlightPositions.put(Badges.Badge.MASTERY_COMBO.asset, new Point(3, 7));
	}

	//adds a shine to an appropriate pixel on a badge
	public static void highlight( Image image, Asset asset ) {
		
		PointF p = new PointF();

		if (highlightPositions.containsKey(asset)){
			p.x = highlightPositions.get(asset).x * image.scale.x;
			p.y = highlightPositions.get(asset).y * image.scale.y;
		} else {

			SmartTexture tx = TextureCache.get(Asset.getAssetFilePath(asset));

			int size = 16;

			int x = 3;
			int y = 4;
			int bgColor = tx.getPixel(x, y);
			int curColor = 0;

			for (x = 3; x <= 12; x++) {
				curColor = tx.getPixel(x, y);
				if (curColor != bgColor) break;
			}

			if (curColor == bgColor) {
				y++;
				for (x = 3; x <= 12; x++) {
					curColor = tx.getPixel(x, y);
					if (curColor != bgColor) break;
				}
			}

			p.x = x * image.scale.x;
			p.y = y * image.scale.y;

			highlightPositions.put(asset, new Point(x, y));
		}

		p.offset(
			-image.origin.x * (image.scale.x - 1),
			-image.origin.y * (image.scale.y - 1) );
		p.offset( image.point() );
		
		Speck star = new Speck();
		star.reset( 0, p.x, p.y, Speck.DISCOVER );
		star.camera = image.camera();
		image.parent.add( star );
	}
	
	public static BadgeBanner show( Asset asset ) {
		BadgeBanner banner = new BadgeBanner(asset);
		showing.add(banner);
		return banner;
	}

	public static boolean isShowingBadges(){
		return !showing.isEmpty();
	}
	
	public static Image image( Asset asset ) {
		return new Image( Asset.getAssetFilePath(asset) );
	}
}
