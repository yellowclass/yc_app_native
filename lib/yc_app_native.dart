import 'dart:async';
import 'dart:convert';
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:yc_app_native/model/notification_type_enum.dart';
import 'package:yc_app_native/model/plugin_response_model.dart';
import 'package:yc_app_native/model/share_mime_type.enum.dart';
import 'package:yc_app_native/model/shareable_package.model.dart';
import 'package:yc_app_native/model/yc_share_resp.dart';

import 'model/in_app_model_model.dart';

class YcAppNative {
  static const MethodChannel _channel = MethodChannel(
    'com.yellowclass/yc_app_native',
  );

  static Future<PluginResponse<String>> shareMediaIntent({
    String imgLocalPath = "",
    String contentText = "",
  }) async {
    Map<String, String> _methodParams = {
      "mImagePath": imgLocalPath,
      "mContentText": contentText,
    };

    final data = await _channel.invokeMethod('shareMediaIntent', _methodParams);
    if (data != null) {
      return PluginResponse.success("Shared");
    } else {
      return PluginResponse.error("Error Found");
    }
  }

  static Future<PluginResponse<YCShareResponse>> launchYCShare({
    String imgLocalPath = "",
    String contentText = "",
    YCMimeType mimeType = YCMimeType.TEXTS,
    required bool launchDefaultShare,
    String sheetTitle = "Share with Friends",
  }) async {
    if (!Platform.isAndroid) {
      return PluginResponse.error("Platform is not android");
    }

    Map<String, String> _methodParams = {
      "mImagePath": imgLocalPath,
      "mContentText": contentText,
      "mimeType": mimeType.shareableMime(),
      "launchDefault": launchDefaultShare ? "true" : "false",
      "sheetTitle": sheetTitle
    };

    final data = await _channel.invokeMethod('launchYCShare', _methodParams);
    try {
      if (data == null) {
        return PluginResponse.error("Something went wrong");
      }

      if (data.runtimeType == String) {
        return PluginResponse.success(
          YCShareResponse.fromMap(data),
        );
      } else {
        /*
            When native default sheet will open, then the response will be bool.
            in that case we will return `appName = default_sheet`.
              */
        return PluginResponse.success(
          YCShareResponse(
            appName: "default_sheet",
            launch: true,
          ),
        );
      }
    } catch (e) {
      return PluginResponse.error("Something went wrong");
    }
  }

  static Future<PluginResponse<YCShareResponse>> launchSingleApp({
    required SharablePackageModel sharablePackageModel,
    String imgLocalPath = "",
    String contentText = "",
    YCMimeType mimeType = YCMimeType.TEXTS,
  }) async {
    ///mContentText can act as url data.
    if (!Platform.isAndroid) {
      return PluginResponse.error("Platform is not android");
    }

    Map<String, String> _methodParams = {
      "mImagePath": imgLocalPath,
      "mContentText": contentText,
      "mPackageName": sharablePackageModel.package,
      "mAppName": sharablePackageModel.appName,
      "mimeType": mimeType.shareableMime(),
    };

    final String? data = await _channel.invokeMethod(
      'launchSingleApp',
      _methodParams,
    );
    if (data != null) {
      return PluginResponse.success(YCShareResponse.fromMap(data));
    } else {
      return PluginResponse.error("Something went wrong");
    }
  }

  static Future<PluginResponse<String>> initCustomShare(
    List<SharablePackageModel> packages, {
    bool isInstaStoryEnabled = false,
    bool isFBStoryEnabled = false,
  }) async {
    if (!Platform.isAndroid) {
      return PluginResponse.error("Platform is not android");
    }

    final initParams = packages.map((e) => e.toMap()).toList();

    final finalParam = {
      "initData": initParams,
      "insta_story": isInstaStoryEnabled,
      "fb_story": isFBStoryEnabled
    };

    String? data = await _channel.invokeMethod('initYCShare', finalParam);

    if (data != null) {
      return PluginResponse.success(data);
    } else {
      return PluginResponse.error("No Payload available");
    }
  }

  static Future<void> stopForegroundService() async {
    if (Platform.isAndroid) {
      await _channel.invokeMethod('removeNotification');
    }
    return;
  }

