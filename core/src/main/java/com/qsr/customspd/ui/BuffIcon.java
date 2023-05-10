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

package com.qsr.customspd.ui;

import com.qsr.customspd.actors.buffs.Buff;
import com.qsr.customspd.assets.Asset;
import com.watabou.noosa.Image;

import kotlin.Pair;

public class BuffIcon extends Image {

	private final boolean large;

	public BuffIcon(Buff buff, boolean large){
		super( Asset.getAssetFilePath(large ? buff.icon().getSecond() : buff.icon().getFirst()) );
		this.large = large;
		refresh(buff);
	}

	public BuffIcon(Pair<Asset, Asset> icon, boolean large){
		super( Asset.getAssetFilePath(large ? icon.getSecond() : icon.getFirst()) );
		this.large = large;
	}

	public void refresh(Buff buff){
		refresh(buff.icon());
		buff.tintIcon(this);
	}

	public void refresh(Pair<Asset, Asset> icon){
		texture(Asset.getAssetFilePath(large ? icon.getSecond() : icon.getFirst()));
	}
}
