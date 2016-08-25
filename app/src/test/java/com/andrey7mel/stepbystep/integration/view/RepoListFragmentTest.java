package com.andrey7mel.stepbystep.integration.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.andrey7mel.stepbystep.R;
import com.andrey7mel.stepbystep.integration.other.IntegrationBaseTest;
import com.andrey7mel.stepbystep.other.TestConst;
import com.andrey7mel.stepbystep.presenter.vo.Repository;
import com.andrey7mel.stepbystep.view.MainActivity;
import com.andrey7mel.stepbystep.view.fragments.RepoListFragment;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.Robolectric;

import java.util.List;

import javax.inject.Inject;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class RepoListFragmentTest extends IntegrationBaseTest {

    @Inject
    protected List<Repository> repoList;

    private RepoListFragment repoListFragment;

    private MainActivity activity;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        component.inject(this);

        repoListFragment = spy(new RepoListFragment());
        activity = Robolectric.setupActivity(MainActivity.class);
        repoListFragment.onAttach((Context) activity);
        doAnswer(invocation -> TestConst.TEST_OWNER)
                .when(repoListFragment)
                .getUserName();
    }

    @Test
    public void testOnCreate() {
        repoListFragment.onCreate(null);
        repoListFragment.onCreateView(LayoutInflater.from(activity), (ViewGroup) activity.findViewById(R.id.container), null);
        repoListFragment.onClickSearch(null);
        repoListFragment.onStop();
        verify(repoListFragment).showRepoList(repoList);
    }

    @Test
    public void testOnCreateWithError() {
        setErrorAnswerWebServer();
        repoListFragment.onCreate(null);
        repoListFragment.onCreateView(LayoutInflater.from(activity), (ViewGroup) activity.findViewById(R.id.container), null);
        repoListFragment.onClickSearch(null);
        repoListFragment.onStop();
        verify(repoListFragment).showError(TestConst.ERROR_RESPONSE_500);
    }

}