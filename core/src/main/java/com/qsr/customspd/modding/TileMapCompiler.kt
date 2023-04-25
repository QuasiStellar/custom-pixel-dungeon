package com.qsr.customspd.modding

import com.qsr.customspd.assets.tiles.TileAsset

interface TileMapCompiler {
    fun compileTileMap(
        name: String,
        tiles: Array<out TileAsset>,
        width: Int = 256,
        height: Int = 256,
        tileWidth: Int = 16,
        tileHeight: Int = 16,
    )
}