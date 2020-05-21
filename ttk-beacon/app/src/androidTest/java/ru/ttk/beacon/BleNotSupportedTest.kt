package ru.ttk.beacon

import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.mock.declare
import ru.ttk.beacon.ui.AppActivity
import ru.ttk.beacon.ui.common.bluetooth.BleHelper

class BleNotSupportedTest : KoinTest {

    @Test
    fun checkScreen() {
        declare {
            mockk<BleHelper> {
                every { isBleSupportedByDevice } returns false
            }
        }
        launchActivity<AppActivity>().use {
            onView(withText(R.string.device_not_supported))
                .check(matches(isDisplayed()))
        }
    }
}