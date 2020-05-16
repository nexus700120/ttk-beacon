package ru.ttk.beacon.ui.navigation

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.android.support.SupportAppScreen
import ru.ttk.beacon.ui.module.DeviceNotSupportedFragment
import ru.ttk.beacon.ui.module.PermissionsNotGrantedFragment

sealed class Screen : SupportAppScreen() {

    object PermissionsNotGranted : Screen() {
        override fun getFragment() = PermissionsNotGrantedFragment()
    }

    object NotSupported : Screen() {
        override fun getFragment(): Fragment = DeviceNotSupportedFragment()
    }

    object BeaconList : Screen() {

    }
}