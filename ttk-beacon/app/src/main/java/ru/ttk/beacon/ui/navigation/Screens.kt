package ru.ttk.beacon.ui.navigation

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.android.support.SupportAppScreen
import ru.ttk.beacon.ui.module.DeviceNotSupportedFragment
import ru.ttk.beacon.ui.module.PermissionsNotGrantedFragment
import ru.ttk.beacon.ui.module.bluetooth.BluetoothDisabledFragment
import ru.ttk.beacon.ui.module.scanner.ScannerFragment
import ru.ttk.beacon.ui.module.scanner.beacon.list.AppleBeaconListFragment
import ru.ttk.beacon.ui.module.scanner.ble.BleDeviceListFragment

sealed class Screens : SupportAppScreen() {

    object PermissionsNotGranted : Screens() {
        override fun getFragment() = PermissionsNotGrantedFragment()
    }

    object NotSupported : Screens() {
        override fun getFragment(): Fragment = DeviceNotSupportedFragment()
    }

    object BluetoothDisabled : Screens() {
        override fun getFragment(): Fragment = BluetoothDisabledFragment()
    }

    object Scanner : Screens() {
        override fun getFragment(): Fragment = ScannerFragment()
    }
}