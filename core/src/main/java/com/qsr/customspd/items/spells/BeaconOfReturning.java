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

package com.qsr.customspd.items.spells;

import com.qsr.customspd.Assets;
import com.qsr.customspd.Dungeon;
import com.qsr.customspd.actors.Actor;
import com.qsr.customspd.actors.Char;
import com.qsr.customspd.actors.buffs.Invisibility;
import com.qsr.customspd.actors.hero.Hero;
import com.qsr.customspd.assets.GeneralAsset;
import com.qsr.customspd.effects.Pushing;
import com.qsr.customspd.items.scrolls.ScrollOfTeleportation;
import com.qsr.customspd.items.scrolls.exotic.ScrollOfPassage;
import com.qsr.customspd.levels.Level;
import com.qsr.customspd.messages.Messages;
import com.qsr.customspd.scenes.GameScene;
import com.qsr.customspd.scenes.InterlevelScene;
import com.qsr.customspd.sprites.ItemSprite;
import com.qsr.customspd.assets.GeneralAsset;
import com.qsr.customspd.utils.GLog;
import com.qsr.customspd.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class BeaconOfReturning extends Spell {
	
	{
		image = GeneralAsset.RETURN_BEACON;
	}
	
	public String returnLevel	= "surface";
	public int returnPos;
	
	@Override
	protected void onCast(final Hero hero) {
		
		if (returnLevel.equals("surface")){
			setBeacon(hero);
		} else {
			GameScene.show(new WndOptions(new ItemSprite(this),
					Messages.titleCase(name()),
					Messages.get(BeaconOfReturning.class, "wnd_body"),
					Messages.get(BeaconOfReturning.class, "wnd_set"),
					Messages.get(BeaconOfReturning.class, "wnd_return")){
				@Override
				protected void onSelect(int index) {
					if (index == 0){
						setBeacon(hero);
					} else if (index == 1){
						returnBeacon(hero);
					}
				}
			});
			
		}
	}
	
	//we reset return depth when beacons are dropped to prevent
	//having two stacks of beacons with different return locations
	
	@Override
	protected void onThrow(int cell) {
		returnLevel = "surface";
		super.onThrow(cell);
	}
	
	@Override
	public void doDrop(Hero hero) {
		returnLevel = "surface";
		super.doDrop(hero);
	}
	
	private void setBeacon(Hero hero ){
		returnLevel = Dungeon.levelName;
		returnPos = hero.pos;
		
		hero.spend( 1f );
		hero.busy();
		
		GLog.i( Messages.get(this, "set") );
		
		hero.sprite.operate( hero.pos );
		Sample.INSTANCE.play( Assets.Sounds.BEACON );
		updateQuickslot();
	}
	
	private void returnBeacon( Hero hero ){
		
		if (returnLevel.equals(Dungeon.levelName)) {

			Char existing = Actor.findChar(returnPos);
			if (existing != null && existing != hero){
				Char toPush = !Char.hasProp(existing, Char.Property.IMMOVABLE) ? hero : existing;

				ArrayList<Integer> candidates = new ArrayList<>();
				for (int n : PathFinder.NEIGHBOURS8) {
					int cell = returnPos + n;
					if (!Dungeon.level.solid[cell] && Actor.findChar( cell ) == null
							&& (!Char.hasProp(toPush, Char.Property.LARGE) || Dungeon.level.openSpace[cell])) {
						candidates.add( cell );
					}
				}
				Random.shuffle(candidates);

				if (!candidates.isEmpty()){
					if (toPush == hero){
						returnPos = candidates.get(0);
					} else {
						Actor.addDelayed( new Pushing( toPush, toPush.pos, candidates.get(0) ), -1 );
						toPush.pos = candidates.get(0);
						Dungeon.level.occupyCell(toPush);
					}
				} else {
					GLog.w( Messages.get(ScrollOfTeleportation.class, "no_tele") );
					return;
				}
			}

			if (ScrollOfTeleportation.teleportToLocation(hero, returnPos)){
				hero.spendAndNext( 1f );
			} else {
				return;
			}

		} else {

			if (!Dungeon.interfloorTeleportAllowed()) {
				GLog.w( Messages.get(this, "preventing") );
				return;
			}

			Level.beforeTransition();
			Invisibility.dispel();
			InterlevelScene.mode = InterlevelScene.Mode.RETURN;
			InterlevelScene.returnLevel = returnLevel;
			InterlevelScene.returnPos = returnPos;
			Game.switchScene( InterlevelScene.class );
		}
		detach(hero.belongings.backpack);
	}
	
	@Override
	public String desc() {
		String desc = super.desc();
		if (!returnLevel.equals("surface")){
			desc += "\n\n" + Messages.get(this, "desc_set", returnLevel);
		}
		return desc;
	}
	
	private static final ItemSprite.Glowing WHITE = new ItemSprite.Glowing( 0xFFFFFF );
	
	@Override
	public ItemSprite.Glowing glowing() {
		return !returnLevel.equals("surface") ? WHITE : null;
	}
	
	private static final String LEVEL	= "level";
	private static final String POS		= "pos";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( LEVEL, returnLevel );
		if (!returnLevel.equals("surface")) {
			bundle.put( POS, returnPos );
		}
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		returnLevel	= bundle.getString( LEVEL );
		returnPos	= bundle.getInt( POS );
	}
	
	@Override
	public int value() {
		//prices of ingredients, divided by output quantity, rounds down
		return (int)((50 + 40) * (quantity/5f));
	}
	
	public static class Recipe extends com.qsr.customspd.items.Recipe.SimpleRecipe {
		
		{
			inputs =  new Class[]{ScrollOfPassage.class, ArcaneCatalyst.class};
			inQuantity = new int[]{1, 1};
			
			cost = 6;
			
			output = BeaconOfReturning.class;
			outQuantity = 5;
		}
		
	}
}
