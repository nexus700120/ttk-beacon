package ru.ttk.beacon

import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import io.mockk.every
import io.mockk.spyk
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import org.junit.Before
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.get
import org.koin.test.mock.declare
import ru.ttk.beacon.ui.AppActivity
import ru.ttk.beacon.ui.common.bluetooth.BluetoothState
import ru.ttk.beacon.ui.common.bluetooth.BluetoothStateObserver
import ru.ttk.beacon.ui.module.bluetooth.BluetoothInteractor
import ru.ttk.beacon.ui.utils.BleHelper
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
    }

    @Test
    fun enabledBySystem() {
        val stateSubject = BehaviorSubject.createDefault(BluetoothState.OFF)
        declare {
            spyk(get<BluetoothStateObserver>()) {
                every { observe() } returns stateSubject
            }
        }
        launchActivity<AppActivity>().use {
            onView(withText(R.string.bluetooth_description))
                .check(matches(isDisplayed()))
            stateSubject.onNext(BluetoothState.ON)
            TODO("Check beacon list opening")
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
            TODO("Check beacon list opening")
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