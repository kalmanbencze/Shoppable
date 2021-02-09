package com.ikea.shoppable.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.ikea.shoppable.R
import com.ikea.shoppable.espresso.ExtraViewMatchers.recyclerViewContains
import com.ikea.shoppable.espresso.ExtraViewMatchers.recyclerViewContainsAtLeast
import com.ikea.shoppable.espresso.TestUtils.withRecyclerView
import com.ikea.shoppable.model.CartItem
import com.ikea.shoppable.persistence.db.CacheDatabase
import com.ikea.shoppable.view.MainActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class CartTest {
    @Rule
    @JvmField
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun before() {
        CacheDatabase.insertData(InstrumentationRegistry.getInstrumentation().targetContext)
        val db = CacheDatabase.getInstance(InstrumentationRegistry.getInstrumentation().targetContext)
        val items = arrayListOf<CartItem>()
        items.add(CartItem("1", 1))
        items.add(CartItem("2", 1))
        items.add(CartItem("3", 2))
        items.add(CartItem("4", 4))
        db.cartDao().insert(items).blockingAwait()
    }

    @Test
    fun testUIAppears() {
        onView(withText("Shoppable"))
            .check(matches(isDisplayed()))
        onView(withId(R.id.rv_cart_items))
            .check(matches(isDisplayed()))
    }

    @Test
    fun test4ElementsAdded() {
        onView(withId(R.id.menu_action_cart))
            .perform(click())
        //we check the number of elements
        onView(withId(R.id.rv_cart_items))
            .check(
                matches(
                    recyclerViewContainsAtLeast(4)
                )
            )
        //we check the total amount after the 8 items have been added
        onView(withId(R.id.tv_total)).check(
            matches(
                withText(
                    InstrumentationRegistry.getInstrumentation().targetContext.getString(
                        R.string.label_total,
                        "38,304 kr"
                    )
                )
            )
        )
        //we check the badge on the cart menu action
        onView(withId(R.id.tv_menu_action_cart_count)).check(matches(withText("8")))
    }

    @Test
    fun testOrderCanBeSent() {
        onView(withId(R.id.menu_action_cart))
            .perform(click())
        onView(withId(R.id.btn_checkout))
            .perform(click())
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(R.string.message_order_sent)))

        onView(withId(R.id.rv_cart_items))
            .check(
                matches(
                    recyclerViewContains(0)
                )
            )
    }
}