package com.colab.myfriend.app

import android.app.Application
import com.google.firebase.FirebaseApp

class MyFriendApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}