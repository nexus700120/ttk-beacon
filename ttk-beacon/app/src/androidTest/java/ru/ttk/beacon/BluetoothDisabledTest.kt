package ru.ttk.beacon

import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.get
import org.koin.test.mock.declare
import ru.ttk.beacon.domain.AppleBeaconScanner
import ru.ttk.beacon.domain.BleDeviceScanner
import ru.ttk.beacon.ui.AppActivity
import ru.ttk.beacon.ui.common.bluetooth.BleHelper
import ru.ttk.beacon.ui.common.bluetooth.stateobserver.BluetoothState
import ru.ttk.beacon.ui.common.bluetooth.stateobserver.BluetoothStateObserver
import ru.ttk.beacon.ui.module.bluetooth.BluetoothInteractor
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class BluetoothDisabledTest : KoinTest {

    @Before
    fun before() {
        declare {
            spyk(get<BleHelper>()) {
                every { isBleSupportedByDevice } returns true
                every { isPermissionsGranted } returns true
                every { isBluetoothEnabled } returns false
            }
        }
        declare {
            mockk<BleDeviceScanner>() {
                every { scan() } returns PublishSubject.create()
            }
        }
        declare {
            mockk<AppleBeaconScanner>() {
                every { scan() } returns PublishSubject.create()
            }
        }
    }

    @Test
    fun enabledBySystem() {
        val stateSubject = BehaviorSubject.createDefault(BluetoothState.OFF)
        declare {
            spyk(get<BluetoothStateObserver>()) {
                every { observe() } returns stateSubject
                    .observeOn(AndroidSchedulers.mainThread())
            }
        }
        launchActivity<AppActivity>().use {
            onView(withText(R.string.bluetooth_description))
                .check(matches(isDisplayed()))
            stateSubject.onNext(BluetoothState.ON)
            onView(
                allOf(
                    isAssignableFrom(BottomNavigationMenuView::class.java),
                    hasDescendant(withText(R.string.devices))
                )
            ).check(matches(isDisplayed()))
            onView(
                allOf(
                    isAssignableFrom(BottomNavigationMenuView::class.java),
                    hasDescendant(withText(R.string.beacons))
                )
            ).check(matches(isDisplayed()))
        }
    }

    @Test
    fun enabledByClickButton() {
        declare {
            spyk(get<BluetoothStateObserver>()) {
                every { observe() } returns Observable.just(BluetoothState.OFF)
            }
        }
        declare {
            spyk(get<BluetoothInteractor>()) {
                every { enableBluetooth() } returns Completable.complete().delay(2, TimeUnit.SECONDS)
            }
        }
        launchActivity<AppActivity>().use {
            onView(withText(R.string.bluetooth_description))
                .check(matches(isDisplayed()))
            onView(withText(R.string.enabled_bluetooth))
                .perform(click())
            onView(isRoot()).perform(
                wait(
                    allOf(
                        isAssignableFrom(BottomNavigationMenuView::class.java),
                        isDisplayed()
                    )
                )
            )
            onView(
                allOf(
                    isAssignableFrom(BottomNavigationMenuView::class.java),
                    hasDescendant(withText(R.string.devices))
                )
            ).check(matches(isDisplayed()))
            onView(
                allOf(
                    isAssignableFrom(BottomNavigationMenuView::class.java),
                    hasDescendant(withText(R.string.beacons))
                )
            ).check(matches(isDisplayed()))
        }
    }

    @Test
    fun timeout() {
        declare {
            spyk(get<BluetoothStateObserver>()) {
                every { observe() } returns Observable.just(BluetoothState.OFF)
            }
        }
        declare {
            spyk(get<BluetoothInteractor>()) {
                every { enableBluetooth() } returns Completable.error(TimeoutException())
            }
        }
        launchActivity<AppActivity>().use {
            onView(withText(R.string.bluetooth_description))
                .check(matches(isDisplayed()))
            onView(withText(R.string.enabled_bluetooth))
                .perform(click())
            onView(withText(R.string.unknown_error))
                .check(matches(isDisplayed()))
        }
    }
}