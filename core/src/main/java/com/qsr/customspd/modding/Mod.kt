package com.qsr.customspd.modding

import com.badlogic.gdx.files.FileHandle
import com.qsr.customspd.ui.Icons
import com.watabou.noosa.Image

class Mod(
    private val folder: FileHandle,
    val info: ModInfo,
) {
    val isEnabled get() = ModManager.isEnabled(this)

    val icon: Image
        get() = with(folder.child("mod_icon.png")) {
            if (exists()) Image(path())
            else Icons.get(Icons.TALENT)
        }

    fun enable() = ModManager.enableMod(info.name)

    fun disable() = ModManager.disableMod(info.name)
}
