package com.yellowclass.plugin_native.yc_native.receivers

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.Intent.EXTRA_CHOSEN_COMPONENT
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Parcel
import android.os.Parcelable

class ShareTapReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val clickedComponent : ComponentName? = intent?.getParcelableExtra(EXTRA_CHOSEN_COMPONENT);

        when(clickedComponent?.packageName){
            "com.instagram" -> shareOnInstagramStories(intent)
            "com.facebook.katana" -> shareOnFacebookFeed(intent, context)
        }
    }

    private fun shareOnInstagramStories(intent: Intent) {
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        //    intent.putExtra("interactive_asset_uri", stickerImageFile);
        intent.putExtra("top_background_color", "#ffffff")
        intent.putExtra("bottom_background_color", "#ffffff")
        // Instantiate activity and verify it will resolve implicit intent
//    this.mContext.startActivity(intent);
    }

    private fun shareOnFacebookFeed(intent: Intent, context: Context?) {
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.putExtra("com.facebook.platform.extra.APPLICATION_ID", "247557386534356")
        intent.removeExtra(Intent.EXTRA_TEXT)
        intent.removeExtra(Intent.EXTRA_STREAM)
        intent.putExtra(Intent.EXTRA_TEXT, "https://www.google.co.in")
//        intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
//        context?.startActivity(intent)
    }
}

