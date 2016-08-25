package com.andrey7mel.stepbystep.view.fragments;

import android.content.Context;

import com.andrey7mel.stepbystep.BuildConfig;
import com.andrey7mel.stepbystep.other.BaseTest;
import com.andrey7mel.stepbystep.presenter.BasePresenter;
import com.andrey7mel.stepbystep.presenter.vo.Repository;
import com.andrey7mel.stepbystep.view.MainActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import javax.inject.Inject;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class BaseFragmentTest extends BaseTest {

    @Inject
    protected Repository repository;

    private BaseFragment baseFragment;
    private MainActivity activity;
    private BasePresenter basePresenter;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        component.inject(this);

        activity = mock(MainActivity.class);

        RepoInfoFragment repoInfoFragment = RepoInfoFragment.newInstance(repository);
        baseFragment = repoInfoFragment;
        baseFragment.onCreate(null); //for Di
        baseFragment.onAttach((Context) activity); //for link activity

        basePresenter = repoInfoFragment.presenter;
    }

    @Test
    public void testAttachActivityCallback() throws Exception {
        assertNotNull(baseFragment.activityCallback);
    }

    @Test
    public void testShowLoadingState() throws Exception {
        baseFragment.showLoading();
        verify(activity).showProgressBar();
    }

    @Test
    public void testHideLoadingState() throws Exception {
        baseFragment.hideLoading();
        verify(activity).hideProgressBar();
    }

    @Test
    public void testOnStop() {
        baseFragment.onStop();
        verify(basePresenter).onStop();
    }
}