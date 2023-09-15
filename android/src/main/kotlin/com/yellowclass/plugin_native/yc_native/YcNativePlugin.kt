package com.yellowclass.plugin_native.yc_native

import android.app.Activity
`import android.content.Context
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.Result


/** YcNativePlugin */
class YcNativePlugin : ContextAwareFlutterPlugin() {


    override val pluginName: String = "com.yellowclass/yc_app_native"
    private lateinit var channel: MethodChannel
    private val _pluginHandler = PluginHandler()
    val inAppUpdateHelper = InAppUpdateHelper()

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, pluginName)
        channel.setMethodCallHandler(this)
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {

        when (call.method) {
            "shareMediaIntent" -> activity?.let { _pluginHandler.onShareMedia(it, call, result) }
            "showNotification" -> activity?.let {
                _pluginHandler.startForegroundService(
                    it, call, result
                )
            }

            "removeNotification" -> activity?.let {
                _pluginHandler.stopForegroundService(
                    it, result
                )
            }

            "getNotificationTappedPayload" -> activity?.let {
                _pluginHandler.getNotificationTappedPayload(
                    it, result
                )
            }

            "launchYCShare" -> activity?.let {
                _pluginHandler.launchYCShare(
                    it, call, result, activeContext = applicationContext
                )
            }

            "launchSingleApp" -> activity?.let {
                _pluginHandler.launchSingleApp(
                    it, call, result, activeContext = applicationContext
                )
            }

            "initYCShare" -> activity?.let { _pluginHandler.initYCShare(it, call, result) }
            "setOrientation" -> activity?.let { _pluginHandler.setOrientation(it, call, result) }
            "checkForFakeUpdate" -> activity?.let {
                inAppUpdateHelper.checkForFakeUpdate(
                    it, result
                )
            }

            "performFakeFlexibleUpdate" -> activity?.let {
                inAppUpdateHelper.performFakeFlexibleUpdate(
                    result
                )
            }

            "completeFakeFlexibleUpdate" -> activity?.let {
                inAppUpdateHelper.completeFakeFlexibleUpdate(
                    result
                )
            }

            "checkForUpdate" -> activity?.let { inAppUpdateHelper.checkForUpdate(it, result, this) }
            "performImmediateUpdate" -> activity?.let {
                inAppUpdateHelper.performImmediateUpdate(
                    it, result
                )
            }

            "completeFlexibleUpdate" -> activity?.let {
                inAppUpdateHelper.completeFlexibleUpdate(
                    it, result
                )
            }

            "startFlexibleUpdate" -> activity?.let {
                inAppUpdateHelper.startFlexibleUpdate(
                    it, result
                )
            }

            "getNetworkOperatorName" -> activity?.let { getNetworkOperatorName(it, result) }
            else -> result.notImplemented()
        }

    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

    override fun onActivityStarted(activity: Activity) {}

    override fun onActivityResumed(activity: Activity) {
        //handles event
        inAppUpdateHelper.handleAppResume(activity)
    }

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {}

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }


    private fun getNetworkOperatorName(activity: Activity, result: Result) {
        try {
            val telephonyManager =
                activity.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            print(">>>> Network Operator Name:${telephonyManager.networkOperatorName}")
            result.success(
                mapOf(
                    "status" to true, "data" to telephonyManager.networkOperatorName
                )
            )
        } catch (e: Exception) {
            print(e);
            result.success("");
        }
    }

}
