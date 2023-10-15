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
package com.qsr.customspd.windows

import com.qsr.customspd.SPDSettings
import com.qsr.customspd.ShatteredPixelDungeon
import com.qsr.customspd.assets.Asset
import com.qsr.customspd.assets.GeneralAsset
import com.qsr.customspd.messages.Messages
import com.qsr.customspd.modding.MarketplaceMod
import com.qsr.customspd.modding.ModManager
import com.qsr.customspd.modding.SpriteSizeConfig
import com.qsr.customspd.modding.TileMapCompilationManager
import com.qsr.customspd.scenes.PixelScene
import com.qsr.customspd.ui.RenderedTextBlock
import com.qsr.customspd.ui.ScrollingListPane
import com.qsr.customspd.utils.Pixmap
import com.watabou.noosa.Game
import com.watabou.noosa.Image
import com.watabou.noosa.ui.Component
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class WndMods : WndTabbed() {
    private val enabledModsTab: EnabledModsTab
    private val installedModsTab: InstalledModsTab
    private val marketplaceTab: MarketplaceTab

    init {
        val width = if (PixelScene.landscape()) WIDTH_L else WIDTH_P
        val height = if (PixelScene.landscape()) HEIGHT_L else HEIGHT_P
        resize(width, height)
        enabledModsTab = EnabledModsTab()
        add(enabledModsTab)
        enabledModsTab.setRect(0f, 0f, width.toFloat(), height.toFloat())
        enabledModsTab.updateList()
        add(object : LabeledTab(Messages.get(this, "enabled")) {
            override fun select(value: Boolean) {
                super.select(value)
                enabledModsTab.active = value
                enabledModsTab.visible = enabledModsTab.active
                enabledModsTab.updateList()
                if (value) last_index = 0
            }
        })
        installedModsTab = InstalledModsTab()
        add(installedModsTab)
        installedModsTab.setRect(0f, 0f, width.toFloat(), height.toFloat())
        installedModsTab.updateList()
        add(object : LabeledTab(Messages.get(this, "installed")) {
            override fun select(value: Boolean) {
                super.select(value)
                installedModsTab.active = value
                installedModsTab.visible = installedModsTab.active
                installedModsTab.updateList()
                if (value) last_index = 1
            }
        })
        marketplaceTab = MarketplaceTab()
        add(marketplaceTab)
        marketplaceTab.setRect(0f, 0f, width.toFloat(), height.toFloat())
        add(object : LabeledTab(Messages.get(this, "marketplace")) {
            override fun select(value: Boolean) {
                super.select(value)
                marketplaceTab.active = value
                marketplaceTab.visible = marketplaceTab.active
                if (value) {
                    marketplaceTab.updateList()
                    last_index = 2
                }
            }
        })
        layoutTabs()
        select(last_index)
    }

    override fun hide() {
        super.hide()
        TileMapCompilationManager.compileTileMaps()
        Messages.setup(SPDSettings.language())
        SpriteSizeConfig.update()
    }

    private class EnabledModsTab : Component() {
        private lateinit var list: ScrollingListPane
        private lateinit var blockText: RenderedTextBlock
        override fun createChildren() {
            list = ScrollingListPane()
            add(list)
            blockText = PixelScene.renderTextBlock(Messages.get(WndMods::class.java, "no_enabled"), 6)
            add(blockText)
        }

        override fun layout() {
            super.layout()
            list.setRect(0f, 0f, width, height)
            blockText.maxWidth(width.toInt())
            blockText.align(RenderedTextBlock.CENTER_ALIGN)
            blockText.setPos((width - blockText.width()) / 2f, (height - blockText.height()) / 2f)
        }

        fun updateList() {
            list.clear()
            val enabledMods = ModManager.getEnabledMods()
            if (enabledMods.isEmpty()) {
                blockText.alpha(1f)
                return
            }
            blockText.alpha(0f)
            for (mod in enabledMods) {
                list.addItem(object : ScrollingListPane.ListItem(
                    mod.icon,
                    null,
                    Messages.titleCase(mod.info.name),
                ) {
                    override fun onClick(x: Float, y: Float): Boolean {
                        if (!inside(x, y)) return false
                        ShatteredPixelDungeon.scene().addToFront(WndModInfo(
                            mod
                        ) { updateList() })
                        return true
                    }
                })
            }
        }
    }

    private class InstalledModsTab : Component() {
        private lateinit var list: ScrollingListPane
        private lateinit var blockText: RenderedTextBlock
        override fun createChildren() {
            list = ScrollingListPane()
            add(list)
            blockText = PixelScene.renderTextBlock(Messages.get(WndMods::class.java, "no_installed"), 6)
            add(blockText)
        }

        override fun layout() {
            super.layout()
            list.setRect(0f, 0f, width, height)
            blockText.maxWidth(width.toInt())
            blockText.align(RenderedTextBlock.CENTER_ALIGN)
            blockText.setPos((width - blockText.width()) / 2f, (height - blockText.height()) / 2f)
        }

        fun updateList() {
            list.clear()
            val installedMods = ModManager.getInstalledMods()
            if (installedMods.isEmpty()) {
                blockText.alpha(1f)
                return
            }
            blockText.alpha(0f)
            for (mod in installedMods) {
                list.addItem(object : ScrollingListPane.ListItem(
                    mod.icon,
                    null,
                    Messages.titleCase(mod.info.name),
                ) {
                    override fun onClick(x: Float, y: Float): Boolean {
                        if (!inside(x, y)) return false
                        ShatteredPixelDungeon.scene().addToFront(WndModInfo(
                            mod
                        ) { updateList() })
                        return true
                    }
                })
            }
        }
    }

    private class MarketplaceTab : Component() {
        private lateinit var list: ScrollingListPane
        private lateinit var loadingText: RenderedTextBlock
        private lateinit var errorText: RenderedTextBlock

        private val scope = CoroutineScope(SupervisorJob())

        init {
            scope.launch() {
                ModManager.marketplaceMods.collect {
                    if (it.isNotEmpty()) {
                        Game.runOnRenderThread {
                            loadingText.alpha(0f)
                            updateList()
                            for (mod: MarketplaceMod in ModManager.marketplaceMods.value) {
                                if (mod.info.name in ModManager.getInstalledModNames() &&
                                    mod.info.version > (ModManager.getInstalledMods().find { m ->
                                        m.info.name == mod.info.name
                                    }?.info?.version ?: 0)
                                ) {
                                    ShatteredPixelDungeon.scene().addToFront(object : WndOptions(
                                        Image(Asset.getAssetFilePath(GeneralAsset.ICON_WARNING)),
                                        Messages.titleCase(Messages.get(WndMods::class.java, "update_title")),
                                        Messages.get(WndMods::class.java, "update_desc"),
                                        Messages.get(WndMods::class.java, "update_yes"),
                                        Messages.get(WndMods::class.java, "update_no"),
                                    ) {
                                        override fun onSelect(index: Int) {
                                            when (index) {
                                                0 -> ModManager.updateMarketplaceMods()
                                                1 -> onBackPressed()
                                            }
                                        }
                                    })
                                    break
                                }
                            }
                        }
                    }
                }
            }
        }

        override fun destroy() {
            scope.cancel()
            super.destroy()
        }

        override fun createChildren() {
            list = ScrollingListPane()
            add(list)
            loadingText = PixelScene.renderTextBlock(Messages.get(WndMods::class.java, "loading"), 6)
            add(loadingText)
            errorText = PixelScene.renderTextBlock(Messages.get(WndMods::class.java, "loading_error"), 6)
            add(errorText)
        }

        override fun layout() {
            super.layout()
            list.setRect(0f, 0f, width, height)
            loadingText.maxWidth(width.toInt())
            loadingText.align(RenderedTextBlock.CENTER_ALIGN)
            loadingText.setPos((width - loadingText.width()) / 2f, (height - loadingText.height()) / 2f)
            errorText.maxWidth(width.toInt())
            errorText.align(RenderedTextBlock.CENTER_ALIGN)
            errorText.setPos((width - errorText.width()) / 2f, (height - errorText.height()) / 2f)
        }

        fun updateList() {
            list.clear()
            errorText.alpha(0f)
            ModManager.marketplaceMods.value.takeIf { it.isNotEmpty() }?.let {
                loadingText.alpha(0f)
                for (mod in it) {
                    list.addItem(object : ScrollingListPane.ListItem(
                        Image(Pixmap(mod.iconPixmap)),
                        null,
                        Messages.titleCase(mod.info.name),
                    ) {
                        override fun onClick(x: Float, y: Float): Boolean {
                            if (!inside(x, y)) return false
                            ShatteredPixelDungeon.scene().addToFront(WndModDownload(
                                mod
                            ) { })
                            return true
                        }
                    })
                }
            } ?: run {
                loadingText.alpha(1f)
                ModManager.receiveMarketplaceInfo()
            }
        }
    }

    companion object {
        private const val WIDTH_P = 126
        private const val HEIGHT_P = 180
        private const val WIDTH_L = 200
        private const val HEIGHT_L = 130
        var last_index = 0
    }
}
