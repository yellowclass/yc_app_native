package com.yellowclass.plugin_native.yc_native

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodChannel
import java.lang.ref.WeakReference
import io.flutter.plugin.common.PluginRegistry
import com.yellowclass.plugin_native.yc_native.InAppUpdateHelper.Companion.REQUEST_CODE_START_UPDATE

abstract class ContextAwareFlutterPlugin : FlutterPlugin,
        ActivityAware,
        MethodChannel.MethodCallHandler, PluginRegistry.ActivityResultListener, Application.ActivityLifecycleCallbacks {

    abstract val pluginName: String

    private lateinit var channel: MethodChannel

    protected val activity get() = activityReference.get()
    protected val applicationContext
        get() = contextReference.get() ?: activity?.applicationContext

    private var activityReference = WeakReference<Activity>(null)
    private var contextReference = WeakReference<Context>(null)

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activityReference = WeakReference(binding.activity)
    }

    override fun onDetachedFromActivityForConfigChanges() {
        activityReference.clear()
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        activityReference = WeakReference(binding.activity)
    }

    override fun onDetachedFromActivity() {
        activityReference.clear()
    }

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, pluginName)
        channel.setMethodCallHandler(this)
        contextReference = WeakReference(flutterPluginBinding.applicationContext)
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        if (requestCode == REQUEST_CODE_START_UPDATE) {
            return YcNativePlugin().inAppUpdateHelper.handleAppResult(resultCode)
        }
        return false
    }
}
