package com.qsr.customspd.modding

import com.qsr.customspd.ShatteredPixelDungeon
import com.qsr.customspd.assets.tiles.TerrainFeature
import com.qsr.customspd.assets.tiles.TileAsset
import com.qsr.customspd.assets.tiles.CavesTile
import com.qsr.customspd.assets.tiles.CityTile
import com.qsr.customspd.assets.tiles.HallsTile
import com.qsr.customspd.assets.tiles.PrisonTile
import com.qsr.customspd.assets.tiles.SewersTile
import com.qsr.customspd.assets.tiles.VisualGrid
import com.qsr.customspd.assets.tiles.WallBlocking
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

object TileMapCompilationManager {

    private var lastJob: Job? = null

    val isBusy get() = lastJob?.isActive ?: false

    @OptIn(DelicateCoroutinesApi::class)
    @JvmStatic
    fun compileTileMaps() {
        lastJob?.cancel()
        lastJob = GlobalScope.launch {
            awaitAll(
                async { compileTileMap("tiles_sewers.png", SewersTile.values()) },
                async { compileTileMap("tiles_prison.png", PrisonTile.values()) },
                async { compileTileMap("tiles_caves.png", CavesTile.values()) },
                async { compileTileMap("tiles_city.png", CityTile.values()) },
                async { compileTileMap("tiles_halls.png", HallsTile.values()) },
                async { compileTileMap("terrain_features.png", TerrainFeature.values(), height = 128) },
                async { compileTileMap("visual_grid.png", VisualGrid.values(), width = 64, height = 64) },
                async { compileTileMap("wall_blocking.png", WallBlocking.values(), width = 64, height = 16) },
            )
        }
    }

    private fun compileTileMap(
        name: String,
        tiles: Array<out TileAsset>,
        width: Int = 256,
        height: Int = 256,
        tileWidth: Int = 16,
        tileHeight: Int = 16,
    ) = ShatteredPixelDungeon.tileMapCompiler.compileTileMap(name, tiles, width, height, tileWidth, tileHeight)
}