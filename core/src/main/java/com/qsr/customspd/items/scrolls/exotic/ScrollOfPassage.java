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

package com.qsr.customspd.items.scrolls.exotic;

import com.qsr.customspd.Dungeon;
import com.qsr.customspd.assets.GeneralAsset;
import com.qsr.customspd.items.scrolls.ScrollOfTeleportation;
import com.qsr.customspd.levels.Level;
import com.qsr.customspd.messages.Messages;
import com.qsr.customspd.scenes.InterlevelScene;
import com.qsr.customspd.utils.GLog;
import com.watabou.noosa.Game;

public class ScrollOfPassage extends ExoticScroll {
	
	{
		icon = GeneralAsset.ITEM_ICON_SCROLL_PASSAGE;
	}
	
	@Override
	public void doRead() {

		identify();
		readAnimation();
		
		if (!Dungeon.interfloorTeleportAllowed()) {
			
			GLog.w( Messages.get(ScrollOfTeleportation.class, "no_tele") );
			return;
			
		}

		Level.beforeTransition();
		InterlevelScene.mode = InterlevelScene.Mode.RETURN;
		InterlevelScene.returnLevel = Dungeon.layout.getDungeon().get(Dungeon.levelName).getPassage();
		InterlevelScene.returnPos = -1;
		Game.switchScene( InterlevelScene.class );
	}
}
