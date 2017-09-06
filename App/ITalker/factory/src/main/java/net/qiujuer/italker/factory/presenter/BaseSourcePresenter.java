package net.qiujuer.italker.factory.presenter;

import net.qiujuer.italker.factory.data.DataSource;
import net.qiujuer.italker.factory.data.DbDataSource;

import java.util.List;

/**
 * Created by admin on 2017/9/6.
 * 基础的仓库源的Presenter 定义
 */

public abstract  class BaseSourcePresenter<Data,ViewMode,
        Source extends DbDataSource<Data>,
        View extends BaseContract.RecyclerView>
        extends BaseRecyclerPresenter<ViewMode,View>
implements DataSource.SuccessCallback<List<Data>>{
    protected Source mSource;

    public BaseSourcePresenter(Source source,View view) {
        super(view);
        this.mSource = source;
    }

    @Override
    public void start() {
        super.start();
        if (mSource!=null){
            mSource.load(this);
        }
    }

    @Override
    public void destory() {
        super.destory();
        mSource.dispose();
        mSource = null;
    }
}
