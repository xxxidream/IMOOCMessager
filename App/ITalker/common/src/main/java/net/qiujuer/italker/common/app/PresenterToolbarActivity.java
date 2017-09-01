package net.qiujuer.italker.common.app;

import android.support.annotation.StringRes;

import net.qiujuer.italker.factory.presenter.BaseContract;

/**
 * Created by admin on 2017/8/31.
 */

public abstract class PresenterToolbarActivity<Presenter extends BaseContract.Presenter> extends ToolbarActivity
implements BaseContract.View<Presenter>{
    protected Presenter mPresenter;

    @Override
    protected void initBefore() {
        super.initBefore();
        initPresenter();
    }

    @Override
    public void showError(@StringRes int str) {
        if (mPlaceHolderView!=null){
            mPlaceHolderView.triggerError(str);
        }else{
            Application.showToast(str);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter!=null){
            mPresenter.destory();
        }
    }

    /**
     * 初始化presenter
     * @return
     */
    protected abstract  Presenter initPresenter();

    @Override
    public void showLoading() {
        // 显示一个Loading
        if (mPlaceHolderView!=null){
            mPlaceHolderView.triggerLoading();
        }
    }

    protected void hideLoading(){
        if (mPlaceHolderView!=null){
            mPlaceHolderView.triggerOk();
        }
    }

    @Override
    public void setPresenter(Presenter presenter) {
        mPresenter = presenter;
    }
}
