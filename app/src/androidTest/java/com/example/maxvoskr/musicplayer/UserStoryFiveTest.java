package com.example.maxvoskr.musicplayer;


import android.Manifest;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
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
import static com.example.maxvoskr.musicplayer.LoadingActivity.currentLocationTimeData;
import static com.example.maxvoskr.musicplayer.MusicArrayList.musicList;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class UserStoryFiveTest {

    private Context context;
    private Song songOne, songTwo;

    @Rule
    public ActivityTestRule<LoadingActivity> mActivityTestRule = new ActivityTestRule<>(LoadingActivity.class);

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

   @Before
    public void before(){

        context = InstrumentationRegistry.getTargetContext();

        songOne = musicList.get(0);
        songTwo = musicList.get(1);

    }



    @Test
    public void higherFlashbackPriorityBasedOnLocationTest() {

        currentLocationTimeData.setTestCurrentLocationTimeData("My House", 1, 1, 43200000);

        songOne.setLocation("My house");
        songOne.setDayOfWeek(1);
        songOne.setPlayed();
        songOne.setTimeOfDay(1);
        songOne.setTimeMS(43200000);

        songTwo.setTimeOfDay(1);
        songTwo.setTimeMS(43200000);
        songTwo.setDayOfWeek(1);
        songTwo.setPlayed();
        songTwo.setLocation("Price Center");

        onView(withId(R.id.vibeMode)).perform(click());

        onView(withText(songOne.getName())).check(matches(isDisplayed()));


    }

    @Test
    public void higherFlashbackPriorityBasedOnTimeTest() {

        currentLocationTimeData.setTestCurrentLocationTimeData("My House", 1, 1, 43200000);

        songOne.setLocation("My house");
        songOne.setDayOfWeek(1);
        songOne.setPlayed();
        songOne.setTimeOfDay(1);
        songOne.setTimeMS(14400000);

        songTwo.setTimeOfDay(1);
        songTwo.setTimeMS(43200000);
        songTwo.setDayOfWeek(1);
        songTwo.setPlayed();
        songTwo.setLocation("My House");

        onView(withId(R.id.vibeMode)).perform(click());

        onView(withText(songTwo.getName())).check(matches(isDisplayed()));


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
