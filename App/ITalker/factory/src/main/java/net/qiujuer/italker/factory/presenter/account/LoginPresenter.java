package net.qiujuer.italker.factory.presenter.account;

import android.support.annotation.StringRes;
import android.text.TextUtils;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;
import net.qiujuer.italker.factory.R;
import net.qiujuer.italker.factory.data.DataSource;
import net.qiujuer.italker.factory.data.helper.AccountHelper;
import net.qiujuer.italker.factory.model.api.account.LoginModel;
import net.qiujuer.italker.factory.model.db.User;
import net.qiujuer.italker.factory.persistence.Account;
import net.qiujuer.italker.factory.presenter.BasePresenter;

/** 登陆的逻辑实现
 * Created by admin on 2017/8/28.
 */

public class LoginPresenter extends BasePresenter<LoginContract.View>
        implements LoginContract.Presenter,DataSource.Callback<User>{

    public LoginPresenter(LoginContract.View view) {
        super(view);
    }

    @Override
    public void login(String phone, String password) {
        start();
        final LoginContract.View view = getView();
        if(TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)){
            view.showError(R.string.data_account_login_invalid_parameter);
        }else{
            LoginModel model = new LoginModel(phone,password, Account.getPushId());
            AccountHelper.login(model,this);
        }
    }

    @Override
    public void onDataNotAvailable(final @StringRes int str) {
        final LoginContract.View view = getView();
        if(view == null) {
            return;
        }
        Run.onUiAsync(new Action(){
            @Override
            public void call() {
                view.showError(str);
            }
        });
    }

    @Override
    public void onDataLoaded(User user) {
        final LoginContract.View view = getView();
        if(view == null) {
            return;
        }
        Run.onUiAsync(new Action(){
            @Override
            public void call() {
                view.loginSuccess();
            }
        });
    }
}
