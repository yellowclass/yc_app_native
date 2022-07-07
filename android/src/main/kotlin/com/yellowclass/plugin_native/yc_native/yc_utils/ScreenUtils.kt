package com.yellowclass.plugin_native.yc_native.yc_utils

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import com.yellowclass.plugin_native.yc_native.models.DeviceScreen
import java.lang.Exception

object ScreenUtils {
    fun size(): DeviceScreen {

        return try {
            val displayMetrics = Resources.getSystem().displayMetrics
            val width = displayMetrics.widthPixels / displayMetrics.density
            val height = displayMetrics.heightPixels / displayMetrics.density
            DeviceScreen(width = width, height = height)
        } catch (e: Exception) {
            DeviceScreen(width = 0f, height = 0f)
        }
    }

    //it may not work on all devices.
    fun isTablet(ctx: Context): Boolean {
        return ctx.resources
            .configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
    }
}