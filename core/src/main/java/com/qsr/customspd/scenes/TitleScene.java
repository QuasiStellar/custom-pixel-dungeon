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

package com.qsr.customspd.scenes;

import com.qsr.customspd.Assets;
import com.qsr.customspd.Chrome;
import com.qsr.customspd.Dungeon;
import com.qsr.customspd.GamesInProgress;
import com.qsr.customspd.SPDSettings;
import com.qsr.customspd.ShatteredPixelDungeon;
import com.qsr.customspd.assets.Asset;
import com.qsr.customspd.assets.GeneralAsset;
import com.qsr.customspd.effects.Fireball;
import com.qsr.customspd.messages.Languages;
import com.qsr.customspd.messages.Messages;
import com.qsr.customspd.modding.TileMapCompilationManager;
import com.qsr.customspd.services.updates.AvailableUpdateData;
import com.qsr.customspd.services.updates.Updates;
import com.qsr.customspd.sprites.CharSprite;
import com.qsr.customspd.ui.Archs;
import com.qsr.customspd.ui.DiscordButton;
import com.qsr.customspd.ui.ExitButton;
import com.qsr.customspd.ui.StyledButton;
import com.qsr.customspd.ui.Window;
import com.qsr.customspd.windows.WndHardNotification;
import com.qsr.customspd.windows.WndMessage;
import com.qsr.customspd.windows.WndMods;
import com.qsr.customspd.windows.WndOptions;
import com.qsr.customspd.windows.WndSettings;
import com.watabou.glwrap.Blending;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.ColorMath;
import com.watabou.utils.DeviceCompat;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class TitleScene extends PixelScene {

	private float time = 0;
	private int direction = 1;
	ArrayList<Fireball> torches = new ArrayList<>();
	Image title;
	int color = Random.Int(16777216);

	@Override
	public void create() {
		
		super.create();

		Music.INSTANCE.playTracks(
				new String[]{Assets.Music.THEME_1, Assets.Music.THEME_2},
				new float[]{1, 1},
				false);

		uiCamera.visible = false;
		
		int w = Camera.main.width;
		int h = Camera.main.height;
		
		Archs archs = new Archs();
		archs.setSize( w, h );
		add( archs );

		for (int i = 0; i < 8; i++) {
			torches.add(placeTorch());
		}

		title = new Image(Asset.getAssetFilePath(GeneralAsset.PIXEL_DUNGEON));
		add( title );

		float topRegion = Math.max(title.height - 6, h*0.45f);

		title.x = (w - title.width()) / 2f;
		title.y = 2 + (topRegion - title.height()) / 2f;

		align(title);

		Image signs = new Image(Asset.getAssetFilePath(GeneralAsset.PIXEL_DUNGEON_SIGNS)) {
			@Override
			public void update() {
				super.update();
				am = Math.max(0f, (float)Math.sin(time / 2f));
				color(color);
			}
			@Override
			public void draw() {
				Blending.setLightMode();
				super.draw();
				Blending.setNormalMode();
			}
		};
		signs.x = title.x + (title.width() - signs.width())/2f;
		signs.y = title.y;
		add( signs );

		final Chrome.Type GREY_TR = Chrome.Type.GREY_BUTTON_TR;

		DiscordButton btnDiscord = new DiscordButton();
		btnDiscord.setPos(5, 5);
		btnDiscord.updateSize();
		add(btnDiscord);

		StyledButton btnPlay = new StyledButton(GREY_TR, Messages.get(this, "enter")){
			@Override
			protected void onClick() {
				if (GamesInProgress.checkAll().size() == 0){
					GamesInProgress.selectedClass = null;
					GamesInProgress.curSlot = 1;
					ShatteredPixelDungeon.switchScene(HeroSelectScene.class);
				} else {
					ShatteredPixelDungeon.switchNoFade( StartScene.class );
				}
			}
			
			@Override
			protected boolean onLongClick() {
				//making it easier to start runs quickly while debugging
				if (DeviceCompat.isDebug()) {
					GamesInProgress.selectedClass = null;
					GamesInProgress.curSlot = 1;
					ShatteredPixelDungeon.switchScene(HeroSelectScene.class);
					return true;
				}
				return super.onLongClick();
			}
		};
		btnPlay.icon(new Image(Asset.getAssetFilePath(GeneralAsset.ICON_ENTER)));
		add(btnPlay);

		StyledButton btnMods = new ModsButton(GREY_TR, Messages.get(this, "mods"));
		add(btnMods);

		StyledButton btnRankings = new StyledButton(GREY_TR,Messages.get(this, "rankings")){
			@Override
			protected void onClick() {
				ShatteredPixelDungeon.switchNoFade( RankingsScene.class );
			}
		};
		btnRankings.icon(new Image(Asset.getAssetFilePath(GeneralAsset.ICON_RANKINGS)));
		add(btnRankings);
		Dungeon.daily = Dungeon.dailyReplay = false;

		StyledButton btnBadges = new StyledButton(GREY_TR, Messages.get(this, "badges")){
			@Override
			protected void onClick() {
				ShatteredPixelDungeon.switchNoFade( BadgesScene.class );
			}
		};
		btnBadges.icon(new Image(Asset.getAssetFilePath(GeneralAsset.ICON_BADGES)));
		add(btnBadges);

		StyledButton btnGuides = new GuidesButton(GREY_TR, Messages.get(this, "guides"));
		btnGuides.icon(new Image(Asset.getAssetFilePath(GeneralAsset.ICON_NEWS)));
		add(btnGuides);

		StyledButton btnChanges = new ChangesButton(GREY_TR, Messages.get(this, "changes"));
		btnChanges.icon(new Image(Asset.getAssetFilePath(GeneralAsset.ICON_CHANGES)));
		add(btnChanges);

		StyledButton btnSettings = new SettingsButton(GREY_TR, Messages.get(this, "settings"));
		add(btnSettings);

		StyledButton btnAbout = new StyledButton(GREY_TR, Messages.get(this, "about")){
			@Override
			protected void onClick() {
				ShatteredPixelDungeon.switchScene( AboutScene.class );
			}
		};
		btnAbout.icon(new Image(Asset.getAssetFilePath(GeneralAsset.ICON_SHPX)));
		add(btnAbout);
		
		final int BTN_HEIGHT = 20;
		int GAP = (int)(h - topRegion - (landscape() ? 3 : 4)*BTN_HEIGHT)/3;
		GAP /= landscape() ? 3 : 5;
		GAP = Math.max(GAP, 2);

		if (landscape()) {
			btnPlay.setRect(title.x-50, topRegion+GAP, ((title.width()+100)/2)-1, BTN_HEIGHT);
			align(btnPlay);
			btnMods.setRect(btnPlay.right()+2, btnPlay.top(), btnPlay.width(), BTN_HEIGHT);
			btnRankings.setRect(btnPlay.left(), btnPlay.bottom()+ GAP, (btnPlay.width()*.67f)-1, BTN_HEIGHT);
			btnBadges.setRect(btnRankings.left(), btnRankings.bottom()+GAP, btnRankings.width(), BTN_HEIGHT);
			btnGuides.setRect(btnRankings.right()+2, btnRankings.top(), btnRankings.width(), BTN_HEIGHT);
			btnChanges.setRect(btnGuides.left(), btnGuides.bottom() + GAP, btnRankings.width(), BTN_HEIGHT);
			btnSettings.setRect(btnGuides.right()+2, btnGuides.top(), btnRankings.width(), BTN_HEIGHT);
			btnAbout.setRect(btnSettings.left(), btnSettings.bottom() + GAP, btnRankings.width(), BTN_HEIGHT);
		} else {
			btnPlay.setRect(title.x, topRegion+GAP, title.width(), BTN_HEIGHT);
			align(btnPlay);
			btnMods.setRect(btnPlay.left(), btnPlay.bottom()+ GAP, btnPlay.width(), BTN_HEIGHT);
			btnRankings.setRect(btnPlay.left(), btnMods.bottom()+ GAP, (btnPlay.width()/2)-1, BTN_HEIGHT);
			btnBadges.setRect(btnRankings.right()+2, btnRankings.top(), btnRankings.width(), BTN_HEIGHT);
			btnGuides.setRect(btnRankings.left(), btnRankings.bottom()+ GAP, btnRankings.width(), BTN_HEIGHT);
			btnChanges.setRect(btnGuides.right()+2, btnGuides.top(), btnGuides.width(), BTN_HEIGHT);
			btnSettings.setRect(btnGuides.left(), btnGuides.bottom()+GAP, btnRankings.width(), BTN_HEIGHT);
			btnAbout.setRect(btnSettings.right()+2, btnSettings.top(), btnSettings.width(), BTN_HEIGHT);
		}

		BitmapText version = new BitmapText( "v" + Game.version, pixelFont);
		version.measure();
		version.hardlight( 0x888888 );
		version.x = w - version.width() - 4;
		version.y = h - version.height() - 2;
		add( version );

		if (DeviceCompat.isDesktop()) {
			ExitButton btnExit = new ExitButton();
			btnExit.setPos( w - btnExit.width(), 0 );
			add( btnExit );
		}

		fadeIn();
	}

	@Override
	public void update() {
		super.update();
		for (int i = 0; i < 8; i++) {
			torches.get(i).setPos(
				title.center().x - (float)Math.sin(time + i * Math.PI / 4f) * 40 * direction,
				title.center().y + (float)Math.cos(time + i * Math.PI / 4f) * 40
			);
			torches.get(i).setAlpha(Math.max(0f, (float)Math.sin(time/ 2f)));
			torches.get(i).setColor(color);
		}
		time += Game.elapsed * 2;
		if (time >= 2f * Math.PI) {
			time = 0;
			direction *= -1;
			color = Random.Int(16777216);
		}
	}

	private Fireball placeTorch() {
		Fireball fb = new Fireball();
		add( fb );
		return fb;
	}

	private static class GuidesButton extends StyledButton {

		public GuidesButton(Chrome.Type type, String label ){
			super(type, label);
		}

		@Override
		protected void onClick() {
			super.onClick();
			ShatteredPixelDungeon.platform.openURI( "https://docs.google.com/document/d/e/2PACX-1vQvjWxI3z9CEp7aAeBzaDi2EBWc-mUOVZ4YbIdIVHvD-L3xq_0ga9PpZkEAxgX2NBaosjqkRssIOSqo/pub" );
		}
	}

	private static class ChangesButton extends StyledButton {

		public ChangesButton( Chrome.Type type, String label ){
			super(type, label);
			if (SPDSettings.updates()) Updates.checkForUpdate();
		}

		boolean updateShown = false;

		@Override
		public void update() {
			super.update();

			if (!updateShown && (Updates.updateAvailable() || Updates.isInstallable())){
				updateShown = true;

				ShatteredPixelDungeon.scene().addToFront(new WndHardNotification(new Image(Asset.getAssetFilePath(GeneralAsset.ICON_WARNING)),
					Messages.get(TitleScene.class, "update_title"),
					Messages.get(TitleScene.class, "update_body"),
					Messages.get(TitleScene.class, "update_okay"),
					5){
					@Override
					public void onBackPressed() {
						//do nothing, must press the okay button
					}
				});

				if (Updates.isInstallable())    text(Messages.get(TitleScene.class, "install"));
				else                            text(Messages.get(TitleScene.class, "update"));
			}

			if (updateShown){
				textColor(ColorMath.interpolate( 0xFFFFFF, Window.SHPX_COLOR, 0.5f + (float)Math.sin(Game.timeTotal*5)/2f));
			}
		}

		@Override
		protected void onClick() {
			if (Updates.isInstallable()){
				Updates.launchInstall();

			} else if (Updates.updateAvailable()){
				AvailableUpdateData update = Updates.updateData();

				ShatteredPixelDungeon.scene().addToFront( new WndOptions(
					new Image(Asset.getAssetFilePath(GeneralAsset.ICON_CHANGES)),
						update.versionName == null ? Messages.get(this,"title") : Messages.get(this,"versioned_title", update.versionName),
						update.desc == null ? Messages.get(this,"desc") : update.desc,
						Messages.get(this,"update"),
						Messages.get(this,"changes")
				) {
					@Override
					protected void onSelect(int index) {
						if (index == 0) {
							Updates.launchUpdate(Updates.updateData());
						} else if (index == 1){
							if (TileMapCompilationManager.INSTANCE.isBusy()) {
								ShatteredPixelDungeon.scene().addToFront(new WndMessage(Messages.get(WndMods.class, "wait")));
								return;
							}
							ChangesScene.changesSelected = 0;
							ShatteredPixelDungeon.switchNoFade( ChangesScene.class );
						}
					}
				});

			} else {
				if (TileMapCompilationManager.INSTANCE.isBusy()) {
					ShatteredPixelDungeon.scene().addToFront(new WndMessage(Messages.get(WndMods.class, "wait")));
					return;
				}
				ChangesScene.changesSelected = 0;
				ShatteredPixelDungeon.switchNoFade( ChangesScene.class );
			}
		}

	}

	private static class SettingsButton extends StyledButton {

		public SettingsButton( Chrome.Type type, String label ){
			super(type, label);
			if (Messages.lang().status() == Languages.Status.UNFINISHED){
				icon(new Image(Asset.getAssetFilePath(GeneralAsset.ICON_LANGS)));
				icon.hardlight(1.5f, 0, 0);
			} else {
				icon(new Image(Asset.getAssetFilePath(GeneralAsset.ICON_PREFS)));
			}
		}

		@Override
		public void update() {
			super.update();

			if (Messages.lang().status() == Languages.Status.UNFINISHED){
				textColor(ColorMath.interpolate( 0xFFFFFF, CharSprite.NEGATIVE, 0.5f + (float)Math.sin(Game.timeTotal*5)/2f));
			}
		}

		@Override
		protected void onClick() {
			if (Messages.lang().status() == Languages.Status.UNFINISHED){
				WndSettings.last_index = 4;
			}
			ShatteredPixelDungeon.scene().add(new WndSettings());
		}
	}

	private static class ModsButton extends StyledButton{

		public ModsButton(Chrome.Type type, String label ){
			super(type, label);
			icon(new Image(Asset.getAssetFilePath(GeneralAsset.ICON_TALENT)));
		}

		@Override
		protected void onClick() {
			ShatteredPixelDungeon.scene().add(new WndMods());
		}
	}
}
