package net.qiujuer.italker.factory.presenter.search;

import net.qiujuer.italker.factory.model.card.GroupCard;
import net.qiujuer.italker.factory.model.card.UserCard;
import net.qiujuer.italker.factory.presenter.BaseContract;

import java.util.List;

/**
 * Created by admin on 2017/8/31.
 */

public interface SearchContract {
    interface Presenter extends BaseContract.Presenter {
        void search(String content);
    }

    interface UserView extends  BaseContract.View<Presenter>{
        void onSearchDone(List<UserCard> userCards);
    }
    interface GroupView extends  BaseContract.View<Presenter>{
        void onSearchDone(List<GroupCard> groupCards);
    }
}
