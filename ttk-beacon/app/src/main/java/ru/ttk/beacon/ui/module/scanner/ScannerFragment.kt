package ru.ttk.beacon.ui.module.scanner

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.android.ext.android.get
import org.koin.core.qualifier.qualifier
import ru.ttk.beacon.R
import ru.ttk.beacon.ui.common.bluetooth.stateobserver.BluetoothStateObserver
import ru.ttk.beacon.ui.navigation.RouterType
import ru.ttk.beacon.ui.utils.lifecycleViewModel

class ScannerFragment : Fragment(R.layout.fragment_scanner) {

    private val viewModel by lifecycleViewModel {
        ScannerViewModel(
            stateObserver = BluetoothStateObserver(get()),
            router = get(RouterType.FULL_SCREEN.qualifier)
        )
    }

    private val pageAndItemId = listOf(
        0 to R.id.devices,
        1 to R.id.beacons
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel // init lazy

        val pager = view.findViewById<ViewPager>(R.id.pager)
        pager.adapter = ScannerPagerAdapter(childFragmentManager)

        val bottomNavigation = view.findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener {
            pager.currentItem = pageAndItemId.pageIndex(it)
            true
        }

        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) = Unit

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = Unit

            override fun onPageSelected(position: Int) {
                bottomNavigation.selectedItemId = pageAndItemId.itemId(position)
            }
        })
    }

    private fun List<Pair<Int, Int>>.pageIndex(item: MenuItem): Int =
        first { it.second == item.itemId }.first


    private fun List<Pair<Int, Int>>.itemId(pageIndex: Int): Int =
        first { it.first == pageIndex }.second
}