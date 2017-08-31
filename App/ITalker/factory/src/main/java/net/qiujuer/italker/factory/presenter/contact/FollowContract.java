package net.qiujuer.italker.factory.presenter.contact;

import net.qiujuer.italker.factory.model.card.UserCard;
import net.qiujuer.italker.factory.presenter.BaseContract;

/**
 * Created by admin on 2017/8/31.
 * 关注的接口定义
 */

public interface FollowContract {
    interface Presenter extends BaseContract.Presenter{
        void follow(String id);
    }
    interface View extends BaseContract.View<Presenter>{
        void onFollowSucceed(UserCard userCard);
    }
}