  static Future<PluginResponse<Map<String, dynamic>>>
      getOngoingNotifTappedPayload() async {
    if (!Platform.isAndroid) {
      return PluginResponse.error("No Payload available");
    }
    var data = await _channel.invokeMethod('getNotificationTappedPayload');
    if (data != null) {
      try {
        Map<String, dynamic> payloadData =
            Map<String, dynamic>.from(jsonDecode(jsonEncode(data)));
        return PluginResponse.success(payloadData);
      } catch (e) {
        return PluginResponse.error("No Payload available");
      }
    }
    return PluginResponse.error("No Payload available");
  }

  static Future<PluginResponse<String>> startForegroundService({
    required int notificationId,
    String? title,
    String? bodyText,
    String? channelId,
    Map<String, dynamic>? payload,
    String? largeIcon,
    NotificationType notificationType = NotificationType.TEXT,
    bool dismissOnAppKill = true,
    bool dismissOnClick = true,
    bool shouldOngoingNotif = false,
    String? channelName,
  }) async {
    Map<String, dynamic> _methodParms = {
      "mNotificationId": notificationId,
      "mNotificationTitle": title,
      "mNotificationBody": bodyText,
      "mNotificationChannelId": channelId,
      "mNotificationPayload": payload,
      "mNotificationLargeIcon": largeIcon,
      "mNotificationType": notificationType.name.toUpperCase(),
      "mDismissOnAppKill": dismissOnAppKill,
      "mDismissOnClick": dismissOnClick,
      "mShouldOngoinNotif": shouldOngoingNotif,
      "mChannelName": channelName
    };

    if (!Platform.isAndroid) {
      return PluginResponse.error(
        "Not Configured for other platforms except Android",
      );
    }

    final bool? data = await _channel.invokeMethod(
      'showNotification',
      _methodParms,
    );
    if (data != false) {
      return PluginResponse.success("Service Started");
    } else {
      return PluginResponse.error("Something went wrong");
    }
  }

  static Future<PluginResponse<Map<String, dynamic>>> setOrientation({
    required Orientation orientation,
  }) async {
    bool isPortrait = orientation == Orientation.portrait;
    var data = await _channel.invokeMethod('setOrientation', {
      'isPortrait': isPortrait,
    });
    if (data != null) {
      try {
        Map<String, dynamic> payloadData = Map<String, dynamic>.from(
          jsonDecode(jsonEncode(data)),
        );
        return PluginResponse.success(payloadData);
      } catch (e) {
        return PluginResponse.error("Failed");
      }
    }
    return PluginResponse.error("Failed");
  }

  // check for fake in app update
  static Future<PluginResponse<InAppUpdateModel>> checkForFakeUpdate() async {
    if (!Platform.isAndroid) {
      return PluginResponse.error(
        "Not Configured for other platforms except Android",
      );
    }

    final result = await _channel.invokeMethod('checkForFakeUpdate');
    if (result["status"]) {
      var data = result["data"];
      InAppUpdateModel inAppUpdateModel = InAppUpdateModel(
        updateAvailability: data["updateAvailability"],
        immediateUpdateAllowed: data["immediateUpdateAllowed"] ?? false,
        flexibleUpdateAllowed: data["flexibleUpdateAllowed"] ?? false,
        availableVersionCode: data["availableVersionCode"],
        installStatus: data["installStatus"],
        packageName: data["packageName"],
        clientVersionStalenessDays: data["clientVersionStalenessDays"] ?? 0,
        updatePriority: data["updatePriority"],
      );
      return PluginResponse.success(inAppUpdateModel);
    } else {
      return PluginResponse.error(
        result["msg"],
        data: result["responseCode"],
      );
    }
  }

  // perform fake flexible download
  static Future<PluginResponse<String>> startFakeFlexibleUpdate() async {
    if (!Platform.isAndroid) {
      return PluginResponse.error(
        "Not Configured for other platforms except Android",
      );
    }

    final result = await _channel.invokeMethod('performFakeFlexibleUpdate');
    if (result["status"] && result["resCode"] == "DOWNLOAD_COMPLETE") {
      return PluginResponse.success("DOWNLOAD_COMPLETE");
    } else {
      return PluginResponse.error(
        result["msg"],
        data: result["responseCode"],
      );
    }
  }

