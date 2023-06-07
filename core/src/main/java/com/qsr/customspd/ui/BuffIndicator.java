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

package com.qsr.customspd.ui;

import com.qsr.customspd.Assets;
import com.qsr.customspd.Dungeon;
import com.qsr.customspd.actors.Char;
import com.qsr.customspd.actors.buffs.Buff;
import com.qsr.customspd.assets.Asset;
import com.qsr.customspd.assets.GeneralAsset;
import com.qsr.customspd.messages.Messages;
import com.qsr.customspd.scenes.GameScene;
import com.qsr.customspd.scenes.PixelScene;
import com.qsr.customspd.sprites.CharSprite;
import com.qsr.customspd.windows.WndInfoBuff;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

import kotlin.Pair;

public class BuffIndicator extends Component {
	
	//transparent icon
	public static final Pair<Asset, Asset> NONE = new Pair<>(GeneralAsset.BLANK, GeneralAsset.BLANK);

	//FIXME this is becoming a mess, should do a big cleaning pass on all of these
	//and think about tinting options
	public static final Pair<Asset, Asset> MIND_VISION = new Pair<>(GeneralAsset.BUFF_MIND_VISION, GeneralAsset.LARGE_BUFF_MIND_VISION);
	public static final Pair<Asset, Asset> LEVITATION = new Pair<>(GeneralAsset.BUFF_LEVITATION, GeneralAsset.LARGE_BUFF_LEVITATION);
	public static final Pair<Asset, Asset> FIRE = new Pair<>(GeneralAsset.BUFF_FIRE, GeneralAsset.LARGE_BUFF_FIRE);
	public static final Pair<Asset, Asset> POISON = new Pair<>(GeneralAsset.BUFF_POISON, GeneralAsset.LARGE_BUFF_POISON);
	public static final Pair<Asset, Asset> PARALYSIS = new Pair<>(GeneralAsset.BUFF_PARALYSIS, GeneralAsset.LARGE_BUFF_PARALYSIS);
	public static final Pair<Asset, Asset> HUNGER = new Pair<>(GeneralAsset.BUFF_HUNGER, GeneralAsset.LARGE_BUFF_HUNGER);
	public static final Pair<Asset, Asset> STARVATION = new Pair<>(GeneralAsset.BUFF_STARVATION, GeneralAsset.LARGE_BUFF_STARVATION);
	public static final Pair<Asset, Asset> TIME = new Pair<>(GeneralAsset.BUFF_TIME, GeneralAsset.LARGE_BUFF_TIME);
	public static final Pair<Asset, Asset> OOZE = new Pair<>(GeneralAsset.BUFF_OOZE, GeneralAsset.LARGE_BUFF_OOZE);
	public static final Pair<Asset, Asset> AMOK = new Pair<>(GeneralAsset.BUFF_AMOK, GeneralAsset.LARGE_BUFF_AMOK);
	public static final Pair<Asset, Asset> TERROR = new Pair<>(GeneralAsset.BUFF_TERROR, GeneralAsset.LARGE_BUFF_TERROR);
	public static final Pair<Asset, Asset> ROOTS = new Pair<>(GeneralAsset.BUFF_ROOTS, GeneralAsset.LARGE_BUFF_ROOTS);
	public static final Pair<Asset, Asset> INVISIBLE = new Pair<>(GeneralAsset.BUFF_INVISIBLE, GeneralAsset.LARGE_BUFF_INVISIBLE);
	public static final Pair<Asset, Asset> SHADOWS = new Pair<>(GeneralAsset.BUFF_SHADOWS, GeneralAsset.LARGE_BUFF_SHADOWS);
	public static final Pair<Asset, Asset> WEAKNESS = new Pair<>(GeneralAsset.BUFF_WEAKNESS, GeneralAsset.LARGE_BUFF_WEAKNESS);
	public static final Pair<Asset, Asset> FROST = new Pair<>(GeneralAsset.BUFF_FROST, GeneralAsset.LARGE_BUFF_FROST);
	public static final Pair<Asset, Asset> BLINDNESS = new Pair<>(GeneralAsset.BUFF_BLINDNESS, GeneralAsset.LARGE_BUFF_BLINDNESS);
	public static final Pair<Asset, Asset> COMBO = new Pair<>(GeneralAsset.BUFF_COMBO, GeneralAsset.LARGE_BUFF_COMBO);
	public static final Pair<Asset, Asset> FURY = new Pair<>(GeneralAsset.BUFF_FURY, GeneralAsset.LARGE_BUFF_FURY);
	public static final Pair<Asset, Asset> HERB_HEALING = new Pair<>(GeneralAsset.BUFF_HERB_HEALING, GeneralAsset.LARGE_BUFF_HERB_HEALING);
	public static final Pair<Asset, Asset> ARMOR = new Pair<>(GeneralAsset.BUFF_ARMOR, GeneralAsset.LARGE_BUFF_ARMOR);
	public static final Pair<Asset, Asset> HEART = new Pair<>(GeneralAsset.BUFF_HEART, GeneralAsset.LARGE_BUFF_HEART);
	public static final Pair<Asset, Asset> LIGHT = new Pair<>(GeneralAsset.BUFF_LIGHT, GeneralAsset.LARGE_BUFF_LIGHT);
	public static final Pair<Asset, Asset> CRIPPLE = new Pair<>(GeneralAsset.BUFF_CRIPPLE, GeneralAsset.LARGE_BUFF_CRIPPLE);
	public static final Pair<Asset, Asset> BARKSKIN = new Pair<>(GeneralAsset.BUFF_BARKSKIN, GeneralAsset.LARGE_BUFF_BARKSKIN);
	public static final Pair<Asset, Asset> IMMUNITY = new Pair<>(GeneralAsset.BUFF_IMMUNITY, GeneralAsset.LARGE_BUFF_IMMUNITY);
	public static final Pair<Asset, Asset> BLEEDING = new Pair<>(GeneralAsset.BUFF_BLEEDING, GeneralAsset.LARGE_BUFF_BLEEDING);
	public static final Pair<Asset, Asset> MARK = new Pair<>(GeneralAsset.BUFF_MARK, GeneralAsset.LARGE_BUFF_MARK);
	public static final Pair<Asset, Asset> DEFERRED = new Pair<>(GeneralAsset.BUFF_DEFERRED, GeneralAsset.LARGE_BUFF_DEFERRED);
	public static final Pair<Asset, Asset> DROWSY = new Pair<>(GeneralAsset.BUFF_DROWSY, GeneralAsset.LARGE_BUFF_DROWSY);
	public static final Pair<Asset, Asset> MAGIC_SLEEP = new Pair<>(GeneralAsset.BUFF_MAGIC_SLEEP, GeneralAsset.LARGE_BUFF_MAGIC_SLEEP);
	public static final Pair<Asset, Asset> THORNS = new Pair<>(GeneralAsset.BUFF_THORNS, GeneralAsset.LARGE_BUFF_THORNS);
	public static final Pair<Asset, Asset> FORESIGHT = new Pair<>(GeneralAsset.BUFF_FORESIGHT, GeneralAsset.LARGE_BUFF_FORESIGHT);
	public static final Pair<Asset, Asset> VERTIGO = new Pair<>(GeneralAsset.BUFF_VERTIGO, GeneralAsset.LARGE_BUFF_VERTIGO);
	public static final Pair<Asset, Asset> RECHARGING = new Pair<>(GeneralAsset.BUFF_RECHARGING, GeneralAsset.LARGE_BUFF_RECHARGING);
	public static final Pair<Asset, Asset> LOCKED_FLOOR = new Pair<>(GeneralAsset.BUFF_LOCKED_FLOOR, GeneralAsset.LARGE_BUFF_LOCKED_FLOOR);
	public static final Pair<Asset, Asset> CORRUPT = new Pair<>(GeneralAsset.BUFF_CORRUPT, GeneralAsset.LARGE_BUFF_CORRUPT);
	public static final Pair<Asset, Asset> BLESS = new Pair<>(GeneralAsset.BUFF_BLESS, GeneralAsset.LARGE_BUFF_BLESS);
	public static final Pair<Asset, Asset> RAGE = new Pair<>(GeneralAsset.BUFF_RAGE, GeneralAsset.LARGE_BUFF_RAGE);
	public static final Pair<Asset, Asset> SACRIFICE = new Pair<>(GeneralAsset.BUFF_SACRIFICE, GeneralAsset.LARGE_BUFF_SACRIFICE);
	public static final Pair<Asset, Asset> BERSERK = new Pair<>(GeneralAsset.BUFF_BERSERK, GeneralAsset.LARGE_BUFF_BERSERK);
	public static final Pair<Asset, Asset> HASTE = new Pair<>(GeneralAsset.BUFF_HASTE, GeneralAsset.LARGE_BUFF_HASTE);
	public static final Pair<Asset, Asset> PREPARATION = new Pair<>(GeneralAsset.BUFF_PREPARATION, GeneralAsset.LARGE_BUFF_PREPARATION);
	public static final Pair<Asset, Asset> WELL_FED = new Pair<>(GeneralAsset.BUFF_WELL_FED, GeneralAsset.LARGE_BUFF_WELL_FED);
	public static final Pair<Asset, Asset> HEALING = new Pair<>(GeneralAsset.BUFF_HEALING, GeneralAsset.LARGE_BUFF_HEALING);
	public static final Pair<Asset, Asset> WEAPON = new Pair<>(GeneralAsset.BUFF_WEAPON, GeneralAsset.LARGE_BUFF_WEAPON);
	public static final Pair<Asset, Asset> VULNERABLE = new Pair<>(GeneralAsset.BUFF_VULNERABLE, GeneralAsset.LARGE_BUFF_VULNERABLE);
	public static final Pair<Asset, Asset> HEX = new Pair<>(GeneralAsset.BUFF_HEX, GeneralAsset.LARGE_BUFF_HEX);
	public static final Pair<Asset, Asset> DEGRADE = new Pair<>(GeneralAsset.BUFF_DEGRADE, GeneralAsset.LARGE_BUFF_DEGRADE);
	public static final Pair<Asset, Asset> PINCUSHION = new Pair<>(GeneralAsset.BUFF_PINCUSHION, GeneralAsset.LARGE_BUFF_PINCUSHION);
	public static final Pair<Asset, Asset> UPGRADE = new Pair<>(GeneralAsset.BUFF_UPGRADE, GeneralAsset.LARGE_BUFF_UPGRADE);
	public static final Pair<Asset, Asset> MOMENTUM = new Pair<>(GeneralAsset.BUFF_MOMENTUM, GeneralAsset.LARGE_BUFF_MOMENTUM);
	public static final Pair<Asset, Asset> ANKH = new Pair<>(GeneralAsset.BUFF_ANKH, GeneralAsset.LARGE_BUFF_ANKH);
	public static final Pair<Asset, Asset> NOINV = new Pair<>(GeneralAsset.BUFF_NOINV, GeneralAsset.LARGE_BUFF_NOINV);
	public static final Pair<Asset, Asset> TARGETED = new Pair<>(GeneralAsset.BUFF_TARGETED, GeneralAsset.LARGE_BUFF_TARGETED);
	public static final Pair<Asset, Asset> IMBUE = new Pair<>(GeneralAsset.BUFF_IMBUE, GeneralAsset.LARGE_BUFF_IMBUE);
	public static final Pair<Asset, Asset> ENDURE = new Pair<>(GeneralAsset.BUFF_ENDURE, GeneralAsset.LARGE_BUFF_ENDURE);
	public static final Pair<Asset, Asset> INVERT_MARK = new Pair<>(GeneralAsset.BUFF_INVERT_MARK, GeneralAsset.LARGE_BUFF_INVERT_MARK);
	public static final Pair<Asset, Asset> NATURE_POWER = new Pair<>(GeneralAsset.BUFF_NATURE_POWER, GeneralAsset.LARGE_BUFF_NATURE_POWER);
	public static final Pair<Asset, Asset> AMULET = new Pair<>(GeneralAsset.BUFF_AMULET, GeneralAsset.LARGE_BUFF_AMULET);
	public static final Pair<Asset, Asset> DUEL_CLEAVE = new Pair<>(GeneralAsset.BUFF_DUEL_CLEAVE, GeneralAsset.LARGE_BUFF_DUEL_CLEAVE);
	public static final Pair<Asset, Asset> DUEL_GUARD = new Pair<>(GeneralAsset.BUFF_DUEL_GUARD, GeneralAsset.LARGE_BUFF_DUEL_GUARD);
	public static final Pair<Asset, Asset> DUEL_SPIN = new Pair<>(GeneralAsset.BUFF_DUEL_SPIN, GeneralAsset.LARGE_BUFF_DUEL_SPIN);
	public static final Pair<Asset, Asset> DUEL_EVASIVE = new Pair<>(GeneralAsset.BUFF_DUEL_EVASIVE, GeneralAsset.LARGE_BUFF_DUEL_EVASIVE);
	public static final Pair<Asset, Asset> DUEL_DANCE = new Pair<>(GeneralAsset.BUFF_DUEL_DANCE, GeneralAsset.LARGE_BUFF_DUEL_DANCE);
	public static final Pair<Asset, Asset> DUEL_BRAWL = new Pair<>(GeneralAsset.BUFF_DUEL_BRAWL, GeneralAsset.LARGE_BUFF_DUEL_BRAWL);
	public static final Pair<Asset, Asset> DUEL_XBOW = new Pair<>(GeneralAsset.BUFF_DUEL_XBOW, GeneralAsset.LARGE_BUFF_DUEL_XBOW);
	public static final Pair<Asset, Asset> CHALLENGE = new Pair<>(GeneralAsset.BUFF_CHALLENGE, GeneralAsset.LARGE_BUFF_CHALLENGE);
	public static final Pair<Asset, Asset> MONK_ENERGY = new Pair<>(GeneralAsset.BUFF_MONK_ENERGY, GeneralAsset.LARGE_BUFF_MONK_ENERGY);
	public static final Pair<Asset, Asset> DUEL_COMBO = new Pair<>(GeneralAsset.BUFF_DUEL_COMBO, GeneralAsset.LARGE_BUFF_DUEL_COMBO);
	public static final Pair<Asset, Asset> DAZE = new Pair<>(GeneralAsset.BUFF_DAZE, GeneralAsset.LARGE_BUFF_DAZE);

