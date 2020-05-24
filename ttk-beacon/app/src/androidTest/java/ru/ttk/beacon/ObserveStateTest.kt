package ru.ttk.beacon

import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.RootMatchers.isFocusable
import androidx.test.espresso.matcher.ViewMatchers.*
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import org.hamcrest.CoreMatchers.allOf
import org.junit.Before
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.get
import org.koin.test.mock.declare
import ru.ttk.beacon.domain.AppleBeaconScanner
import ru.ttk.beacon.domain.BleDeviceScanner
import ru.ttk.beacon.domain.entity.AppleBeacon
import ru.ttk.beacon.ui.AppActivity
import ru.ttk.beacon.ui.common.bluetooth.BleHelper
import ru.ttk.beacon.ui.common.bluetooth.stateobserver.BluetoothState
import ru.ttk.beacon.ui.common.bluetooth.stateobserver.BluetoothStateObserver
import ru.ttk.beacon.ui.utils.toOptional

class ObserveStateTest : KoinTest {

    private val appleBeaconSubject = PublishSubject.create<List<AppleBeacon>>()
    private val stateSubject = PublishSubject.create<BluetoothState>()

    @Before
    fun before() {
        declare {
            spyk(get<BleHelper>()) {
                every { isBleSupportedByDevice } returns true
                every { isPermissionsGranted } returns true
                every { isBluetoothEnabled } returns true
            }
        }
        declare {
            mockk<BleDeviceScanner>() {
                every { scan() } returns PublishSubject.create()
            }
        }
        declare {
            mockk<AppleBeaconScanner>() {
                every { scan() } returns appleBeaconSubject
                every { scan(any()) } returns appleBeaconSubject
                    .map { it.firstOrNull().toOptional() }
            }
        }
        declare {
            mockk<BluetoothStateObserver>() {
                every { observe() } returns stateSubject
                    .observeOn(AndroidSchedulers.mainThread())
            }
        }
    }

    @Test
    fun bluetoothDisabled() {
        launchActivity<AppActivity>().use {
            onView(isAssignableFrom(BottomNavigationMenuView::class.java))
                .check(matches(isCompletelyDisplayed()))
            stateSubject.onNext(BluetoothState.OFF)
            onView(isRoot()).perform(wait(withText(R.string.bluetooth_description)))
        }
    }

    @Test
    fun bluetoothDisabledWithBottomSheet() {
        launchActivity<AppActivity>().use {
            onView(isAssignableFrom(BottomNavigationMenuView::class.java))
                .check(matches(isCompletelyDisplayed()))
            onView(
                allOf(
                    isAssignableFrom(BottomNavigationItemView::class.java),
                    hasDescendant(withText(R.string.beacons))
                )
            ).perform(click())

            val uuid = "fake uuid"
            appleBeaconSubject.onNext(
                listOf(
                    AppleBeacon(
                        uuid = uuid,
                        mac = "fake mac",
                        major = 0,
                        minor = 0,
                        rssi = 0,
                        distance = 0.0
                    )
                )
            )
            onView(isRoot()).perform(wait(allOf(withText(uuid), isCompletelyDisplayed())))
            onView(allOf(isClickable(), hasDescendant(withText(uuid)))).perform(click())

            onView(isAssignableFrom(CoordinatorLayout::class.java))
                .inRoot(RootMatchers.isDialog())
                .check(matches(isDisplayed()))

            stateSubject.onNext(BluetoothState.OFF)

            onView(isRoot())
                .inRoot(isFocusable())
                .check(matches(isActivityDecorView()))
            onView(isRoot()).perform(wait(withText(R.string.bluetooth_description)))
        }
    }
}