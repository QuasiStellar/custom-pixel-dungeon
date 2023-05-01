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

package com.qsr.customspd;

public class Assets {

	public static class Environment {
		public static final String TERRAIN_FEATURES = "temp_terrain_features.png";

		public static final String VISUAL_GRID  = "temp_visual_grid.png";
		public static final String WALL_BLOCKING= "temp_wall_blocking.png";

		public static final String TILES_SEWERS = "temp_tiles_sewers.png";
		public static final String TILES_PRISON = "temp_tiles_prison.png";
		public static final String TILES_CAVES  = "temp_tiles_caves.png";
		public static final String TILES_CITY   = "temp_tiles_city.png";
		public static final String TILES_HALLS  = "temp_tiles_halls.png";
	}
	
	//TODO include other font assets here? Some are platform specific though...
	public static class Fonts {
		public static final String PIXELFONT= "fonts/pixel_font.png";
	}

	//these points to resource bundles, not raw asset files
	public static class Messages {
		public static final String ACTORS   = "messages/actors/actors";
		public static final String ITEMS    = "messages/items/items";
		public static final String JOURNAL  = "messages/journal/journal";
		public static final String LEVELS   = "messages/levels/levels";
		public static final String MISC     = "messages/misc/misc";
		public static final String PLANTS   = "messages/plants/plants";
		public static final String SCENES   = "messages/scenes/scenes";
		public static final String UI       = "messages/ui/ui";
		public static final String WINDOWS  = "messages/windows/windows";
	}

	public static class Music {
		public static final String THEME_1      = "music/theme_1.ogg";
		public static final String THEME_2      = "music/theme_2.ogg";

		public static final String SEWERS_1     = "music/sewers_1.ogg";
		public static final String SEWERS_2     = "music/sewers_2.ogg";
		public static final String SEWERS_BOSS  = "music/sewers_boss.ogg";

		public static final String PRISON_1     = "music/prison_1.ogg";
		public static final String PRISON_2     = "music/prison_2.ogg";
		public static final String PRISON_BOSS  = "music/prison_boss.ogg";

		public static final String CAVES_1      = "music/caves_1.ogg";
		public static final String CAVES_2      = "music/caves_2.ogg";
		public static final String CAVES_BOSS   = "music/caves_boss.ogg";

		public static final String CITY_1       = "music/city_1.ogg";
		public static final String CITY_2       = "music/city_2.ogg";
		public static final String CITY_BOSS    = "music/city_boss.ogg";

		public static final String HALLS_1      = "music/halls_1.ogg";
		public static final String HALLS_2      = "music/halls_2.ogg";
		public static final String HALLS_BOSS   = "music/halls_boss.ogg";
	}

	public static class Sounds {
		public static final String CLICK    = "sounds/click.mp3";
		public static final String BADGE    = "sounds/badge.mp3";
		public static final String GOLD     = "sounds/gold.mp3";

		public static final String OPEN     = "sounds/door_open.mp3";
		public static final String UNLOCK   = "sounds/unlock.mp3";
		public static final String ITEM     = "sounds/item.mp3";
		public static final String DEWDROP  = "sounds/dewdrop.mp3";
		public static final String STEP     = "sounds/step.mp3";
		public static final String WATER    = "sounds/water.mp3";
		public static final String GRASS    = "sounds/grass.mp3";
		public static final String TRAMPLE  = "sounds/trample.mp3";
		public static final String STURDY   = "sounds/sturdy.mp3";

		public static final String HIT              = "sounds/hit.mp3";
		public static final String MISS             = "sounds/miss.mp3";
		public static final String HIT_SLASH        = "sounds/hit_slash.mp3";
		public static final String HIT_STAB         = "sounds/hit_stab.mp3";
		public static final String HIT_CRUSH        = "sounds/hit_crush.mp3";
		public static final String HIT_MAGIC        = "sounds/hit_magic.mp3";
		public static final String HIT_STRONG       = "sounds/hit_strong.mp3";
		public static final String HIT_PARRY        = "sounds/hit_parry.mp3";
		public static final String HIT_ARROW        = "sounds/hit_arrow.mp3";
		public static final String ATK_SPIRITBOW    = "sounds/atk_spiritbow.mp3";
		public static final String ATK_CROSSBOW     = "sounds/atk_crossbow.mp3";
		public static final String HEALTH_WARN      = "sounds/health_warn.mp3";
		public static final String HEALTH_CRITICAL  = "sounds/health_critical.mp3";

