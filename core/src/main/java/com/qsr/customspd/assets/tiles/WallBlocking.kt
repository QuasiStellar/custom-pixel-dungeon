package com.qsr.customspd.assets.tiles

enum class WallBlocking(override val path: String, override val pos: Int) : TileAsset {
    BLOCK_RIGHT("environment/wall_blocking/block_right.png", 0),
    BLOCK_LEFT("environment/wall_blocking/block_left.png", 1),
    BLOCK_ALL("environment/wall_blocking/block_all.png", 2),
    BLOCK_BELOW("environment/wall_blocking/block_below.png", 3),
}
