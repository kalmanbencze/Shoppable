package com.ikea.shoppable.ui.robots

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import com.ikea.shoppable.R
import com.ikea.shoppable.espresso.RecycleViewAssertions
import com.ikea.shoppable.espresso.withRecyclerView
import com.ikea.shoppable.model.Product
import com.ikea.shoppable.persistence.db.CacheDatabase

class ProductRobot(private val context: Context) {

    fun clearCart() {
        val db = CacheDatabase.getInstance(context)
        db.cartDao().deleteAll().blockingAwait()
    }

    fun checkProductDetailsAreVisible(name: String, type: Product.Companion.Type, price: String, info: String) {
        onView(withId(R.id.toolbar))
            .check(
                matches(
                    hasDescendant(
                        withText(name)
                    )
                )
            )
        onView(withId(R.id.tv_name)).check(matches(withText(name)))
        onView(withId(R.id.tv_info)).check(matches(withText(info)))
        onView(withId(R.id.tv_type)).check(matches(withText("Category: ${type.name.lowercase()}")))
        onView(withId(R.id.tv_price)).check(matches(withText(price)))
    }

    fun pressAddToCart() {
        onView(withId(R.id.fab_add_to_cart)).check(matches(isDisplayed()))
        onView(withId(R.id.fab_add_to_cart)).perform(click())
    }

    fun checkCartHasItemCount(count: Int) {
        onView(withId(R.id.tv_menu_action_cart_count)).check(matches(withText("$count")))
    }

    fun openProductFromList(index: Int) {
        onView(withId(R.id.rv_product_list))
            .check(matches(RecycleViewAssertions.recyclerViewContainsAtLeast(index + 1)))

        onView(withId(R.id.rv_product_list))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(index))

        onView(
            withRecyclerView(R.id.rv_product_list)
                .atPosition(index)
        ).perform(click())
    }

    fun checkBadgeHasCount(count: Int) {
        onView(withId(R.id.tv_count)).check(matches(withText("$count")))
    }

    fun checkAddButtonIsVisible() {
        onView(withId(R.id.fab_add_to_cart)).check(matches(isDisplayed()))
    }
}