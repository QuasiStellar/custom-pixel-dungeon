package com.qsr.customspd.modding

import com.qsr.customspd.assets.GeneralAsset

object SpriteSizeConfig {
    private var value: Map<String, List<Int>>? = null

    @JvmStatic
    fun getSizes(asset: GeneralAsset): List<Int> {
        val name = asset.path
        if (value == null) value = JsonConfigRetriever.retrieveSpriteSizes()
        return value!![name]!!
    }

    @JvmStatic
    fun update() {
        value = JsonConfigRetriever.retrieveSpriteSizes()
    }
}