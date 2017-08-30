package net.qiujuer.italker.factory.presenter.user;

import net.qiujuer.italker.factory.presenter.BaseContract;

/**
 * Created by 16571 on 2017/8/30.
 */

public interface UpdateInfoContract {
    interface Presenter extends BaseContract.Presenter{
        void update(String photoFilePath,String desc,boolean isMan);
    }
    interface View extends BaseContract.View<Presenter>{
        void updateSucceed();
    }
}
