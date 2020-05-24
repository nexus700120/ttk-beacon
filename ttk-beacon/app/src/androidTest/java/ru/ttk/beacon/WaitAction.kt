package ru.ttk.beacon

import android.view.View
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.util.HumanReadables
import androidx.test.espresso.util.TreeIterables
import org.hamcrest.Matcher
import java.util.concurrent.TimeoutException

private const val DEFAULT_TIMEOUT = 5000L

fun wait(conditionMatcher: Matcher<out View>): ViewAction =
    WaitForViewAction(conditionMatcher, DEFAULT_TIMEOUT)


class WaitForViewAction(
    private val viewMatcher: Matcher<out View>,
    private val timeout: Long
) : ViewAction {
    override fun getDescription(): String =
        "wait for a specific view $viewMatcher during ${timeout}ms"

    override fun getConstraints(): Matcher<View> = ViewMatchers.isRoot()

    override fun perform(uiController: UiController, view: View) {
        uiController.loopMainThreadUntilIdle()
        val startTime = System.currentTimeMillis()
        val endTime = startTime + timeout

        do {
            uiController.loopMainThreadForAtLeast(50)
            for (child in TreeIterables.breadthFirstViewTraversal(view)) {
                if (viewMatcher.matches(child)) {
                    return
                }
            }
        } while (System.currentTimeMillis() < endTime)

        throw PerformException.Builder()
            .withActionDescription(this.description)
            .withViewDescription(HumanReadables.describe(view))
            .withCause(TimeoutException())
            .build()
    }
}