package net.qiujuer.italker.factory.data;

import java.util.List;

/**
 * 基础的数据库数据源接口定义
 * Created by admin on 2017/9/5.
 */

public interface DbDataSource<Data> extends DataSource{
    /**
     * 基础的数据源加载方法
     * @param callback 传递一个callback
     */
    void load(SuccessCallback<List<Data>> callback);
}
