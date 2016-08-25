package com.andrey7mel.stepbystep.integration.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.andrey7mel.stepbystep.R;
import com.andrey7mel.stepbystep.integration.other.IntegrationBaseTest;
import com.andrey7mel.stepbystep.other.TestConst;
import com.andrey7mel.stepbystep.presenter.vo.Branch;
import com.andrey7mel.stepbystep.presenter.vo.Contributor;
import com.andrey7mel.stepbystep.presenter.vo.Repository;
import com.andrey7mel.stepbystep.view.MainActivity;
import com.andrey7mel.stepbystep.view.fragments.RepoInfoFragment;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.Robolectric;

import java.util.List;

import javax.inject.Inject;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class RepoInfoFragmentTest extends IntegrationBaseTest {

    @Inject
    protected List<Contributor> contributorList;

    @Inject
    protected List<Branch> branchList;

    @Inject
    protected Repository repository;

    private RepoInfoFragment repoInfoFragment;
    private MainActivity activity;


    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        component.inject(this);
        repoInfoFragment = spy(RepoInfoFragment.newInstance(repository));
        activity = Robolectric.setupActivity(MainActivity.class);
        repoInfoFragment.onAttach((Context) activity);
    }


    @Test
    public void testLoadData() {
        repoInfoFragment.onCreate(null);
        repoInfoFragment.onCreateView(LayoutInflater.from(activity), (ViewGroup) activity.findViewById(R.id.container), null);
        verify(repoInfoFragment).showBranches(branchList);
        verify(repoInfoFragment).showContributors(contributorList);

    }


    @Test
    public void testLoadDataWithError() {
        setErrorAnswerWebServer();
        repoInfoFragment.onCreate(null);
        repoInfoFragment.onCreateView(LayoutInflater.from(activity), (ViewGroup) activity.findViewById(R.id.container), null);
        verify(repoInfoFragment, times(2)).showError(TestConst.ERROR_RESPONSE_500);
    }
}