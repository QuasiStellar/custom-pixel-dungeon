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
import com.qsr.customspd.Badges;
import com.qsr.customspd.Chrome;
import com.qsr.customspd.Dungeon;
import com.qsr.customspd.GamesInProgress;
import com.qsr.customspd.Rankings;
import com.qsr.customspd.SPDSettings;
import com.qsr.customspd.ShatteredPixelDungeon;
import com.qsr.customspd.assets.Asset;
import com.qsr.customspd.assets.GeneralAsset;
import com.qsr.customspd.effects.Fireball;
import com.qsr.customspd.journal.Document;
import com.qsr.customspd.journal.Journal;
import com.qsr.customspd.messages.Messages;
import com.qsr.customspd.modding.TileMapCompilationManager;
import com.qsr.customspd.ui.Archs;
import com.qsr.customspd.ui.RenderedTextBlock;
import com.qsr.customspd.ui.StyledButton;
import com.qsr.customspd.windows.WndError;
import com.qsr.customspd.windows.WndHardNotification;
import com.qsr.customspd.windows.WndMessage;
import com.qsr.customspd.windows.WndMods;
import com.watabou.glwrap.Blending;
import com.watabou.input.ControllerHandler;
import com.watabou.noosa.Camera;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.FileUtils;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collections;

public class WelcomeScene extends PixelScene {

	private static final int LATEST_UPDATE = ShatteredPixelDungeon.v2_1_0_1_0;

	//used so that the game does not keep showing the window forever if cleaning fails
	private static boolean triedCleaningTemp = false;

	private float time = 0;
	private int direction = 1;
	ArrayList<Fireball> torches = new ArrayList<>();
	Image title;
	int color = Random.Int(16777216);

	@Override
	public void create() {
		super.create();

		final int previousVersion = SPDSettings.version();

		if (!triedCleaningTemp && FileUtils.cleanTempFiles()){
			add(new WndHardNotification(new Image(Asset.getAssetFilePath(GeneralAsset.ICON_WARNING)),
					Messages.get(WndError.class, "title"),
					Messages.get(this, "save_warning"),
					Messages.get(this, "continue"),
					5){
				@Override
				public void hide() {
					super.hide();
					triedCleaningTemp = true;
					ShatteredPixelDungeon.resetScene();
				}
			});
			return;
		}

		if (ShatteredPixelDungeon.versionCode == previousVersion && !SPDSettings.intro()) {
			ShatteredPixelDungeon.switchNoFade(TitleScene.class);
			return;
		}

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

		//darkens the arches
		add(new ColorBlock(w, h, 0x88000000));

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
		
		StyledButton okay = new StyledButton(Chrome.Type.GREY_BUTTON_TR, Messages.get(this, "continue")){
			@Override
			protected void onClick() {
				super.onClick();
				if (previousVersion == 0 || SPDSettings.intro()){

					if (previousVersion > 0){
						updateVersion(previousVersion);
					}

					SPDSettings.version(ShatteredPixelDungeon.versionCode);
					GamesInProgress.selectedClass = null;
					GamesInProgress.curSlot = GamesInProgress.firstEmpty();
					if (GamesInProgress.curSlot == -1){
						SPDSettings.intro(false);
						ShatteredPixelDungeon.switchScene(TitleScene.class);
					} else {
						ShatteredPixelDungeon.switchScene(HeroSelectScene.class);
					}
				} else {
					updateVersion(previousVersion);
					ShatteredPixelDungeon.switchScene(TitleScene.class);
				}
			}
		};

		float buttonY = Math.min(topRegion + (PixelScene.landscape() ? 60 : 120), h - 24);

		if (previousVersion != 0 && !SPDSettings.intro()){
			StyledButton changes = new StyledButton(Chrome.Type.GREY_BUTTON_TR, Messages.get(TitleScene.class, "changes")){
				@Override
				protected void onClick() {
					super.onClick();
					if (TileMapCompilationManager.INSTANCE.isBusy()) {
						ShatteredPixelDungeon.scene().addToFront(new WndMessage(Messages.get(WndMods.class, "wait")));
						return;
					}
					updateVersion(previousVersion);
					ShatteredPixelDungeon.switchScene(ChangesScene.class);
				}
			};
			okay.setRect(title.x, buttonY, (title.width()/2)-2, 20);
			add(okay);

			changes.setRect(okay.right()+2, buttonY, (title.width()/2)-2, 20);
			changes.icon(new Image(Asset.getAssetFilePath(GeneralAsset.ICON_CHANGES)));
			add(changes);
		} else {
			okay.text(Messages.get(TitleScene.class, "enter"));
			okay.setRect(title.x, buttonY, title.width(), 20);
			okay.icon(new Image(Asset.getAssetFilePath(GeneralAsset.ICON_ENTER)));
			add(okay);
		}

		RenderedTextBlock text = PixelScene.renderTextBlock(6);
		String message;
		if (previousVersion == 0 || SPDSettings.intro()) {
			message = Document.INTROS.pageBody(0);
		} else if (previousVersion <= ShatteredPixelDungeon.versionCode) {
			if (previousVersion < LATEST_UPDATE){
				message = Messages.get(this, "update_intro");
				message += "\n\n" + Messages.get(this, "update_msg");
			} else {
				//TODO: change the messages here in accordance with the type of patch.
				message = Messages.get(this, "patch_intro");
				message += "\n";
				//message += "\n" + Messages.get(this, "patch_balance");
				message += "\n" + Messages.get(this, "patch_bugfixes");
				message += "\n" + Messages.get(this, "patch_translations");

			}

		} else {
			message = Messages.get(this, "what_msg");
		}

		text.text(message, Math.min(w-20, 300));
		float textSpace = okay.top() - topRegion - 4;
		text.setPos((w - text.width()) / 2f, (topRegion + 2) + (textSpace - text.height())/2);
		add(text);

		if (SPDSettings.intro() && ControllerHandler.isControllerConnected()){
			addToFront(new WndHardNotification(new Image(Asset.getAssetFilePath(GeneralAsset.ICON_CONTROLLER)),
					Messages.get(WelcomeScene.class, "controller_title"),
					Messages.get(WelcomeScene.class, "controller_body"),
					Messages.get(WelcomeScene.class, "controller_okay"),
					0){
				@Override
				public void onBackPressed() {
					//do nothing, must press the okay button
				}
			});
		}
	}

