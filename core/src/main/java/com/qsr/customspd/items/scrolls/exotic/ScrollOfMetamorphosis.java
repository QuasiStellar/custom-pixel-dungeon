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

import com.qsr.customspd.Assets;
import com.qsr.customspd.Dungeon;
import com.qsr.customspd.actors.hero.HeroClass;
import com.qsr.customspd.actors.hero.Talent;
import com.qsr.customspd.assets.GeneralAsset;
import com.qsr.customspd.effects.Speck;
import com.qsr.customspd.effects.Transmuting;
import com.qsr.customspd.items.scrolls.InventoryScroll;
import com.qsr.customspd.messages.Messages;
import com.qsr.customspd.scenes.GameScene;
import com.qsr.customspd.scenes.PixelScene;
import com.qsr.customspd.sprites.ItemSprite;
import com.qsr.customspd.ui.RenderedTextBlock;
import com.qsr.customspd.ui.TalentButton;
import com.qsr.customspd.ui.TalentsPane;
import com.qsr.customspd.ui.Window;
import com.qsr.customspd.windows.IconTitle;
import com.qsr.customspd.windows.WndOptions;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;

public class ScrollOfMetamorphosis extends ExoticScroll {
	
	{
		icon = GeneralAsset.ITEM_ICON_SCROLL_METAMORPH;
	}

	protected static boolean identifiedByUse = false;
	
	@Override
	public void doRead() {
		if (!isKnown()) {
			identify();
			identifiedByUse = true;
		} else {
			identifiedByUse = false;
		}
		GameScene.show(new WndMetamorphChoose());
	}

	public static void onMetamorph( Talent oldTalent, Talent newTalent ){
		((ScrollOfMetamorphosis) curItem).readAnimation();
		Sample.INSTANCE.play( Assets.Sounds.READ );
		curUser.sprite.emitter().start(Speck.factory(Speck.CHANGE), 0.2f, 10);
		Transmuting.show(curUser, oldTalent, newTalent);

		if (Dungeon.hero.hasTalent(newTalent)) {
			Talent.onTalentUpgraded(Dungeon.hero, newTalent);
		}
	}

	private void confirmCancelation( Window chooseWindow ) {
		GameScene.show( new WndOptions(new ItemSprite(this),
				Messages.titleCase(name()),
				Messages.get(InventoryScroll.class, "warning"),
				Messages.get(InventoryScroll.class, "yes"),
				Messages.get(InventoryScroll.class, "no") ) {
			@Override
			protected void onSelect( int index ) {
				switch (index) {
					case 0:
						curUser.spendAndNext( TIME_TO_READ );
						identifiedByUse = false;
						chooseWindow.hide();
						break;
					case 1:
						//do nothing
						break;
				}
			}
			public void onBackPressed() {}
		} );
	}

	public static class WndMetamorphChoose extends Window {

		public static WndMetamorphChoose INSTANCE;

		TalentsPane pane;

		public WndMetamorphChoose(){
			super();

			INSTANCE = this;

			float top = 0;

			IconTitle title = new IconTitle( curItem );
			title.color( TITLE_COLOR );
			title.setRect(0, 0, 120, 0);
			add(title);

			top = title.bottom() + 2;

			RenderedTextBlock text = PixelScene.renderTextBlock(Messages.get(ScrollOfMetamorphosis.class, "choose_desc"), 6);
			text.maxWidth(120);
			text.setPos(0, top);
			add(text);

			top = text.bottom() + 2;

			ArrayList<LinkedHashMap<Talent, Integer>> talents = new ArrayList<>();
			Talent.initClassTalents(Dungeon.hero.heroClass, talents, Dungeon.hero.metamorphedTalents);

			for (LinkedHashMap<Talent, Integer> tier : talents){
				for (Talent talent : tier.keySet()){
					tier.put(talent, Dungeon.hero.pointsInTalent(talent));
				}
			}

			pane = new TalentsPane(TalentButton.Mode.METAMORPH_CHOOSE, talents);
			add(pane);
			pane.setPos(0, top);
			pane.setSize(120, pane.content().height());
			resize((int)pane.width(), (int)pane.bottom());
			pane.setPos(0, top);
		}

