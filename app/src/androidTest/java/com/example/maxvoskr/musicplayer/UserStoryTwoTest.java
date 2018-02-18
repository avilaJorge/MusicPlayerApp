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
public class UserStoryTwoTest {

    private static Song song;
    private static Album album;

    @Rule
    public ActivityTestRule<LoadingActivity> mActivityTestRule = new ActivityTestRule<>(LoadingActivity.class);


    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

    @Before
    public void before(){

        album = albumList.get(0);
        song = album.getMusicList().get(0);

    }



    @Test
    public void userViewsTracksInAnAlbum() {

        /*
            Given: app is open
            And the user is in playback mode
            And the user is on the Album menu
            When "Hello World: The Album" is selected
            Then all songs in the album will be listed in a new page
            And the user can then select songs from that page to play

        */

        onView(withId(R.id.albumMode)).perform(click());


        // Select first album
        onData(anything()).inAdapterView(withId(R.id.albumListDisplay))
                .atPosition(0)
                .perform(click());


        onView(withText(album.getAlbumName())).check(matches(isDisplayed()));

        // Select first song of album
        onData(anything()).inAdapterView(withId(R.id.trackList))
                .atPosition(0)
                .perform(click());

        // check that we have switched
        onView(withText(song.getName())).check(matches(isDisplayed()));

    }

    @Test
    public void userSelectsAlbumToPlay(){

/*
        Given: The App is open
        And the User is in playback mode
        And the User is on the album menu
        When "Hello World: The Album" is selected
        Then the songs in the Album are listed along with a play album button
        When the user selects the play album button
        Then songs begin to play in order on the album
*/

        onView(withId(R.id.albumMode)).perform(click());


        // Select first album
        onData(anything()).inAdapterView(withId(R.id.albumListDisplay))
                .atPosition(0)
                .perform(click());


        onView(withText(album.getAlbumName())).check(matches(isDisplayed()));

        // Select first song of album
        onData(anything()).inAdapterView(withId(R.id.trackList))
                .atPosition(0)
                .perform(click());

        // check that we have switched
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
