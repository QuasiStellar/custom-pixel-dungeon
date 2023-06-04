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

import com.qsr.customspd.ShatteredPixelDungeon;
import com.qsr.customspd.messages.Messages;
import com.qsr.customspd.modding.Mod;
import com.qsr.customspd.modding.ModManager;
import com.qsr.customspd.scenes.PixelScene;
import com.qsr.customspd.ui.RedButton;
import com.qsr.customspd.ui.RenderedTextBlock;
import com.qsr.customspd.ui.Window;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.utils.Callback;
import java.util.Arrays;
import java.util.List;

public class WndModInfo extends Window {

	private static final float GAP	= 5;

	private static final int WIDTH_MIN = 120;
	private static final int WIDTH_MAX = 220;

	//only one WndModInfo can appear at a time
	private static WndModInfo INSTANCE;

	private Callback updateCallback;

	private List<Image> previews;
	private int currentPreview = 0;

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

		previews = mod.getPreviews();

		RenderedTextBlock author = PixelScene.renderTextBlock(
			Messages.get(this, "author", mod.getInfo().getAuthor()),
			6
		);

		RenderedTextBlock version = PixelScene.renderTextBlock(
			Messages.get(this, "version", mod.getInfo().getVersion()),
			6
		);

		List<String> langs = mod.getInfo().getLanguages();
		RenderedTextBlock languages;
		if (langs != null) {
			String langChars = mod.getInfo().getLanguages().toString();
			languages = PixelScene.renderTextBlock(
				Messages.get(this, "languages", langChars.substring(1, langChars.length() - 1)),
				6
			);
		} else languages = null;

		RenderedTextBlock license = PixelScene.renderTextBlock(
			Messages.get(this, "license", mod.getInfo().getLicense()),
			6
		);

		RenderedTextBlock gameplayMod;
		if (mod.getInfo().getGameplayMod()) {
			gameplayMod = PixelScene.renderTextBlock(
				Messages.get(this, "gameplay_mod"),
				6
			);
		} else gameplayMod = null;

		RedButton link = mod.getInfo().getLink() != null ? new RedButton(Messages.get(
			this,
			"learn_more"
		)) {
			@Override
			protected void onClick() {
				super.onClick();
				Game.platform.openURI( mod.getInfo().getLink() );
			}
		} : null;

		RedButton button = new RedButton(Messages.get(
			this,
			mod.isEnabled() ? "disable" : "enable"
		)) {
			@Override
			protected void onClick() {
				super.onClick();
				boolean isEnabled = mod.isEnabled();
				if (isEnabled) mod.disable();
				else {
					for (String dependency : mod.getInfo().getDependencies()) {
						if (!ModManager.INSTANCE.isEnabled(dependency)) {
							String deps = Arrays.toString(mod.getInfo().getDependencies().toArray(new String[0]));
							ShatteredPixelDungeon.scene().addToFront(new WndMessage(Messages.get(WndModInfo.class, "dependencies", deps.substring(1, deps.length() - 1))));
							return;
						}
					}
					mod.enable();
				}
				text.text(Messages.get(WndModInfo.class, isEnabled ? "enable" : "disable"));
				setSize(reqWidth(), 16);
				setPos(
					link != null ? (title.left() + (title.width() - width() - GAP - link.width()) / 2) : (title.left() + (title.width() - width()) / 2),
					(gameplayMod != null ? gameplayMod.bottom() : license.bottom()) + GAP
				);
				if (link != null) link.setPos(
					right() + GAP,
					top()
				);
				updateCallback.call();
			}
		};

		RedButton left = new RedButton("<") {
			@Override
			protected void onClick() {
				super.onClick();
				previews.get(currentPreview).alpha(0);
				previews.get((currentPreview - 1 + previews.size()) % previews.size()).alpha(1);
				currentPreview = (currentPreview - 1 + previews.size()) % previews.size();
			}
		};

		RedButton right = new RedButton(">") {
			@Override
			protected void onClick() {
				super.onClick();
				previews.get(currentPreview).alpha(0);
				previews.get((currentPreview + 1) % previews.size()).alpha(1);
				currentPreview = (currentPreview + 1) % previews.size();
			}
		};

		layoutFields(title, description, author, version, languages, license, gameplayMod, button, link, left, right);
	}

	private void layoutFields(
		IconTitle title,
		RenderedTextBlock info,
		RenderedTextBlock author,
		RenderedTextBlock version,
		RenderedTextBlock languages,
		RenderedTextBlock license,
		RenderedTextBlock gameplayMod,
		RedButton button,
		RedButton link,
		RedButton left,
		RedButton right
	){
		int width = WIDTH_MIN;

		info.maxWidth(width);
		author.maxWidth(width);
		version.maxWidth(width);
		if (languages != null) languages.maxWidth(width);
		license.maxWidth(width);
		if (gameplayMod != null) gameplayMod.maxWidth(width);

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
			if (languages != null) languages.maxWidth(width);
			license.maxWidth(width);
			if (gameplayMod != null) gameplayMod.maxWidth(width);
		}

		title.setRect( 0, 0, width, 0 );
		add( title );

		info.setPos(title.left(), title.bottom() + GAP);
		add( info );

		float maxHeight = 0;
		for (Image preview : previews) {
			preview.scale.set(width / preview.width);
			preview.x = title.left();
			preview.y = info.bottom() + GAP;
			preview.height *= width / preview.width;
			maxHeight = Math.max(maxHeight, preview.height);
			preview.width *= width / preview.width;
			add( preview );
			preview.alpha(0);
		}
		for (Image preview : previews) {
			preview.y = info.bottom() + GAP + maxHeight / 2 - preview.height / 2;
		}
		if (!previews.isEmpty()) previews.get(0).alpha(1);
		if (previews.size() > 1) {
			left.setSize(left.reqWidth(), 8);
			right.setSize(right.reqWidth(), 8);
			left.setPos(title.left() + (float) width / 2 - left.width() - GAP / 2, previews.get(0).y + maxHeight + GAP / 2);
			right.setPos(left.right() + GAP, left.top());
			add(left);
			add(right);
		}

		float authorY;
		if (previews.isEmpty()) authorY = info.bottom() + GAP;
		else if (previews.size() == 1) authorY = previews.get(0).y + maxHeight + GAP / 2;
		else authorY = left.bottom() + GAP / 2;
		author.setPos(title.left(), authorY);
		add( author );

		version.setPos(title.left(), author.bottom() + GAP);
		add( version );

		if (languages != null) {
			languages.setPos(title.left(), version.bottom() + GAP);
			add( languages );
		}

		license.setPos(title.left(), (languages != null ? languages.bottom() : version.bottom()) + GAP);
		add( license );

		if (gameplayMod != null) {
			gameplayMod.setPos(title.left(), license.bottom() + GAP);
			add( gameplayMod );
		}

		if (link != null) {
			link.setSize(link.reqWidth(), 16);
		}

		button.setSize(button.reqWidth(), 16);
		button.setPos(
			link != null ? (title.left() + (title.width() - button.width() - GAP - link.width()) / 2) : (title.left() + (title.width() - button.width()) / 2),
			(gameplayMod != null ? gameplayMod.bottom() : license.bottom()) + GAP
		);
		add(button);

		if (link != null) {
			link.setPos(
				button.right() + GAP,
				button.top()
			);
			add(link);
		}

		resize(width, (int)(button.bottom() + 2));
	}
}
