package net.qiujuer.italker.common.app;

import android.content.Context;
import android.support.annotation.StringRes;

import net.qiujuer.italker.factory.presenter.BaseContract;

/**
 * Created by admin on 2017/8/18.
 */

public abstract class PresenterFragment<Presenter extends BaseContract.Presenter> extends Fragment implements BaseContract.View<Presenter> {

    protected  Presenter mPresenter;

    /**
     * 初始化presenter
     * @return
     */
    protected abstract  Presenter initPresenter();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //在界面Attach之后就触发初始化
        initPresenter();
    }

    @Override
    public void showError(@StringRes int str) {
        Application.showToast(str);
    }

    @Override
    public void showLoading() {
        //TODO 显示一个Loading
    }

    @Override
    public void setPresenter(Presenter presenter) {
        mPresenter = presenter;
    }
}
