package net.qiujuer.italker.factory.data;

import android.support.annotation.StringRes;

/**
 * 数据源接口定义
 * Created by admin on 2017/8/18.
 */

public interface DataSource {
    interface Callback<T> extends SuccessCallback<T>,FailedCallback{

    }
    interface SuccessCallback<T>{
        void onDataLoaded(T t);
    }
    interface FailedCallback{
        void onDataNotAvailable(@StringRes int str);
    }
}
