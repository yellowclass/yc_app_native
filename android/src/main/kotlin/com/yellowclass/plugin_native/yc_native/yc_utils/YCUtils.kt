package com.yellowclass.plugin_native.yc_native.yc_utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable

import androidx.annotation.NonNull

import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import com.yellowclass.plugin_native.yc_native.R

import com.yellowclass.plugin_native.yc_native.models.SelectedAppData
import io.flutter.plugin.common.MethodChannel
import org.json.JSONObject
import java.io.File
import java.lang.Exception

object YCUtils {
    /*
    The response of every app click will be a Map :
    {"app": "appName", "launch":true/false"}
    */
    fun handleSelectedApp(
        selectedAppData: SelectedAppData,
        activity: Activity,
        @NonNull result: MethodChannel.Result,
        activeContext: Context?
    ) {
        when (selectedAppData.selectedPackage.packageName) {
            "com.instagram.android" ->
                createDefaultIntent(selectedAppData, activity, result = result)
            "com.instagram.share.ADD_TO_STORY" ->
                createInstagramStoryIntent(
                    selectedAppData,
                    activity,
                    result = result,
                    activeContext = activeContext
                )
            "com.facebook.katana" ->
                createFacebookIntent(selectedAppData, activity, result = result)
            "com.facebook.stories.ADD_TO_STORY" ->
                createFacebookStoryIntent(
                    selectedAppData,
                    activity,
                    result = result,
                    activeContext = activeContext
                )
            "com.whatsapp" ->
                createDefaultIntent(selectedAppData, activity, result = result)
            "com.google.android.gm" ->
                createDefaultIntent(
                    selectedAppData,
                    activity,
                    result = result,
                    intentType = "message/rfc822"
                )
            else ->
                createDefaultIntent(selectedAppData, activity, result = result)
        }
    }


    //Default share intent
    private fun createDefaultIntent(
        appData: SelectedAppData,
        activity: Activity,
        result: MethodChannel.Result,
        intentType: String? = null
    ) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = intentType ?: appData.mimeType

        if (appData.imgPath.isNotEmpty()) {
            val imageFile = File(appData.imgPath)
            val imageFileUri = FileProvider.getUriForFile(
                activity,
                activity.applicationContext.packageName + ".provider",
                imageFile
            )

            activity.grantUriPermission(
                appData.selectedPackage.packageName,
                imageFileUri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageFileUri)
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }

        shareIntent.setPackage(appData.selectedPackage.packageName)
        shareIntent.putExtra(Intent.EXTRA_TEXT, appData.contentText)

