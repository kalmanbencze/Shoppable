package com.ikea.shoppable.ui.robots

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import com.ikea.shoppable.R
import com.ikea.shoppable.espresso.RecycleViewAssertions.recyclerViewContainsAtLeast
import com.ikea.shoppable.espresso.onSnackbar
import com.ikea.shoppable.espresso.onSnackbarButton
import com.ikea.shoppable.espresso.withRecyclerView
import com.ikea.shoppable.persistence.db.CacheDatabase

open class BrowseRobot(private val context: Context) {
    fun insertTestData() {
        CacheDatabase.insertData(context)
    }

    fun checkProductListIsVisible() {
        onView(withId(R.id.toolbar))
            .check(
                matches(
                    hasDescendant(
                        withText(context.getString(R.string.list_screen_title))
                    )
                )
            )
        onView(withId(R.id.rv_product_list))
            .check(matches(isDisplayed()))
    }

    fun checkElementIsVisible(index: Int) {
        onView(withId(R.id.rv_product_list))
            .check(matches(recyclerViewContainsAtLeast(index + 1)))

        onView(withId(R.id.rv_product_list))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(index))

        onView(
            withRecyclerView(R.id.rv_product_list)
                .atPositionOnView(index, R.id.civ_photo)
        ).check(matches(isDisplayed()))

    }

    fun checkDataIsPresentInRow(index: Int, productName: String) {
        onView(withId(R.id.rv_product_list))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(index))
        onView(
            withRecyclerView(R.id.rv_product_list)
                .atPositionOnView(index, R.id.tv_name)
        ).check(matches(hasTextColor(R.color.gray)))

        onView(
            withRecyclerView(R.id.rv_product_list)
                .atPositionOnView(index, R.id.tv_name)
        ).check(matches(withText(productName)))
        onView(
            withRecyclerView(R.id.rv_product_list)
                .atPositionOnView(index, R.id.tv_price)
        ).check(matches(isDisplayed()))
    }

    fun listItemCountIsAtLeast(count: Int) {
        onView(withId(R.id.rv_product_list))
            .check(matches(recyclerViewContainsAtLeast(count)))
    }

    fun addItemToCart(index: Int) {
        onView(withId(R.id.rv_product_list))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(index))
        onView(
            withRecyclerView(R.id.rv_product_list)
                .atPositionOnView(index, R.id.btn_add_to_cart)
        ).perform(ViewActions.click())
    }

    fun checkAddSucceededMessageIsVisible() {
        onSnackbar(R.string.label_add_successful)
            .check(matches(isDisplayed()))
        onSnackbarButton(R.string.action_undo)
            .check(matches(isDisplayed()))
    }

    fun checkCartIconBadgeText(text: String) {
        onView(withId(R.id.tv_menu_action_cart_count))
            .check(matches(withText(text)))
    }

    fun openCart() {
        onView(withId(R.id.iv_menu_action_cart)).perform(ViewActions.click())
    }

    fun checkProductListIsNotVisible() {
        onView(withId(R.id.rv_product_list))
            .check(ViewAssertions.doesNotExist())
    }

    fun clickOnItem(index: Int) {
        onView(withId(R.id.rv_product_list))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(index))
        onView(
            withRecyclerView(R.id.rv_product_list)
                .atPosition(index)
        ).perform(ViewActions.click())
    }

    fun checkCartIsVisible() {
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

    fun checkProductPageIsVisible(productName: String) {
        onView(withId(R.id.toolbar))
            .check(
                matches(
                    hasDescendant(
                        withText(productName)
                    )
                )
            )
        onView(withId(R.id.tv_name))
            .check(matches(isDisplayed()))
        onView(withId(R.id.tv_info))
            .check(matches(isDisplayed()))
    }

    fun scrollListTo(index: Int) {
        onView(withId(R.id.rv_product_list))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(index))
    }

}