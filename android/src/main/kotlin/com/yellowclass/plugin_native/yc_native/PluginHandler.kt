package com.yellowclass.plugin_native.yc_native

import android.app.*

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo

import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log

import androidx.annotation.NonNull
import androidx.annotation.RequiresApi

import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

import java.io.File
import java.lang.Exception

import com.yellowclass.plugin_native.yc_native.receivers.ShareTapReceiver
import com.yellowclass.plugin_native.yc_native.services.ForegroundService
import com.yellowclass.plugin_native.yc_native.models.ForegroundServiceArgsModel
import com.yellowclass.plugin_native.yc_native.models.NotificationType
import com.yellowclass.plugin_native.yc_native.models.ShareablePackage
import com.yellowclass.plugin_native.yc_native.bottom_sheets.ShareSheetV2
import com.yellowclass.plugin_native.yc_native.models.SelectedAppData
import com.yellowclass.plugin_native.yc_native.yc_utils.YCUtils
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


val shareablePackagesCache = mutableListOf<ShareablePackage>()

open class PluginHandler {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    fun onShareMedia(
        activity: Activity,
        @NonNull call: MethodCall,
        @NonNull result: MethodChannel.Result
    ) {

        val mContentText: String? = call.argument("mContentText")
        val mImagePath: String? = call.argument("mImagePath")

        val intent = Intent()
        intent.action = Intent.ACTION_SEND

        if (mImagePath != null && mImagePath != "") {

            //check if  image is also provided

            val imagefile = File(mImagePath)
            val imageFileUri = FileProvider.getUriForFile(
                activity,
                activity.applicationContext.packageName + ".provider",
                imagefile
            )

            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_STREAM, imageFileUri)

            val resInfoList: List<ResolveInfo> = activity.packageManager.queryIntentActivities(
                intent,
                PackageManager.MATCH_DEFAULT_ONLY
            )

            for (resInfo: ResolveInfo in resInfoList) {
                val packageName: String = resInfo.activityInfo.packageName
                Log.d("packageName", "shareFiles PackageName: $packageName")
                activity.grantUriPermission(
                    packageName,
                    imageFileUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        } else {
            intent.type = "text/plain"
        }

        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(
            activity,
            1, Intent(activity, ShareTapReceiver::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )

        intent.putExtra(Intent.EXTRA_TEXT, mContentText)

        //create chooser intent to launch intent
        //source: "share" package by flutter (https://github.com/flutter/plugins/blob/master/packages/share/)

        val dialogTitle = null
        val chooserIntent: Intent =
            Intent.createChooser(intent, dialogTitle, pendingIntent.intentSender)
        activity.startActivity(chooserIntent)
        result.success(true)
    }


    fun getNotificationTappedPayload(activity: Activity, @NonNull result: MethodChannel.Result) {

        val mappedPayload = NotificationHelper().getPayload(
            activity.intent,
            NotificationHelper.FOREGROUND_NOTIFICATION_CLICK_ACTION
        )
        if (mappedPayload.isEmpty()) {
            result.success(null)
        } else {
            result.success(mappedPayload)
            activity.intent.action = ""

        }
    }

    fun initYCShare(
        activity: Activity,
        @NonNull call: MethodCall,
        @NonNull result: MethodChannel.Result
    ) {

        val mInitData = call.arguments<Map<String, Any>>()

        val actualData = mInitData?.get("initData") as ArrayList<*>
        val isInstStoryEnabled = mInitData["insta_story"] as Boolean
        val isFBStoryEnabled = mInitData["fb_story"] as Boolean

        shareablePackagesCache.clear()

        Thread {

            val jsonObj = JSONObject()

            for (d in actualData) {
                val pkg = d as HashMap<*, *>
                val name = pkg["appName"] as String
                val packageName = pkg["package"] as String
                val iconUrl = pkg["appIcon"] as String

                //App installed on the device add it to cache.
                val appInfo =
                    YCUtils.isAppInstalled(packageName = packageName, activity = activity);
                if (appInfo.first) {
                    //This check working is in progress.
                    var subJsonObj = JSONObject()
                    when (packageName) {
                        "com.instagram.android" -> {
                            shareablePackagesCache.add(
                                element = ShareablePackage(
                                    name = name,
                                    packageName = packageName,
                                    iconUrl = iconUrl,
                                    appDrawableIconRes = appInfo.second
                                )
                            )
                            subJsonObj.put("package", packageName)
                            subJsonObj.put("appIcon", iconUrl)
                            subJsonObj.put("appName", name)
                            jsonObj.put(name, subJsonObj)

                            if (isInstStoryEnabled) {
                                shareablePackagesCache.add(
                                    element = ShareablePackage(
                                        name = "Instagram Story",
                                        packageName = "com.instagram.share.ADD_TO_STORY",
                                        iconUrl = iconUrl,
                                        appDrawableIconRes = appInfo.second
                                    )
                                )
                                subJsonObj = JSONObject()
                                subJsonObj.put("package", "com.instagram.share.ADD_TO_STORY")
                                subJsonObj.put("appIcon", iconUrl)
                                subJsonObj.put("appName", "Instagram Story")

                                jsonObj.put("INSTAGRAM_STORY", subJsonObj)
                            }
                        }
                        "com.facebook.katana" -> {
                            shareablePackagesCache.add(
                                element = ShareablePackage(
                                    name = name,
                                    packageName = packageName,
                                    iconUrl = iconUrl,
                                    appDrawableIconRes = appInfo.second
                                )
                            )
                            subJsonObj.put("package", packageName)
                            subJsonObj.put("appIcon", iconUrl)
                            subJsonObj.put("appName", name)
                            jsonObj.put(name, subJsonObj)

                            if (isFBStoryEnabled) {
                                shareablePackagesCache.add(
                                    element = ShareablePackage(
                                        name = "Facebook Story",
                                        packageName = "com.facebook.stories.ADD_TO_STORY",
                                        iconUrl = iconUrl,
                                        appDrawableIconRes = appInfo.second
                                    )
                                )

                                subJsonObj = JSONObject()
                                subJsonObj.put("package", "com.facebook.stories.ADD_TO_STORY")
                                subJsonObj.put("appName", "Facebook Story")
                                subJsonObj.put("appIcon", iconUrl)

                                jsonObj.put("FACEBOOK_STORY", subJsonObj)
                            }
                        }
                        else -> {
                            shareablePackagesCache.add(
                                element = ShareablePackage(
                                    name = name,
                                    packageName = packageName,
                                    iconUrl = iconUrl,
                                    appDrawableIconRes = appInfo.second
                                )
                            )

                            subJsonObj.put("package", packageName)
                            subJsonObj.put("appIcon", iconUrl)
                            subJsonObj.put("appName", name)

                            jsonObj.put(name, subJsonObj)
                        }
                    }

                }
            }
            println("JSON OBJECT KOTLIN: $jsonObj");
            result.success(jsonObj.toString())
        }.start()
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    fun launchYCShare(
        activity: Activity,
        @NonNull call: MethodCall,
        @NonNull result: MethodChannel.Result,
        activeContext: Context?
    ) {


        val imgPath = call.argument<String>("mImagePath")
        val contentText = call.argument<String>("mContentText")
        val mimeType = call.argument<String>("mimeType")
        val launchDefault: Boolean = call.argument<String>("launchDefault") == "true"
        val sheetTitle = call.argument<String>("sheetTitle")

        var selectedPkg: ShareablePackage


        //Handled default case
        if (launchDefault || shareablePackagesCache.isEmpty()) {
            onShareMedia(activity = activity, call = call, result = result);
            return
        }

        val appsList: List<ShareablePackage> = if (imgPath.isNullOrBlank()) {
            shareablePackagesCache.filter {
                val n = it.name.toUpperCase(Locale.getDefault())
                n != "INSTAGRAM STORY" && n != "FACEBOOK STORY" && n != "FACEBOOK"
            }
        } else {
            shareablePackagesCache
        }

        //open custom sheet
        ShareSheetV2.openBottomSheet(
            activity,
            appsList,
            itemClickListener = object : ShareSheetV2.OnBottomSheetItemClickListener {
                override fun onClicked(selectedPackage: ShareablePackage) {

                    selectedPkg = selectedPackage
                    val selectedAppData = SelectedAppData(
                        imgPath = imgPath!!,
                        contentText = contentText!!,
                        mimeType = mimeType!!,
                        selectedPackage = selectedPkg
                    )


                    YCUtils.handleSelectedApp(
                        selectedAppData = selectedAppData,
                        activity = activity,
                        result = result,
                        activeContext = activeContext
                    )

                }

                override fun onCancelled() {
                    result.success(false)
                }
            }, title = sheetTitle!!
        )
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    fun launchSingleApp(
        activity: Activity,
        @NonNull call: MethodCall,
        @NonNull result: MethodChannel.Result,
        activeContext: Context?
    ) {


        val imgPath = call.argument<String>("mImagePath")
        val contentText = call.argument<String>("mContentText")
        val pkgName = call.argument<String>("mPackageName")
        val appName = call.argument<String>("mAppName")
        val mimeType = call.argument<String>("mimeType")

        if (pkgName != null) {
            val selectedAppData = SelectedAppData(
                imgPath = imgPath!!,
                contentText = contentText!!,
                mimeType = mimeType!!,
                selectedPackage = ShareablePackage(
                    name = appName ?: "",
                    packageName = pkgName,
                    appDrawableIconRes = null,
                    iconUrl = ""
                )
            )

            YCUtils.handleSelectedApp(
                selectedAppData = selectedAppData,
                activity = activity,
                result = result,
                activeContext = activeContext
            )

        }


    }

    fun startForegroundService(
        activity: Activity,
        @NonNull call: MethodCall,
        @NonNull result: MethodChannel.Result
    ) {
        val mNotificationTitle = call.argument<String>("mNotificationTitle")
        val mNotificationBody = call.argument<String>("mNotificationBody")
        val mNotificationId = call.argument<Int>("mNotificationId")
        val mChannelId = call.argument<String>("mNotificationChannelId")
        val mNotificationLargeIconPath = call.argument<String>("mNotificationLargeIcon")
        val mNotificationType = call.argument<String>("mNotificationType")
        val mDismissOnAppKill = call.argument<Boolean>("mDismissOnAppKill")
        val mDismissOnClick = call.argument<Boolean>("mDismissOnClick")
        val mShouldOngoinNotif = call.argument<Boolean>("mShouldOngoinNotif")
        val mChannelName = call.argument<String>("mChannelName")
        val mNotificationPayload = call.argument<Map<String, Any>>("mNotificationPayload")

        var _notifType = NotificationType.TEXT
        if (mNotificationType != null && mNotificationType.isNotEmpty()) {
            _notifType = NotificationType.valueOf(mNotificationType)
        }


        val params = ForegroundServiceArgsModel(
            id = mNotificationId!!,
            title = mNotificationTitle!!,
            body = mNotificationBody!!,
            channelId = mChannelId!!,
            payload = mNotificationPayload!!,
            largeIcon = mNotificationLargeIconPath,
            dismissOnAppKill = mDismissOnAppKill ?: true,
            notificationType = _notifType,
            channelName = mChannelName ?: "Continue Learning",
            dismissOnClick = mDismissOnClick ?: true,
            shouldOngoingNotif = mShouldOngoinNotif ?: false
        )

        val intent = Intent(activity, ForegroundService::class.java)
        intent.putExtra(ForegroundService.FOREGROUND_SERVICE_ARGS, params)
        ContextCompat.startForegroundService(activity, intent)
        result.success(true)
    }

    fun stopForegroundService(activity: Activity, @NonNull result: MethodChannel.Result) {
        try {
            stopForgroundService(activity)
            result.success(true)
        } catch (e: Exception) {
            result.success(false)
        }

    }

    fun stopForgroundService(context: Context) {
        val intent = Intent(context, ForegroundService::class.java)
        context.stopService(intent)
    }

    fun setOrientation(activity: Activity, @NonNull call: MethodCall, @NonNull result: MethodChannel.Result) {
        var isPortrait = call.argument<Boolean>("isPortrait")
        if (isPortrait == true) {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
        var resultData = mapOf("status" to "0", "data" to "Success")
        result.success(resultData)
    }




}
