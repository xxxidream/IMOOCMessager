package net.qiujuer.italker.factory.presenter.account;

import net.qiujuer.italker.factory.presenter.BasePresenter;

/**登陆的逻辑实现
 * Created by admin on 2017/8/28.
 */

public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter{

    public LoginPresenter(LoginContract.View view) {
        super(view);
    }

    @Override
    public void login(String phone, String password) {

    }
}
