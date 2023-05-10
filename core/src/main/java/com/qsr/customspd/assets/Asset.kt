package com.qsr.customspd.assets

import com.qsr.customspd.modding.ModManager

interface Asset {
    val path: String

    companion object {
        @JvmStatic
        fun getAssetFilePath(asset: Asset) = ModManager.getModdedAssetFilePath(asset.path)
    }
}
