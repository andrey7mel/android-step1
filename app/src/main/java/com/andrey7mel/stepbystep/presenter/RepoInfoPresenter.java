package com.andrey7mel.stepbystep.presenter;

import android.os.Bundle;

import com.andrey7mel.stepbystep.other.App;
import com.andrey7mel.stepbystep.other.EspressoIdlingResource;
import com.andrey7mel.stepbystep.presenter.mappers.RepoBranchesMapper;
import com.andrey7mel.stepbystep.presenter.mappers.RepoContributorsMapper;
import com.andrey7mel.stepbystep.presenter.vo.Branch;
import com.andrey7mel.stepbystep.presenter.vo.Contributor;
import com.andrey7mel.stepbystep.presenter.vo.Repository;
import com.andrey7mel.stepbystep.view.fragments.RepoInfoView;
import com.andrey7mel.stepbystep.view.fragments.View;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;

public class RepoInfoPresenter extends BasePresenter {

    private static final String BUNDLE_BRANCHES_KEY = "BUNDLE_BRANCHES_KEY";
    private static final String BUNDLE_CONTRIBUTORS_KEY = "BUNDLE_CONTRIBUTORS_KEY";
    private static final int COUNT_SUBSCRIPTION = 2;

    @Inject
    protected RepoBranchesMapper branchesMapper;

    @Inject
    protected RepoContributorsMapper contributorsMapper;

    private int countCompletedSubscription = 0;

    private RepoInfoView view;

    private List<Contributor> contributorList;
    private List<Branch> branchList;

    private Repository repository;

    private void loadData() {
        String owner = repository.getOwnerName();
        String name = repository.getRepoName();

        showLoadingState();
        EspressoIdlingResource.increment();
        Subscription subscriptionBranches = model.getRepoBranches(owner, name)
                .map(branchesMapper)
                .subscribe(new Observer<List<Branch>>() {
                    @Override
                    public void onCompleted() {
                        hideInfoLoadingState();
                        EspressoIdlingResource.decrement();
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideInfoLoadingState();
                        EspressoIdlingResource.decrement();
                        showError(e);
                    }

                    @Override
                    public void onNext(List<Branch> list) {
                        branchList = list;
                        view.showBranches(list);
                    }
                });
        addSubscription(subscriptionBranches);

        EspressoIdlingResource.increment();
        Subscription subscriptionContributors = model.getRepoContributors(owner, name)
                .map(contributorsMapper)
                .subscribe(new Observer<List<Contributor>>() {
                    @Override
                    public void onCompleted() {
                        hideInfoLoadingState();
                        EspressoIdlingResource.decrement();
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideInfoLoadingState();
                        EspressoIdlingResource.decrement();
                        showError(e);
                    }

                    @Override
                    public void onNext(List<Contributor> list) {
                        contributorList = list;
                        view.showContributors(list);
                    }
                });

        addSubscription(subscriptionContributors);
    }

    public void onCreate(RepoInfoView view, Repository repository) {
        App.getComponent().inject(this);
        this.view = view;
        this.repository = repository;
    }

    protected void hideInfoLoadingState() {
        countCompletedSubscription++;

        if (countCompletedSubscription == COUNT_SUBSCRIPTION) {
            hideLoadingState();
            countCompletedSubscription = 0;
        }
    }

    public void onCreateView(Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            contributorList = (List<Contributor>) savedInstanceState.getSerializable(BUNDLE_CONTRIBUTORS_KEY);
            branchList = (List<Branch>) savedInstanceState.getSerializable(BUNDLE_BRANCHES_KEY);
        }

        if (contributorList == null || branchList == null) {
            loadData();
        } else {
            view.showBranches(branchList);
            view.showContributors(contributorList);
        }

    }

    public void onSaveInstanceState(Bundle outState) {
        if (contributorList != null)
            outState.putSerializable(BUNDLE_CONTRIBUTORS_KEY, new ArrayList<>(contributorList));
        if (branchList != null)
            outState.putSerializable(BUNDLE_BRANCHES_KEY, new ArrayList<>(branchList));

    }

    @Override
    protected View getView() {
        return view;
    }
}
