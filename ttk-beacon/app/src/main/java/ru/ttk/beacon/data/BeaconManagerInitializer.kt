package ru.ttk.beacon.data

import android.content.Context
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.BuildConfig

class BeaconManagerInitializer(private val context: Context) {

    fun initialize(): BeaconManager = BeaconManager.getInstanceForApplication(context).apply {
        foregroundBetweenScanPeriod = SCAN_PERIOD
        foregroundScanPeriod = SCAN_PERIOD
        beaconParsers.apply {
            clear()
            add(BeaconParser().setBeaconLayout(APPLE_BEACON_LAYOUT))
        }
    }

    companion object {
        private const val SCAN_PERIOD = 1000L
        private const val APPLE_BEACON_LAYOUT = "m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24"

        init {
            BeaconManager.setDebug(BuildConfig.DEBUG)
        }
    }
}