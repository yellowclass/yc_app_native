package com.yellowclass.plugin_native.yc_native.services

import android.app.*
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_MIN
import com.yellowclass.plugin_native.yc_native.NotificationHelper
import com.yellowclass.plugin_native.yc_native.R
import com.yellowclass.plugin_native.yc_native.models.ForegroundServiceArgsModel
import com.yellowclass.plugin_native.yc_native.models.NotificationType
import com.yellowclass.plugin_native.yc_native.models.defaultArgs

class ForegroundService : Service() {

    private var _globalParams: ForegroundServiceArgsModel = defaultArgs()

    companion object {
        const val NOTIFICATION_REQ_CODE = 102
        const val FOREGROUND_SERVICE_ARGS = "FOREGROUND_SERVICE_ARGS"
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        _globalParams = if(intent?.getSerializableExtra(FOREGROUND_SERVICE_ARGS) != null){
            intent.getSerializableExtra(FOREGROUND_SERVICE_ARGS) as ForegroundServiceArgsModel
        } else {
            defaultArgs()
        }
        val notificationId = _globalParams.id
        val notification = NotificationHelper().getForegroundNotification(
                context = this,
                requestCode = NOTIFICATION_REQ_CODE,
                params = _globalParams)
        this.startForeground(notificationId, notification)
        if(_globalParams.notificationType == NotificationType.OTHER){
            stopForeground(true)
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        if (_globalParams.dismissOnAppKill) {
            stopSelf()
            stopForeground(true)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }


}




