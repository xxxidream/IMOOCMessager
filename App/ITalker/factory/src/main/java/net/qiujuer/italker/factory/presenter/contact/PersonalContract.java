package net.qiujuer.italker.factory.presenter.contact;

import net.qiujuer.italker.factory.model.db.User;
import net.qiujuer.italker.factory.presenter.BaseContract;

/**
 * Created by admin on 2017/9/1.
 */

public interface PersonalContract {
    interface Presenter extends BaseContract.Presenter{
        User getUserPersonal();
    }
    interface View extends BaseContract.View<Presenter>{
        void onLoadDone(User user);
        void allowSayHello(boolean isAllow);
        void setFollowStatus(boolean isFollow);
    }
}
