package com.yellowclass.plugin_native.yc_native.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import com.yellowclass.plugin_native.yc_native.NotificationHelper
import com.yellowclass.plugin_native.yc_native.PluginHandler


class NotifClickReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            NotificationHelper.DISMISS_NOTIFICATION_CLICK_ACTION -> dismissNotification(intent, context!!)
        }
    }

    private fun dismissNotification(intent: Intent, context: Context?) {
//        val notifId = intent.getIntExtra("NOTIF_ID", ForegroundService.NOTIFICATION_ID)

//        NotificationHelper().getNotifManager(context = context!!).cancel(notifId)

        // remove/dismiss notification only if there's more than 1 custom generated notification in the tray

        // as there is only notification is generated that's whu stopping current foreground service

        PluginHandler().stopForgroundService(context!!)

    }
}
