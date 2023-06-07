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

package com.qsr.customspd.actors.hero;

import com.qsr.customspd.Assets;
import com.qsr.customspd.Dungeon;
import com.qsr.customspd.GamesInProgress;
import com.qsr.customspd.ShatteredPixelDungeon;
import com.qsr.customspd.actors.Actor;
import com.qsr.customspd.actors.Char;
import com.qsr.customspd.actors.buffs.ArtifactRecharge;
import com.qsr.customspd.actors.buffs.Barrier;
import com.qsr.customspd.actors.buffs.Buff;
import com.qsr.customspd.actors.buffs.CounterBuff;
import com.qsr.customspd.actors.buffs.EnhancedRings;
import com.qsr.customspd.actors.buffs.FlavourBuff;
import com.qsr.customspd.actors.buffs.Haste;
import com.qsr.customspd.actors.buffs.LostInventory;
import com.qsr.customspd.actors.buffs.PhysicalEmpower;
import com.qsr.customspd.actors.buffs.Recharging;
import com.qsr.customspd.actors.buffs.RevealedArea;
import com.qsr.customspd.actors.buffs.Roots;
import com.qsr.customspd.actors.buffs.WandEmpower;
import com.qsr.customspd.actors.hero.abilities.ArmorAbility;
import com.qsr.customspd.actors.hero.abilities.Ratmogrify;
import com.qsr.customspd.actors.mobs.Mob;
import com.qsr.customspd.assets.Asset;
import com.qsr.customspd.effects.CellEmitter;
import com.qsr.customspd.effects.Speck;
import com.qsr.customspd.effects.SpellSprite;
import com.qsr.customspd.effects.particles.LeafParticle;
import com.qsr.customspd.items.BrokenSeal;
import com.qsr.customspd.items.Item;
import com.qsr.customspd.items.armor.Armor;
import com.qsr.customspd.items.armor.ClothArmor;
import com.qsr.customspd.items.artifacts.CloakOfShadows;
import com.qsr.customspd.items.artifacts.HornOfPlenty;
import com.qsr.customspd.items.rings.Ring;
import com.qsr.customspd.items.scrolls.ScrollOfRecharging;
import com.qsr.customspd.items.wands.Wand;
import com.qsr.customspd.items.weapon.SpiritBow;
import com.qsr.customspd.items.weapon.Weapon;
import com.qsr.customspd.items.weapon.melee.Gloves;
import com.qsr.customspd.items.weapon.melee.MagesStaff;
import com.qsr.customspd.items.weapon.melee.MeleeWeapon;
import com.qsr.customspd.items.weapon.missiles.MissileWeapon;
import com.qsr.customspd.levels.Level;
import com.qsr.customspd.levels.Terrain;
import com.qsr.customspd.messages.Messages;
import com.qsr.customspd.assets.GeneralAsset;
import com.qsr.customspd.scenes.GameScene;
import com.qsr.customspd.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;

import kotlin.Pair;

public enum Talent {

	//Warrior T1
	HEARTY_MEAL(GeneralAsset.TALENT_ICON_HEARTY_MEAL), VETERANS_INTUITION(GeneralAsset.TALENT_ICON_VETERANS_INTUITION), TEST_SUBJECT(GeneralAsset.TALENT_ICON_TEST_SUBJECT), IRON_WILL(GeneralAsset.TALENT_ICON_IRON_WILL),
	//Warrior T2
	IRON_STOMACH(GeneralAsset.TALENT_ICON_IRON_STOMACH), RESTORED_WILLPOWER(GeneralAsset.TALENT_ICON_RESTORED_WILLPOWER), RUNIC_TRANSFERENCE(GeneralAsset.TALENT_ICON_RUNIC_TRANSFERENCE), LETHAL_MOMENTUM(GeneralAsset.TALENT_ICON_LETHAL_MOMENTUM), IMPROVISED_PROJECTILES(GeneralAsset.TALENT_ICON_IMPROVISED_PROJECTILES),
	//Warrior T3
	HOLD_FAST(GeneralAsset.TALENT_ICON_HOLD_FAST, 3), STRONGMAN(GeneralAsset.TALENT_ICON_STRONGMAN, 3),
	//Berserker T3
	ENDLESS_RAGE(GeneralAsset.TALENT_ICON_ENDLESS_RAGE, 3), DEATHLESS_FURY(GeneralAsset.TALENT_ICON_DEATHLESS_FURY, 3), ENRAGED_CATALYST(GeneralAsset.TALENT_ICON_ENRAGED_CATALYST, 3),
	//Gladiator T3
	CLEAVE(GeneralAsset.TALENT_ICON_CLEAVE, 3), LETHAL_DEFENSE(GeneralAsset.TALENT_ICON_LETHAL_DEFENSE, 3), ENHANCED_COMBO(GeneralAsset.TALENT_ICON_ENHANCED_COMBO, 3),
	//Heroic Leap T4
	BODY_SLAM(GeneralAsset.TALENT_ICON_BODY_SLAM, 4), IMPACT_WAVE(GeneralAsset.TALENT_ICON_IMPACT_WAVE, 4), DOUBLE_JUMP(GeneralAsset.TALENT_ICON_DOUBLE_JUMP, 4),
	//Shockwave T4
	EXPANDING_WAVE(GeneralAsset.TALENT_ICON_EXPANDING_WAVE, 4), STRIKING_WAVE(GeneralAsset.TALENT_ICON_STRIKING_WAVE, 4), SHOCK_FORCE(GeneralAsset.TALENT_ICON_SHOCK_FORCE, 4),
	//Endure T4
	SUSTAINED_RETRIBUTION(GeneralAsset.TALENT_ICON_SUSTAINED_RETRIBUTION, 4), SHRUG_IT_OFF(GeneralAsset.TALENT_ICON_SHRUG_IT_OFF, 4), EVEN_THE_ODDS(GeneralAsset.TALENT_ICON_EVEN_THE_ODDS, 4),

