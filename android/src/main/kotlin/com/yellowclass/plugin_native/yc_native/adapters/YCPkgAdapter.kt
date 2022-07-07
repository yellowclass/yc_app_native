package com.yellowclass.plugin_native.yc_native.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.ImageView
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop

import com.yellowclass.plugin_native.yc_native.R
import com.yellowclass.plugin_native.yc_native.bottom_sheets.ShareSheetV2
import com.yellowclass.plugin_native.yc_native.models.ShareablePackage
import java.lang.Exception

class YCPkgAdapter(
    private val context: Context,
    private val list: List<ShareablePackage>,
    private val clickListener: ShareSheetV2.OnBottomSheetItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var appNameTV: TextView = itemView.findViewById(R.id.tv_name)
        var appIcon: ImageView = itemView.findViewById(R.id.appIcon)

        fun bind(position: Int) {
            val name = list[position].name
            appNameTV.text = name
            appNameTV.inputType = InputType.TYPE_TEXT_FLAG_CAP_WORDS

            val url: String = list[position].iconUrl

            val appIconId: Drawable? = list[position].appDrawableIconRes


            if (appIconId != null) {
                try {
                    appIcon.setImageDrawable(appIconId)
                } catch (e: Exception) {
                    Glide.with(context)
                        .load(url)
                        .placeholder(R.drawable.notify_icon_256)
                        .transform(CircleCrop())
                        .into(appIcon)
                }
            } else {
                Glide.with(context)
                    .load(url)
                    .placeholder(R.drawable.notify_icon_256)
                    .transform(CircleCrop())
                    .into(appIcon)

            }
            itemView.setOnClickListener { clickListener.onClicked(list[position]) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(position)


    }

    override fun getItemCount(): Int {
        return list.size
    }


}