		@Override
		public void hide() {
			super.hide();
			INSTANCE = null;
		}

		@Override
		public void onBackPressed() {

			if (identifiedByUse){
				((ScrollOfMetamorphosis)curItem).confirmCancelation(this);
			} else {
				super.onBackPressed();
				curItem.collect();
			}
		}

		@Override
		public void offset(int xOffset, int yOffset) {
			super.offset(xOffset, yOffset);
			pane.setPos(pane.left(), pane.top()); //triggers layout
		}
	}

	public static class WndMetamorphReplace extends Window {

		public static WndMetamorphReplace INSTANCE;

		public Talent replacing;
		public int tier;
		LinkedHashMap<Talent, Integer> replaceOptions;

		//for window restoring
		public WndMetamorphReplace(){
			super();

			if (INSTANCE != null){
				replacing = INSTANCE.replacing;
				tier = INSTANCE.tier;
				replaceOptions = INSTANCE.replaceOptions;
				INSTANCE = this;
				setup(replacing, tier, replaceOptions);
			} else {
				hide();
			}
		}

		public WndMetamorphReplace(Talent replacing, int tier){
			super();

			INSTANCE = this;

			this.replacing = replacing;
			this.tier = tier;

			LinkedHashMap<Talent, Integer> options = new LinkedHashMap<>();
			Set<Talent> curTalentsAtTier = Dungeon.hero.talents.get(tier-1).keySet();

			for (HeroClass cls : HeroClass.values()){
				ArrayList<LinkedHashMap<Talent, Integer>> clsTalents = new ArrayList<>();
				Talent.initClassTalents(cls, clsTalents);

				Set<Talent> clsTalentsAtTier = clsTalents.get(tier-1).keySet();
				boolean replacingIsInSet = false;
				for (Talent talent : clsTalentsAtTier.toArray(new Talent[0])){
					if (talent == replacing){
						replacingIsInSet = true;
						break;
					} else {
						if (curTalentsAtTier.contains(talent)){
							clsTalentsAtTier.remove(talent);
						}
					}
				}
				if (!replacingIsInSet && !clsTalentsAtTier.isEmpty()) {
					options.put(Random.element(clsTalentsAtTier), Dungeon.hero.pointsInTalent(replacing));
				}
			}

			replaceOptions = options;
			setup(replacing, tier, options);
		}

		private void setup(Talent replacing, int tier, LinkedHashMap<Talent, Integer> replaceOptions){
			float top = 0;

			IconTitle title = new IconTitle( curItem );
			title.color( TITLE_COLOR );
			title.setRect(0, 0, 120, 0);
			add(title);

			top = title.bottom() + 2;

			RenderedTextBlock text = PixelScene.renderTextBlock(Messages.get(ScrollOfMetamorphosis.class, "replace_desc"), 6);
			text.maxWidth(120);
			text.setPos(0, top);
			add(text);

			top = text.bottom() + 2;

			TalentsPane.TalentTierPane optionsPane = new TalentsPane.TalentTierPane(replaceOptions, tier, TalentButton.Mode.METAMORPH_REPLACE);
			add(optionsPane);
			optionsPane.title.text(" ");
			optionsPane.setPos(0, top);
			optionsPane.setSize(120, optionsPane.height());
			resize((int)optionsPane.width(), (int)optionsPane.bottom());

			resize(120, (int)optionsPane.bottom());
		}

		@Override
		public void hide() {
			super.hide();
			if (INSTANCE == this) {
				INSTANCE = null;
			}
		}

		@Override
		public void onBackPressed() {
			((ScrollOfMetamorphosis)curItem).confirmCancelation(this);
		}
	}
}
