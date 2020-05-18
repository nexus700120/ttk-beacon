package ru.ttk.beacon.ui

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import ru.ttk.beacon.BuildConfig
import ru.ttk.beacon.di.bluetoothModule
import ru.ttk.beacon.di.navModule
import ru.ttk.beacon.ui.utils.BleHelper
import timber.log.Timber
import timber.log.Timber.DebugTree

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
        startKoin {
            androidContext(this@App)
            modules(bluetoothModule, navModule)
        }
    }
}