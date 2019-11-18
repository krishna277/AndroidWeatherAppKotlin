package com.krishna.weatherapp

import android.content.res.Resources
import android.view.View

import androidx.recyclerview.widget.RecyclerView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule

import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.junit.Assert.assertEquals


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest() {
    @get:Rule
    var mActivityTestRule : ActivityTestRule<MainActivity> = ActivityTestRule<MainActivity>(MainActivity::class.java)
    internal lateinit var recyclerView: RecyclerView
    private var recyclerViewMatcher: RecyclerViewMatcher? = null

    @Before
    fun initActivity() {
        recyclerView = mActivityTestRule.activity.findViewById(R.id.rvData)
        recyclerViewMatcher = RecyclerViewMatcher(R.id.rvData)
    }


    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        assertEquals("com.krishna.weatherapp", appContext.packageName)
    }

    @Test
    fun verifyFirstEntryHasTextFieldDescription() {

        try {
            Thread.sleep(6000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        // Make sure an item displayed in recycler view
        onView(recyclerViewMatcher!!.atPosition(0))
                .check(matches(hasDescendant(withId(R.id.tv_desc))))

    }

    inner class RecyclerViewMatcher(private val recyclerViewId: Int) {

        fun atPosition(position: Int): Matcher<View> {
            return atPositionOnView(position, -1)
        }

        fun atPositionOnView(position: Int, targetViewId: Int): Matcher<View> {

            return object : TypeSafeMatcher<View>() {
                internal var resources: Resources? = null
                internal var childView: View? = null

                override fun describeTo(description: Description) {
                    var idDescription = Integer.toString(recyclerViewId)
                    if (this.resources != null) {
                        try {
                            idDescription = this.resources!!.getResourceName(recyclerViewId)
                        } catch (var4: Resources.NotFoundException) {
                            idDescription = String.format("%s (resource name not found)",
                                    recyclerViewId)
                        }

                    }

                    description.appendText("with id: $idDescription")
                }

                public override fun matchesSafely(view: View): Boolean {

                    this.resources = view.resources

                    if (childView == null) {
                        val recyclerView = view.rootView.findViewById<RecyclerView>(recyclerViewId)
                        if (recyclerView != null && recyclerView.id == recyclerViewId) {
                            childView = recyclerView.findViewHolderForAdapterPosition(position)!!.itemView
                        } else {
                            return false
                        }
                    }

                    if (targetViewId == -1) {
                        return view === childView
                    } else {
                        val targetView = childView!!.findViewById<View>(targetViewId)
                        return view === targetView
                    }

                }
            }
        }

    }
}