	//Mage T1
	EMPOWERING_MEAL(GeneralAsset.TALENT_ICON_EMPOWERING_MEAL), SCHOLARS_INTUITION(GeneralAsset.TALENT_ICON_SCHOLARS_INTUITION), TESTED_HYPOTHESIS(GeneralAsset.TALENT_ICON_TESTED_HYPOTHESIS), BACKUP_BARRIER(GeneralAsset.TALENT_ICON_BACKUP_BARRIER),
	//Mage T2
	ENERGIZING_MEAL(GeneralAsset.TALENT_ICON_ENERGIZING_MEAL), ENERGIZING_UPGRADE(GeneralAsset.TALENT_ICON_ENERGIZING_UPGRADE), WAND_PRESERVATION(GeneralAsset.TALENT_ICON_WAND_PRESERVATION), ARCANE_VISION(GeneralAsset.TALENT_ICON_ARCANE_VISION), SHIELD_BATTERY(GeneralAsset.TALENT_ICON_SHIELD_BATTERY),
	//Mage T3
	EMPOWERING_SCROLLS(GeneralAsset.TALENT_ICON_EMPOWERING_SCROLLS, 3), ALLY_WARP(GeneralAsset.TALENT_ICON_ALLY_WARP, 3),
	//Battlemage T3
	EMPOWERED_STRIKE(GeneralAsset.TALENT_ICON_EMPOWERED_STRIKE, 3), MYSTICAL_CHARGE(GeneralAsset.TALENT_ICON_MYSTICAL_CHARGE, 3), EXCESS_CHARGE(GeneralAsset.TALENT_ICON_EXCESS_CHARGE, 3),
	//Warlock T3
	SOUL_EATER(GeneralAsset.TALENT_ICON_SOUL_EATER, 3), SOUL_SIPHON(GeneralAsset.TALENT_ICON_SOUL_SIPHON, 3), NECROMANCERS_MINIONS(GeneralAsset.TALENT_ICON_NECROMANCERS_MINIONS, 3),
	//Elemental Blast T4
	BLAST_RADIUS(GeneralAsset.TALENT_ICON_BLAST_RADIUS, 4), ELEMENTAL_POWER(GeneralAsset.TALENT_ICON_ELEMENTAL_POWER, 4), REACTIVE_BARRIER(GeneralAsset.TALENT_ICON_REACTIVE_BARRIER, 4),
	//Wild Magic T4
	WILD_POWER(GeneralAsset.TALENT_ICON_WILD_POWER, 4), FIRE_EVERYTHING(GeneralAsset.TALENT_ICON_FIRE_EVERYTHING, 4), CONSERVED_MAGIC(GeneralAsset.TALENT_ICON_CONSERVED_MAGIC, 4),
	//Warp Beacon T4
	TELEFRAG(GeneralAsset.TALENT_ICON_TELEFRAG, 4), REMOTE_BEACON(GeneralAsset.TALENT_ICON_REMOTE_BEACON, 4), LONGRANGE_WARP(GeneralAsset.TALENT_ICON_LONGRANGE_WARP, 4),

	//Rogue T1
	CACHED_RATIONS(GeneralAsset.TALENT_ICON_CACHED_RATIONS), THIEFS_INTUITION(GeneralAsset.TALENT_ICON_THIEFS_INTUITION), SUCKER_PUNCH(GeneralAsset.TALENT_ICON_SUCKER_PUNCH), PROTECTIVE_SHADOWS(GeneralAsset.TALENT_ICON_PROTECTIVE_SHADOWS),
	//Rogue T2
	MYSTICAL_MEAL(GeneralAsset.TALENT_ICON_MYSTICAL_MEAL), MYSTICAL_UPGRADE(GeneralAsset.TALENT_ICON_MYSTICAL_UPGRADE), WIDE_SEARCH(GeneralAsset.TALENT_ICON_WIDE_SEARCH), SILENT_STEPS(GeneralAsset.TALENT_ICON_SILENT_STEPS), ROGUES_FORESIGHT(GeneralAsset.TALENT_ICON_ROGUES_FORESIGHT),
	//Rogue T3
	ENHANCED_RINGS(GeneralAsset.TALENT_ICON_ENHANCED_RINGS, 3), LIGHT_CLOAK(GeneralAsset.TALENT_ICON_LIGHT_CLOAK, 3),
	//Assassin T3
	ENHANCED_LETHALITY(GeneralAsset.TALENT_ICON_ENHANCED_LETHALITY, 3), ASSASSINS_REACH(GeneralAsset.TALENT_ICON_ASSASSINS_REACH, 3), BOUNTY_HUNTER(GeneralAsset.TALENT_ICON_BOUNTY_HUNTER, 3),
	//Freerunner T3
	EVASIVE_ARMOR(GeneralAsset.TALENT_ICON_EVASIVE_ARMOR, 3), PROJECTILE_MOMENTUM(GeneralAsset.TALENT_ICON_PROJECTILE_MOMENTUM, 3), SPEEDY_STEALTH(GeneralAsset.TALENT_ICON_SPEEDY_STEALTH, 3),
	//Smoke Bomb T4
	HASTY_RETREAT(GeneralAsset.TALENT_ICON_HASTY_RETREAT, 4), BODY_REPLACEMENT(GeneralAsset.TALENT_ICON_BODY_REPLACEMENT, 4), SHADOW_STEP(GeneralAsset.TALENT_ICON_SHADOW_STEP, 4),
	//Death Mark T4
	FEAR_THE_REAPER(GeneralAsset.TALENT_ICON_FEAR_THE_REAPER, 4), DEATHLY_DURABILITY(GeneralAsset.TALENT_ICON_DEATHLY_DURABILITY, 4), DOUBLE_MARK(GeneralAsset.TALENT_ICON_DOUBLE_MARK, 4),
	//Shadow Clone T4
	SHADOW_BLADE(GeneralAsset.TALENT_ICON_SHADOW_BLADE, 4), CLONED_ARMOR(GeneralAsset.TALENT_ICON_CLONED_ARMOR, 4), PERFECT_COPY(GeneralAsset.TALENT_ICON_PERFECT_COPY, 4),

	//Huntress T1
	NATURES_BOUNTY(GeneralAsset.TALENT_ICON_NATURES_BOUNTY), SURVIVALISTS_INTUITION(GeneralAsset.TALENT_ICON_SURVIVALISTS_INTUITION), FOLLOWUP_STRIKE(GeneralAsset.TALENT_ICON_FOLLOWUP_STRIKE), NATURES_AID(GeneralAsset.TALENT_ICON_NATURES_AID),
	//Huntress T2
	INVIGORATING_MEAL(GeneralAsset.TALENT_ICON_INVIGORATING_MEAL), RESTORED_NATURE(GeneralAsset.TALENT_ICON_RESTORED_NATURE), REJUVENATING_STEPS(GeneralAsset.TALENT_ICON_REJUVENATING_STEPS), HEIGHTENED_SENSES(GeneralAsset.TALENT_ICON_HEIGHTENED_SENSES), DURABLE_PROJECTILES(GeneralAsset.TALENT_ICON_DURABLE_PROJECTILES),
	//Huntress T3
	POINT_BLANK(GeneralAsset.TALENT_ICON_POINT_BLANK, 3), SEER_SHOT(GeneralAsset.TALENT_ICON_SEER_SHOT, 3),
	//Sniper T3
	FARSIGHT(GeneralAsset.TALENT_ICON_FARSIGHT, 3), SHARED_ENCHANTMENT(GeneralAsset.TALENT_ICON_SHARED_ENCHANTMENT, 3), SHARED_UPGRADES(GeneralAsset.TALENT_ICON_SHARED_UPGRADES, 3),
	//Warden T3
	DURABLE_TIPS(GeneralAsset.TALENT_ICON_DURABLE_TIPS, 3), BARKSKIN(GeneralAsset.TALENT_ICON_BARKSKIN, 3), SHIELDING_DEW(GeneralAsset.TALENT_ICON_SHIELDING_DEW, 3),
	//Spectral Blades T4
	FAN_OF_BLADES(GeneralAsset.TALENT_ICON_FAN_OF_BLADES, 4), PROJECTING_BLADES(GeneralAsset.TALENT_ICON_PROJECTING_BLADES, 4), SPIRIT_BLADES(GeneralAsset.TALENT_ICON_SPIRIT_BLADES, 4),
	//Natures Power T4
	GROWING_POWER(GeneralAsset.TALENT_ICON_GROWING_POWER, 4), NATURES_WRATH(GeneralAsset.TALENT_ICON_NATURES_WRATH, 4), WILD_MOMENTUM(GeneralAsset.TALENT_ICON_WILD_MOMENTUM, 4),
	//Spirit Hawk T4
	EAGLE_EYE(GeneralAsset.TALENT_ICON_EAGLE_EYE, 4), GO_FOR_THE_EYES(GeneralAsset.TALENT_ICON_GO_FOR_THE_EYES, 4), SWIFT_SPIRIT(GeneralAsset.TALENT_ICON_SWIFT_SPIRIT, 4),

