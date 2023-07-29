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

package com.qsr.customspd.items.weapon.enchantments;

import com.qsr.customspd.Dungeon;
import com.qsr.customspd.actors.Char;
import com.qsr.customspd.effects.CellEmitter;
import com.qsr.customspd.effects.particles.LeafParticle;
import com.qsr.customspd.items.weapon.Weapon;
import com.qsr.customspd.levels.Level;
import com.qsr.customspd.levels.Terrain;
import com.qsr.customspd.scenes.GameScene;
import com.qsr.customspd.sprites.ItemSprite;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Blooming extends Weapon.Enchantment {
	
	private static ItemSprite.Glowing DARK_GREEN = new ItemSprite.Glowing( 0x008800 );

	@Override
	public int proc(float probability, int strength, Char attacker, Char defender, int damage) {
		if (Random.Float() < probability) {

			float plants = strength / 10f;
			if (Random.Float() < plants%1){
				plants = (float)Math.ceil(plants);
			} else {
				plants = (float)Math.floor(plants);
			}

			if (plantGrass(defender.pos)){
				plants--;
				if (plants <= 0){
					return damage;
				}
			}

			ArrayList<Integer> positions = new ArrayList<>();
			for (int i : PathFinder.NEIGHBOURS8){
				if (defender.pos + i != attacker.pos) {
					positions.add(defender.pos + i);
				}
			}
			Random.shuffle( positions );

			//The attacker's position is always lowest priority
			if (Dungeon.level.adjacent(attacker.pos, defender.pos)){
				positions.add(attacker.pos);
			}

			for (int i : positions){
				if (plantGrass(i)){
					plants--;
					if (plants <= 0) {
						return damage;
					}
				}
			}

		}

		return damage;
	}

	@Override
	public int proc(Weapon weapon, Char attacker, Char defender, int damage) {
		int level = Math.max( 0, weapon.buffedLvl() );

		// lvl 0 - 33%
		// lvl 1 - 50%
		// lvl 2 - 60%
		float procChance = (level+1f)/(level+3f) * procChanceMultiplier(attacker);
		if (Random.Float() < procChance) {

			float powerMulti = Math.max(1f, procChance);

			float plants = (1f + 0.1f*level) * powerMulti;
			if (Random.Float() < plants%1){
				plants = (float)Math.ceil(plants);
			} else {
				plants = (float)Math.floor(plants);
			}

			if (plantGrass(defender.pos)){
				plants--;
				if (plants <= 0){
					return damage;
				}
			}

			ArrayList<Integer> positions = new ArrayList<>();
			for (int i : PathFinder.NEIGHBOURS8){
				if (defender.pos + i != attacker.pos) {
					positions.add(defender.pos + i);
				}
			}
			Random.shuffle( positions );

			//The attacker's position is always lowest priority
			if (Dungeon.level.adjacent(attacker.pos, defender.pos)){
				positions.add(attacker.pos);
			}

			for (int i : positions){
				if (plantGrass(i)){
					plants--;
					if (plants <= 0) {
						return damage;
					}
				}
			}

		}

		return damage;
	}
	
	private boolean plantGrass(int cell){
		int t = Dungeon.level.map[cell];
		if ((t == Terrain.EMPTY || t == Terrain.EMPTY_DECO || t == Terrain.EMBERS
				|| t == Terrain.GRASS || t == Terrain.FURROWED_GRASS)
				&& Dungeon.level.plants.get(cell) == null){
			Level.set(cell, Terrain.HIGH_GRASS);
			GameScene.updateMap(cell);
			CellEmitter.get( cell ).burst( LeafParticle.LEVEL_SPECIFIC, 4 );
			return true;
		}
		return false;
	}
	
	@Override
	public ItemSprite.Glowing glowing() {
		return DARK_GREEN;
	}
}
