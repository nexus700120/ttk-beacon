package ru.ttk.beacon.ui

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import ru.ttk.beacon.ui.di.navModule
import ru.ttk.beacon.ui.utils.BleHelper

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(module {
                single { BleHelper(get()) }
            }, navModule)
        }
    }
}