	public static final int SIZE_SMALL  = 7;
	public static final int SIZE_LARGE  = 16;
	
	private static BuffIndicator heroInstance;
	private static BuffIndicator bossInstance;
	
	private LinkedHashMap<Buff, BuffButton> buffButtons = new LinkedHashMap<>();
	private boolean needsRefresh;
	private Char ch;

	private boolean large = false;
	
	public BuffIndicator( Char ch, boolean large ) {
		super();
		
		this.ch = ch;
		this.large = large;
		if (ch == Dungeon.hero) {
			heroInstance = this;
		}
	}
	
	@Override
	public void destroy() {
		super.destroy();
		
		if (this == heroInstance) {
			heroInstance = null;
		}
	}

	@Override
	public synchronized void update() {
		super.update();
		if (needsRefresh){
			needsRefresh = false;
			layout();
		}
	}

	private boolean buffsHidden = false;

	@Override
	protected void layout() {

		ArrayList<Buff> newBuffs = new ArrayList<>();
		for (Buff buff : ch.buffs()) {
			if (buff.icon() != NONE) {
				newBuffs.add(buff);
			}
		}

		int size = large ? SIZE_LARGE : SIZE_SMALL;

		//remove any icons no longer present
		for (Buff buff : buffButtons.keySet().toArray(new Buff[0])){
			if (!newBuffs.contains(buff)){
				Image icon = buffButtons.get( buff ).icon;
				icon.originToCenter();
				icon.alpha(0.6f);
				add( icon );
				add( new AlphaTweener( icon, 0, 0.6f ) {
					@Override
					protected void updateValues( float progress ) {
						super.updateValues( progress );
						image.scale.set( 1 + 5 * progress );
					}
					
					@Override
					protected void onComplete() {
						image.killAndErase();
					}
				} );
				
				buffButtons.get( buff ).destroy();
				remove(buffButtons.get( buff ));
				buffButtons.remove( buff );
			}
		}
		
		//add new icons
		for (Buff buff : newBuffs) {
			if (!buffButtons.containsKey(buff)) {
				BuffButton icon = new BuffButton(buff, large);
				add(icon);
				buffButtons.put( buff, icon );
			}
		}
		
		//layout
		int pos = 0;
		float lastIconLeft = 0;
		for (BuffButton icon : buffButtons.values()){
			icon.updateIcon();
			//button areas are slightly oversized, especially on small buttons
			icon.setRect(x + pos * (size + 1), y, size + 1, size + (large ? 0 : 5));
			PixelScene.align(icon);
			pos++;

			icon.visible = icon.left() <= right();
			lastIconLeft = icon.left();
		}

		buffsHidden = false;
		//squish buff icons together if there isn't enough room
		float excessWidth = lastIconLeft - right();
		if (excessWidth > 0) {
			float leftAdjust = excessWidth/(buffButtons.size()-1);
			//can't squish by more than 50% on large and 62% on small
			if (large && leftAdjust >= size*0.48f) leftAdjust = size*0.5f;
			if (!large && leftAdjust >= size*0.62f) leftAdjust = size*0.65f;
			float cumulativeAdjust = leftAdjust * (buffButtons.size()-1);

			ArrayList<BuffButton> buttons = new ArrayList<>(buffButtons.values());
			Collections.reverse(buttons);
			for (BuffButton icon : buttons) {
				icon.setPos(icon.left() - cumulativeAdjust, icon.top());
				icon.visible = icon.left() <= right();
				if (!icon.visible) buffsHidden = true;
				PixelScene.align(icon);
				bringToFront(icon);
				icon.givePointerPriority();
				cumulativeAdjust -= leftAdjust;
			}
		}
	}

