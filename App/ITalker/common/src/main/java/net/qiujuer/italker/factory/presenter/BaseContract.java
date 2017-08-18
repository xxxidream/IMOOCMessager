package net.qiujuer.italker.factory.presenter;

import android.support.annotation.StringRes;

/**
 * Created by admin on 2017/8/17.
 */

public interface BaseContract {
    interface View<T extends Presenter>{
        void showError(@StringRes int str);
        void showLoading();
        void setPresenter(T presenter);
    }
    interface Presenter{
        void start();
        void destory();
    }
}
