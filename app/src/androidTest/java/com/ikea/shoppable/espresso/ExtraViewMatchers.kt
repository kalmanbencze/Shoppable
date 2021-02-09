package com.ikea.shoppable.espresso

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description

object ExtraViewMatchers {
    fun recyclerViewContainsAtLeast(expectedItemsCount: Int): BoundedMatcher<View?, RecyclerView> {
        return object : BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has at least $expectedItemsCount items")
            }

            public override fun matchesSafely(recyclerView: RecyclerView): Boolean {
                val adapter = recyclerView.adapter!!
                return adapter.itemCount >= expectedItemsCount
            }
        }
    }

    fun recyclerViewContains(expectedItemsCount: Int): BoundedMatcher<View?, RecyclerView> {
        return object : BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has at least $expectedItemsCount items")
            }

            public override fun matchesSafely(recyclerView: RecyclerView): Boolean {
                val adapter = recyclerView.adapter!!
                return adapter.itemCount == expectedItemsCount
            }
        }
    }
}