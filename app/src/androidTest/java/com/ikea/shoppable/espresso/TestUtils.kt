package com.ikea.shoppable.espresso

import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.util.HumanReadables
import org.hamcrest.Matcher
import org.hamcrest.Matchers

object TestUtils {
    fun actionOnItemViewAtPosition(
        position: Int,
        @IdRes viewId: Int,
        viewAction: ViewAction
    ): ViewAction {
        return ActionOnItemViewAtPositionViewAction(position, viewId, viewAction)
    }

    fun withRecyclerView(recyclerViewId: Int): RecyclerViewMatcher {
        return RecyclerViewMatcher(recyclerViewId)
    }

    private class ActionOnItemViewAtPositionViewAction(
        private val position: Int,
        @param:IdRes private val viewId: Int,
        private val viewAction: ViewAction
    ) : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return Matchers.allOf(
                ViewMatchers.isAssignableFrom(RecyclerView::class.java), ViewMatchers.isDisplayed()
            )
        }

        override fun getDescription(): String {
            return ("actionOnItemAtPosition performing ViewAction: "
                + viewAction.description
                + " on item at position: "
                + position)
        }

        override fun perform(uiController: UiController, view: View) {
            val recyclerView = view as RecyclerView
            ScrollToPositionViewAction(position).perform(uiController, view)
            uiController.loopMainThreadUntilIdle()
            val targetView = recyclerView.getChildAt(position).findViewById<View>(viewId)
            if (targetView == null) {
                throw PerformException.Builder().withActionDescription(this.toString())
                    .withViewDescription(
                        HumanReadables.describe(view)
                    )
                    .withCause(
                        IllegalStateException(
                            "No view with id "
                                + viewId
                                + " found at position: "
                                + position
                        )
                    )
                    .build()
            } else {
                viewAction.perform(uiController, targetView)
            }
        }
    }

    private class ScrollToPositionViewAction(private val position: Int) : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return Matchers.allOf(
                ViewMatchers.isAssignableFrom(RecyclerView::class.java), ViewMatchers.isDisplayed()
            )
        }

        override fun getDescription(): String {
            return "scroll RecyclerView to position: $position"
        }

        override fun perform(uiController: UiController, view: View) {
            val recyclerView = view as RecyclerView
            recyclerView.scrollToPosition(position)
        }
    }
}