package ru.ttk.beacon

import android.annotation.TargetApi
import android.widget.Button
import android.widget.CheckBox
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import io.mockk.every
import io.mockk.spyk
import org.hamcrest.CoreMatchers.allOf
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.get
import org.koin.test.mock.declare
import ru.ttk.beacon.ui.AppActivity
import ru.ttk.beacon.ui.utils.BleHelper

class PermissionTest : KoinTest {

    private val permissionDialog = By.res(INSTALLER_PKG, "dialog_container")
    private val denyCondition = By.res(INSTALLER_PKG, "permission_deny_button").clazz(Button::class.java)
    private val allowCondition = By.res(INSTALLER_PKG, "permission_allow_button").clazz(Button::class.java)
    private val doNotAskAgainCondition = By.res(INSTALLER_PKG, "do_not_ask_checkbox").clazz(CheckBox::class.java)
    private val settingCondition = By.pkg(SETTINGS_PKG)

    @Before
    fun before() {
        declare {
            spyk(get<BleHelper>()) {
                every { isBleSupportedByDevice } returns true
            }
        }
    }

    @Test
    @TargetApi(28)
    fun denyPermission() {
        launchActivity<AppActivity>().use {
            val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
            assertTrue(device.wait(Until.hasObject(permissionDialog), WAIT_TIMEOUT))
            device.findObject(denyCondition).click()
            assertTrue(device.wait(Until.gone(permissionDialog), WAIT_TIMEOUT))

            onView(withText(R.string.permissions_not_granted)).check(matches(isDisplayed()))
            onView(allOf(isAssignableFrom(Button::class.java), withText(R.string.allow_access)))
                .check(matches(isDisplayed()))
        }
    }

    @Test
    @TargetApi(28)
    fun allowPermission() {
        launchActivity<AppActivity>().use {
            val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
            assertTrue(device.wait(Until.hasObject(permissionDialog), WAIT_TIMEOUT))
            device.findObject(allowCondition).click()
            assertTrue(device.wait(Until.gone(permissionDialog), WAIT_TIMEOUT))
        }
    }

    @Test
    @TargetApi(28)
    fun doNotAskAgain() {
        launchActivity<AppActivity>().use {
            val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
            assertTrue(device.wait(Until.hasObject(permissionDialog), WAIT_TIMEOUT))
            device.findObject(denyCondition).click()
            assertTrue(device.wait(Until.gone(permissionDialog), WAIT_TIMEOUT))

            onView(withText(R.string.permissions_not_granted)).check(matches(isDisplayed()))
            onView(allOf(isAssignableFrom(Button::class.java), withText(R.string.allow_access)))
                .check(matches(isDisplayed()))
                .perform(click())

            assertTrue(device.wait(Until.hasObject(permissionDialog), WAIT_TIMEOUT))
            device.findObject(doNotAskAgainCondition).click()
            device.findObject(denyCondition).click()
            assertTrue(device.wait(Until.gone(permissionDialog), WAIT_TIMEOUT))

            onView(withText(R.string.permissions_not_granted)).check(matches(isDisplayed()))
            onView(allOf(isAssignableFrom(Button::class.java), withText(R.string.allow_access)))
                .check(matches(isDisplayed()))
                .perform(click())

            assertTrue(device.wait(Until.hasObject(settingCondition), WAIT_TIMEOUT))
        }
    }


    companion object {
        private const val WAIT_TIMEOUT = 5000L
        private const val INSTALLER_PKG = "com.android.packageinstaller"
        private const val SETTINGS_PKG = "com.android.settings"
    }
}