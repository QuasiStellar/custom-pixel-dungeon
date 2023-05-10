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

package com.qsr.customspd;

import com.qsr.customspd.assets.Asset;
import com.qsr.customspd.assets.GeneralAsset;
import com.watabou.noosa.NinePatch;

public class Chrome {

	public enum  Type {
		TOAST,
		TOAST_TR,
		TOAST_WHITE,
		WINDOW,
		WINDOW_SILVER,
		RED_BUTTON,
		GREY_BUTTON,
		GREY_BUTTON_TR,
		TAG,
		GEM,
		SCROLL,
		TAB_SET,
		TAB_SELECTED,
		TAB_UNSELECTED,
		BLANK
	}
	
	public static NinePatch get( Type type ) {
		switch (type) {
		case WINDOW:
			return new NinePatch( Asset.getAssetFilePath(GeneralAsset.WINDOW), 6 );
		case WINDOW_SILVER:
			return new NinePatch( Asset.getAssetFilePath(GeneralAsset.WINDOW_SILVER), 7 );
		case TOAST:
			return new NinePatch( Asset.getAssetFilePath(GeneralAsset.TOAST), 4 );
		case TOAST_TR:
		case GREY_BUTTON_TR:
			return new NinePatch( Asset.getAssetFilePath(GeneralAsset.TOAST_TR), 4 );
		case TOAST_WHITE:
			return new NinePatch( Asset.getAssetFilePath(GeneralAsset.TOAST_WHITE), 4 );
		case RED_BUTTON:
			return new NinePatch( Asset.getAssetFilePath(GeneralAsset.RED_BUTTON), 2 );
		case GREY_BUTTON:
			return new NinePatch( Asset.getAssetFilePath(GeneralAsset.GREY_BUTTON), 2 );
		case TAG:
			return new NinePatch( Asset.getAssetFilePath(GeneralAsset.TAG), 3 );
		case GEM:
			return new NinePatch( Asset.getAssetFilePath(GeneralAsset.GEM), 13 );
		case SCROLL:
			return new NinePatch( Asset.getAssetFilePath(GeneralAsset.SCROLL), 5, 11, 5, 11 );
		case TAB_SET:
			return new NinePatch( Asset.getAssetFilePath(GeneralAsset.TAB_SET), 6 );
		case TAB_SELECTED:
			return new NinePatch( Asset.getAssetFilePath(GeneralAsset.TAB_SELECTED), 3, 7, 3, 5 );
		case TAB_UNSELECTED:
			return new NinePatch( Asset.getAssetFilePath(GeneralAsset.TAB_UNSELECTED), 3, 7, 3, 5 );
		case BLANK:
			return new NinePatch( Asset.getAssetFilePath(GeneralAsset.BLANK), 0 );
		default:
			return null;
		}
	}
}
