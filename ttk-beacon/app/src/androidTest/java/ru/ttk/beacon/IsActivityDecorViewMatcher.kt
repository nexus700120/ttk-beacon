package ru.ttk.beacon

import android.view.View
import android.view.WindowManager
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

fun isActivityDecorView(): Matcher<View> = IsActivityDecorViewMatcher()

class IsActivityDecorViewMatcher : TypeSafeMatcher<View>() {

    override fun describeTo(description: Description) {
        description.appendText("is activity decor view")
    }

    override fun matchesSafely(item: View): Boolean {
        val layoutParams = item.layoutParams
        if (layoutParams is WindowManager.LayoutParams) {
            return layoutParams.type == WindowManager.LayoutParams.TYPE_BASE_APPLICATION
        }
        return false
    }

}