	//Duelist T1
	STRENGTHENING_MEAL(GeneralAsset.TALENT_ICON_STRENGTHENING_MEAL), ADVENTURERS_INTUITION(GeneralAsset.TALENT_ICON_ADVENTURERS_INTUITION), PATIENT_STRIKE(GeneralAsset.TALENT_ICON_PATIENT_STRIKE), AGGRESSIVE_BARRIER(GeneralAsset.TALENT_ICON_AGGRESSIVE_BARRIER),
	//Duelist T2
	FOCUSED_MEAL(GeneralAsset.TALENT_ICON_FOCUSED_MEAL), RESTORED_AGILITY(GeneralAsset.TALENT_ICON_RESTORED_AGILITY), WEAPON_RECHARGING(GeneralAsset.TALENT_ICON_WEAPON_RECHARGING), LETHAL_HASTE(GeneralAsset.TALENT_ICON_LETHAL_HASTE), SWIFT_EQUIP(GeneralAsset.TALENT_ICON_SWIFT_EQUIP),
	//Duelist T3
    PRECISE_ASSAULT(GeneralAsset.TALENT_ICON_PRECISE_ASSAULT, 3), DEADLY_FOLLOWUP(GeneralAsset.TALENT_ICON_DEADLY_FOLLOWUP, 3),
	//Champion T3
	SECONDARY_CHARGE(GeneralAsset.TALENT_ICON_SECONDARY_CHARGE, 3), TWIN_UPGRADES(GeneralAsset.TALENT_ICON_TWIN_UPGRADES, 3), COMBINED_LETHALITY(GeneralAsset.TALENT_ICON_COMBINED_LETHALITY, 3),
	//Monk T3
	UNENCUMBERED_SPIRIT(GeneralAsset.TALENT_ICON_UNENCUMBERED_SPIRIT, 3), MONASTIC_VIGOR(GeneralAsset.TALENT_ICON_MONASTIC_VIGOR, 3), COMBINED_ENERGY(GeneralAsset.TALENT_ICON_COMBINED_ENERGY, 3),
	//Challenge T4
	CLOSE_THE_GAP(GeneralAsset.TALENT_ICON_CLOSE_THE_GAP, 4), INVIGORATING_VICTORY(GeneralAsset.TALENT_ICON_INVIGORATING_VICTORY, 4), ELIMINATION_MATCH(GeneralAsset.TALENT_ICON_ELIMINATION_MATCH, 4),
	//Elemental Strike T4
	ELEMENTAL_REACH(GeneralAsset.TALENT_ICON_ELEMENTAL_REACH, 4), STRIKING_FORCE(GeneralAsset.TALENT_ICON_STRIKING_FORCE, 4), DIRECTED_POWER(GeneralAsset.TALENT_ICON_DIRECTED_POWER, 4),
	//Duelist A3 T4
	FEIGNED_RETREAT(GeneralAsset.TALENT_ICON_FEIGNED_RETREAT, 4), EXPOSE_WEAKNESS(GeneralAsset.TALENT_ICON_EXPOSE_WEAKNESS, 4), COUNTER_ABILITY(GeneralAsset.TALENT_ICON_COUNTER_ABILITY, 4),

	//universal T4
	HEROIC_ENERGY(GeneralAsset.BLANK, 4),
	//Ratmogrify T4
	RATSISTANCE(GeneralAsset.TALENT_ICON_RATSISTANCE, 4), RATLOMACY(GeneralAsset.TALENT_ICON_RATLOMACY, 4), RATFORCEMENTS(GeneralAsset.TALENT_ICON_RATFORCEMENTS, 4);

