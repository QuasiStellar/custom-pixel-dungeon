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

package com.qsr.customspd.windows;

import com.qsr.customspd.Dungeon;
import com.qsr.customspd.actors.buffs.MonkEnergy;
import com.qsr.customspd.messages.Messages;
import com.qsr.customspd.scenes.CellSelector;
import com.qsr.customspd.scenes.GameScene;
import com.qsr.customspd.scenes.PixelScene;
import com.qsr.customspd.ui.RedButton;
import com.qsr.customspd.ui.RenderedTextBlock;
import com.qsr.customspd.ui.Window;

public class WndMonkAbilities extends Window {

	private static final int WIDTH_P = 120;
	private static final int WIDTH_L = 160;

	private static final int MARGIN  = 2;

	public WndMonkAbilities( MonkEnergy energyBuff ){
		super();

		int width = PixelScene.landscape() ? WIDTH_L : WIDTH_P;

		float pos = MARGIN;
		RenderedTextBlock title = PixelScene.renderTextBlock(Messages.titleCase(Messages.get(this, "title")), 9);
		title.hardlight(TITLE_COLOR);
		title.setPos((width-title.width())/2, pos);
		title.maxWidth(width - MARGIN * 2);
		add(title);

		pos = title.bottom() + 3*MARGIN;

		for (MonkEnergy.MonkAbility abil : MonkEnergy.MonkAbility.abilities) {
			String text = "_" + Messages.titleCase(abil.name()) + " " + Messages.get(this, "energycost", abil.energyCost()) + ":_ " + abil.desc();
			RedButton moveBtn = new RedButton(text, 6){
				@Override
				protected void onClick() {
					super.onClick();
					hide();
					if (abil.targetingPrompt() != null) {
						abilityBeingUsed = abil;
						GameScene.selectCell(listener);
					} else {
						abil.doAbility(Dungeon.hero, null);
					}
				}
			};
			moveBtn.leftJustify = true;
			moveBtn.multiline = true;
			moveBtn.setSize(width, moveBtn.reqHeight());
			moveBtn.setRect(0, pos, width, moveBtn.reqHeight());
			moveBtn.enable(energyBuff.energy >= abil.energyCost());
			add(moveBtn);
			pos = moveBtn.bottom() + MARGIN;
		}

		resize(width, (int)pos);

	}

	MonkEnergy.MonkAbility abilityBeingUsed;

	private CellSelector.Listener listener = new CellSelector.Listener() {

		@Override
		public void onSelect(Integer cell) {
			abilityBeingUsed.doAbility(Dungeon.hero, cell);
		}

		@Override
		public String prompt() {
			return abilityBeingUsed.targetingPrompt();
		}
	};

}
