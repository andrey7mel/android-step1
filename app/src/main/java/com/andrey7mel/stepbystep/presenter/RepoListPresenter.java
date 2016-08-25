package com.andrey7mel.stepbystep.presenter;

import android.os.Bundle;
import android.text.TextUtils;

import com.andrey7mel.stepbystep.other.App;
import com.andrey7mel.stepbystep.other.EspressoIdlingResource;
import com.andrey7mel.stepbystep.presenter.mappers.RepoListMapper;
import com.andrey7mel.stepbystep.presenter.vo.Repository;
import com.andrey7mel.stepbystep.view.fragments.RepoListView;
import com.andrey7mel.stepbystep.view.fragments.View;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;

public class RepoListPresenter extends BasePresenter {

    private static final String BUNDLE_REPO_LIST_KEY = "BUNDLE_REPO_LIST_KEY";

    @Inject
    protected RepoListMapper repoListMapper;

    private RepoListView view;

    private List<Repository> repoList;

    // for DI
    @Inject
    public RepoListPresenter() {
    }

    public RepoListPresenter(RepoListView view) {
        App.getComponent().inject(this);
        this.view = view;
    }

    @Override
    protected View getView() {
        return view;
    }

    public void onSearchButtonClick() {
        String name = view.getUserName();
        if (TextUtils.isEmpty(name)) return;

        showLoadingState();
        EspressoIdlingResource.increment();
        Subscription subscription = model.getRepoList(name)
                .map(repoListMapper)
                .subscribe(new Observer<List<Repository>>() {

                    @Override
                    public void onCompleted() {
                        hideLoadingState();
                        EspressoIdlingResource.decrement();
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideLoadingState();
                        EspressoIdlingResource.decrement();
                        showError(e);
                    }

                    @Override
                    public void onNext(List<Repository> list) {
                        if (list != null && !list.isEmpty()) {
                            repoList = list;
                            view.showRepoList(list);
                        } else {
                            view.showEmptyList();
                        }
                    }
                });
        addSubscription(subscription);
    }

    public void onCreateView(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            repoList = (List<Repository>) savedInstanceState.getSerializable(BUNDLE_REPO_LIST_KEY);
        }
        if (isRepoListNotEmpty()) {
            view.showRepoList(repoList);
        }
    }

    private boolean isRepoListNotEmpty() {
        return (repoList != null && !repoList.isEmpty());
    }

    public void onSaveInstanceState(Bundle outState) {
        if (isRepoListNotEmpty()) {
            outState.putSerializable(BUNDLE_REPO_LIST_KEY, new ArrayList<>(repoList));
        }
    }

    public void clickRepo(Repository repository) {
        view.startRepoInfoFragment(repository);
    }

}
