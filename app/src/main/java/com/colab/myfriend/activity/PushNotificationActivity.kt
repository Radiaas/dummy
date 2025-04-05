package com.colab.myfriend.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.crocodic.core.base.activity.NoViewModelActivity
import com.example.myfriend.R
import com.example.myfriend.databinding.ActivityPushNotificationBinding
import com.google.firebase.messaging.FirebaseMessaging
import timber.log.Timber

class PushNotificationActivity :
    NoViewModelActivity<ActivityPushNotificationBinding>(R.layout.activity_push_notification) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        getFcmToken()
    }

    private fun getFcmToken() {
        generateFirebaseToken {FcmToken ->
            Timber.tag("firebase-token").d(FcmToken)
        }
    }

    private fun generateFirebaseToken(result: (String) -> Unit) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            result(it.result)
        }
    }
}