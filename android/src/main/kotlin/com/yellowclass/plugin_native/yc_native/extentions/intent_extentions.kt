package com.yellowclass.plugin_native.yc_native.extentions

import android.content.Intent

fun Intent.fromHistory(): Boolean {
    return (this.flags and Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY == Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY)
}