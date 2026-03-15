package com.example.newsapp

import android.app.Application
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject


@HiltAndroidApp
class NewsApp : Application() {

    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate() {
        super.onCreate()
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN,null)
    }

}