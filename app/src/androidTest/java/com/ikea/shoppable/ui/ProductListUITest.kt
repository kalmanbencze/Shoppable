package com.ikea.shoppable.ui

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.ikea.shoppable.R
import com.ikea.shoppable.espresso.ExtraViewMatchers.recyclerViewContainsAtLeast
import com.ikea.shoppable.espresso.TestUtils.withRecyclerView
import com.ikea.shoppable.persistence.db.CacheDatabase
import com.ikea.shoppable.view.MainActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class ProductListUITest {
    @Rule
    @JvmField
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun before() {
        CacheDatabase.insertData(InstrumentationRegistry.getInstrumentation().targetContext)
    }

    @Test
    fun testUIAppears() {
        onView(withText("Shoppable")).check(matches(isDisplayed()))
        onView(withId(R.id.rv_product_list)).check(matches(isDisplayed()))
    }

    @Test
    fun test13thElementIsVisible() {
        onView(withId(R.id.rv_product_list)).check(matches(recyclerViewContainsAtLeast(13)))

        onView(withId(R.id.rv_product_list)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(13))

        onView(
            withRecyclerView(R.id.rv_product_list)
                .atPositionOnView(12, R.id.civ_photo)
        ).check(matches(isDisplayed()))
        //vector drawables can't be compared the same way as bitmap drawables so it's disabled for now
//        onView(withImageDrawable(R.drawable.broken_image_black)).check(matches(isDisplayed())


        onView(
            withRecyclerView(R.id.rv_product_list)
                .atPositionOnView(12, R.id.tv_name)
        ).check(matches(hasTextColor(R.color.gray)))

        onView(
            withRecyclerView(R.id.rv_product_list)
                .atPositionOnView(12, R.id.tv_name)
        ).check(matches(withText("Janinge")))
        onView(
            withRecyclerView(R.id.rv_product_list)
                .atPositionOnView(12, R.id.tv_price)
        ).check(matches(isDisplayed()))
    }


    @Test
    fun testAddingItemToCartDisplaysSuccessMessage() {
        onView(withId(R.id.rv_product_list)).check(matches(recyclerViewContainsAtLeast(1)))

        onView(
            withRecyclerView(R.id.rv_product_list)
                .atPositionOnView(0, R.id.iv_add_to_cart)
        ).perform(click())
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(R.string.label_add_successful)))
        onView(withId(com.google.android.material.R.id.snackbar_action))
            .check(matches(withText(R.string.action_undo)))
    }

    @Test
    fun testItemsGetPutInCart() {
        onView(withId(R.id.rv_product_list)).check(matches(recyclerViewContainsAtLeast(5)))

        onView(
            withRecyclerView(R.id.rv_product_list)
                .atPositionOnView(0, R.id.iv_add_to_cart)
        ).perform(click())
        onView(
            withRecyclerView(R.id.rv_product_list)
                .atPositionOnView(1, R.id.iv_add_to_cart)
        ).perform(click())
        onView(
            withRecyclerView(R.id.rv_product_list)
                .atPositionOnView(2, R.id.iv_add_to_cart)
        ).perform(click())
        onView(withId(R.id.tv_cart_count)).check(matches(withText("3")))
        onView(
            withRecyclerView(R.id.rv_product_list)
                .atPositionOnView(3, R.id.iv_add_to_cart)
        ).perform(click())
        onView(
            withRecyclerView(R.id.rv_product_list)
                .atPositionOnView(4, R.id.iv_add_to_cart)
        ).perform(click())

        onView(withId(R.id.tv_cart_count)).check(matches(withText("5")))
    }

    @Test
    fun testCartOpens() {
        onView(withId(R.id.rv_product_list)).check(matches(recyclerViewContainsAtLeast(5)))

        onView(
            withRecyclerView(R.id.rv_product_list)
                .atPositionOnView(0, R.id.iv_add_to_cart)
        ).perform(click())
        onView(
            withRecyclerView(R.id.rv_product_list)
                .atPositionOnView(1, R.id.iv_add_to_cart)
        ).perform(click())

        onView(withId(R.id.tv_cart_count)).check(matches(withText("2")))
        onView(withId(R.id.iv_cart)).perform(click())
        onView(withId(R.id.rv_cart_items)).check(matches(isDisplayed()))
    }

    @Test
    fun testProductOpens() {
        onView(withId(R.id.rv_product_list)).check(matches(recyclerViewContainsAtLeast(1)))

        onView(
            withRecyclerView(R.id.rv_product_list)
                .atPosition(0)
        ).perform(click())


        onView(withId(R.id.tv_cart_count)).check(matches(withText("2")))
    }
}