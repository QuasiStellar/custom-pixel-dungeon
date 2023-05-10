@file:JvmName("ListUtils")
package com.qsr.customspd.utils

fun <T> random(list: List<T>, number: Int) = list.asSequence().shuffled().take(number).toList()
