package com.andrey7mel.stepbystep;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
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
public class RepoInfoFragmentTests {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Inject
    protected ApiConfig apiConfig;

    @Before
    public void setUp() {
        Espresso.registerIdlingResources(
                mActivityRule.getActivity().getCountingIdlingResource());
        ((TestComponent) App.getComponent()).inject(this);
        apiConfig.setCorrectAnswer();
        onView(withId(R.id.edit_text)).perform(clearText());
        onView(withId(R.id.edit_text)).perform(typeText(TestConst.TEST_OWNER));
        onView(withId(R.id.button_search)).perform(click());
    }

    private void clickRepo() {
        onView(withId(R.id.recycler_view)).perform(
                RecyclerViewActions.actionOnItemAtPosition(2, click()));
    }

    @Test
    public void testElementDisplayed() {
        clickRepo();

        //check view items
        onView(withId(R.id.repo_info))
                .check(matches(isDisplayed()))
                .check(matches(withText(TestConst.TEST_REPO + " (" + TestConst.TEST_OWNER + ")")));
        onView(withId(R.id.branches_title))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.branches)));
        onView(withId(R.id.contributors_title))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.contributors)));
        onView(withId(R.id.recycler_view_branches)).check(matches(isDisplayed()));
        onView(withId(R.id.recycler_view_contributors)).check(matches(isDisplayed()));

        //check RecyclerView count
        onView(withId(R.id.recycler_view_branches)).check(EspressoTools.hasItemsCount(3));
        onView(withId(R.id.recycler_view_contributors)).check(EspressoTools.hasItemsCount(11));

        //check RecyclerView items
        onView(withId(R.id.recycler_view_branches)).check(EspressoTools.hasViewWithTextAtPosition(0, "QuickStart"));
        onView(withId(R.id.recycler_view_branches)).check(EspressoTools.hasViewWithTextAtPosition(1, "gh-pages"));
        onView(withId(R.id.recycler_view_branches)).check(EspressoTools.hasViewWithTextAtPosition(2, "master"));

        onView(withId(R.id.recycler_view_contributors)).check(EspressoTools.hasViewWithTextAtPosition(0, "hotchemi"));
        onView(withId(R.id.recycler_view_contributors)).check(EspressoTools.hasViewWithTextAtPosition(1, "mrmike"));
        onView(withId(R.id.recycler_view_contributors)).check(EspressoTools.hasViewWithTextAtPosition(2, "amitkot"));
    }


    @Test
    public void testBranches() {
        clickRepo();

        onView(withId(R.id.recycler_view_branches)).check(EspressoTools.hasItemsCount(3));

        onView(withId(R.id.recycler_view_branches)).check(EspressoTools.hasViewWithTextAtPosition(0, "QuickStart"));
        onView(withId(R.id.recycler_view_branches)).check(EspressoTools.hasViewWithTextAtPosition(1, "gh-pages"));
        onView(withId(R.id.recycler_view_branches)).check(EspressoTools.hasViewWithTextAtPosition(2, "master"));
    }

    @Test
    public void testContributors() {
        clickRepo();

        onView(withId(R.id.recycler_view_contributors)).check(EspressoTools.hasItemsCount(11));

        onView(withId(R.id.recycler_view_contributors)).check(EspressoTools.hasViewWithTextAtPosition(0, "hotchemi"));
        onView(withId(R.id.recycler_view_contributors)).check(EspressoTools.hasViewWithTextAtPosition(1, "mrmike"));
        onView(withId(R.id.recycler_view_contributors)).check(EspressoTools.hasViewWithTextAtPosition(2, "amitkot"));
    }


    @Test
    public void testClickUserError() {
        apiConfig.setErrorAnswer();
        clickRepo();

        //check view items
        onView(withId(R.id.repo_info))
                .check(matches(isDisplayed()))
                .check(matches(withText(TestConst.TEST_REPO + " (" + TestConst.TEST_OWNER + ")")));
        onView(withId(R.id.branches_title))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.branches)));
        onView(withId(R.id.contributors_title))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.contributors)));
        //onView(withId(R.id.recycler_view_branches)).check(matches(isDisplayed()));
        //onView(withId(R.id.recycler_view_contributors)).check(matches(isDisplayed()));

        //check RecyclerView count
        onView(withId(R.id.recycler_view_branches)).check(EspressoTools.hasItemsCount(0));
        onView(withId(R.id.recycler_view_contributors)).check(EspressoTools.hasItemsCount(0));


        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(TestConst.TEST_ERROR)))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testClickUserContributorsError() {
        apiConfig.setCustomAnswer(true, false);
        clickRepo();

        //check view items
        onView(withId(R.id.repo_info))
                .check(matches(isDisplayed()))
                .check(matches(withText(TestConst.TEST_REPO + " (" + TestConst.TEST_OWNER + ")")));
        onView(withId(R.id.branches_title))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.branches)));
        onView(withId(R.id.contributors_title))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.contributors)));
        onView(withId(R.id.recycler_view_branches)).check(matches(isDisplayed()));
        //onView(withId(R.id.recycler_view_contributors)).check(matches(isDisplayed()));

        //check RecyclerView count
        onView(withId(R.id.recycler_view_branches)).check(EspressoTools.hasItemsCount(3));
        onView(withId(R.id.recycler_view_contributors)).check(EspressoTools.hasItemsCount(0));

        onView(withId(R.id.recycler_view_branches)).check(EspressoTools.hasViewWithTextAtPosition(0, "QuickStart"));
        onView(withId(R.id.recycler_view_branches)).check(EspressoTools.hasViewWithTextAtPosition(1, "gh-pages"));
        onView(withId(R.id.recycler_view_branches)).check(EspressoTools.hasViewWithTextAtPosition(2, "master"));

        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(TestConst.TEST_ERROR)))
                .check(matches(isDisplayed()));
    }


    @Test
    public void testClickUserBranchesError() {
        apiConfig.setCustomAnswer(false, true);
        clickRepo();

        //check view items
        onView(withId(R.id.repo_info))
                .check(matches(isDisplayed()))
                .check(matches(withText(TestConst.TEST_REPO + " (" + TestConst.TEST_OWNER + ")")));
        onView(withId(R.id.branches_title))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.branches)));
        onView(withId(R.id.contributors_title))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.contributors)));
        //onView(withId(R.id.recycler_view_branches)).check(matches(isDisplayed()));
        onView(withId(R.id.recycler_view_contributors)).check(matches(isDisplayed()));

        //check RecyclerView count
        onView(withId(R.id.recycler_view_branches)).check(EspressoTools.hasItemsCount(0));
        onView(withId(R.id.recycler_view_contributors)).check(EspressoTools.hasItemsCount(11));

        onView(withId(R.id.recycler_view_contributors)).check(EspressoTools.hasViewWithTextAtPosition(0, "hotchemi"));
        onView(withId(R.id.recycler_view_contributors)).check(EspressoTools.hasViewWithTextAtPosition(1, "mrmike"));
        onView(withId(R.id.recycler_view_contributors)).check(EspressoTools.hasViewWithTextAtPosition(2, "amitkot"));

        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(TestConst.TEST_ERROR)))
                .check(matches(isDisplayed()));
    }


    @Test
    public void testHideProgressBar() {
        onView(withId(R.id.recycler_view)).perform(
                RecyclerViewActions.actionOnItemAtPosition(2, click()));

        onView(withId(R.id.toolbar_progress_bar)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
    }

    @Test
    public void testHideProgressBarOnError() {
        apiConfig.setErrorAnswer();

        onView(withId(R.id.recycler_view)).perform(
                RecyclerViewActions.actionOnItemAtPosition(2, click()));

        onView(withId(R.id.toolbar_progress_bar)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
    }

    @Test
    public void testHideProgressBarOnErrorBranches() {
        apiConfig.setCustomAnswer(false, true);

        onView(withId(R.id.recycler_view)).perform(
                RecyclerViewActions.actionOnItemAtPosition(2, click()));

        onView(withId(R.id.toolbar_progress_bar)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
    }

    @Test
    public void testHideProgressBarOnErrorContributors() {
        apiConfig.setCustomAnswer(true, false);

        onView(withId(R.id.recycler_view)).perform(
                RecyclerViewActions.actionOnItemAtPosition(2, click()));

        onView(withId(R.id.toolbar_progress_bar)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
    }

}
