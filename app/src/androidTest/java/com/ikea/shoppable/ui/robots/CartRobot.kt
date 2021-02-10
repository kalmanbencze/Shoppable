package com.ikea.shoppable.ui.robots

import android.content.Context
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.ikea.shoppable.R
import com.ikea.shoppable.espresso.RecycleViewAssertions.recyclerViewContainsAtLeast
import com.ikea.shoppable.espresso.onSnackbar
import com.ikea.shoppable.espresso.withRecyclerView
import com.ikea.shoppable.model.CartItem
import com.ikea.shoppable.persistence.db.CacheDatabase

/**
 * Robot controlling the steps for the cart UI testing
 *
 * Note: Robot pattern
 */
open class CartRobot(val context: Context) {

    fun checkCartIsVisible() {
        onView(withId(R.id.menu_action_cart))
            .perform(ViewActions.click())
        onView(withId(R.id.toolbar))
            .check(
                matches(
                    hasDescendant(
                        withText(context.getString(R.string.cart_screen_title))
                    )
                )
            )
        onView(withId(R.id.rv_cart_items))
            .check(matches(isDisplayed()))
    }

    fun insertItems(nrOfItems: Int) {
        CacheDatabase.insertData(context)
        val db = CacheDatabase.getInstance(context)
        val items = arrayListOf<CartItem>()
        for (i in 1..nrOfItems) {
            items.add(CartItem(i.toString(), 1))
        }
        db.cartDao().insert(items).blockingAwait()
    }

    fun openCart() {
        onView(withId(R.id.menu_action_cart))
            .perform(ViewActions.click())
    }

    fun checkNumberOfItemsInCart(expectedItemsCount: Int) {
        onView(withId(R.id.rv_cart_items))
            .check(
                matches(
                    recyclerViewContainsAtLeast(expectedItemsCount)
                )
            )
    }

    fun checkTotalAmountIs(totalAmountString: String) {
        onView(withId(R.id.tv_total)).check(
            matches(
                withText(
                    context.getString(
                        R.string.label_total, totalAmountString
                    )
                )
            )
        )
    }

    fun checkCartIconBadgeText(text: String) {
        onView(withId(R.id.tv_menu_action_cart_count))
            .check(matches(withText(text)))
    }

    fun checkout() {
        onView(withId(R.id.btn_checkout))
            .perform(ViewActions.click())
    }

    fun checkSuccessMessageIsVisible() {
        onSnackbar(R.string.message_order_sent)
            .check(matches(isDisplayed()))
    }

    fun checkCartIsClosed() {
        onView(withId(R.id.rv_cart_items))
            .check(ViewAssertions.doesNotExist())
    }

    fun removeFirstItemFromCart() {
        onView(
            withRecyclerView(R.id.rv_cart_items)
                .atPositionOnView(0, R.id.iv_remove_from_cart)
        ).perform(ViewActions.click())
    }

}