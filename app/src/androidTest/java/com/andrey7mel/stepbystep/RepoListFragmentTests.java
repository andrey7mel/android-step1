package com.andrey7mel.stepbystep;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.andrey7mel.stepbystep.di.TestComponent;
import com.andrey7mel.stepbystep.other.App;
import com.andrey7mel.stepbystep.tools.ApiConfig;
import com.andrey7mel.stepbystep.tools.EspressoTools;
import com.andrey7mel.stepbystep.tools.TestConst;
import com.andrey7mel.stepbystep.view.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class RepoListFragmentTests {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Inject
    ApiConfig apiConfig;

    @Before
    public void setUp() {
        Espresso.registerIdlingResources(
                mActivityRule.getActivity().getCountingIdlingResource());
        ((TestComponent) App.getComponent()).inject(this);
    }

    @Test
    public void testElementsDisplayed() {
        onView(withId(R.id.button_search)).check(matches(isDisplayed()));
        onView(withId(R.id.edit_text)).check(matches(isDisplayed()));
    }


    @Test
    public void testGetUserRepo() {
        apiConfig.setCorrectAnswer();
        enterOwner();

        onView(withId(R.id.recycler_view)).check(EspressoTools.hasItemsCount(7));

        onView(withId(R.id.recycler_view)).check(EspressoTools.hasViewWithTextAtPosition(0, "Android-Rate"));
        onView(withId(R.id.recycler_view)).check(EspressoTools.hasViewWithTextAtPosition(1, "android-simple-architecture"));
        onView(withId(R.id.recycler_view)).check(EspressoTools.hasViewWithTextAtPosition(2, TestConst.TEST_REPO));
    }

    @Test
    public void testGetUserRepoError() {
        apiConfig.setErrorAnswer();
        enterOwner();

        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(TestConst.TEST_ERROR)))
                .check(matches(isDisplayed()));

        onView(withId(R.id.recycler_view)).check(EspressoTools.hasItemsCount(0));
    }

    @Test
    public void testHideProgressBar() {
        apiConfig.setCorrectAnswer();
        enterOwner();

        onView(withId(R.id.toolbar_progress_bar)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
    }


    @Test
    public void testHideProgressBarOnError() {
        apiConfig.setErrorAnswer();
        enterOwner();

        onView(withId(R.id.toolbar_progress_bar)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
    }

    private void enterOwner() {
        onView(withId(R.id.edit_text)).perform(clearText());
        onView(withId(R.id.edit_text)).perform(typeText(TestConst.TEST_OWNER));
        onView(withId(R.id.button_search)).perform(click());
    }
}
