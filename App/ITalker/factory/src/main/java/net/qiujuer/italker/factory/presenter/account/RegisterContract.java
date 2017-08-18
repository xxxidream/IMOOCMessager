package net.qiujuer.italker.factory.presenter.account;

import net.qiujuer.italker.factory.presenter.BaseContract;

/**
 * Created by admin on 2017/8/17.
 */

public interface RegisterContract {
    interface View extends BaseContract.View<RegisterContract.Presenter>{
        //注册成功
        void registerSuccess();
    }
    interface Presenter extends BaseContract.Presenter{
        void register(String phone,String password, String name);
        boolean checkMobile(String phone);
    }
}
