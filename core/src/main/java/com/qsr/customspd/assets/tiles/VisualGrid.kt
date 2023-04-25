package com.qsr.customspd.assets.tiles

enum class VisualGrid(override val path: String, override val pos: Int) : TileAsset {
    NORMAL_0("environment/visual_grid/normal_0.png", 0),
    NORMAL_1("environment/visual_grid/normal_1.png", 1),
    NORMAL_2("environment/visual_grid/normal_2.png", 2),
    DOOR_0("environment/visual_grid/door_0.png", 4),
    DOOR_1("environment/visual_grid/door_1.png", 5),
    DOOR_2("environment/visual_grid/door_2.png", 6),
    DOOR_OPEN_0("environment/visual_grid/door_open_0.png", 8),
    DOOR_OPEN_1("environment/visual_grid/door_open_1.png", 9),
    DOOR_OPEN_2("environment/visual_grid/door_open_2.png", 10),
    DOOR_SIDEWAYS_0("environment/visual_grid/door_sideways_0.png", 12),
    DOOR_SIDEWAYS_1("environment/visual_grid/door_sideways_1.png", 13),
    DOOR_SIDEWAYS_2("environment/visual_grid/door_sideways_2.png", 14),
}
