package net.qiujuer.italker.factory.presenter.search;

import net.qiujuer.italker.factory.presenter.BasePresenter;

/**
 * Created by admin on 2017/8/31.
 */

public class SearchGroupPresenter extends BasePresenter<SearchContract.GroupView> implements SearchContract.Presenter{
    public SearchGroupPresenter(SearchContract.GroupView view) {
        super(view);
    }

    @Override
    public void search(String content) {

    }
}

