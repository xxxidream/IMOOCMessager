package net.qiujuer.italker.factory.presenter.contact;

import android.support.annotation.StringRes;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;
import net.qiujuer.italker.factory.data.DataSource;
import net.qiujuer.italker.factory.data.helper.UserHelper;
import net.qiujuer.italker.factory.model.card.UserCard;
import net.qiujuer.italker.factory.presenter.BasePresenter;

/**
 * Created by admin on 2017/8/31.
 */

public class FollowPresenter extends BasePresenter<FollowContract.View>
        implements FollowContract.Presenter,DataSource.Callback<UserCard>{
    public FollowPresenter(FollowContract.View view) {
        super(view);
    }

    @Override
    public void follow(String id) {
        start();
        UserHelper.follow(id,this);
    }

    @Override
    public void onDataNotAvailable(final @StringRes int str) {
        final FollowContract.View view = getView();
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
    public void onDataLoaded(final UserCard userCard) {
        final FollowContract.View view = getView();
        if (view!=null){
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.onFollowSucceed(userCard);
                }
            });
        }
    }
}
