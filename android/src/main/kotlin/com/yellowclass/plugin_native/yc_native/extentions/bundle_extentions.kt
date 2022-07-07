package com.yellowclass.plugin_native.yc_native.extentions

import android.os.Bundle
import java.util.HashMap

fun Bundle.bundleToMap(): Map<String, String?> {
    val map: MutableMap<String, String?> = HashMap()
    val ks = this.keySet()
    val iterator: Iterator<String> = ks.iterator()
    while (iterator.hasNext()) {
        val key = iterator.next()
        map[key] = this.getString(key)
    }
    return map
}
