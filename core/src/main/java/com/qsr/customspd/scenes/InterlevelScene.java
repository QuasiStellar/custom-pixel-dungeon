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

import com.qsr.customspd.Chrome;
import com.qsr.customspd.Dungeon;
import com.qsr.customspd.GamesInProgress;
import com.qsr.customspd.ShatteredPixelDungeon;
import com.qsr.customspd.Statistics;
import com.qsr.customspd.actors.Actor;
import com.qsr.customspd.actors.buffs.Buff;
import com.qsr.customspd.actors.buffs.LockedFloor;
import com.qsr.customspd.actors.mobs.Mob;
import com.qsr.customspd.assets.Asset;
import com.qsr.customspd.assets.GeneralAsset;
import com.qsr.customspd.items.Item;
import com.qsr.customspd.items.LostBackpack;
import com.qsr.customspd.levels.Level;
import com.qsr.customspd.levels.Terrain;
import com.qsr.customspd.levels.features.Chasm;
import com.qsr.customspd.levels.features.LevelTransition;
import com.qsr.customspd.levels.rooms.special.SpecialRoom;
import com.qsr.customspd.messages.Messages;
import com.qsr.customspd.services.updates.Updates;
import com.qsr.customspd.ui.GameLog;
import com.qsr.customspd.ui.RenderedTextBlock;
import com.qsr.customspd.ui.StyledButton;
import com.qsr.customspd.ui.Window;
import com.qsr.customspd.utils.BArray;
import com.qsr.customspd.windows.WndError;
import com.watabou.gltextures.TextureCache;
import com.watabou.glwrap.Blending;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.NoosaScript;
import com.watabou.noosa.NoosaScriptNoLighting;
import com.watabou.noosa.SkinnedBlock;
import com.watabou.utils.DeviceCompat;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InterlevelScene extends PixelScene {

	//slow fade on entering a new region
	private static final float SLOW_FADE = 1f; //.33 in, 1.33 steady, .33 out, 2 seconds total
	//norm fade when loading, falling, returning, or descending to a new floor
	private static final float NORM_FADE = 0.67f; //.33 in, .67 steady, .33 out, 1.33 seconds total
	//fast fade when ascending, or descending to a floor you've been on
	private static final float FAST_FADE = 0.50f; //.33 in, .33 steady, .33 out, 1 second total

	private static float fadeTime;

	public enum Mode {
		DESCEND, ASCEND, CONTINUE, RESURRECT, RETURN, FALL, RESET, NONE
	}

	public static Mode mode;

	public static LevelTransition curTransition = null;
	public static String returnLevel;
	public static int returnPos;

	public static boolean fallIntoPit;

	private enum Phase {
		FADE_IN, STATIC, FADE_OUT
	}

	private Phase phase;
	private float timeLeft;

	private RenderedTextBlock message;

	private static Thread thread;
	private static Exception error = null;
	private float waitingTime;

	public static int lastRegion = -1;

	{
		inGameScene = true;
	}

	@Override
	public void create() {
		super.create();

		if (Dungeon.level != null && Dungeon.hero != null && Dungeon.layout.getDungeon().get(Dungeon.levelName).getLocked()) {
			Dungeon.level.unseal();
		}

		String loadingAsset;
		int loadingDepth;
		final float scrollSpeed;
		fadeTime = NORM_FADE;
		switch (mode){
			default:
				scrollSpeed = 0;
				break;
			case CONTINUE:
				scrollSpeed = 5;
				break;
			case DESCEND:
				if (Dungeon.hero == null){
					fadeTime = SLOW_FADE;
				} else {
					loadingDepth = Dungeon.layout.getDungeon().get(curTransition.destLevel).getDepth();
					if (Statistics.deepestFloor >= loadingDepth) {
						fadeTime = FAST_FADE;
					} else if (Dungeon.layout.getDungeon().get(Dungeon.levelName).getBoss()) {
						fadeTime = SLOW_FADE;
					}
				}
				scrollSpeed = 5;
				break;
			case FALL:
				scrollSpeed = 50;
				break;
			case ASCEND:
				fadeTime = FAST_FADE;
				scrollSpeed = -5;
				break;
			case RETURN:
				scrollSpeed = Dungeon.layout.getDungeon().get(returnLevel).getDepth() > Dungeon.depth ? 15 : -15;
				break;
		}

		//flush the texture cache whenever moving between regions, helps reduce memory load
		int region;
		if (curTransition == null) {
			region = -1;
		} else if (Dungeon.layout.getDungeon().get(curTransition.destLevel).getCustomLayout() != null) {
			region = Dungeon.layout.getDungeon().get(curTransition.destLevel).getCustomLayout().getRegion();
		} else {
			switch (Dungeon.layout.getDungeon().get(curTransition.destLevel).getLayout()) {
				case "SewerLevel":
				case "SewerBossLevel":
					region = 1;
					break;
				case "PrisonLevel":
				case "PrisonBossLevel":
					region = 2;
					break;
				case "CavesLevel":
				case "CavesBossLevel":
					region = 3;
					break;
				case "CityLevel":
				case "CityBossLevel":
					region = 4;
					break;
				case "HallsLevel":
				case "HallsBossLevel":
				case "LastLevel":
					region = 5;
					break;
				default:
					region = -1;
			}
		}

		if (region != lastRegion) {
			TextureCache.clear();
			lastRegion = region;
		}

		if      (lastRegion == 1)    loadingAsset = Asset.getAssetFilePath(GeneralAsset.LOADING_SEWERS);
        else if (lastRegion == 2)    loadingAsset = Asset.getAssetFilePath(GeneralAsset.LOADING_PRISON);
		else if (lastRegion == 3)    loadingAsset = Asset.getAssetFilePath(GeneralAsset.LOADING_CAVES);
		else if (lastRegion == 4)    loadingAsset = Asset.getAssetFilePath(GeneralAsset.LOADING_CITY);
		else if (lastRegion == 5)    loadingAsset = Asset.getAssetFilePath(GeneralAsset.LOADING_HALLS);
		else                         loadingAsset = Asset.getAssetFilePath(GeneralAsset.SHADOW);

		//slow down transition when displaying an install prompt
		if (Updates.isInstallable()){
			fadeTime += 0.5f; //adds 1 second total
			//speed up transition when debugging
		} else if (DeviceCompat.isDebug()){
			fadeTime = 0f;
		}

		SkinnedBlock bg = new SkinnedBlock(Camera.main.width, Camera.main.height, loadingAsset ){
			@Override
			protected NoosaScript script() {
				return NoosaScriptNoLighting.get();
			}

			@Override
			public void draw() {
				Blending.disable();
				super.draw();
				Blending.enable();
			}

			@Override
			public void update() {
				super.update();
				offset(0, Game.elapsed * scrollSpeed);
			}
		};
		bg.scale(4, 4);
		bg.autoAdjust = true;
		add(bg);

		Image im = new Image(TextureCache.createGradient(0xAA000000, 0xBB000000, 0xCC000000, 0xDD000000, 0xFF000000)){
			@Override
			public void update() {
				super.update();
				if (phase == Phase.FADE_IN)         aa = Math.max( 0, (timeLeft - (fadeTime - 0.333f)));
				else if (phase == Phase.FADE_OUT)   aa = Math.max( 0, (0.333f - timeLeft));
				else                                aa = 0;
			}
		};
		im.angle = 90;
		im.x = Camera.main.width;
		im.scale.x = Camera.main.height/5f;
		im.scale.y = Camera.main.width;
		add(im);

		String text = Messages.get(Mode.class, mode.name());

		message = PixelScene.renderTextBlock( text, 9 );
		message.setPos(
			(Camera.main.width - message.width()) / 2,
			(Camera.main.height - message.height()) / 2
		);
		align(message);
		add( message );

		if (Updates.isInstallable()){
			StyledButton install = new StyledButton(Chrome.Type.GREY_BUTTON_TR, Messages.get(this, "install")){
				@Override
				public void update() {
					super.update();
					float p = timeLeft / fadeTime;
					if (phase == Phase.FADE_IN)         alpha(1 - p);
					else if (phase == Phase.FADE_OUT)   alpha(p);
					else                                alpha(1);
				}

				@Override
				protected void onClick() {
					super.onClick();
					Updates.launchInstall();
				}
			};
			install.icon(new Image(Asset.getAssetFilePath(GeneralAsset.ICON_CHANGES)));
			install.textColor(Window.SHPX_COLOR);
			install.setSize(install.reqWidth()+5, 20);
			install.setPos((Camera.main.width - install.width())/2, (Camera.main.height - message.bottom())/3 + message.bottom());
			add(install);
		}

		phase = Phase.FADE_IN;
		timeLeft = fadeTime;

		if (thread == null) {
			thread = new Thread() {
				@Override
				public void run() {

					try {

						if (Dungeon.hero != null){
							Dungeon.hero.spendToWhole();
						}
						Actor.fixTime();

						switch (mode) {
							case DESCEND:
								descend();
								break;
							case ASCEND:
								ascend();
								break;
							case CONTINUE:
								restore();
								break;
							case RESURRECT:
								resurrect();
								break;
							case RETURN:
								returnTo();
								break;
							case FALL:
								fall();
								break;
							case RESET:
								reset();
								break;
						}

					} catch (Exception e) {

						error = e;

					}

					if (phase == Phase.STATIC && error == null) {
						phase = Phase.FADE_OUT;
						timeLeft = fadeTime;
					}

					if (Dungeon.level != null && Dungeon.layout.getDungeon().get(Dungeon.levelName).getLocked()) {
						Dungeon.level.seal();
						Dungeon.hero.buff(LockedFloor.class).removeTime(50);
					}
				}
			};
			thread.start();
		}
		waitingTime = 0f;
	}

	@Override
	public void update() {
		super.update();

		waitingTime += Game.elapsed;

		float p = timeLeft / fadeTime;

		switch (phase) {

			case FADE_IN:
				message.alpha( 1 - p );
				if ((timeLeft -= Game.elapsed) <= 0) {
					if (!thread.isAlive() && error == null) {
						phase = Phase.FADE_OUT;
						timeLeft = fadeTime;
					} else {
						phase = Phase.STATIC;
					}
				}
				break;

			case FADE_OUT:
				message.alpha( p );

				if ((timeLeft -= Game.elapsed) <= 0) {
					Game.switchScene( GameScene.class );
					thread = null;
					error = null;
				}
				break;

			case STATIC:
				if (error != null) {
					String errorMsg;
					if (error instanceof FileNotFoundException)     errorMsg = Messages.get(this, "file_not_found");
					else if (error instanceof IOException)          errorMsg = Messages.get(this, "io_error");
					else if (error.getMessage() != null &&
						error.getMessage().equals("old save")) errorMsg = Messages.get(this, "io_error");

					else throw new RuntimeException("fatal error occured while moving between floors. " +
							"Seed:" + Dungeon.seed + " depth:" + Dungeon.depth, error);

					add( new WndError( errorMsg ) {
						public void onBackPressed() {
							super.onBackPressed();
							Game.switchScene( StartScene.class );
						}
					} );
					thread = null;
					error = null;
				} else if (thread != null && (int)waitingTime == 10){
					waitingTime = 11f;
					String s = "";
					for (StackTraceElement t : thread.getStackTrace()){
						s += "\n";
						s += t.toString();
					}
					ShatteredPixelDungeon.reportException(
						new RuntimeException("waited more than 10 seconds on levelgen. " +
							"Seed:" + Dungeon.seed + " depth:" + Dungeon.depth + " trace:" +
							s)
					);
				}
				break;
		}
	}

	private void descend() throws IOException {

		if (Dungeon.hero == null) {
			Mob.clearHeldAllies();
			Dungeon.init();
			GameLog.wipe();

			Level level = Dungeon.newLevel();
			addLevelToVisited();
			Dungeon.switchLevel( level, -1 );
		} else {
			Mob.holdAllies( Dungeon.level );
			Dungeon.saveAll();

			Level level;
			Dungeon.depth = Dungeon.layout.getDungeon().get(curTransition.destLevel).getDepth();
			Dungeon.levelName = curTransition.destLevel;
			if (Arrays.asList(Dungeon.visited).contains(Dungeon.levelName)) {
				level = Dungeon.loadLevel( GamesInProgress.curSlot );
			} else {
				level = Dungeon.newLevel();
				addLevelToVisited();
			}

			LevelTransition destTransition = level.getTransition(curTransition.destType, curTransition.departLevel);
			curTransition = null;
			Dungeon.switchLevel( level, destTransition.cell() );
		}

	}

	private void fall() throws IOException {

		Mob.holdAllies( Dungeon.level );

		Buff.affect( Dungeon.hero, Chasm.Falling.class );
		Dungeon.saveAll();

		Level level;
		Dungeon.levelName = Dungeon.layout.getDungeon().get(Dungeon.levelName).getChasm();
		Dungeon.depth = Dungeon.layout.getDungeon().get(Dungeon.levelName).getDepth();
		if (Arrays.asList(Dungeon.visited).contains(Dungeon.levelName)) {
			level = Dungeon.loadLevel( GamesInProgress.curSlot );
		} else {
			level = Dungeon.newLevel();
			addLevelToVisited();
		}
		Dungeon.switchLevel( level, level.fallCell( fallIntoPit ));
	}

	private void ascend() throws IOException {

		Mob.holdAllies( Dungeon.level );

		Dungeon.saveAll();
		Dungeon.depth = Dungeon.layout.getDungeon().get(curTransition.destLevel).getDepth();
		Dungeon.levelName = curTransition.destLevel;
		Level level;
		if (Arrays.asList(Dungeon.visited).contains(Dungeon.levelName)) {
			level = Dungeon.loadLevel( GamesInProgress.curSlot );
		} else {
			level = Dungeon.newLevel();
			addLevelToVisited();
		}

		LevelTransition destTransition = level.getTransition(curTransition.destType, curTransition.departLevel);
		curTransition = null;
		Dungeon.switchLevel( level, destTransition.cell() );
	}

	private void returnTo() throws IOException {

		Mob.holdAllies( Dungeon.level );

		Dungeon.saveAll();
		Dungeon.depth = Dungeon.layout.getDungeon().get(returnLevel).getDepth();
		Dungeon.levelName = returnLevel;
		Level level;
		if (Arrays.asList(Dungeon.visited).contains(Dungeon.levelName)) {
			level = Dungeon.loadLevel( GamesInProgress.curSlot );
		} else {
			level = Dungeon.newLevel();
			addLevelToVisited();
		}
		Dungeon.switchLevel( level, returnPos );
	}

	private void restore() throws IOException {

		Mob.clearHeldAllies();

		GameLog.wipe();

		Dungeon.loadGame( GamesInProgress.curSlot );

		Level level;
		if (Arrays.asList(Dungeon.visited).contains(Dungeon.levelName)) {
			level = Dungeon.loadLevel( GamesInProgress.curSlot );
		} else {
			level = Dungeon.newLevel();
			addLevelToVisited();
		}
		Dungeon.switchLevel( level, Dungeon.hero.pos );
	}

	private void resurrect() {

		Mob.holdAllies( Dungeon.level );

		Level level;
		if (Dungeon.level.locked) {
			ArrayList<Item> preservedItems = Dungeon.level.getItemsToPreserveFromSealedResurrect();

			Dungeon.hero.resurrect();
			level = Dungeon.newLevel();
			Dungeon.hero.pos = level.randomRespawnCell(Dungeon.hero);
			if (Dungeon.hero.pos == -1) Dungeon.hero.pos = level.entrance();

			for (Item i : preservedItems){
				int pos = level.randomRespawnCell(null);
				if (pos == -1) pos = level.entrance();
				level.drop(i, pos);
			}
			int pos = level.randomRespawnCell(null);
			if (pos == -1) pos = level.entrance();
			level.drop(new LostBackpack(), pos);

		} else {
			level = Dungeon.level;
			BArray.setFalse(level.heroFOV);
			BArray.setFalse(level.visited);
			BArray.setFalse(level.mapped);
			int invPos = Dungeon.hero.pos;
			int tries = 0;
			do {
				Dungeon.hero.pos = level.randomRespawnCell(Dungeon.hero);
				tries++;

				//prevents spawning on traps or plants, prefers farther locations first
			} while (level.traps.get(Dungeon.hero.pos) != null
				|| (level.plants.get(Dungeon.hero.pos) != null && tries < 500)
				|| level.trueDistance(invPos, Dungeon.hero.pos) <= 30 - (tries/10));

			//directly trample grass
			if (level.map[Dungeon.hero.pos] == Terrain.HIGH_GRASS || level.map[Dungeon.hero.pos] == Terrain.FURROWED_GRASS){
				level.map[Dungeon.hero.pos] = Terrain.GRASS;
			}
			Dungeon.hero.resurrect();
			level.drop(new LostBackpack(), invPos);
		}

		Dungeon.switchLevel( level, Dungeon.hero.pos );
	}

	private void reset() throws IOException {

		Mob.holdAllies( Dungeon.level );

		SpecialRoom.resetPitRoom(Dungeon.layout.getDungeon().get(Dungeon.levelName).getChasm());

		Level level = Dungeon.newLevel();
		Dungeon.switchLevel( level, level.entrance() );
	}

	private void addLevelToVisited() {
		List<String> tempVisited = new ArrayList<>(Arrays.asList(Dungeon.visited));
		tempVisited.add(Dungeon.levelName);
		Dungeon.visited = tempVisited.toArray(new String[]{});
	}

	@Override
	protected void onBackPressed() {
		//Do nothing
	}
}