	@Override
	public void update() {
		super.update();
		if (torches.size() == 0) return;
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

	private void updateVersion(int previousVersion){

		//update rankings, to update any data which may be outdated
		if (previousVersion < LATEST_UPDATE){

			Badges.loadGlobal();
			Journal.loadGlobal();

			try {
				Rankings.INSTANCE.load();
				for (Rankings.Record rec : Rankings.INSTANCE.records.toArray(new Rankings.Record[0])){
					try {
						Rankings.INSTANCE.loadGameData(rec);
						Rankings.INSTANCE.saveGameData(rec);
					} catch (Exception e) {
						//if we encounter a fatal per-record error, then clear that record's data
						rec.gameData = null;
						Game.reportException( new RuntimeException("Rankings Updating Failed!",e));
					}
				}
				if (Rankings.INSTANCE.latestDaily != null){
					try {
						Rankings.INSTANCE.loadGameData(Rankings.INSTANCE.latestDaily);
						Rankings.INSTANCE.saveGameData(Rankings.INSTANCE.latestDaily);
					} catch (Exception e) {
						//if we encounter a fatal per-record error, then clear that record's data
						Rankings.INSTANCE.latestDaily.gameData = null;
						Game.reportException( new RuntimeException("Rankings Updating Failed!",e));
					}
				}
				Collections.sort(Rankings.INSTANCE.records, Rankings.scoreComparator);
				Rankings.INSTANCE.save();
			} catch (Exception e) {
				//if we encounter a fatal error, then just clear the rankings
				FileUtils.deleteFile( Rankings.RANKINGS_FILE );
				Game.reportException( new RuntimeException("Rankings Updating Failed!",e));
			}
			Dungeon.daily = Dungeon.dailyReplay = false;

			Badges.saveGlobal(true);
			Journal.saveGlobal(true);

		}

		SPDSettings.version(ShatteredPixelDungeon.versionCode);
	}
	
}
