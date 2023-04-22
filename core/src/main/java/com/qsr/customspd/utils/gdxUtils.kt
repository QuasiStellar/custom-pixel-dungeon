@file:JvmName("GdxUtils")
package com.qsr.customspd.utils

import com.badlogic.gdx.graphics.Pixmap

fun Pixmap(encodedData: ByteArray) = Pixmap(encodedData, 0, encodedData.size)
