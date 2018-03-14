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
import static com.example.maxvoskr.musicplayer.MusicArrayList.musicList;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class UserStoryFourTest {

    private Song songOne;
    private static final String NO_LOC_MSG = "Song has not been played before";

    @Rule
    public ActivityTestRule<LoadingActivity> mActivityTestRule = new ActivityTestRule<>(LoadingActivity.class);


    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

    @Before
    public void before(){

        songOne = musicList.get(0);

    }


    @Test
    public void viewLastPlayedInfoForSongNotPlayedTest() {


        // TODO need to reset state of song to completely unplayed
        songOne.setLastLocation("");
        songOne.unsetPlayed();
        songOne.setTimeMS(0);

        onData(anything()).inAdapterView(withId(R.id.trackList)).atPosition(0).perform(click());

        onView(withText(NO_LOC_MSG)).check(matches(isDisplayed()));

    }

    @Test
    public void viewLastPlayedInfoForSongInPlaybackModeTest() {

        Song songTwo = musicList.get(1);
        songTwo.setTimeMS(0);
        songTwo.unsetPlayed();

        onData(anything()).inAdapterView(withId(R.id.trackList)).atPosition(1).perform(click());

        onView(withText(NO_LOC_MSG)).check(matches(isDisplayed()));

        onView(withId(R.id.songsMode)).perform(click());

        songTwo.setLastLocation("Price Center");
        songTwo.setPlayed();
        songTwo.setDayOfWeek(1);
        songTwo.setTimeOfDay(1);
        songTwo.setTimeMS(43200000);

        onData(anything()).inAdapterView(withId(R.id.trackList)).atPosition(1).perform(click());

        onView(withText("Price Center")).check(matches(isDisplayed()));


    }

    @Test
    public void viewLastPlayedInfoForSongInFlashbackModeTest() {

        songOne.setLastLocation("Price Center");
        songOne.setPlayed();
        songOne.setDayOfWeek(1);
        songOne.setTimeOfDay(1);
        songOne.setTimeMS(43200000);

        onView(withId(R.id.vibeMode)).perform(click());

        onView(withText("Price Center")).check(matches(isDisplayed()));

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
