package ru.ttk.beacon.ui.utils

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class BleHelper(private val context: Context) {

    val isBleSupportedByDevice: Boolean by lazy {
        context.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)
    }

    val isPermissionsGranted: Boolean
        get() = isLocationPermissionGranted

    private val isLocationPermissionGranted: Boolean
        get() = ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED

    val requiredPermissions: Array<String>
        get() = mutableListOf<String>().apply {
            if (!isLocationPermissionGranted) {
                add(ACCESS_FINE_LOCATION)
            }
        }.toTypedArray()

    val isBluetoothEnabled: Boolean
        get() = BluetoothAdapter.getDefaultAdapter()?.isEnabled ?: false
}