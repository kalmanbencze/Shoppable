package com.ikea.shoppable.espresso;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.CoordinatesProvider;
import androidx.test.espresso.action.GeneralClickAction;
import androidx.test.espresso.action.GeneralLocation;
import androidx.test.espresso.action.GeneralSwipeAction;
import androidx.test.espresso.action.Press;
import androidx.test.espresso.action.Swipe;
import androidx.test.espresso.action.Tap;
import androidx.test.espresso.util.EspressoOptional;
import androidx.test.espresso.util.HumanReadables;
import androidx.test.espresso.util.TreeIterables;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import static androidx.test.espresso.action.ViewActions.actionWithAssertions;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.anything;

public class ViewActions {

    /**
     * ViewAction that waits a number of milliseconds for the given matcher to match the selected view.
     */
    public static ViewAction waitMatcher(final Matcher<View> matcher, final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return (Matcher) anything();
            }

            @Override
            public String getDescription() {
                final StringDescription matcherDescription = new StringDescription();
                matcher.describeTo(matcherDescription);
                return "Wait at most " + millis + " milliseconds for " + matcherDescription;
            }

            @Override
            public void perform(final UiController uiController, final View view) {
                uiController.loopMainThreadUntilIdle();
                final long startTime = System.currentTimeMillis();
                final long endTime = startTime + millis;

                do {
                    if (matcher.matches(view)) {
                        return;
                    }

                    uiController.loopMainThreadForAtLeast(50);
                }
                while (System.currentTimeMillis() < endTime);

                // timeout happens
                throw new PerformException.Builder()
                        .withActionDescription(this.getDescription())
                        .withViewDescription(HumanReadables.describe(view))
                        .withCause(new TimeoutException())
                        .build();
            }
        };
    }

    public static ViewAction waitId(final int viewId, final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "wait for a specific view with id <" + viewId + "> during " + millis + " millis.";
            }

            @Override
            public void perform(final UiController uiController, final View view) {
                uiController.loopMainThreadUntilIdle();
                final long startTime = System.currentTimeMillis();
                final long endTime = startTime + millis;
                final Matcher<View> viewMatcher = withId(viewId);

                do {
                    for (final View child : TreeIterables.breadthFirstViewTraversal(view)) {
                        // found view with required ID
                        if (viewMatcher.matches(child)) {
                            return;
                        }
                    }

                    uiController.loopMainThreadForAtLeast(50);
                }
                while (System.currentTimeMillis() < endTime);

                // timeout happens
                throw new PerformException.Builder()
                        .withActionDescription(this.getDescription())
                        .withViewDescription(HumanReadables.describe(view))
                        .withCause(new TimeoutException())
                        .build();
            }
        };
    }

    public static ViewAction waitUntilCompletelyDisplayed(final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return (Matcher) anything();
            }

            @Override
            public String getDescription() {
                return "Wait " + millis + " milliseconds for the view to be completely displayed.";
            }

            @Override
            public void perform(final UiController uiController, final View view) {
                uiController.loopMainThreadUntilIdle();
                final long startTime = System.currentTimeMillis();
                final long endTime = startTime + millis;
                final Matcher<View> viewCompletelyDisplayedMatcher = isCompletelyDisplayed();

                do {
                    if (viewCompletelyDisplayedMatcher.matches(view)) {
                        return;
                    }

                    uiController.loopMainThreadForAtLeast(50);
                }
                while (System.currentTimeMillis() < endTime);
                // timeout happens
                throw new PerformException.Builder()
                        .withActionDescription(this.getDescription())
                        .withViewDescription(HumanReadables.describe(view.getRootView()))
                        .withCause(new NoMatchingViewException.Builder()
                                .withViewMatcher(getConstraints())
                                .withRootView(view.getRootView())
                                .withAdapterViews(new ArrayList<>())
                                .withAdapterViewWarning(EspressoOptional.absent())
                                .includeViewHierarchy(true)
                                .build())
                        .build();
            }
        };
    }

    public static ViewAction swipeLeftSlow() {
        return actionWithAssertions(new GeneralSwipeAction(Swipe.SLOW, GeneralLocation.CENTER_RIGHT, GeneralLocation.CENTER_LEFT, Press.FINGER));
    }

    public static ViewAction clickChildViewWithId(final int id) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return "Click on a child view with specified id.";
            }

            @Override
            public void perform(final UiController uiController, final View view) {
                final View v = view.findViewById(id);
                v.performClick();
            }
        };
    }

    @NonNull
    public static ViewAction clickXY(final int x, final int y) {
        return new GeneralClickAction(
                Tap.SINGLE,
                getCoordinatesProvider(x, y),
                Press.FINGER);
    }

    @NonNull
    public static ViewAction longClickXY(final int x, final int y) {
        return new GeneralClickAction(
                Tap.LONG,
                getCoordinatesProvider(x, y),
                Press.FINGER);
    }

    @NonNull
    private static CoordinatesProvider getCoordinatesProvider(final int x, final int y) {
        return view -> {
            final int[] screenPos = new int[2];
            view.getLocationOnScreen(screenPos);

            final float screenX = screenPos[0] + x;
            final float screenY = screenPos[1] + y;
            return new float[]{screenX, screenY};
        };
    }
}