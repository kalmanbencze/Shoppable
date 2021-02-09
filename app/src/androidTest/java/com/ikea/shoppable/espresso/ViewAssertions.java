package com.ikea.shoppable.espresso;

import androidx.test.espresso.ViewAssertion;

import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

public class ViewAssertions {

    /* isNotVisible checks that the found view is not visible. This is the case if there is no view, or
       the view is not displayed.

       Be aware that this assertion will succeed if the ViewMatcher used did not find a view.
     */
    public static ViewAssertion isNotVisible() {
        return (view, noViewFoundException) -> {
            if (view != null) {
                if (isDisplayed().matches(view)) {
                    throw new AssertionError("View is visible");
                }
            }
        };
    }
}