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
import com.qsr.customspd.utils.Pixmap
import com.watabou.noosa.Game
import com.watabou.noosa.Image
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
                        button.setSize(button.reqWidth(), 16f)
                        button.setPos(
                            if (link != null) title.left() + (title.width() - button.width() - GAP - link!!.width()) / 2 else title.left() + (title.width() - button.width()) / 2,
                            (gameplayMod?.bottom() ?: license.bottom()) + GAP
                        )
                        link?.setPos(
                            button.right() + GAP,
                            button.top()
                        )
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

    private lateinit var previews: List<Image>
    private var currentPreview = 0

    private lateinit var title: IconTitle
    private lateinit var description: RenderedTextBlock
    private lateinit var author: RenderedTextBlock
    private lateinit var version: RenderedTextBlock
    private var languages: RenderedTextBlock? = null
    private lateinit var license: RenderedTextBlock
    private var gameplayMod: RenderedTextBlock? = null
    private lateinit var button: RedButton
    private var link: RedButton? = null
    private lateinit var left: RedButton
    private lateinit var right: RedButton

    private fun fillFields() {
        title = IconTitle(mod)
        title.color(TITLE_COLOR)
        description = PixelScene.renderTextBlock(
            mod.info.description,
            6
        )

        previews = mod.previewPixmaps.map { Image(Pixmap(it)) }

        author = PixelScene.renderTextBlock(
            Messages.get(WndModInfo::class.java, "author", mod.info.author),
            6
        )
        version = PixelScene.renderTextBlock(
            Messages.get(WndModInfo::class.java, "version", mod.info.version),
            6
        )
        mod.info.languages?.let {
            val langChars = mod.info.languages.toString()
            languages = PixelScene.renderTextBlock(
                Messages.get(WndModInfo::class.java, "languages", langChars.slice(1 until langChars.length - 1)),
                6
            )
        }
        license = PixelScene.renderTextBlock(
            Messages.get(WndModInfo::class.java, "license", mod.info.license),
            6
        )
        if (mod.info.gameplayMod) {
            gameplayMod = PixelScene.renderTextBlock(
                Messages.get(WndModInfo::class.java, "gameplay_mod"),
                6
            )
        }
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
                    if (link != null) title.left() + (title.width() - button.width() - GAP - link!!.width()) / 2 else title.left() + (title.width() - button.width()) / 2,
                    (gameplayMod?.bottom() ?: license.bottom()) + GAP
                )
                link?.setPos(
                    button.right() + GAP,
                    button.top()
                )
                updateCallback.call()
            }
        }

        if (mod.info.link != null) {
            link = object : RedButton(
                Messages.get(
                    WndModInfo::class.java,
                    "learn_more"
                )
            ) {
                override fun onClick() {
                    super.onClick()
                    Game.platform.openURI(mod.info.link)
                }
            }
        }

        left = object : RedButton("<") {
            override fun onClick() {
                super.onClick()
                previews[currentPreview].alpha(0f)
                previews[(currentPreview - 1 + previews.size) % previews.size].alpha(1f)
                currentPreview = (currentPreview - 1 + previews.size) % previews.size
            }
        }

        right = object : RedButton(">") {
            override fun onClick() {
                super.onClick()
                previews[currentPreview].alpha(0f)
                previews[(currentPreview + 1) % previews.size].alpha(1f)
                currentPreview = (currentPreview + 1) % previews.size
            }
        }

        layoutFields()
    }

    private fun updateButton(state: String) {
        if (!::button.isInitialized) return
        button.text(Messages.get(this, state))
    }

    private fun layoutFields() {
        var width = WIDTH_MIN
        description.maxWidth(width)
        author.maxWidth(width)
        version.maxWidth(width)
        languages?.maxWidth(width)
        license.maxWidth(width)
        gameplayMod?.maxWidth(width)

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
            languages?.maxWidth(width)
            license.maxWidth(width)
            gameplayMod?.maxWidth(width)
        }
        title.setRect(0f, 0f, width.toFloat(), 0f)
        add(title)
        description.setPos(title.left(), title.bottom() + GAP)
        add(description)

        var maxHeight = 0f
        for (preview in previews) {
            preview.scale.set(width / preview.width)
            preview.x = title.left()
            preview.y = description.bottom() + GAP
            preview.height *= width / preview.width
            maxHeight = Math.max(maxHeight, preview.height)
            preview.width *= width / preview.width
            add(preview)
            preview.alpha(0f)
        }
        for (preview in previews) {
            preview.y = description.bottom() + GAP + maxHeight / 2 - preview.height / 2
        }
        if (previews.isNotEmpty()) previews[0].alpha(1f)
        if (previews.size > 1) {
            left.setSize(left.reqWidth(), 8f)
            right.setSize(right.reqWidth(), 8f)
            left.setPos(title.left() + width.toFloat() / 2 - left.width() - GAP / 2, previews[0].y + maxHeight + GAP / 2)
            right.setPos(left.right() + GAP, left.top())
            add(left)
            add(right)
        }

        val authorY = if (previews.isEmpty()) description.bottom() + GAP else if (previews.size == 1) previews[0].y + maxHeight + GAP / 2 else left.bottom() + GAP / 2
        author.setPos(title.left(), authorY)
        add(author)
        version.setPos(title.left(), author.bottom() + GAP)
        add(version)
        languages?.setPos(title.left(), version.bottom() + GAP)
        languages?.let { add(it) }
        license.setPos(title.left(), (languages?.bottom() ?: version.bottom()) + GAP)
        add(license)
        gameplayMod?.setPos(title.left(), license.bottom() + GAP)
        gameplayMod?.let { add(gameplayMod) }
        languages?.let { add(it) }
        link?.setSize(link!!.reqWidth(), 16f)
        button.setSize(button.reqWidth(), 16f)
        button.setPos(
            if (link != null) title.left() + (title.width() - button.width() - GAP - link!!.width()) / 2 else title.left() + (title.width() - button.width()) / 2,
            (gameplayMod?.bottom() ?: license.bottom()) + GAP
        )
        add(button)
        link?.setPos(
            button.right() + GAP,
            button.top()
        )
        link?.let { add(link) }

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
