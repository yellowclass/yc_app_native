package com.yellowclass.plugin_native.yc_native.models

import android.graphics.drawable.Drawable

data class ShareablePackage(val name: String, val iconUrl: String, val packageName: String, val appDrawableIconRes: Drawable?)

data class SelectedAppData(val mimeType:String, val imgPath: String, val contentText: String, val selectedPackage: ShareablePackage)