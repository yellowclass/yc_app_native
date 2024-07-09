package com.yellowclass.plugin_native.yc_native

import android.app.Activity
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.Application
import io.flutter.plugin.common.MethodChannel.Result

class InAppUpdateHelper {
    companion object {
        const val REQUEST_CODE_START_UPDATE = 1777
    }

    private var updateResult: Result? = null
    private var appUpdateType: Int? = null
//    private var appUpdateInfo: AppUpdateInfo? = null
//    private var appUpdateManager: AppUpdateManager? = null
//    private var fakeUpdateManager: FakeAppUpdateManager? = null

    private fun checkAppState( activity:Activity?,result: Result, block: () -> Unit) {
//        requireNotNull(appUpdateInfo) {
//            result.success(mapOf(
//                    "status" to false,
//                    "responseCode" to "REQUIRE_CHECK_FOR_UPDATE",
//                    "msg" to "Call checkForUpdate first!"
//            ))
//        }
//        requireNotNull(activity) {
//            result.success(mapOf(
//                    "status" to false,
//                    "responseCode" to "REQUIRE_FOREGROUND_ACTIVITY",
//                    "msg" to "in_app_update requires a foreground activity"
//            ))
//        }
//        requireNotNull(appUpdateManager) {
//            result.success(mapOf(
//                    "status" to false,
//                    "responseCode" to "REQUIRE_CHECK_FOR_UPDATE",
//                    "msg" to "Call checkForUpdate first!"
//            ))
//        }
//        block()
    }

    fun startFlexibleUpdate(activity:Activity, result: Result) = checkAppState(activity, result) {
//        appUpdateType = AppUpdateType.FLEXIBLE
//        updateResult = result
//        appUpdateManager?.startUpdateFlowForResult(
//                appUpdateInfo!!,
//                AppUpdateType.FLEXIBLE,
//                activity,
//                REQUEST_CODE_START_UPDATE
//        )
//        appUpdateManager?.registerListener { state ->
//            if (state.installStatus() == InstallStatus.DOWNLOADED) {
//                updateResult?.success(mapOf(
//                        "status" to true,
//                        "responseCode" to "DOWNLOAD_COMPLETE"
//                ))
//                updateResult = null
//            } else if (state.installErrorCode() != InstallErrorCode.NO_ERROR) {
//                updateResult?.success(mapOf(
//                        "status" to false,
//                        "responseCode" to "ERROR_DURING_INSTALLATION",
//                        "msg" to state.installErrorCode().toString()
//                ))
//                updateResult = null
//            }
//        }
    }

    fun completeFlexibleUpdate(activity:Activity, result: Result) = checkAppState( activity, result) {
//        appUpdateManager?.completeUpdate()
    }

    fun performImmediateUpdate(activity:Activity, result: Result) = checkAppState(activity, result) {
//        appUpdateType = AppUpdateType.IMMEDIATE
//        updateResult = result
//        appUpdateManager?.startUpdateFlowForResult(
//                appUpdateInfo!!,
//                AppUpdateType.IMMEDIATE,
//                activity,
//                REQUEST_CODE_START_UPDATE
//        )
    }

    fun checkForUpdate(activity:Activity?, result: Result, lifecycleCallback: Application.ActivityLifecycleCallbacks) {
//
//        requireNotNull(activity) {
//            result.success(mapOf(
//                    "status" to false,
//                    "responseCode" to "REQUIRE_FOREGROUND_ACTIVITY",
//                    "msg" to "in_app_update requires a foreground activity"
//            ))
//        }
//
//        activity.application?.registerActivityLifecycleCallbacks(lifecycleCallback)
//        appUpdateManager = AppUpdateManagerFactory.create(activity)
//
//        // Returns an intent object that you use to check for an update.
//        val appUpdateInfoTask = appUpdateManager!!.appUpdateInfo
//
//        // Checks that the platform will allow the specified type of update.
//        appUpdateInfoTask.addOnSuccessListener { info ->
//            appUpdateInfo = info
//            var clientVStalenessDays = -1;
//            /**
//             * making return of [info.clientVersionStalenessDays()] null safe.
//             */
//            if(info.clientVersionStalenessDays() != null){
//                clientVStalenessDays = info.clientVersionStalenessDays()!!
//            }
//
//            result.success(
//                mapOf(
//                    "status" to true,
//                    "data" to mapOf(
//                        "updateAvailability" to info.updateAvailability(),
//                        "immediateAllowed" to info.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE),
//                        "flexibleAllowed" to info.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE),
//                        "availableVersionCode" to info.availableVersionCode(),
//                        "installStatus" to info.installStatus(),
//                        "packageName" to info.packageName(),
//                        "clientVersionStalenessDays" to clientVStalenessDays,
//                        "updatePriority" to info.updatePriority()
//                    )
//                )
//            )
//        }
//        appUpdateInfoTask.addOnFailureListener {
//            result.success(mapOf(
//                    "status" to false,
//                    "responseCode" to "TASK_FAILURE",
//                    "msg" to it.message
//            ))
//
//        }
    }