        val resp = JSONObject()
        resp.put("app", appData.selectedPackage.name.replace(" ", "_"))
        try {
            startActivity(activity, shareIntent, null)
            resp.put("launch", true)
        } catch (ex: Exception) {
            //handle error if needed.
            resp.put("launch", false)
        } finally {
            result.success(resp.toString())
        }
    }

    private fun createFacebookIntent(
        appData: SelectedAppData,
        activity: Activity,
        result: MethodChannel.Result
    ) {
        val shareIntent = Intent()

        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = appData.mimeType

        if (appData.imgPath.isNotEmpty()) {
            val imageFile = File(appData.imgPath)
            val imageFileUri = FileProvider.getUriForFile(
                activity,
                activity.applicationContext.packageName + ".provider",
                imageFile
            )

            activity.grantUriPermission(
                appData.selectedPackage.packageName,
                imageFileUri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageFileUri)
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)

            //Insta stories extra params
            shareIntent.putExtra("top_background_color", "#ffffff")
            shareIntent.putExtra("bottom_background_color", "#ffffff")
        }

        shareIntent.setPackage(appData.selectedPackage.packageName)

        val resp = JSONObject()
        resp.put("app", appData.selectedPackage.name.replace(" ", "_"))
        try {
            startActivity(activity, shareIntent, null)
            resp.put("launch", true)
        } catch (ex: Exception) {
            //handle error if needed.
            resp.put("launch", false)
        } finally {
            result.success(resp.toString())
        }
    }

    private fun createInstagramStoryIntent(
        appData: SelectedAppData,
        activity: Activity,
        result: MethodChannel.Result,
        activeContext: Context?
    ) {
        val intent = Intent("com.instagram.share.ADD_TO_STORY")

        intent.type = "image/*"

        val resp = JSONObject()
        resp.put("app", appData.selectedPackage.name.replace(" ", "_"))

        if (appData.imgPath.isNotEmpty()) {
            val imageFile = File(appData.imgPath)

            val imageFileUri = FileProvider.getUriForFile(
                activeContext!!,
                activeContext.applicationContext.packageName + ".provider",
                imageFile
            )

            activity.grantUriPermission(
                appData.selectedPackage.packageName,
                imageFileUri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
            )

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            intent.putExtra("interactive_asset_uri", imageFileUri)
            intent.putExtra("content_url", "https://yellowclass.com")
            intent.putExtra("top_background_color", "#ffffff")
            intent.putExtra("bottom_background_color", "#000000")
            intent.putExtra(Intent.EXTRA_TEXT, appData.contentText);

            activity.grantUriPermission(
                "com.instagram.android",
                imageFileUri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )

            if (activity.packageManager.resolveActivity(intent, 0) != null) {
                try {
                    activity.startActivity(intent)
                    resp.put("launch", true)
                } catch (ex: Exception) {
                    //handle error if needed.
                    resp.put("launch", false)
                } finally {
                    result.success(resp.toString())
                }
            }

        } else {
            resp.put("launch", false)
            result.success(resp.toString())
        }
    }

    private fun createFacebookStoryIntent(
        appData: SelectedAppData,
        activity: Activity,
        result: MethodChannel.Result,
        activeContext: Context?
    ) {


        val intent = Intent("com.facebook.stories.ADD_TO_STORY")
        intent.type = "image/*"

        val resp = JSONObject()
        resp.put("app", appData.selectedPackage.name.replace(" ", "_"))

        if (appData.imgPath.isNotEmpty()) {
            val imageFile = File(appData.imgPath)

            val imageFileUri = FileProvider.getUriForFile(
                activeContext!!,
                activeContext.applicationContext.packageName + ".provider",
                imageFile
            )

            val appId = R.string.facebook_id

            activity.grantUriPermission(
                appData.selectedPackage.packageName,
                imageFileUri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
            )

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            intent.putExtra("com.facebook.platform.extra.APPLICATION_ID", appId)
            intent.putExtra("interactive_asset_uri", imageFileUri)
            intent.putExtra("content_url", "https://yellowclass.com")
            intent.putExtra("top_background_color", "#ffffff")
            intent.putExtra("bottom_background_color", "#000000")

            activity.grantUriPermission(
                "com.facebook.katana",
                imageFileUri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )

            if (activity.packageManager.resolveActivity(intent, 0) != null) {
                try {
                    activity.startActivity(intent)
                    resp.put("launch", true)
                } catch (ex: Exception) {
                    //handle error if needed.
                    resp.put("launch", false)
                } finally {
                    result.success(resp.toString())
                }
            }

        } else {
            resp.put("launch", false)
            result.success(resp.toString())
        }
    }


    fun isAppInstalled(packageName: String, activity: Activity): Pair<Boolean, Drawable?> {
        var pkgInfo: ApplicationInfo? = null
        var drawableIcon: Drawable? = null
        try {

            pkgInfo = activity.packageManager.getApplicationInfo(packageName, 0)
            drawableIcon = getAppIcon(packageName, activity)
        } catch (e: PackageManager.NameNotFoundException) {
        }
        return Pair(pkgInfo?.enabled ?: false, drawableIcon)
    }

    private fun getAppIcon(packageName: String, activity: Activity): Drawable? {
        var drawableIcon: Drawable? = null
        return try {
            activity.packageManager.getApplicationIcon(packageName).also { drawableIcon = it }
        } catch (e: Exception) {
            drawableIcon
        }
    }

}
