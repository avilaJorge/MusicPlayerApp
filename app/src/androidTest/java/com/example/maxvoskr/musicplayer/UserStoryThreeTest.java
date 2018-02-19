package com.example.maxvoskr.musicplayer;


import android.Manifest;
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
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.example.maxvoskr.musicplayer.MusicArrayList.albumList;
import static com.example.maxvoskr.musicplayer.MusicArrayList.musicList;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;


@LargeTest
@RunWith(AndroidJUnit4.class)
public class UserStoryThreeTest {

    private Song song;

    @Rule
    public ActivityTestRule<LoadingActivity> mActivityTestRule = new ActivityTestRule<>(LoadingActivity.class);


    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

    @Before
    public void before(){

        song = musicList.get(0);

    }



    @Test
    public void UserNavigatesFromPlaybackToFlashBack() {

        /*
            Given: The user is in the Playback Activity
            When: The user taps the flashback button at the top of the UI
            Then:  The activity will change into Flashback mode
            And the next songs to be played are now automatically selected for play based on priorities

        */

        onView(withId(R.id.flashbackMode)).perform(click());

        onView(withText("Last Played:")).check(matches(isDisplayed()));

    }

    @Test
    public void UserNavigatesFromFlashbackToPlayback() {

        onView(withId(R.id.flashbackMode)).perform(click());

        onView(withText("Last Played:")).check(matches(isDisplayed()));

        onView(withId(R.id.songsMode)).perform(click());

        onData(anything()).inAdapterView(withId(R.id.trackList))
                .atPosition(0)
                .perform(click());

        onView(withText(song.getName())).check(matches(isDisplayed()));
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