    fun checkForFakeUpdate(activity:Activity, result: Result) {
//        fakeUpdateManager = FakeAppUpdateManager(activity)
//        val appUpdateInfoTask = fakeUpdateManager?.appUpdateInfo!!
//        appUpdateInfoTask.addOnSuccessListener { info ->
//            appUpdateInfo = info
//            var clientVStalenessDays = -1;
//            /**
//             * making return of [info.clientVersionStalenessDays()] null safe.
//             */
//            if(info.clientVersionStalenessDays() != null){
//                clientVStalenessDays = info.clientVersionStalenessDays()!!
//            }
//
//            result.success(
//                    mapOf("status" to true,
//                            "data" to mapOf(
//                                    "updateAvailability" to info.updateAvailability(),
//                                    "immediateAllowed" to info.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE),
//                                    "flexibleAllowed" to info.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE),
//                                    "availableVersionCode" to info.availableVersionCode(), //Nullable according to docs
//                                    "installStatus" to info.installStatus(),
//                                    "packageName" to info.packageName(),
//                                    "clientVersionStalenessDays" to clientVStalenessDays,
//                                    "updatePriority" to info.updatePriority()
//                            )
//                    )
//            )
//        }
//        appUpdateInfoTask.addOnFailureListener {
//            result.success(mapOf(
//                    "status" to false,
//                    "responseCode" to "TASK_FAILURE",
//                    "msg" to it.message
//            ))
//        }
    }

    fun performFakeFlexibleUpdate(result: Result) {
//        fakeUpdateManager?.userAcceptsUpdate()
//        fakeUpdateManager?.downloadStarts()
//        fakeUpdateManager?.downloadCompletes()
//        result.success(mapOf(
//                "status" to true,
//                "responseCode" to "DOWNLOAD_COMPLETE"
//        ))
    }

    fun completeFakeFlexibleUpdate(result: Result) {
//        fakeUpdateManager?.completeUpdate()
//        result.success(mapOf(
//                "status" to true,
//                "responseCode" to "UPDATE_COMPLETE"
//        ))

    }

    fun handleAppResume(activity: Activity) {
//        appUpdateManager
//                ?.appUpdateInfo
//                ?.addOnSuccessListener { appUpdateInfo ->
//                    if (appUpdateInfo.updateAvailability()
//                            == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
//                            && appUpdateType == AppUpdateType.IMMEDIATE
//                    ) {
//                        appUpdateManager?.startUpdateFlowForResult(
//                                appUpdateInfo,
//                                AppUpdateType.IMMEDIATE,
//                                activity,
//                                REQUEST_CODE_START_UPDATE
//                        )
//                    }
//                }
    }

    fun handleAppResult(resultCode: Int): Boolean {
//        if (appUpdateType == AppUpdateType.IMMEDIATE) {
//            if (resultCode == RESULT_CANCELED)
//                updateResult?.success(mapOf(
//                        "status" to false,
//                        "responseCode" to "USER_DENIED_UPDATE",
//                        "msg" to "User has manually denied the update"
//                ))
//            else if (resultCode == RESULT_OK)
//                updateResult?.success(mapOf(
//                        "status" to true,
//                        "responseCode" to "UPDATE_COMPLETE"
//                ))
//            else if (resultCode == ActivityResult.RESULT_IN_APP_UPDATE_FAILED)
//                updateResult?.success(mapOf(
//                        "status" to false,
//                        "responseCode" to "IN_APP_UPDATE_FAILED",
//                        "msg" to "Some other error prevented either the user from providing consent or the update to proceed."
//                ))
//            updateResult = null
//            return true
//        } else if (appUpdateType == AppUpdateType.FLEXIBLE) {
//            if (resultCode == RESULT_CANCELED) {
//                updateResult?.success(mapOf(
//                        "status" to false,
//                        "responseCode" to "USER_DENIED_UPDATE",
//                        "msg" to "User has manually denied the update"
//                ))
//                updateResult = null
//            } else if (resultCode == ActivityResult.RESULT_IN_APP_UPDATE_FAILED) {
//                updateResult?.success(mapOf(
//                        "status" to false,
//                        "responseCode" to "IN_APP_UPDATE_FAILED",
//                        "msg" to "Some other error prevented either the user from providing consent or the update to proceed."
//                ))
//                updateResult = null
//            }
//            return true
//        }
        return false
    }
}