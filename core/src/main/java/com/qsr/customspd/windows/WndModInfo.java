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

import com.qsr.customspd.messages.Messages;
import com.qsr.customspd.modding.Mod;
import com.qsr.customspd.scenes.PixelScene;
import com.qsr.customspd.ui.RedButton;
import com.qsr.customspd.ui.RenderedTextBlock;
import com.qsr.customspd.ui.Window;
import com.watabou.utils.Callback;

public class WndModInfo extends Window {

	private static final float GAP	= 5;

	private static final int WIDTH_MIN = 120;
	private static final int WIDTH_MAX = 220;

	//only one WndModInfo can appear at a time
	private static WndModInfo INSTANCE;

	private Callback updateCallback;

	public WndModInfo(Mod mod, Callback updateCallback) {

		super();

		if (INSTANCE != null){
			INSTANCE.hide();
		}
		INSTANCE = this;

		fillFields(mod);
		this.updateCallback = updateCallback;
	}

	@Override
	public void hide() {
		super.hide();
		if (INSTANCE == this){
			INSTANCE = null;
		}
	}

	private void fillFields(Mod mod ) {

		IconTitle title = new IconTitle(mod);
		title.color( TITLE_COLOR );

		RenderedTextBlock description = PixelScene.renderTextBlock(
			mod.getInfo().getDescription(),
			6
		);

		RenderedTextBlock author = PixelScene.renderTextBlock(
			Messages.get(this, "author", mod.getInfo().getAuthor()),
			6
		);

		RenderedTextBlock version = PixelScene.renderTextBlock(
			Messages.get(this, "version", mod.getInfo().getVersion()),
			6
		);

		RenderedTextBlock license = PixelScene.renderTextBlock(
			Messages.get(this, "license", mod.getInfo().getLicense()),
			6
		);

		RedButton button = new RedButton(Messages.get(
			this,
			mod.isEnabled() ? "disable" : "enable"
		)) {
			@Override
			protected void onClick() {
				super.onClick();
				boolean isEnabled = mod.isEnabled();
				if (isEnabled) mod.disable();
				else mod.enable();
				text.text(Messages.get(WndModInfo.class, isEnabled ? "enable" : "disable"));
				setSize(reqWidth(), 16);
				setPos(
					title.left() + (title.width() - width()) / 2,
					license.bottom() + GAP
				);
				updateCallback.call();
			}
		};

		layoutFields(title, description, author, version, license, button);
	}

	private void layoutFields(
		IconTitle title,
		RenderedTextBlock info,
		RenderedTextBlock author,
		RenderedTextBlock version,
		RenderedTextBlock license,
		RedButton button
	){
		int width = WIDTH_MIN;

		info.maxWidth(width);
		author.maxWidth(width);
		version.maxWidth(width);
		license.maxWidth(width);

		//window can go out of the screen on landscape, so widen it as appropriate
		while (PixelScene.landscape()
				&& info.height() > 100
				&& author.height() > 100
				&& version.height() > 100
				&& license.height() > 100
				&& width < WIDTH_MAX){
			width += 20;
			info.maxWidth(width);
			author.maxWidth(width);
			version.maxWidth(width);
			license.maxWidth(width);
		}

		title.setRect( 0, 0, width, 0 );
		add( title );

		info.setPos(title.left(), title.bottom() + GAP);
		add( info );

		author.setPos(title.left(), info.bottom() + GAP);
		add( author );

		version.setPos(title.left(), author.bottom() + GAP);
		add( version );

		license.setPos(title.left(), version.bottom() + GAP);
		add( license );

		button.setSize(button.reqWidth(), 16);
		button.setPos(
			title.left() + (title.width() - button.width()) / 2,
			license.bottom() + GAP
		);
		add(button);

		resize(width, (int)(button.bottom() + 2));
	}
}
