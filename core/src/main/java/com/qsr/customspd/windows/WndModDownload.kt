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

import com.qsr.customspd.messages.Messages
import com.qsr.customspd.modding.MarketplaceMod
import com.qsr.customspd.modding.ModManager
import com.qsr.customspd.scenes.PixelScene
import com.qsr.customspd.ui.RedButton
import com.qsr.customspd.ui.RenderedTextBlock
import com.qsr.customspd.ui.Window
import com.watabou.noosa.Game
import com.watabou.utils.Callback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class WndModDownload(
    private val mod: MarketplaceMod,
    private val updateCallback: Callback,
) : Window() {
    private val scope = CoroutineScope(SupervisorJob())

    init {
        scope.launch {
            ModManager.downloadedMods.collect {
                if (it.contains(mod.info.name)) {
                    Game.runOnRenderThread {
                        updateButton("delete")
                    }
                }
            }
        }
        INSTANCE?.hide()
        INSTANCE = this
        fillFields()
    }

    override fun destroy() {
        scope.cancel()
        super.destroy()
    }

    override fun hide() {
        super.hide()
        if (INSTANCE === this) {
            INSTANCE = null
        }
    }

    private lateinit var title: IconTitle
    private lateinit var description: RenderedTextBlock
    private lateinit var author: RenderedTextBlock
    private lateinit var version: RenderedTextBlock
    private lateinit var license: RenderedTextBlock
    private lateinit var button: RedButton

    private fun fillFields() {
        title = IconTitle(mod)
        title.color(TITLE_COLOR)
        description = PixelScene.renderTextBlock(
            mod.info.description,
            6
        )
        author = PixelScene.renderTextBlock(
            Messages.get(WndModInfo::class.java, "author", mod.info.author),
            6
        )
        version = PixelScene.renderTextBlock(
            Messages.get(WndModInfo::class.java, "version", mod.info.version),
            6
        )
        license = PixelScene.renderTextBlock(
            Messages.get(WndModInfo::class.java, "license", mod.info.license),
            6
        )
        button = object : RedButton(
            Messages.get(
                this,
                if (ModManager.getInstalledModNames().contains(mod.info.name)) "delete"
                else "download"
            )
        ) {
            override fun onClick() {
                super.onClick()
                val downloaded = ModManager.getInstalledModNames().contains(mod.info.name)
                if (downloaded) {
                    ModManager.delete(mod.info.name)
                    updateButton("download")
                } else {
                    scope.launch { ModManager.download(mod.info.name) }
                    updateButton("downloading")
                }
                setSize(reqWidth(), 16f)
                setPos(
                    title.left() + (title.width() - width()) / 2,
                    license.bottom() + GAP
                )
                updateCallback.call()
            }
        }
        layoutFields()
    }

    private fun updateButton(state: String) {
        if (!::button.isInitialized) return
        button.text(Messages.get(this, state)) // My goals are
        button.setSize(button.reqWidth(), 16f)
        button.setPos(
            title.left() + (title.width() - button.width()) / 2,
            license.bottom() + GAP
        )
        button.text(Messages.get(this, state)) // beyond your understanding
    }

    private fun layoutFields() {
        var width = WIDTH_MIN
        description.maxWidth(width)
        author.maxWidth(width)
        version.maxWidth(width)
        license.maxWidth(width)

        //window can go out of the screen on landscape, so widen it as appropriate
        while (
            PixelScene.landscape()
            && description.height() > 100
            && author.height() > 100
            && version.height() > 100
            && license.height() > 100
            && width < WIDTH_MAX
        ) {
            width += 20
            description.maxWidth(width)
            author.maxWidth(width)
            version.maxWidth(width)
            license.maxWidth(width)
        }
        title.setRect(0f, 0f, width.toFloat(), 0f)
        add(title)
        description.setPos(title.left(), title.bottom() + GAP)
        add(description)
        author.setPos(title.left(), description.bottom() + GAP)
        add(author)
        version.setPos(title.left(), author.bottom() + GAP)
        add(version)
        license.setPos(title.left(), version.bottom() + GAP)
        add(license)
        button.setSize(button.reqWidth(), 16f)
        button.setPos(
            title.left() + (title.width() - button.width()) / 2,
            license.bottom() + GAP
        )
        add(button)
        resize(width, (button.bottom() + 2).toInt())
    }

    companion object {
        private const val GAP = 5f
        private const val WIDTH_MIN = 120
        private const val WIDTH_MAX = 220

        //only one WndModInfo can appear at a time
        private var INSTANCE: WndModDownload? = null
    }
}