	public boolean allBuffsVisible(){
		return !buffsHidden;
	}

	private static class BuffButton extends IconButton {

		private Buff buff;

		private boolean large;

		public Image grey; //only for small
		public BitmapText text; //only for large

		public BuffButton( Buff buff, boolean large ){
			super( new BuffIcon(buff, large));
			this.buff = buff;
			this.large = large;

			bringToFront(grey);
			bringToFront(text);
		}

		@Override
		protected void createChildren() {
			super.createChildren();
			grey = new Image( TextureCache.createSolid(0xCC666666));
			add( grey );

			text = new BitmapText(PixelScene.pixelFont);
			add( text );
		}

		public void updateIcon(){
			((BuffIcon)icon).refresh(buff);
			//round up to the nearest pixel if <50% faded, otherwise round down
			if (!large || buff.iconTextDisplay().isEmpty()) {
				text.visible = false;
				grey.visible = true;
				float fadeHeight = buff.iconFadePercent() * icon.height();
				float zoom = (camera() != null) ? camera().zoom : 1;
				if (fadeHeight < icon.height() / 2f) {
					grey.scale.set(icon.width(), (float) Math.ceil(zoom * fadeHeight) / zoom);
				} else {
					grey.scale.set(icon.width(), (float) Math.floor(zoom * fadeHeight) / zoom);
				}
			} else if (!buff.iconTextDisplay().isEmpty()) {
				text.visible = true;
				grey.visible = false;
				if (buff.type == Buff.buffType.POSITIVE)        text.hardlight(CharSprite.POSITIVE);
				else if (buff.type == Buff.buffType.NEGATIVE)   text.hardlight(CharSprite.NEGATIVE);
				text.alpha(0.7f);

				text.text(buff.iconTextDisplay());
				text.measure();
			}
		}

		@Override
		protected void layout() {
			super.layout();
			grey.x = icon.x = this.x + (large ? 0 : 1);
			grey.y = icon.y = this.y + (large ? 0 : 2);

			if (text.width > width()){
				text.scale.set(PixelScene.align(0.5f));
			} else {
				text.scale.set(1f);
			}
			text.x = this.x + width() - text.width() - 1;
			text.y = this.y + width() - text.baseLine() - 2;
		}

		@Override
		protected void onClick() {
			if (buff.icon() != NONE) GameScene.show(new WndInfoBuff(buff));
		}

		@Override
		protected void onPointerDown() {
			//don't affect buff color
			Sample.INSTANCE.play( Assets.Sounds.CLICK );
		}

		@Override
		protected void onPointerUp() {
			//don't affect buff color
		}

		@Override
		protected String hoverText() {
			return Messages.titleCase(buff.name());
		}
	}
	
	public static void refreshHero() {
		if (heroInstance != null) {
			heroInstance.needsRefresh = true;
		}
	}

	public static void refreshBoss(){
		if (bossInstance != null) {
			bossInstance.needsRefresh = true;
		}
	}

	public static void setBossInstance(BuffIndicator boss){
		bossInstance = boss;
	}
}
