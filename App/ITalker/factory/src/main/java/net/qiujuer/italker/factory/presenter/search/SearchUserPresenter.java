package net.qiujuer.italker.factory.presenter.search;

import android.support.annotation.StringRes;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;
import net.qiujuer.italker.factory.data.DataSource;
import net.qiujuer.italker.factory.data.helper.UserHelper;
import net.qiujuer.italker.factory.model.card.UserCard;
import net.qiujuer.italker.factory.presenter.BasePresenter;

import java.util.List;

import retrofit2.Call;

/**
 * Created by admin on 2017/8/31.
 */

public class SearchUserPresenter extends BasePresenter<SearchContract.UserView>
        implements SearchContract.Presenter,DataSource.Callback<List<UserCard>>{
    private Call searchCall;

    public SearchUserPresenter(SearchContract.UserView view) {
        super(view);
    }

    @Override
    public void search(String content) {
        start();
        Call call = searchCall;
        if (call!=null && !call.isCanceled()){
            call.cancel();
        }
        searchCall = UserHelper.search(content,this);
    }

    @Override
    public void onDataNotAvailable(final @StringRes int str) {
        final SearchContract.UserView view = getView();
        if (view!=null){
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.showError(str);
                }
            });
        }
    }

    @Override
    public void onDataLoaded(final List<UserCard> userCards) {
        final SearchContract.UserView view = getView();
        if (view!=null){
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.onSearchDone(userCards);
                }
            });
        }
    }
}
