package com.andrey7mel.stepbystep.integration.presenter;

import android.os.Bundle;

import com.andrey7mel.stepbystep.integration.other.IntegrationBaseTest;
import com.andrey7mel.stepbystep.model.Model;
import com.andrey7mel.stepbystep.other.TestConst;
import com.andrey7mel.stepbystep.presenter.RepoInfoPresenter;
import com.andrey7mel.stepbystep.presenter.mappers.RepoBranchesMapper;
import com.andrey7mel.stepbystep.presenter.mappers.RepoContributorsMapper;
import com.andrey7mel.stepbystep.presenter.vo.Branch;
import com.andrey7mel.stepbystep.presenter.vo.Contributor;
import com.andrey7mel.stepbystep.presenter.vo.Repository;
import com.andrey7mel.stepbystep.view.fragments.RepoInfoView;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import javax.inject.Inject;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class RepoInfoPresenterTest extends IntegrationBaseTest {

    @Inject
    protected List<Contributor> contributorList;

    @Inject
    protected List<Branch> branchList;

    @Inject
    protected RepoBranchesMapper branchesMapper;

    @Inject
    protected RepoContributorsMapper contributorsMapper;

    @Inject
    protected Repository repository;

    @Inject
    protected Model model;

    private RepoInfoView mockView;
    private RepoInfoPresenter repoInfoPresenter;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        component.inject(this);

        mockView = mock(RepoInfoView.class);
        repoInfoPresenter = new RepoInfoPresenter();
        repoInfoPresenter.onCreate(mockView, repository);
    }


    @Test
    public void testLoadData() {
        repoInfoPresenter.onCreateView(null);
        repoInfoPresenter.onStop();


        verify(mockView).showBranches(branchList);
        verify(mockView).showContributors(contributorList);
    }

    @Test
    public void testLoadDataWithError() {
        setErrorAnswerWebServer();

        repoInfoPresenter.onCreateView(null);
        repoInfoPresenter.onStop();

        verify(mockView, times(2)).showError(TestConst.ERROR_RESPONSE_500);
    }

    @Test
    public void testOnErrorBranches() {
        setCustomAnswer(false, true);

        repoInfoPresenter.onCreateView(null);

        verify(mockView).showError(TestConst.ERROR_RESPONSE_404);
        verify(mockView).showContributors(contributorList);
    }

    @Test
    public void testOnErrorContributors() {
        setCustomAnswer(true, false);

        repoInfoPresenter.onCreateView(null);

        verify(mockView).showError(TestConst.ERROR_RESPONSE_404);
        verify(mockView).showBranches(branchList);
    }


    @Test
    public void testSaveState() {
        repoInfoPresenter.onCreateView(null);

        Bundle bundle = new Bundle();
        repoInfoPresenter.onSaveInstanceState(bundle);
        repoInfoPresenter.onStop();

        repoInfoPresenter.onCreateView(bundle);

        verify(mockView, times(2)).showBranches(branchList);
        verify(mockView, times(2)).showContributors(contributorList);
    }


    @Test
    public void testShowLoading() {
        repoInfoPresenter.onCreateView(null);

        verify(mockView).showLoading();
    }

    @Test
    public void testHideLoading() {
        repoInfoPresenter.onCreateView(null);

        verify(mockView).hideLoading();
    }

    @Test
    public void testShowLoadingOnError() {
        setErrorAnswerWebServer();

        repoInfoPresenter.onCreateView(null);

        verify(mockView).showLoading();
    }

    @Test
    public void testHideLoadingOnError() {
        setErrorAnswerWebServer();

        repoInfoPresenter.onCreateView(null);

        verify(mockView).hideLoading();
    }

    @Test
    public void testShowLoadingOnErrorBranches() {
        setCustomAnswer(false, true);

        repoInfoPresenter.onCreateView(null);

        verify(mockView).showLoading();
    }

    @Test
    public void testHideLoadingOnErrorBranches() {
        setCustomAnswer(false, true);

        repoInfoPresenter.onCreateView(null);

        verify(mockView).hideLoading();
    }

    @Test
    public void testShowLoadingOnErrorContributors() {
        setCustomAnswer(true, false);


        repoInfoPresenter.onCreateView(null);

        verify(mockView).showLoading();
    }

    @Test
    public void testHideLoadingOnErrorContributors() {
        setCustomAnswer(true, false);

        repoInfoPresenter.onCreateView(null);

        verify(mockView).hideLoading();
    }
}