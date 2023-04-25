package com.qsr.customspd.assets

import com.qsr.customspd.modding.ModManager

interface Asset {
    val path: String

    companion object {
        @JvmStatic
        fun getAssetFileHandle(asset: GeneralAsset) = ModManager.getAssetFileHandle(asset.path)
    }
}