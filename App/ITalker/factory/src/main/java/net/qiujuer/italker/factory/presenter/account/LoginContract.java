package net.qiujuer.italker.factory.presenter.account;

import net.qiujuer.italker.factory.presenter.BaseContract;

/**
 * Created by admin on 2017/8/17.
 */

public interface LoginContract {
    interface View extends BaseContract.View<LoginContract.Presenter>{
        void loginSuccess();
    }
    interface Presenter extends BaseContract.Presenter{
        void login(String phone,String password);
    }
}
