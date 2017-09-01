package net.qiujuer.italker.factory.presenter;

import android.support.annotation.StringRes;

import net.qiujuer.italker.common.widget.recycler.RecyclerAdapter;

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

    /**
     * 基本的一个列表的View 的职责
     * @param <T>
     */
    interface RecyclerView<T extends  Presenter,ViewModel> extends View<T>{
        //界面只能全部刷新，不能精确到某条数据
        //void onDone(List<User>users);
        //拿到一个适配器，然后自己自主的进行刷新
        RecyclerAdapter<ViewModel> getRecyclerAdapter();
        //当适配器的数据更改的时候进行通知
        void onAdapterDataChanged();
    }
}
