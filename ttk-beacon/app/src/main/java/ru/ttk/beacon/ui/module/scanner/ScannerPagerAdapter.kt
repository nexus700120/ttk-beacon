package ru.ttk.beacon.ui.module.scanner

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import ru.ttk.beacon.ui.module.scanner.beacon.list.AppleBeaconListFragment
import ru.ttk.beacon.ui.module.scanner.ble.BleDeviceListFragment

class ScannerPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment = when (position) {
        0 -> BleDeviceListFragment()
        1 -> AppleBeaconListFragment()
        else -> error("Unexpected fragment position: $position")
    }

    override fun getCount(): Int = 2
}
