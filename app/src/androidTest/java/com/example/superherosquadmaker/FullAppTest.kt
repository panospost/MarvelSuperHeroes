package com.example.superherosquadmaker


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class FullAppTest {
    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)
    @Test
    fun mainActivityTest() {
        val recyclerView = onView(
            allOf(
                withId(R.id.listView),
                childAtPosition(
                    withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                    2
                )
            )
        )
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        val appCompatButton = onView(
            allOf(
                withId(R.id.addRemoveButton), withText("\uD83D\uDCAA Hire to Squad"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    2
                )
            )
        )
        appCompatButton.perform(scrollTo(), click())

        val appCompatImageView = onView(
            allOf(
                withId(R.id.goBackToList), withContentDescription("go back img"),
                childAtPosition(
                    allOf(
                        withId(R.id.framelayout), withContentDescription("super hero image"),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            0
                        )
                    ),
                    1
                )
            )
        )
        appCompatImageView.perform(scrollTo(), click())

        val recyclerView2 = onView(
            allOf(
                withId(R.id.listView),
                childAtPosition(
                    withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                    2
                )
            )
        )
        recyclerView2.perform(actionOnItemAtPosition<ViewHolder>(24, click()))

        val appCompatButton2 = onView(
            allOf(
                withId(R.id.addRemoveButton), withText("\uD83D\uDCAA Hire to Squad"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    2
                )
            )
        )
        appCompatButton2.perform(scrollTo(), click())

        val appCompatImageView2 = onView(
            allOf(
                withId(R.id.goBackToList), withContentDescription("go back img"),
                childAtPosition(
                    allOf(
                        withId(R.id.framelayout), withContentDescription("super hero image"),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            0
                        )
                    ),
                    1
                )
            )
        )
        appCompatImageView2.perform(scrollTo(), click())

        val recyclerView3 = onView(
            allOf(
                withId(R.id.my_squad),
                childAtPosition(
                    withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                    1
                )
            )
        )
        recyclerView3.perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        val appCompatButton3 = onView(
            allOf(
                withId(R.id.addRemoveButton), withText("\uD83D\uDD25 Fire from Squad"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    2
                )
            )
        )
        appCompatButton3.perform(scrollTo(), click())

        val appCompatButton4 = onView(
            allOf(
                withId(android.R.id.button2), withText("No"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.buttonPanel),
                        0
                    ),
                    2
                )
            )
        )
        appCompatButton4.perform(scrollTo(), click())

        val appCompatButton5 = onView(
            allOf(
                withId(R.id.addRemoveButton), withText("\uD83D\uDD25 Fire from Squad"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    2
                )
            )
        )
        appCompatButton5.perform(scrollTo(), click())

        val appCompatButton6 = onView(
            allOf(
                withId(android.R.id.button1), withText("Yes"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.buttonPanel),
                        0
                    ),
                    3
                )
            )
        )
        appCompatButton6.perform(scrollTo(), click())

        val appCompatImageView3 = onView(
            allOf(
                withId(R.id.goBackToList), withContentDescription("go back img"),
                childAtPosition(
                    allOf(
                        withId(R.id.framelayout), withContentDescription("super hero image"),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            0
                        )
                    ),
                    1
                )
            )
        )
        appCompatImageView3.perform(scrollTo(), click())
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