  // perform fake flexible download
  static Future<PluginResponse<String>> completeFakeFlexibleUpdate() async {
    if (!Platform.isAndroid) {
      return PluginResponse.error(
        "Not Configured for other platforms except Android",
      );
    }

    final result = await _channel.invokeMethod('completeFakeFlexibleUpdate');
    if (result["status"] && result["resCode"] == "UPDATE_COMPLETE") {
      return PluginResponse.success("UPDATE_COMPLETE");
    } else {
      return PluginResponse.error(
        result["msg"],
        data: result["responseCode"],
      );
    }
  }

  // check for in app update
  static Future<PluginResponse<InAppUpdateModel>> checkForInAppUpdate() async {
    if (!Platform.isAndroid) {
      return PluginResponse.error(
        "Not Configured for other platforms except Android",
      );
    }

    final result = await _channel.invokeMethod('checkForUpdate');
    try {
      if (result["status"]) {
        var data = result["data"];
        InAppUpdateModel inAppUpdateModel = InAppUpdateModel(
          updateAvailability: data["updateAvailability"] ?? -1,
          immediateUpdateAllowed: data["immediateUpdateAllowed"] ?? false,
          flexibleUpdateAllowed: data["flexibleUpdateAllowed"] ?? false,
          availableVersionCode: data["availableVersionCode"] ?? -1,
          installStatus: data["installStatus"] ?? -1,
          packageName: data["packageName"] ?? "",
          clientVersionStalenessDays: data["clientVersionStalenessDays"] ?? -1,
          updatePriority: data["updatePriority"] ?? -1,
        );
        return PluginResponse.success(inAppUpdateModel);
      } else {
        // return error
        return PluginResponse.error(
          result["msg"],
          data: result["responseCode"],
        );
      }
    } catch (e) {
      return PluginResponse.error(e.toString());
    }
  }

  /// Performs an immediate update that is entirely handled by the Play API.
  ///
  /// [checkForUpdate] has to be called first to be able to run this.
  static Future<PluginResponse<String>> performImmediateUpdate() async {
    if (!Platform.isAndroid) {
      return PluginResponse.error(
        "Not Configured for other platforms except Android",
      );
    }

    final result = await _channel.invokeMethod('performImmediateUpdate');
    if (result["status"]) {
      return PluginResponse.success("");
    } else {
      //possible values of @errorCode: {USER_DENIED_UPDATE, IN_APP_UPDATE_FAILED, TASK_FAILURE}
      return PluginResponse.error(
        result["msg"],
        data: result["responseCode"],
      );
    }
  }

  /// Starts the download of the app update.
  ///
  /// Throws a [PlatformException] if the download fails.
  /// When the returned [Future] is completed without any errors,
  /// [completeFlexibleUpdate] can be called to install the update.
  ///
  /// [checkForUpdate] has to be called first to be able to run this.
  static Future<PluginResponse<String>> startFlexibleUpdate() async {
    if (!Platform.isAndroid) {
      return PluginResponse.error(
        "Not Configured for other platforms except Android",
      );
    }
    final result = await _channel.invokeMethod('startFlexibleUpdate');
    if (result["status"] && result["resCode"] == "DOWNLOAD_COMPLETE") {
      return PluginResponse.success("DOWNLOAD_COMPLETE");
    } else {
      //possible values of @errorCode: {USER_DENIED_UPDATE, IN_APP_UPDATE_FAILED, TASK_FAILURE}
      return PluginResponse.error(
        result["msg"],
        data: result["responseCode"],
      );
    }
  }

  /// Installs the update downloaded via [startFlexibleUpdate].
  /// [startFlexibleUpdate] has to be completed successfully.
  static Future<PluginResponse<String>> completeFlexibleUpdate() async {
    if (!Platform.isAndroid) {
      return PluginResponse.error(
        "Not Configured for other platforms except Android",
      );
    }

    final result = await _channel.invokeMethod('completeFlexibleUpdate');
    if (result["status"] && result["resCode"] == "UPDATE_COMPLETE") {
      return PluginResponse.success("UPDATE_COMPLETE");
    } else {
      return PluginResponse.error(
        result["msg"],
        data: result["responseCode"],
      );
    }
  }

  static Future<PluginResponse<String>> getNetworkOperatorName() async {
    if (!Platform.isAndroid) {
      return PluginResponse.error(
        "Not Configured for other platforms except Android",
      );
    }

    final result = await _channel.invokeMethod('getNetworkOperatorName');
    if (result["status"]) {
      return PluginResponse.success(result["data"]);
    } else {
      return PluginResponse.error(
        result["msg"],
        data: result["responseCode"],
      );
    }
  }
}
