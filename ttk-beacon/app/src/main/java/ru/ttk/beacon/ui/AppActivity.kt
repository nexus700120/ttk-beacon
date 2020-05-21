package ru.ttk.beacon.ui

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import org.koin.android.ext.android.inject
import org.koin.core.qualifier.qualifier
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.ttk.beacon.ui.navigation.RouterType.FULL_SCREEN
import ru.ttk.beacon.ui.navigation.Screens
import ru.ttk.beacon.ui.common.bluetooth.BleHelper

class AppActivity : AppCompatActivity() {

    private val bleHelper by inject<BleHelper>()
    private val navHolder by inject<NavigatorHolder>(FULL_SCREEN.qualifier)
    private val router by inject<Router>(FULL_SCREEN.qualifier)

    override fun onCreate(savedInstanceState: Bundle?) {
        val isBleSupported = bleHelper.isBleSupportedByDevice
        val isPermissionGranted = bleHelper.isPermissionsGranted
        val isBluetoothEnabled = bleHelper.isBluetoothEnabled

        if (!isBleSupported || !isPermissionGranted || !isBluetoothEnabled) {
            super.onCreate(null)
        } else {
            super.onCreate(savedInstanceState)
        }

        if (!isBleSupported) {
            router.newRootScreen(Screens.NotSupported)
        } else if (!isPermissionGranted) {
            ActivityCompat.requestPermissions(
                this,
                bleHelper.requiredPermissions,
                PERMISSIONS_REQUEST_CODE
            )
        } else if (!isBluetoothEnabled) {
            router.newRootScreen(Screens.BluetoothDisabled)
        } else if (savedInstanceState == null) {
            router.newRootScreen(Screens.BeaconList)
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navHolder.setNavigator(SupportAppNavigator(this, android.R.id.content))
    }

    override fun onPause() {
        super.onPause()
        navHolder.removeNavigator()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.any { it != PackageManager.PERMISSION_GRANTED }) {
            router.newRootScreen(Screens.PermissionsNotGranted)
        } else if (!bleHelper.isBluetoothEnabled) {
            router.newRootScreen(Screens.BluetoothDisabled)
        } else {
            router.newRootScreen(Screens.BeaconList)
        }
    }

    companion object {
        private const val PERMISSIONS_REQUEST_CODE = 300
    }
}
