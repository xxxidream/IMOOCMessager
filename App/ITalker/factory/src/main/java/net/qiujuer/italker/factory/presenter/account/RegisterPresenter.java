package net.qiujuer.italker.factory.presenter.account;

import android.support.annotation.StringRes;
import android.text.TextUtils;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;
import net.qiujuer.italker.common.Common;
import net.qiujuer.italker.factory.R;
import net.qiujuer.italker.factory.data.DataSource;
import net.qiujuer.italker.factory.data.helper.AccountHelper;
import net.qiujuer.italker.factory.model.api.account.RegisterModel;
import net.qiujuer.italker.factory.model.db.User;
import net.qiujuer.italker.factory.presenter.BasePresenter;

import java.util.regex.Pattern;

/**
 * Created by admin on 2017/8/18.
 */

public class RegisterPresenter extends BasePresenter<RegisterContract.View>
        implements RegisterContract.Presenter,DataSource.Callback<User> {
    public RegisterPresenter(RegisterContract.View view) {
        super(view);
    }

    @Override
    public void register(String phone, String password, String name) {
        //调用开始，在start中默认启动了Loading
        start();
        RegisterContract.View view = getView();

        if(!checkMobile(phone)){
            //提示
            view.showError(R.string.data_account_register_invalid_parameter_mobile);
        }else if (name.length()<2){
            //姓名必须大于2位
            view.showError(R.string.data_account_register_invalid_parameter_name);

        }else if(password.length()<6){
            //密码需要大于6位
            view.showError(R.string.data_account_register_invalid_parameter_password);
        }else{
//            http
            RegisterModel model = new RegisterModel( phone, password, name);
            AccountHelper.register(model, this);
        }
    }

    @Override
    public boolean checkMobile(String phone) {
        return !TextUtils.isEmpty(phone)
                && Pattern.matches(Common.Constant.REGEX_MOBILE,phone);
    }

    @Override
    public void onDataNotAvailable(final @StringRes int str) {
        final RegisterContract.View view = getView();
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
        final RegisterContract.View view = getView();
        if(view == null) {
            return;
        }
        Run.onUiAsync(new Action(){
            @Override
            public void call() {
                view.registerSuccess();
            }
        });
    }
}
