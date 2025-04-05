package com.colab.myfriend.worker

import com.crocodic.core.data.model.AppNotification
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.greenrobot.eventbus.EventBus
import timber.log.Timber

class FirebaseMsgService: FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.tag("fcm-token").d("Token: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val inAppNotif = AppNotification(
            title = message.notification?.title,
            content = message.notification?.body
        )

        EventBus.getDefault().post(inAppNotif)
    }
}