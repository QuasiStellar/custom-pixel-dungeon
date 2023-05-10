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

package com.qsr.customspd.items.keys;

import com.qsr.customspd.Assets;
import com.qsr.customspd.actors.hero.Hero;
import com.qsr.customspd.items.Item;
import com.qsr.customspd.journal.Notes;
import com.qsr.customspd.scenes.GameScene;
import com.qsr.customspd.windows.WndJournal;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

public abstract class Key extends Item {

	public static final float TIME_TO_UNLOCK = 1f;
	
	{
		stackable = true;
		unique = true;
	}
	
	public String levelName;
	
	@Override
	public boolean isSimilar( Item item ) {
		return super.isSimilar(item) && ((Key)item).levelName.equals(levelName);
	}

	@Override
	public boolean doPickUp(Hero hero, int pos) {
		GameScene.pickUpJournal(this, pos);
		WndJournal.last_index = 2;
		Notes.add(this);
		Sample.INSTANCE.play( Assets.Sounds.ITEM );
		hero.spendAndNext( TIME_TO_PICK_UP );
		GameScene.updateKeyDisplay();
		return true;
	}

	private static final String LEVEL_NAME = "levelName";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put(LEVEL_NAME, levelName );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		levelName = bundle.getString(LEVEL_NAME);
	}
	
	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}

}
