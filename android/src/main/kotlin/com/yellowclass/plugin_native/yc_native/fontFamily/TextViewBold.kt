package com.yellowclass.plugin_native.yc_native.fontFamily

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class TextViewBold(context: Context, set: AttributeSet?) :
    AppCompatTextView(context, set) {
    init {
        typeface = Typeface.createFromAsset(context.assets, "fonts/Nunito/Nunito-Bold.ttf")
    }
}