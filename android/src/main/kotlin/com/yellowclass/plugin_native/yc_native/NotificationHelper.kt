package com.yellowclass.plugin_native.yc_native

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.yellowclass.plugin_native.yc_native.extentions.bundleToMap
import com.yellowclass.plugin_native.yc_native.extentions.fromHistory
import com.yellowclass.plugin_native.yc_native.models.ForegroundServiceArgsModel
import com.yellowclass.plugin_native.yc_native.models.NotificationType
import com.yellowclass.plugin_native.yc_native.receivers.NotifClickReceiver
import com.yellowclass.plugin_native.yc_native.services.ForegroundService
import java.util.HashMap

class NotificationHelper {
    companion object {
        val FOREGROUND_NOTIFICATION_CLICK_ACTION = "FOREGROUND_NOTIFICATION_CLICK_ACTION"
        val DISMISS_NOTIFICATION_CLICK_ACTION = "DISMISS_NOTIFICATION_CLICK_ACTION"
        private val NOTIFICATION_PAYLOAD_KEY = "NOTIFICATION_PAYLOAD_KEY"
        val TAP_INTENT_REQ_CODE = 112

    }


    fun getPayload(intent: Intent, actionName: String): Map<String, Any?> {
        var mappedPayload: Map<String, Any?> = HashMap()
        val notificationLaunchedApp = (actionName == intent.action) && !intent.fromHistory()
        if (notificationLaunchedApp && intent.extras != null) {
            var params = intent.getSerializableExtra(NOTIFICATION_PAYLOAD_KEY) as ForegroundServiceArgsModel
            return params.payload
        }
        return mappedPayload
    }

    fun getNotifManager(context: Context): NotificationManager {
        val notifManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        return notifManager
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel(context: Context, channelId: String, channelName: String): String {

        val chan = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_MIN)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        getNotifManager(context).createNotificationChannel(chan)
        return channelId
    }

    fun getForegroundNotification(context: Context, requestCode: Int, params: ForegroundServiceArgsModel): Notification {

        val channelId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(context, params.channelId, params.channelName)
        } else {

            // If earlier version channel ID is not used
            // https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html#NotificationCompat.Builder(android.content.Context)
            ""
        }

        val packageName: String = context.packageName
        val packageManager: PackageManager = context.packageManager

        val intent = Intent(packageManager.getLaunchIntentForPackage(packageName))
        intent.action = FOREGROUND_NOTIFICATION_CLICK_ACTION
        intent.putExtra(NOTIFICATION_PAYLOAD_KEY, params)

        val stopIntent = Intent(context, NotifClickReceiver::class.java).apply {
            action = DISMISS_NOTIFICATION_CLICK_ACTION
        }

        val flags = PendingIntent.FLAG_UPDATE_CURRENT
        val pendingIntent = PendingIntent.getActivity(context, requestCode, intent, flags)
        val stopPendingIntent = PendingIntent.getBroadcast(context, requestCode,stopIntent, flags)
        val notificationBuilder = NotificationCompat.Builder(context, channelId)

        val _notification: Notification?

