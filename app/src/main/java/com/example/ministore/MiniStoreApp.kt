package com.example.ministore

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MiniStoreApp : Application() {

        override fun onCreate() {
            super.onCreate()
        }

    }