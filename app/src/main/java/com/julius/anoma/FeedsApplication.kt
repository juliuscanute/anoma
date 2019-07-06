package com.julius.anoma

import android.app.Application
import com.julius.anoma.di.feedModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class FeedsApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@FeedsApplication)
            modules(feedModule)
        }
    }
}