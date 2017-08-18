package net.qiujuer.italker.push.frags.account;


import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.EditText;

import net.qiujuer.genius.ui.widget.Button;
import net.qiujuer.genius.ui.widget.Loading;
import net.qiujuer.italker.common.app.Fragment;
import net.qiujuer.italker.common.app.PresenterFragment;
import net.qiujuer.italker.factory.presenter.account.RegisterContract;
import net.qiujuer.italker.factory.presenter.account.RegisterPresenter;
import net.qiujuer.italker.push.R;
import net.qiujuer.italker.push.activities.MainActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends PresenterFragment<RegisterContract.Presenter> implements RegisterContract.View{
    private AccountTrigger mAccountTrigger;

    @BindView(R.id.edit_phone)
    EditText mPhone;
    @BindView(R.id.edit_name)
    EditText mName;
    @BindView(R.id.edit_password)
    EditText mPassword;
    @BindView(R.id.loading)
    Loading mLoading;

    @BindView(R.id.btn_submit)
    Button mSubmit;


    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_register;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAccountTrigger = (AccountTrigger)context;

    }
    @Override
    protected RegisterContract.Presenter initPresenter() {
        return new RegisterPresenter(this);
    }


    @OnClick(R.id.txt_go_login)
    void onShowLoginClick(){
        mAccountTrigger.triggerView();
    }

    @OnClick(R.id.btn_submit)
    void onSubmitClick(){
        String phone = mPhone.getText().toString();
        String password = mPassword.getText().toString();
        String name = mName.getText().toString();

        //调用p层进行注册
        mPresenter.register(phone,password,name);
    }

    @Override
    public void showLoading() {
        super.showLoading();
       //正在进行时，正在进行注册，界面不可操作
        //开始Loading
        mLoading.start();
        mPhone.setEnabled(false);
        mName.setEnabled(false);
        mPassword.setEnabled(false);
        mSubmit.setEnabled(false);
    }

    @Override
    public void showError(@StringRes int str) {
        super.showError(str);
        //当提示需要显示错误的时候触发，一定是结束了
        mLoading.stop();
        mPhone.setEnabled(true);
        mName.setEnabled(true);
        mPassword.setEnabled(true);
        mSubmit.setEnabled(true);
    }

    @Override
    public void registerSuccess() {
        //注册成功，这个时候账户已经登录
        //我们需要进行跳转到MainActivity
        MainActivity.show(getContext());
        getActivity().finish();
    }
}
