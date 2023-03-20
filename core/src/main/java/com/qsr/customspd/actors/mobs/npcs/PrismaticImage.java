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

package com.qsr.customspd.actors.mobs.npcs;

import com.qsr.customspd.Assets;
import com.qsr.customspd.actors.Actor;
import com.qsr.customspd.actors.Char;
import com.qsr.customspd.actors.blobs.CorrosiveGas;
import com.qsr.customspd.actors.blobs.ToxicGas;
import com.qsr.customspd.actors.buffs.AllyBuff;
import com.qsr.customspd.actors.buffs.Buff;
import com.qsr.customspd.actors.buffs.Burning;
import com.qsr.customspd.actors.buffs.PrismaticGuard;
import com.qsr.customspd.actors.hero.Hero;
import com.qsr.customspd.actors.mobs.Mob;
import com.qsr.customspd.effects.CellEmitter;
import com.qsr.customspd.effects.Speck;
import com.qsr.customspd.items.armor.glyphs.AntiMagic;
import com.qsr.customspd.items.armor.glyphs.Brimstone;
import com.qsr.customspd.items.rings.RingOfAccuracy;
import com.qsr.customspd.items.rings.RingOfEvasion;
import com.qsr.customspd.levels.features.Chasm;
import com.qsr.customspd.sprites.CharSprite;
import com.qsr.customspd.sprites.PrismaticSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class PrismaticImage extends NPC {
	
	{
		spriteClass = PrismaticSprite.class;
		
		HP = HT = 10;
		defenseSkill = 1;
		
		alignment = Alignment.ALLY;
		intelligentAlly = true;
		state = HUNTING;
		
		WANDERING = new Wandering();
		
		//before other mobs
		actPriority = MOB_PRIO + 1;
	}
	
	private Hero hero;
	private int heroID;
	public int armTier;
	
	private int deathTimer = -1;
	
	@Override
	protected boolean act() {
		
		if (!isAlive()){
			deathTimer--;
			
			if (deathTimer > 0) {
				sprite.alpha((deathTimer + 3) / 8f);
				spend(TICK);
			} else {
				destroy();
				sprite.die();
			}
			return true;
		}
		
		if (deathTimer != -1){
			if (paralysed == 0) sprite.remove(CharSprite.State.PARALYSED);
			deathTimer = -1;
			sprite.resetColor();
		}
		
		if ( hero == null ){
			hero = (Hero) Actor.findById(heroID);
			if ( hero == null ){
				destroy();
				sprite.die();
				return true;
			}
		}
		
		if (hero.tier() != armTier){
			armTier = hero.tier();
			((PrismaticSprite)sprite).updateArmor( armTier );
		}
		
		return super.act();
	}
	
	@Override
	public void die(Object cause) {
		if (deathTimer == -1) {
			if (cause == Chasm.class){
				super.die( cause );
			} else {
				deathTimer = 5;
				sprite.add(CharSprite.State.PARALYSED);
			}
		}
	}
	
	private static final String HEROID	= "hero_id";
	private static final String TIMER	= "timer";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( HEROID, heroID );
		bundle.put( TIMER, deathTimer );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		heroID = bundle.getInt( HEROID );
		deathTimer = bundle.getInt( TIMER );
	}
	
	public void duplicate( Hero hero, int HP ) {
		this.hero = hero;
		heroID = this.hero.id();
		this.HP = HP;
		HT = PrismaticGuard.maxHP( hero );
	}
	
	@Override
	public int damageRoll() {
		if (hero != null) {
			return Random.NormalIntRange( 2 + hero.lvl/4, 4 + hero.lvl/2 );
		} else {
			return Random.NormalIntRange( 2, 4 );
		}
	}
	
	@Override
	public int attackSkill( Char target ) {
		if (hero != null) {
			//same base attack skill as hero, benefits from accuracy ring
			return (int)((9 + hero.lvl) * RingOfAccuracy.accuracyMultiplier(hero));
		} else {
			return 0;
		}
	}
	
	@Override
	public int defenseSkill(Char enemy) {
		if (hero != null) {
			int baseEvasion = 4 + hero.lvl;
			int heroEvasion = (int)((4 + hero.lvl) * RingOfEvasion.evasionMultiplier( hero ));
			if (hero.belongings.armor() != null){
				heroEvasion = (int)hero.belongings.armor().evasionFactor(this, heroEvasion);
			}

			//if the hero has more/less evasion, 50% of it is applied
			//includes ring of evasion and armor boosts
			return super.defenseSkill(enemy) * (baseEvasion + heroEvasion) / 2;
		} else {
			return 0;
		}
	}
	
	@Override
	public int drRoll() {
		int dr = super.drRoll();
		if (hero != null){
			return dr + hero.drRoll();
		} else {
			return dr;
		}
	}
	
	@Override
	public int defenseProc(Char enemy, int damage) {
		if (hero != null && hero.belongings.armor() != null){
			damage = hero.belongings.armor().proc( enemy, this, damage );
		}
		return super.defenseProc(enemy, damage);
	}
	
	@Override
	public void damage(int dmg, Object src) {
		
		//TODO improve this when I have proper damage source logic
		if (hero != null && hero.belongings.armor() != null && hero.belongings.armor().hasGlyph(AntiMagic.class, this)
				&& AntiMagic.RESISTS.contains(src.getClass())){
			dmg -= AntiMagic.drRoll(hero, hero.belongings.armor().buffedLvl());
		}
		
		super.damage(dmg, src);
	}
	
	@Override
	public float speed() {
		if (hero != null && hero.belongings.armor() != null){
			return hero.belongings.armor().speedFactor(this, super.speed());
		}
		return super.speed();
	}
	
	@Override
	public int attackProc( Char enemy, int damage ) {
		
		if (enemy instanceof Mob) {
			((Mob)enemy).aggro( this );
		}
		
		return super.attackProc( enemy, damage );
	}
	
	@Override
	public CharSprite sprite() {
		CharSprite s = super.sprite();
		
		hero = (Hero)Actor.findById(heroID);
		if (hero != null) {
			armTier = hero.tier();
		}
		((PrismaticSprite)s).updateArmor( armTier );
		return s;
	}
	
	@Override
	public boolean isImmune(Class effect) {
		if (effect == Burning.class
				&& hero != null
				&& hero.belongings.armor() != null
				&& hero.belongings.armor().hasGlyph(Brimstone.class, this)){
			return true;
		}
		return super.isImmune(effect);
	}
	
	{
		immunities.add( ToxicGas.class );
		immunities.add( CorrosiveGas.class );
		immunities.add( Burning.class );
		immunities.add( AllyBuff.class );
	}
	
	private class Wandering extends Mob.Wandering{
		
		@Override
		public boolean act(boolean enemyInFOV, boolean justAlerted) {
			if (!enemyInFOV){
				Buff.affect(hero, PrismaticGuard.class).set( HP );
				destroy();
				CellEmitter.get(pos).start( Speck.factory(Speck.LIGHT), 0.2f, 3 );
				sprite.die();
				Sample.INSTANCE.play( Assets.Sounds.TELEPORT );
				return true;
			} else {
				return super.act(enemyInFOV, justAlerted);
			}
		}
		
	}
	
}
