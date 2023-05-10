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

import com.qsr.customspd.Dungeon;
import com.qsr.customspd.actors.hero.HeroClass;
import com.qsr.customspd.assets.Asset;
import com.qsr.customspd.assets.GeneralAsset;
import com.qsr.customspd.levels.Level;
import com.watabou.noosa.Image;

public class IconUtils {

	public static Image get( HeroClass cl ) {
		switch (cl) {
			case WARRIOR:
				return new Image(Asset.getAssetFilePath(GeneralAsset.ICON_WARRIOR));
			case MAGE:
				return new Image(Asset.getAssetFilePath(GeneralAsset.ICON_MAGE));
			case ROGUE:
				return new Image(Asset.getAssetFilePath(GeneralAsset.ICON_ROGUE));
			case HUNTRESS:
				return new Image(Asset.getAssetFilePath(GeneralAsset.ICON_HUNTRESS));
			case DUELIST:
				return new Image(Asset.getAssetFilePath(GeneralAsset.ICON_DUELIST));
			default:
				return null;
		}
	}

	public static Image get(Level.Feeling feeling){
		if (Dungeon.daily){
			if (Dungeon.dailyReplay){
				switch (feeling){
					case NONE: default:
						return new Image(Asset.getAssetFilePath(GeneralAsset.ICON_DEPTH_DAILY_REPEAT));
					case CHASM:
						return new Image(Asset.getAssetFilePath(GeneralAsset.ICON_DEPTH_CHASM_DAILY_REPEAT));
					case WATER:
						return new Image(Asset.getAssetFilePath(GeneralAsset.ICON_DEPTH_WATER_DAILY_REPEAT));
					case GRASS:
						return new Image(Asset.getAssetFilePath(GeneralAsset.ICON_DEPTH_GRASS_DAILY_REPEAT));
					case DARK:
						return new Image(Asset.getAssetFilePath(GeneralAsset.ICON_DEPTH_DARK_DAILY_REPEAT));
					case LARGE:
						return new Image(Asset.getAssetFilePath(GeneralAsset.ICON_DEPTH_LARGE_DAILY_REPEAT));
					case TRAPS:
						return new Image(Asset.getAssetFilePath(GeneralAsset.ICON_DEPTH_TRAPS_DAILY_REPEAT));
					case SECRETS:
						return new Image(Asset.getAssetFilePath(GeneralAsset.ICON_DEPTH_SECRETS_DAILY_REPEAT));
				}
			} else {
				switch (feeling){
					case NONE: default:
						return new Image(Asset.getAssetFilePath(GeneralAsset.ICON_DEPTH_DAILY));
					case CHASM:
						return new Image(Asset.getAssetFilePath(GeneralAsset.ICON_DEPTH_CHASM_DAILY));
					case WATER:
						return new Image(Asset.getAssetFilePath(GeneralAsset.ICON_DEPTH_WATER_DAILY));
					case GRASS:
						return new Image(Asset.getAssetFilePath(GeneralAsset.ICON_DEPTH_GRASS_DAILY));
					case DARK:
						return new Image(Asset.getAssetFilePath(GeneralAsset.ICON_DEPTH_DARK_DAILY));
					case LARGE:
						return new Image(Asset.getAssetFilePath(GeneralAsset.ICON_DEPTH_LARGE_DAILY));
					case TRAPS:
						return new Image(Asset.getAssetFilePath(GeneralAsset.ICON_DEPTH_TRAPS_DAILY));
					case SECRETS:
						return new Image(Asset.getAssetFilePath(GeneralAsset.ICON_DEPTH_SECRETS_DAILY));
				}
			}
		} else if (!Dungeon.customSeedText.isEmpty()){
			switch (feeling){
				case NONE: default:
					return new Image(Asset.getAssetFilePath(GeneralAsset.ICON_DEPTH_SEED));
				case CHASM:
					return new Image(Asset.getAssetFilePath(GeneralAsset.ICON_DEPTH_CHASM_SEED));
				case WATER:
					return new Image(Asset.getAssetFilePath(GeneralAsset.ICON_DEPTH_WATER_SEED));
				case GRASS:
					return new Image(Asset.getAssetFilePath(GeneralAsset.ICON_DEPTH_GRASS_SEED));
				case DARK:
					return new Image(Asset.getAssetFilePath(GeneralAsset.ICON_DEPTH_DARK_SEED));
				case LARGE:
					return new Image(Asset.getAssetFilePath(GeneralAsset.ICON_DEPTH_LARGE_SEED));
				case TRAPS:
					return new Image(Asset.getAssetFilePath(GeneralAsset.ICON_DEPTH_TRAPS_SEED));
				case SECRETS:
					return new Image(Asset.getAssetFilePath(GeneralAsset.ICON_DEPTH_SECRETS_SEED));
			}
		} else {
			switch (feeling){
				case NONE: default:
					return new Image(Asset.getAssetFilePath(GeneralAsset.ICON_DEPTH));
				case CHASM:
					return new Image(Asset.getAssetFilePath(GeneralAsset.ICON_DEPTH_CHASM));
				case WATER:
					return new Image(Asset.getAssetFilePath(GeneralAsset.ICON_DEPTH_WATER));
				case GRASS:
					return new Image(Asset.getAssetFilePath(GeneralAsset.ICON_DEPTH_GRASS));
				case DARK:
					return new Image(Asset.getAssetFilePath(GeneralAsset.ICON_DEPTH_DARK));
				case LARGE:
					return new Image(Asset.getAssetFilePath(GeneralAsset.ICON_DEPTH_LARGE));
				case TRAPS:
					return new Image(Asset.getAssetFilePath(GeneralAsset.ICON_DEPTH_TRAPS));
				case SECRETS:
					return new Image(Asset.getAssetFilePath(GeneralAsset.ICON_DEPTH_SECRETS));
			}
		}
	}
}