        when (params.notificationType) {

            NotificationType.MEDIA -> {
                _notification = notificationBuilder.setOngoing(true)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setSmallIcon(R.drawable.notify_icon_72)
                        .setPriority(NotificationCompat.PRIORITY_MIN)
                        .setCategory(Notification.CATEGORY_SERVICE)
                        .setContentTitle(params.title)
                        .setContentText(params.body)
                        .setContentIntent(pendingIntent)
                        .setLargeIcon( getBitmap(params.largeIcon))
                        .addAction(R.drawable.stop_btn_s, "Stop", stopPendingIntent)
                        .addAction(R.drawable.play_btn_s, "Play", pendingIntent)
                        .setAutoCancel(params.dismissOnClick)
                        .setOngoing(params.shouldOngoingNotif)
                        .setStyle(androidx.media.app.NotificationCompat.MediaStyle()
                                .setShowActionsInCompactView(1))
                        .build()

            }
            NotificationType.PROGRESS -> {

                val snoozeIntent = Intent(context, NotifClickReceiver::class.java).apply {
                    action = context.getString(R.string.dismiss)
                    putExtra("NOTIF_ID", params.id)
                }

                val snoozePendingIntent = PendingIntent.getBroadcast(context, TAP_INTENT_REQ_CODE, snoozeIntent, 0)


                _notification = notificationBuilder.setOngoing(true)
                        .setSmallIcon(R.drawable.notify_icon_72)
                        .setPriority(NotificationCompat.PRIORITY_MIN)
                        .setCategory(Notification.CATEGORY_SERVICE)
                        .setContentTitle(params.title)
                        .setContentText(params.body)
//                        .setProgress(100, percentComplete, true)
//                        .setContentInfo("Progress completed $percentComplete %")
                        .setLargeIcon(getBitmap(params.largeIcon))
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(params.dismissOnClick)
                        .setOngoing(params.shouldOngoingNotif)
                        .addAction(R.drawable.notify_icon_72,
                                context.getString(R.string.dismiss),
                                snoozePendingIntent)
                        .build()


                runProgressTimes(notificationId = params.id,
                        notifBuilder = notificationBuilder,
                        notificationManager = getNotifManager(context))

            }
            NotificationType.TEXT -> {

                _notification = notificationBuilder.setOngoing(true)
                        .setSmallIcon(R.drawable.notify_icon_72)
                        .setPriority(NotificationCompat.PRIORITY_MIN)
                        .setCategory(Notification.CATEGORY_SERVICE)
                        .setContentTitle(params.title)
                        .setContentText(params.body)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(params.dismissOnClick)
                        .setOngoing(params.shouldOngoingNotif)
                        .build()
            }
            NotificationType.IMAGE -> {
                _notification = notificationBuilder.setOngoing(true)
                        .setSmallIcon(R.drawable.notify_icon_72)
                        .setPriority(NotificationCompat.PRIORITY_MIN)
                        .setCategory(Notification.CATEGORY_SERVICE)
                        .setContentTitle(params.title)
                        .setContentText(params.body)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(params.dismissOnClick)
                        .setOngoing(params.shouldOngoingNotif)
                        .setStyle(NotificationCompat.BigPictureStyle()
                                .bigPicture(getBitmap(params.largeIcon)))
                        .build()
            }
            NotificationType.OTHER ->{
                _notification = notificationBuilder.setTimeoutAfter(1).build()
            }
            else -> {
                _notification = notificationBuilder.setOngoing(true)
                        .setSmallIcon(R.drawable.notify_icon_72)
                        .setPriority(NotificationCompat.PRIORITY_MIN)
                        .setCategory(Notification.CATEGORY_SERVICE)
                        .setContentTitle(params.title)
                        .setContentText(params.body)
                        .setContentIntent(pendingIntent)
//                        .setLargeIcon(if (params.largeIcon != "" && params.largeIcon != null) getBitmap(params.largeIcon) else null)
                        .setAutoCancel(params.dismissOnClick)
                        .setOngoing(params.shouldOngoingNotif)
//                        .setStyle(androidx.media.app.NotificationCompat.MediaStyle())
                        .build()
            }
        }



        return _notification

    }


    private fun getBitmap(path: String?): Bitmap? {
        return if (path != "" && path != null){
            val bitmap: Bitmap? = try {
                val bmOptions: BitmapFactory.Options = BitmapFactory.Options();

                BitmapFactory.decodeFile(path, bmOptions)
            } catch (e: Exception) {
                null
            }
            bitmap
        }else{
            null
        }
    }

    fun runProgressTimes(notificationId: Int,
                         notifBuilder: NotificationCompat.Builder,
                         notificationManager: NotificationManager) {
        Thread {

            val totalUnits = 100
            var completedUnits = 20
            var percentComplete = 0

            while (completedUnits <= 100) {

                // Sets the progress indicator to a max value, the current completion percentage, and "determinate" state

                if (totalUnits > 0 && completedUnits > 0) {
                    percentComplete = (100 * completedUnits / totalUnits)
                }

                notifBuilder.setContentText("Downloaded $percentComplete %")
                        .setProgress(totalUnits, completedUnits, false)

                // Displays the progress bar for the first time.
                notificationManager.notify(notificationId, notifBuilder.build())
                // Sleeps the thread, simulating an operation
                // that takes time

                try {
                    // Sleep for 5 seconds
                    Thread.sleep((2 * 1000).toLong())
                } catch (e: InterruptedException) {
//                    Log.d(TAG, "sleep failure")
                }



                completedUnits += 5
            }
            // When the loop is finished, updates the notification
            notifBuilder.setContentText("Download complete").setProgress(0, 0, false)
            notificationManager.notify(notificationId, notifBuilder.build())
        }.start()
    }

}
