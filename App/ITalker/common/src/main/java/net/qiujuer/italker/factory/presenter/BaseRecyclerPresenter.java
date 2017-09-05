package net.qiujuer.italker.factory.presenter;

import android.support.v7.util.DiffUtil;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;
import net.qiujuer.italker.common.widget.recycler.RecyclerAdapter;

import java.util.List;

/**
 * 对RecyclerView 进行一个简单的Pesenter
 * Created by admin on 2017/9/5.
 */

public class BaseRecyclerPresenter<ViewMode,View extends BaseContract.RecyclerView> extends BasePresenter<View>{
    public BaseRecyclerPresenter(View view) {
        super(view);
    }

    protected void refreshData(final List<ViewMode> dataList){
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                View view = getView();
                if (view==null)
                    return;
                //基本的更新数据并刷新界面
                RecyclerAdapter<ViewMode> adapter = view.getRecyclerAdapter();
                adapter.replace(dataList);
                view.onAdapterDataChanged();
            }
        });
    }

    protected  void refreshData(final DiffUtil.DiffResult diffResult,final List<ViewMode> dataList){
        //diff 在子线程
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                refreshDataOnUiThread(diffResult,dataList);
            }
        });
    }

    private void refreshDataOnUiThread(final DiffUtil.DiffResult diffResult,final List<ViewMode> dataList){
        View view = getView();
        if (view==null)
            return;
        RecyclerAdapter<ViewMode> adapter = view.getRecyclerAdapter();
        //改变数据集合并不通知界面刷新
        adapter.getItems().clear();
        adapter.getItems().addAll(dataList);
        //通知界面刷新占位布局
        view.onAdapterDataChanged();
        //进行增量更新
        diffResult.dispatchUpdatesTo(adapter);
    }
}