		public static final String DESCEND  = "sounds/descend.mp3";
		public static final String EAT      = "sounds/eat.mp3";
		public static final String READ     = "sounds/read.mp3";
		public static final String LULLABY  = "sounds/lullaby.mp3";
		public static final String DRINK    = "sounds/drink.mp3";
		public static final String SHATTER  = "sounds/shatter.mp3";
		public static final String ZAP      = "sounds/zap.mp3";
		public static final String LIGHTNING= "sounds/lightning.mp3";
		public static final String LEVELUP  = "sounds/levelup.mp3";
		public static final String DEATH    = "sounds/death.mp3";
		public static final String CHALLENGE= "sounds/challenge.mp3";
		public static final String CURSED   = "sounds/cursed.mp3";
		public static final String TRAP     = "sounds/trap.mp3";
		public static final String EVOKE    = "sounds/evoke.mp3";
		public static final String TOMB     = "sounds/tomb.mp3";
		public static final String ALERT    = "sounds/alert.mp3";
		public static final String MELD     = "sounds/meld.mp3";
		public static final String BOSS     = "sounds/boss.mp3";
		public static final String BLAST    = "sounds/blast.mp3";
		public static final String PLANT    = "sounds/plant.mp3";
		public static final String RAY      = "sounds/ray.mp3";
		public static final String BEACON   = "sounds/beacon.mp3";
		public static final String TELEPORT = "sounds/teleport.mp3";
		public static final String CHARMS   = "sounds/charms.mp3";
		public static final String MASTERY  = "sounds/mastery.mp3";
		public static final String PUFF     = "sounds/puff.mp3";
		public static final String ROCKS    = "sounds/rocks.mp3";
		public static final String BURNING  = "sounds/burning.mp3";
		public static final String FALLING  = "sounds/falling.mp3";
		public static final String GHOST    = "sounds/ghost.mp3";
		public static final String SECRET   = "sounds/secret.mp3";
		public static final String BONES    = "sounds/bones.mp3";
		public static final String BEE      = "sounds/bee.mp3";
		public static final String DEGRADE  = "sounds/degrade.mp3";
		public static final String MIMIC    = "sounds/mimic.mp3";
		public static final String DEBUFF   = "sounds/debuff.mp3";
		public static final String CHARGEUP = "sounds/chargeup.mp3";
		public static final String GAS      = "sounds/gas.mp3";
		public static final String CHAINS   = "sounds/chains.mp3";
		public static final String SCAN     = "sounds/scan.mp3";
		public static final String SHEEP    = "sounds/sheep.mp3";

		public static final String[] all = new String[]{
				CLICK, BADGE, GOLD,

				OPEN, UNLOCK, ITEM, DEWDROP, STEP, WATER, GRASS, TRAMPLE, STURDY,

				HIT, MISS, HIT_SLASH, HIT_STAB, HIT_CRUSH, HIT_MAGIC, HIT_STRONG, HIT_PARRY,
				HIT_ARROW, ATK_SPIRITBOW, ATK_CROSSBOW, HEALTH_WARN, HEALTH_CRITICAL,

				DESCEND, EAT, READ, LULLABY, DRINK, SHATTER, ZAP, LIGHTNING, LEVELUP, DEATH,
				CHALLENGE, CURSED, TRAP, EVOKE, TOMB, ALERT, MELD, BOSS, BLAST, PLANT, RAY, BEACON,
				TELEPORT, CHARMS, MASTERY, PUFF, ROCKS, BURNING, FALLING, GHOST, SECRET, BONES,
				BEE, DEGRADE, MIMIC, DEBUFF, CHARGEUP, GAS, CHAINS, SCAN, SHEEP
		};
	}
}
