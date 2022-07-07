package com.yellowclass.plugin_native.yc_native.models

import java.io.Serializable

class ForegroundServiceArgsModel(
        val id: Int,
        val title: String,
        val body: String,
        val payload: Map<String, Any>,
        val channelId: String,
        val largeIcon: String?,
        var channelName: String,
        val notificationType: NotificationType,
        val dismissOnAppKill: Boolean = true,
        val dismissOnClick: Boolean = true,
        val shouldOngoingNotif: Boolean = false
) : Serializable {
    override fun toString(): String {
        return ("ForegroundServiceStartParameter{"
                + "id="
                + id
                + ", title="
                + title
                + ", body="
                + body
                + ", payload"
                + payload
                + ", channelId"
                + channelId
                +", largeIcon "
                + largeIcon
                +", channelName "
                + channelName
                +", notificationType "
                + notificationType
                +", dismissOnAppKill "
                + dismissOnAppKill
                +", dismissOnClick "
                + dismissOnClick
                +", shouldOngoingNotif"
                + shouldOngoingNotif
                + '}')
    }
}

fun defaultArgs(): ForegroundServiceArgsModel{
    return ForegroundServiceArgsModel(
            id= 101,
            title="",
            body = "",
            channelId = "CL",
            channelName = "Continue Learning",
            dismissOnAppKill = true,
            dismissOnClick = true,
            largeIcon = null,
            shouldOngoingNotif = false,
            notificationType = NotificationType.OTHER,
            payload = mapOf("one" to "one")
    )
}

enum class NotificationType {
    MEDIA,
    PROGRESS,
    TEXT,
    IMAGE,
    OTHER
}