	public static class ImprovisedProjectileCooldown extends FlavourBuff{
		public Pair<Asset, Asset> icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight(0.15f, 0.2f, 0.5f); }
		public float iconFadePercent() { return Math.max(0, visualcooldown() / 50); }
	};
	public static class LethalMomentumTracker extends FlavourBuff{};
	public static class StrikingWaveTracker extends FlavourBuff{};
	public static class WandPreservationCounter extends CounterBuff{{revivePersists = true;}};
	public static class EmpoweredStrikeTracker extends FlavourBuff{};
	public static class ProtectiveShadowsTracker extends Buff {
		float barrierInc = 0.5f;

		@Override
		public boolean act() {
			//barrier every 2/1 turns, to a max of 3/5
			if (((Hero)target).hasTalent(Talent.PROTECTIVE_SHADOWS) && target.invisible > 0){
				Barrier barrier = Buff.affect(target, Barrier.class);
				if (barrier.shielding() < 1 + 2*((Hero)target).pointsInTalent(Talent.PROTECTIVE_SHADOWS)) {
					barrierInc += 0.5f * ((Hero) target).pointsInTalent(Talent.PROTECTIVE_SHADOWS);
				}
				if (barrierInc >= 1){
					barrierInc = 0;
					barrier.incShield(1);
				} else {
					barrier.incShield(0); //resets barrier decay
				}
			} else {
				detach();
			}
			spend( TICK );
			return true;
		}

		private static final String BARRIER_INC = "barrier_inc";
		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put( BARRIER_INC, barrierInc);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			barrierInc = bundle.getFloat( BARRIER_INC );
		}
	}
	public static class BountyHunterTracker extends FlavourBuff{};
	public static class RejuvenatingStepsCooldown extends FlavourBuff{
		public Pair<Asset, Asset> icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight(0f, 0.35f, 0.15f); }
		public float iconFadePercent() { return GameMath.gate(0, visualcooldown() / (15 - 5*Dungeon.hero.pointsInTalent(REJUVENATING_STEPS)), 1); }
	};
	public static class RejuvenatingStepsFurrow extends CounterBuff{{revivePersists = true;}};
	public static class SeerShotCooldown extends FlavourBuff{
		public Pair<Asset, Asset> icon() { return target.buff(RevealedArea.class) != null ? BuffIndicator.NONE : BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight(0.7f, 0.4f, 0.7f); }
		public float iconFadePercent() { return Math.max(0, visualcooldown() / 20); }
	};
	public static class SpiritBladesTracker extends FlavourBuff{};
	public static class PatientStrikeTracker extends Buff {
		public int pos;
		{ type = Buff.buffType.POSITIVE; }
		public Pair<Asset, Asset> icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight(0.5f, 0f, 1f); }
		@Override
		public boolean act() {
			if (pos != target.pos) {
				detach();
			} else {
				spend(TICK);
			}
			return true;
		}
		private static final String POS = "pos";
		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(POS, pos);
		}
		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			pos = bundle.getInt(POS);
		}
	};
	public static class AggressiveBarrierCooldown extends FlavourBuff{
		public Pair<Asset, Asset> icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight(0.35f, 0f, 0.7f); }
		public float iconFadePercent() { return Math.max(0, visualcooldown() / 50); }
	};
	public static class RestoredAgilityTracker extends FlavourBuff{};
	public static class LethalHasteCooldown extends FlavourBuff{
		public Pair<Asset, Asset> icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) { icon.hardlight(0.35f, 0f, 0.7f); }
		public float iconFadePercent() { return Math.max(0, visualcooldown() / 100); }
	};
	public static class SwiftEquipCooldown extends FlavourBuff{
		public boolean secondUse;
		public boolean hasSecondUse(){
			return secondUse && cooldown() > 14f;
		}

		public Pair<Asset, Asset> icon() { return BuffIndicator.TIME; }
		public void tintIcon(Image icon) {
			if (hasSecondUse()) icon.hardlight(0.85f, 0f, 1.0f);
			else                icon.hardlight(0.35f, 0f, 0.7f);
		}
		public float iconFadePercent() { return GameMath.gate(0, visualcooldown() / 20f, 1); }

		private static final String SECOND_USE = "second_use";
		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(SECOND_USE, secondUse);
		}
		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			secondUse = bundle.getBoolean(SECOND_USE);
		}
	};
	public static class DeadlyFollowupTracker extends FlavourBuff{
		public int object;
		{ type = Buff.buffType.POSITIVE; }
		public Pair<Asset, Asset> icon() { return BuffIndicator.INVERT_MARK; }
		public void tintIcon(Image icon) { icon.hardlight(0.5f, 0f, 1f); }
		public float iconFadePercent() { return Math.max(0, 1f - (visualcooldown() / 5)); }
		private static final String OBJECT    = "object";
		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(OBJECT, object);
		}
		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			object = bundle.getInt(OBJECT);
		}
	}
	public static class PreciseAssaultTracker extends FlavourBuff{
		{ type = buffType.POSITIVE; }
		public Pair<Asset, Asset> icon() { return BuffIndicator.INVERT_MARK; }
		public void tintIcon(Image icon) { icon.hardlight(1f, 1f, 0.0f); }
		public float iconFadePercent() { return Math.max(0, 1f - (visualcooldown() / 5)); }
	};
	public static class CombinedLethalityAbilityTracker extends FlavourBuff{
		public MeleeWeapon weapon;
	};
	public static class CombinedLethalityTriggerTracker extends FlavourBuff{
		{ type = buffType.POSITIVE; }
		public Pair<Asset, Asset> icon() { return BuffIndicator.CORRUPT; }
		public void tintIcon(Image icon) { icon.hardlight(0.6f, 0.15f, 0.6f); }
		public float iconFadePercent() { return Math.max(0, 1f - (visualcooldown() / 5)); }
	};
	public static class CombinedEnergyAbilityTracker extends FlavourBuff{
		public int energySpent = -1;
		public boolean wepAbilUsed = false;
	}
	public static class CounterAbilityTacker extends FlavourBuff{};

	Asset asset;
	int maxPoints;

	// tiers 1/2/3/4 start at levels 2/7/13/21
	public static int[] tierLevelThresholds = new int[]{0, 2, 7, 13, 21, 31};

	Talent( Asset asset){
		this(asset, 2);
	}

	Talent(Asset asset, int maxPoints ){
		this.asset = asset;
		this.maxPoints = maxPoints;
	}

	public Asset icon(){
		if (this == HEROIC_ENERGY){
			if (Ratmogrify.useRatroicEnergy){
				return GeneralAsset.TALENT_ICON_HEROIC_ENERGY_RAT;
			}
			HeroClass cls = Dungeon.hero != null ? Dungeon.hero.heroClass : GamesInProgress.selectedClass;
			switch (cls){
				case WARRIOR: default:
					return GeneralAsset.TALENT_ICON_HEROIC_ENERGY_WARRIOR;
				case MAGE:
					return GeneralAsset.TALENT_ICON_HEROIC_ENERGY_MAGE;
				case ROGUE:
					return GeneralAsset.TALENT_ICON_HEROIC_ENERGY_ROGUE;
				case HUNTRESS:
					return GeneralAsset.TALENT_ICON_HEROIC_ENERGY_HUNTRESS;
				case DUELIST:
					return GeneralAsset.TALENT_ICON_HEROIC_ENERGY_DUELIST;
			}
		} else {
			return asset;
		}
	}

	public int maxPoints(){
		return maxPoints;
	}

	public String title(){
		if (this == HEROIC_ENERGY && Ratmogrify.useRatroicEnergy){
			return Messages.get(this, name() + ".rat_title");
		}
		return Messages.get(this, name() + ".title");
	}

	public final String desc(){
		return desc(false);
	}

	public String desc(boolean metamorphed){
		if (metamorphed){
			String metaDesc = Messages.get(this, name() + ".meta_desc");
			if (!metaDesc.equals(Messages.NO_TEXT_FOUND)){
				return Messages.get(this, name() + ".desc") + "\n\n" + metaDesc;
			}
		}
		return Messages.get(this, name() + ".desc");
	}

	public static void onTalentUpgraded( Hero hero, Talent talent ){
		//for metamorphosis
		if (talent == IRON_WILL && hero.heroClass != HeroClass.WARRIOR){
			Buff.affect(hero, BrokenSeal.WarriorShield.class);
		}

		if (talent == VETERANS_INTUITION && hero.pointsInTalent(VETERANS_INTUITION) == 2){
			if (hero.belongings.armor() != null)  hero.belongings.armor.identify();
		}
		if (talent == THIEFS_INTUITION && hero.pointsInTalent(THIEFS_INTUITION) == 2){
			if (hero.belongings.ring instanceof Ring) hero.belongings.ring.identify();
			if (hero.belongings.misc instanceof Ring) hero.belongings.misc.identify();
			for (Item item : Dungeon.hero.belongings){
				if (item instanceof Ring){
					((Ring) item).setKnown();
				}
			}
		}
		if (talent == THIEFS_INTUITION && hero.pointsInTalent(THIEFS_INTUITION) == 1){
			if (hero.belongings.ring instanceof Ring) hero.belongings.ring.setKnown();
			if (hero.belongings.misc instanceof Ring) ((Ring) hero.belongings.misc).setKnown();
		}
		if (talent == ADVENTURERS_INTUITION && hero.pointsInTalent(ADVENTURERS_INTUITION) == 2){
			if (hero.belongings.weapon() != null) hero.belongings.weapon().identify();
		}

		if (talent == PROTECTIVE_SHADOWS && hero.invisible > 0){
			Buff.affect(hero, Talent.ProtectiveShadowsTracker.class);
		}

		if (talent == LIGHT_CLOAK && hero.heroClass == HeroClass.ROGUE){
			for (Item item : Dungeon.hero.belongings.backpack){
				if (item instanceof CloakOfShadows){
					if (hero.buff(LostInventory.class) == null || item.keptThoughLostInvent) {
						((CloakOfShadows) item).activate(Dungeon.hero);
					}
				}
			}
		}

		if (talent == HEIGHTENED_SENSES || talent == FARSIGHT){
			Dungeon.observe();
		}

		if (talent == SECONDARY_CHARGE || talent == TWIN_UPGRADES){
			Item.updateQuickslot();
		}

		if (talent == UNENCUMBERED_SPIRIT && hero.pointsInTalent(talent) == 3){
			Item toGive = new ClothArmor().identify();
			if (!toGive.collect()){
				Dungeon.level.drop(toGive, hero.pos).sprite.drop();
			}
			toGive = new Gloves().identify();
			if (!toGive.collect()){
				Dungeon.level.drop(toGive, hero.pos).sprite.drop();
			}
		}
	}

	public static class CachedRationsDropped extends CounterBuff{{revivePersists = true;}};
	public static class NatureBerriesAvailable extends CounterBuff{{revivePersists = true;}}; //for pre-1.3.0 saves
	public static class NatureBerriesDropped extends CounterBuff{{revivePersists = true;}};

	public static void onFoodEaten( Hero hero, float foodVal, Item foodSource ){
		if (hero.hasTalent(HEARTY_MEAL)){
			//3/5 HP healed, when hero is below 25% health
			if (hero.HP <= hero.HT/4) {
				hero.HP = Math.min(hero.HP + 1 + 2 * hero.pointsInTalent(HEARTY_MEAL), hero.HT);
				hero.sprite.emitter().burst(Speck.factory(Speck.HEALING), 1+hero.pointsInTalent(HEARTY_MEAL));
			//2/3 HP healed, when hero is below 50% health
			} else if (hero.HP <= hero.HT/2){
				hero.HP = Math.min(hero.HP + 1 + hero.pointsInTalent(HEARTY_MEAL), hero.HT);
				hero.sprite.emitter().burst(Speck.factory(Speck.HEALING), hero.pointsInTalent(HEARTY_MEAL));
			}
		}
		if (hero.hasTalent(IRON_STOMACH)){
			if (hero.cooldown() > 0) {
				Buff.affect(hero, WarriorFoodImmunity.class, hero.cooldown());
			}
		}
		if (hero.hasTalent(EMPOWERING_MEAL)){
			//2/3 bonus wand damage for next 3 zaps
			Buff.affect( hero, WandEmpower.class).set(1 + hero.pointsInTalent(EMPOWERING_MEAL), 3);
			ScrollOfRecharging.charge( hero );
		}
		if (hero.hasTalent(ENERGIZING_MEAL)){
			//5/8 turns of recharging
			Buff.prolong( hero, Recharging.class, 2 + 3*(hero.pointsInTalent(ENERGIZING_MEAL)) );
			ScrollOfRecharging.charge( hero );
			SpellSprite.show(hero, GeneralAsset.CHARGE);
		}
		if (hero.hasTalent(MYSTICAL_MEAL)){
			//3/5 turns of recharging
			ArtifactRecharge buff = Buff.affect( hero, ArtifactRecharge.class);
			if (buff.left() < 1 + 2*(hero.pointsInTalent(MYSTICAL_MEAL))){
				Buff.affect( hero, ArtifactRecharge.class).set(1 + 2*(hero.pointsInTalent(MYSTICAL_MEAL))).ignoreHornOfPlenty = foodSource instanceof HornOfPlenty;
			}
			ScrollOfRecharging.charge( hero );
			SpellSprite.show(hero, GeneralAsset.CHARGE, 0, 1, 1);
		}
		if (hero.hasTalent(INVIGORATING_MEAL)){
			//effectively 1/2 turns of haste
			Buff.prolong( hero, Haste.class, 0.67f+hero.pointsInTalent(INVIGORATING_MEAL));
		}
		if (hero.hasTalent(STRENGTHENING_MEAL)){
			//3 bonus physical damage for next 2/3 attacks
			Buff.affect( hero, PhysicalEmpower.class).set(3, 1 + hero.pointsInTalent(STRENGTHENING_MEAL));
		}
		if (hero.hasTalent(FOCUSED_MEAL)){
			if (hero.heroClass == HeroClass.DUELIST){
				//1/1.5 charge for the duelist
				Buff.affect( hero, MeleeWeapon.Charger.class ).gainCharge(0.5f*(hero.pointsInTalent(FOCUSED_MEAL)+1));
			} else {
				// lvl/3 / lvl/2 bonus dmg on next hit for other classes
				Buff.affect( hero, PhysicalEmpower.class).set(Math.round(hero.lvl / (4f - hero.pointsInTalent(FOCUSED_MEAL))), 1);
			}
		}
	}

	public static class WarriorFoodImmunity extends FlavourBuff{
		{ actPriority = HERO_PRIO+1; }
	}

	public static float itemIDSpeedFactor( Hero hero, Item item ){
		// 1.75x/2.5x speed with Huntress talent
		float factor = 1f + 0.75f*hero.pointsInTalent(SURVIVALISTS_INTUITION);

		// Affected by both Warrior(1.75x/2.5x) and Duelist(2.5x/inst.) talents
		if (item instanceof MeleeWeapon){
			factor *= 1f + 1.5f*hero.pointsInTalent(ADVENTURERS_INTUITION); //instant at +2 (see onItemEquipped)
			factor *= 1f + 0.75f*hero.pointsInTalent(VETERANS_INTUITION);
		}
		// Affected by both Warrior(2.5x/inst.) and Duelist(1.75x/2.5x) talents
		if (item instanceof Armor){
			factor *= 1f + 0.75f*hero.pointsInTalent(ADVENTURERS_INTUITION);
			factor *= 1f + hero.pointsInTalent(VETERANS_INTUITION); //instant at +2 (see onItemEquipped)
		}
		// 3x/instant for Mage (see Wand.wandUsed())
		if (item instanceof Wand){
			factor *= 1f + 2.0f*hero.pointsInTalent(SCHOLARS_INTUITION);
		}
		// 2x/instant for Rogue (see onItemEqupped), also id's type on equip/on pickup
		if (item instanceof Ring){
			factor *= 1f + hero.pointsInTalent(THIEFS_INTUITION);
		}
		return factor;
	}

	public static void onHealingPotionUsed( Hero hero ){
		if (hero.hasTalent(RESTORED_WILLPOWER)){
			if (hero.heroClass == HeroClass.WARRIOR) {
				BrokenSeal.WarriorShield shield = hero.buff(BrokenSeal.WarriorShield.class);
				if (shield != null) {
					int shieldToGive = Math.round(shield.maxShield() * 0.33f * (1 + hero.pointsInTalent(RESTORED_WILLPOWER)));
					shield.supercharge(shieldToGive);
				}
			} else {
				int shieldToGive = Math.round( hero.HT * (0.025f * (1+hero.pointsInTalent(RESTORED_WILLPOWER))));
				Buff.affect(hero, Barrier.class).setShield(shieldToGive);
			}
		}
		if (hero.hasTalent(RESTORED_NATURE)){
			ArrayList<Integer> grassCells = new ArrayList<>();
			for (int i : PathFinder.NEIGHBOURS8){
				grassCells.add(hero.pos+i);
			}
			Random.shuffle(grassCells);
			for (int cell : grassCells){
				Char ch = Actor.findChar(cell);
				if (ch != null && ch.alignment == Char.Alignment.ENEMY){
					Buff.affect(ch, Roots.class, 1f + hero.pointsInTalent(RESTORED_NATURE));
				}
				if (Dungeon.level.map[cell] == Terrain.EMPTY ||
						Dungeon.level.map[cell] == Terrain.EMBERS ||
						Dungeon.level.map[cell] == Terrain.EMPTY_DECO){
					Level.set(cell, Terrain.GRASS);
					GameScene.updateMap(cell);
				}
				CellEmitter.get(cell).burst(LeafParticle.LEVEL_SPECIFIC, 4);
			}
			if (hero.pointsInTalent(RESTORED_NATURE) == 1){
				grassCells.remove(0);
				grassCells.remove(0);
				grassCells.remove(0);
			}
			for (int cell : grassCells){
				int t = Dungeon.level.map[cell];
				if ((t == Terrain.EMPTY || t == Terrain.EMPTY_DECO || t == Terrain.EMBERS
						|| t == Terrain.GRASS || t == Terrain.FURROWED_GRASS)
						&& Dungeon.level.plants.get(cell) == null){
					Level.set(cell, Terrain.HIGH_GRASS);
					GameScene.updateMap(cell);
				}
			}
			Dungeon.observe();
		}
		if (hero.hasTalent(RESTORED_AGILITY)){
			Buff.prolong(hero, RestoredAgilityTracker.class, hero.cooldown());
		}
	}

	public static void onUpgradeScrollUsed( Hero hero ){
		if (hero.hasTalent(ENERGIZING_UPGRADE)){
			if (hero.heroClass == HeroClass.MAGE) {
				MagesStaff staff = hero.belongings.getItem(MagesStaff.class);
				if (staff != null) {
					staff.gainCharge(2 + 2 * hero.pointsInTalent(ENERGIZING_UPGRADE), true);
					ScrollOfRecharging.charge(Dungeon.hero);
					SpellSprite.show(hero, GeneralAsset.CHARGE);
				}
			} else {
				Buff.affect(hero, Recharging.class, 4 + 8 * hero.pointsInTalent(ENERGIZING_UPGRADE));
			}
		}
		if (hero.hasTalent(MYSTICAL_UPGRADE)){
			if (hero.heroClass == HeroClass.ROGUE) {
				CloakOfShadows cloak = hero.belongings.getItem(CloakOfShadows.class);
				if (cloak != null) {
					cloak.overCharge(1 + hero.pointsInTalent(MYSTICAL_UPGRADE));
					ScrollOfRecharging.charge(Dungeon.hero);
					SpellSprite.show(hero, GeneralAsset.CHARGE, 0, 1, 1);
				}
			} else {
				Buff.affect(hero, ArtifactRecharge.class).set( 2 + 4*hero.pointsInTalent(MYSTICAL_UPGRADE) ).ignoreHornOfPlenty = false;
				ScrollOfRecharging.charge(Dungeon.hero);
				SpellSprite.show(hero, GeneralAsset.CHARGE, 0, 1, 1);
			}
		}
	}

	public static void onArtifactUsed( Hero hero ){
		if (hero.hasTalent(ENHANCED_RINGS)){
			Buff.prolong(hero, EnhancedRings.class, 3f*hero.pointsInTalent(ENHANCED_RINGS));
		}
	}

	public static void onItemEquipped( Hero hero, Item item ){
		if (hero.pointsInTalent(VETERANS_INTUITION) == 2 && item instanceof Armor){
			item.identify();
		}
		if (hero.hasTalent(THIEFS_INTUITION) && item instanceof Ring){
			if (hero.pointsInTalent(THIEFS_INTUITION) == 2){
				item.identify();
			} else {
				((Ring) item).setKnown();
			}
		}
		if (hero.pointsInTalent(ADVENTURERS_INTUITION) == 2 && item instanceof Weapon){
			item.identify();
		}
	}

	public static void onItemCollected( Hero hero, Item item ){
		if (hero.pointsInTalent(THIEFS_INTUITION) == 2){
			if (item instanceof Ring) ((Ring) item).setKnown();
		}
	}

	//note that IDing can happen in alchemy scene, so be careful with VFX here
	public static void onItemIdentified( Hero hero, Item item ){
		if (hero.hasTalent(TEST_SUBJECT)){
			//heal for 2/3 HP
			hero.HP = Math.min(hero.HP + 1 + hero.pointsInTalent(TEST_SUBJECT), hero.HT);
			if (hero.sprite != null) {
				Emitter e = hero.sprite.emitter();
				if (e != null) e.burst(Speck.factory(Speck.HEALING), hero.pointsInTalent(TEST_SUBJECT));
			}
		}
		if (hero.hasTalent(TESTED_HYPOTHESIS)){
			//2/3 turns of wand recharging
			Buff.affect(hero, Recharging.class, 1f + hero.pointsInTalent(TESTED_HYPOTHESIS));
			ScrollOfRecharging.charge(hero);
		}
	}

	public static int onAttackProc( Hero hero, Char enemy, int dmg ){
		if (hero.hasTalent(Talent.SUCKER_PUNCH)
				&& enemy instanceof Mob && ((Mob) enemy).surprisedBy(hero)
				&& enemy.buff(SuckerPunchTracker.class) == null){
			dmg += Random.IntRange(hero.pointsInTalent(Talent.SUCKER_PUNCH) , 2);
			Buff.affect(enemy, SuckerPunchTracker.class);
		}

		if (hero.hasTalent(Talent.FOLLOWUP_STRIKE) && enemy.alignment == Char.Alignment.ENEMY) {
			if (hero.belongings.attackingWeapon() instanceof MissileWeapon) {
				Buff.prolong(hero, FollowupStrikeTracker.class, 5f).object = enemy.id();
			} else if (hero.buff(FollowupStrikeTracker.class) != null
					&& hero.buff(FollowupStrikeTracker.class).object == enemy.id()){
				dmg += 1 + hero.pointsInTalent(FOLLOWUP_STRIKE);
				hero.buff(FollowupStrikeTracker.class).detach();
			}
		}

		if (hero.buff(Talent.SpiritBladesTracker.class) != null
				&& Random.Int(10) < 3*hero.pointsInTalent(Talent.SPIRIT_BLADES)){
			SpiritBow bow = hero.belongings.getItem(SpiritBow.class);
			if (bow != null) dmg = bow.proc( hero, enemy, dmg );
			hero.buff(Talent.SpiritBladesTracker.class).detach();
		}

		if (hero.hasTalent(PATIENT_STRIKE)){
			if (hero.buff(PatientStrikeTracker.class) != null
					&& !(hero.belongings.attackingWeapon() instanceof MissileWeapon)){
				hero.buff(PatientStrikeTracker.class).detach();
				dmg += Random.IntRange(hero.pointsInTalent(Talent.PATIENT_STRIKE), 2);
			}
		}

		if (hero.hasTalent(DEADLY_FOLLOWUP) && enemy.alignment == Char.Alignment.ENEMY) {
			if (hero.belongings.attackingWeapon() instanceof MissileWeapon) {
				if (!(hero.belongings.attackingWeapon() instanceof SpiritBow.SpiritArrow)) {
					Buff.prolong(hero, DeadlyFollowupTracker.class, 5f).object = enemy.id();
				}
			} else if (hero.buff(DeadlyFollowupTracker.class) != null
					&& hero.buff(DeadlyFollowupTracker.class).object == enemy.id()){
				dmg = Math.round(dmg * (1.0f + .08f*hero.pointsInTalent(DEADLY_FOLLOWUP)));
			}
		}

		return dmg;
	}

	public static class SuckerPunchTracker extends Buff{};
	public static class FollowupStrikeTracker extends FlavourBuff{
		public int object;
		{ type = Buff.buffType.POSITIVE; }
		public Pair<Asset, Asset> icon() { return BuffIndicator.INVERT_MARK; }
		public void tintIcon(Image icon) { icon.hardlight(0f, 0.75f, 1f); }
		public float iconFadePercent() { return Math.max(0, 1f - (visualcooldown() / 5)); }
		private static final String OBJECT    = "object";
		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(OBJECT, object);
		}
		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			object = bundle.getInt(OBJECT);
		}
	};

	public static final int MAX_TALENT_TIERS = 4;

	public static void initClassTalents( Hero hero ){
		initClassTalents( hero.heroClass, hero.talents, hero.metamorphedTalents );
	}

	public static void initClassTalents( HeroClass cls, ArrayList<LinkedHashMap<Talent, Integer>> talents){
		initClassTalents( cls, talents, new LinkedHashMap<>());
	}

	public static void initClassTalents( HeroClass cls, ArrayList<LinkedHashMap<Talent, Integer>> talents, LinkedHashMap<Talent, Talent> replacements ){
		while (talents.size() < MAX_TALENT_TIERS){
			talents.add(new LinkedHashMap<>());
		}

		ArrayList<Talent> tierTalents = new ArrayList<>();

		//tier 1
		switch (cls){
			case WARRIOR: default:
				Collections.addAll(tierTalents, HEARTY_MEAL, VETERANS_INTUITION, TEST_SUBJECT, IRON_WILL);
				break;
			case MAGE:
				Collections.addAll(tierTalents, EMPOWERING_MEAL, SCHOLARS_INTUITION, TESTED_HYPOTHESIS, BACKUP_BARRIER);
				break;
			case ROGUE:
				Collections.addAll(tierTalents, CACHED_RATIONS, THIEFS_INTUITION, SUCKER_PUNCH, PROTECTIVE_SHADOWS);
				break;
			case HUNTRESS:
				Collections.addAll(tierTalents, NATURES_BOUNTY, SURVIVALISTS_INTUITION, FOLLOWUP_STRIKE, NATURES_AID);
				break;
			case DUELIST:
				Collections.addAll(tierTalents, STRENGTHENING_MEAL, ADVENTURERS_INTUITION, PATIENT_STRIKE, AGGRESSIVE_BARRIER);
				break;
		}
		for (Talent talent : tierTalents){
			if (replacements.containsKey(talent)){
				talent = replacements.get(talent);
			}
			talents.get(0).put(talent, 0);
		}
		tierTalents.clear();

		//tier 2
		switch (cls){
			case WARRIOR: default:
				Collections.addAll(tierTalents, IRON_STOMACH, RESTORED_WILLPOWER, RUNIC_TRANSFERENCE, LETHAL_MOMENTUM, IMPROVISED_PROJECTILES);
				break;
			case MAGE:
				Collections.addAll(tierTalents, ENERGIZING_MEAL, ENERGIZING_UPGRADE, WAND_PRESERVATION, ARCANE_VISION, SHIELD_BATTERY);
				break;
			case ROGUE:
				Collections.addAll(tierTalents, MYSTICAL_MEAL, MYSTICAL_UPGRADE, WIDE_SEARCH, SILENT_STEPS, ROGUES_FORESIGHT);
				break;
			case HUNTRESS:
				Collections.addAll(tierTalents, INVIGORATING_MEAL, RESTORED_NATURE, REJUVENATING_STEPS, HEIGHTENED_SENSES, DURABLE_PROJECTILES);
				break;
			case DUELIST:
				Collections.addAll(tierTalents, FOCUSED_MEAL, RESTORED_AGILITY, WEAPON_RECHARGING, LETHAL_HASTE, SWIFT_EQUIP);
				break;
		}
		for (Talent talent : tierTalents){
			if (replacements.containsKey(talent)){
				talent = replacements.get(talent);
			}
			talents.get(1).put(talent, 0);
		}
		tierTalents.clear();

		//tier 3
		switch (cls){
			case WARRIOR: default:
				Collections.addAll(tierTalents, HOLD_FAST, STRONGMAN);
				break;
			case MAGE:
				Collections.addAll(tierTalents, EMPOWERING_SCROLLS, ALLY_WARP);
				break;
			case ROGUE:
				Collections.addAll(tierTalents, ENHANCED_RINGS, LIGHT_CLOAK);
				break;
			case HUNTRESS:
				Collections.addAll(tierTalents, POINT_BLANK, SEER_SHOT);
				break;
			case DUELIST:
				Collections.addAll(tierTalents, PRECISE_ASSAULT, DEADLY_FOLLOWUP);
				break;
		}
		for (Talent talent : tierTalents){
			if (replacements.containsKey(talent)){
				talent = replacements.get(talent);
			}
			talents.get(2).put(talent, 0);
		}
		tierTalents.clear();

		//tier4
		//TBD
	}

	public static void initSubclassTalents( Hero hero ){
		initSubclassTalents( hero.subClass, hero.talents );
	}

	public static void initSubclassTalents( HeroSubClass cls, ArrayList<LinkedHashMap<Talent, Integer>> talents ){
		if (cls == HeroSubClass.NONE) return;

		while (talents.size() < MAX_TALENT_TIERS){
			talents.add(new LinkedHashMap<>());
		}

		ArrayList<Talent> tierTalents = new ArrayList<>();

		//tier 3
		switch (cls){
			case BERSERKER: default:
				Collections.addAll(tierTalents, ENDLESS_RAGE, DEATHLESS_FURY, ENRAGED_CATALYST);
				break;
			case GLADIATOR:
				Collections.addAll(tierTalents, CLEAVE, LETHAL_DEFENSE, ENHANCED_COMBO);
				break;
			case BATTLEMAGE:
				Collections.addAll(tierTalents, EMPOWERED_STRIKE, MYSTICAL_CHARGE, EXCESS_CHARGE);
				break;
			case WARLOCK:
				Collections.addAll(tierTalents, SOUL_EATER, SOUL_SIPHON, NECROMANCERS_MINIONS);
				break;
			case ASSASSIN:
				Collections.addAll(tierTalents, ENHANCED_LETHALITY, ASSASSINS_REACH, BOUNTY_HUNTER);
				break;
			case FREERUNNER:
				Collections.addAll(tierTalents, EVASIVE_ARMOR, PROJECTILE_MOMENTUM, SPEEDY_STEALTH);
				break;
			case SNIPER:
				Collections.addAll(tierTalents, FARSIGHT, SHARED_ENCHANTMENT, SHARED_UPGRADES);
				break;
			case WARDEN:
				Collections.addAll(tierTalents, DURABLE_TIPS, BARKSKIN, SHIELDING_DEW);
				break;
			case CHAMPION:
				Collections.addAll(tierTalents, SECONDARY_CHARGE, TWIN_UPGRADES, COMBINED_LETHALITY);
				break;
			case MONK:
				Collections.addAll(tierTalents, UNENCUMBERED_SPIRIT, MONASTIC_VIGOR, COMBINED_ENERGY);
				break;
		}
		for (Talent talent : tierTalents){
			talents.get(2).put(talent, 0);
		}
		tierTalents.clear();

	}

	public static void initArmorTalents( Hero hero ){
		initArmorTalents( hero.armorAbility, hero.talents);
	}

	public static void initArmorTalents(ArmorAbility abil, ArrayList<LinkedHashMap<Talent, Integer>> talents ){
		if (abil == null) return;

		while (talents.size() < MAX_TALENT_TIERS){
			talents.add(new LinkedHashMap<>());
		}

		for (Talent t : abil.talents()){
			talents.get(3).put(t, 0);
		}
	}

	private static final String TALENT_TIER = "talents_tier_";

	public static void storeTalentsInBundle( Bundle bundle, Hero hero ){
		for (int i = 0; i < MAX_TALENT_TIERS; i++){
			LinkedHashMap<Talent, Integer> tier = hero.talents.get(i);
			Bundle tierBundle = new Bundle();

			for (Talent talent : tier.keySet()){
				if (tier.get(talent) > 0){
					tierBundle.put(talent.name(), tier.get(talent));
				}
				if (tierBundle.contains(talent.name())){
					tier.put(talent, Math.min(tierBundle.getInt(talent.name()), talent.maxPoints()));
				}
			}
			bundle.put(TALENT_TIER+(i+1), tierBundle);
		}

		Bundle replacementsBundle = new Bundle();
		for (Talent t : hero.metamorphedTalents.keySet()){
			replacementsBundle.put(t.name(), hero.metamorphedTalents.get(t));
		}
		bundle.put("replacements", replacementsBundle);
	}

	private static final HashMap<String, String> renamedTalents = new HashMap<>();
	static{
		//v2.0.0
		renamedTalents.put("ARMSMASTERS_INTUITION",     "VETERANS_INTUITION");
		//v2.0.0 BETA
		renamedTalents.put("LIGHTLY_ARMED",             "UNENCUMBERED_SPIRIT");
		//v2.1.0
		renamedTalents.put("LIGHTWEIGHT_CHARGE",             "PRECISE_ASSAULT");
	}

	public static void restoreTalentsFromBundle( Bundle bundle, Hero hero ){
		if (bundle.contains("replacements")){
			Bundle replacements = bundle.getBundle("replacements");
			for (String key : replacements.getKeys()){
				String value = replacements.getString(key);
				if (renamedTalents.containsKey(key)) key = renamedTalents.get(key);
				if (renamedTalents.containsKey(value)) value = renamedTalents.get(value);
				hero.metamorphedTalents.put(Talent.valueOf(key), Talent.valueOf(value));
			}
		}

		if (hero.heroClass != null)     initClassTalents(hero);
		if (hero.subClass != null)      initSubclassTalents(hero);
		if (hero.armorAbility != null)  initArmorTalents(hero);

		for (int i = 0; i < MAX_TALENT_TIERS; i++){
			LinkedHashMap<Talent, Integer> tier = hero.talents.get(i);
			Bundle tierBundle = bundle.contains(TALENT_TIER+(i+1)) ? bundle.getBundle(TALENT_TIER+(i+1)) : null;

			if (tierBundle != null){
				for (String tName : tierBundle.getKeys()){
					int points = tierBundle.getInt(tName);
					if (renamedTalents.containsKey(tName)) tName = renamedTalents.get(tName);
					try {
						Talent talent = Talent.valueOf(tName);
						if (tier.containsKey(talent)) {
							tier.put(talent, Math.min(points, talent.maxPoints()));
						}
					} catch (Exception e){
						ShatteredPixelDungeon.reportException(e);
					}
				}
			}
		}
	}

}
