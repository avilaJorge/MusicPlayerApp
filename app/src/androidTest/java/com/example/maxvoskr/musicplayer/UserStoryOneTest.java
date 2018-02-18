package com.example.maxvoskr.musicplayer;


import android.Manifest;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withResourceName;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class UserStoryOneTest {

    @Rule
    public ActivityTestRule<LoadingActivity> mActivityTestRule = new ActivityTestRule<>(LoadingActivity.class);


    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

    @Test
    public void UserSelectsATrackToPlay() {

        /*
            Given: app is open
            And the user is in playback mode
            And the user is on the song menu
            When "Hello World!" is selected
            Then  the song playing page will open
            And the song will play
            And the user can then select to change to another song or listen till the end

        */

        // App should be open, check for song list mode and click first song
        onData(anything()).inAdapterView(withId(R.id.trackList))
                .atPosition(0)
                .perform(click());

        // check that we have switched
        onData(allOf(is(instanceOf(String.class)), is("Last Played:")));

        //



        // Perform switch to flashBack mode
        //onView(withId(R.id.flashbackMode)).perform(click());

        // Check for Last played object
        //onData(allOf(is(instanceOf(String.class)), is("Last Played:")));

    }


    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
