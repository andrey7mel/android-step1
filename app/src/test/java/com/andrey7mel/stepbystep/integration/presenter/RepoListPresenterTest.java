package com.andrey7mel.stepbystep.integration.presenter;

import android.os.Bundle;

import com.andrey7mel.stepbystep.integration.other.IntegrationBaseTest;
import com.andrey7mel.stepbystep.model.Model;
import com.andrey7mel.stepbystep.other.TestConst;
import com.andrey7mel.stepbystep.presenter.RepoListPresenter;
import com.andrey7mel.stepbystep.presenter.mappers.RepoListMapper;
import com.andrey7mel.stepbystep.presenter.vo.Repository;
import com.andrey7mel.stepbystep.view.fragments.RepoListView;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import javax.inject.Inject;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class RepoListPresenterTest extends IntegrationBaseTest {

    @Inject
    protected RepoListMapper repoListMapper;

    @Inject
    protected Model model;

    @Inject
    protected List<Repository> repoList;

    private RepoListView mockView;
    private RepoListPresenter repoListPresenter;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        component.inject(this);

        mockView = mock(RepoListView.class);
        doAnswer(invocation -> TestConst.TEST_OWNER)
                .when(mockView)
                .getUserName();
        repoListPresenter = new RepoListPresenter(mockView);
    }


    @Test
    public void testLoadData() {
        repoListPresenter.onCreateView(null);
        repoListPresenter.onSearchButtonClick();
        repoListPresenter.onStop();

        verify(mockView).showRepoList(repoList);
    }


    @Test
    public void testEmptyName() {
        doAnswer(invocation -> "")
                .when(mockView)
                .getUserName();

        repoListPresenter.onSearchButtonClick();

        verify(mockView, never()).showEmptyList();
        verify(mockView, never()).showRepoList(repoList);

    }

    @Test
    public void testClickRepo() {
        Repository repository = new Repository(TestConst.TEST_REPO, TestConst.TEST_OWNER);

        repoListPresenter.clickRepo(repository);

        verify(mockView).startRepoInfoFragment(repository);
    }

    @Test
    public void testLoadDataWithError() {
        setErrorAnswerWebServer();
        repoListPresenter.onCreateView(null);
        repoListPresenter.onSearchButtonClick();
        repoListPresenter.onStop();

        verify(mockView).showError(TestConst.ERROR_RESPONSE_500);
    }


    @Test
    public void testSaveState() {
        repoListPresenter.onCreateView(null);
        repoListPresenter.onSearchButtonClick();

        Bundle bundle = new Bundle();
        repoListPresenter.onSaveInstanceState(bundle);
        repoListPresenter.onStop();

        repoListPresenter.onCreateView(bundle);

        verify(mockView, times(2)).showRepoList(repoList);
    }


    @Test
    public void testLoadingState() {
        repoListPresenter.onCreateView(null);
        repoListPresenter.onSearchButtonClick();
        repoListPresenter.onStop();

        verify(mockView).showLoading();
        verify(mockView).hideLoading();
    }

    @Test
    public void testOnErrorLoadingState() {
        setErrorAnswerWebServer();
        repoListPresenter.onSearchButtonClick();

        verify(mockView).showLoading();
        verify(mockView).hideLoading();
    }
}