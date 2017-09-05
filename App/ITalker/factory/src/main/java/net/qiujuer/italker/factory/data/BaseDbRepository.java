package net.qiujuer.italker.factory.data;

import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import net.qiujuer.italker.factory.data.helper.DbHelper;
import net.qiujuer.italker.factory.model.db.BaseDbModel;
import net.qiujuer.italker.utils.CollectionUtil;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by admin on 2017/9/5.
 * 基本的数据库仓库
 * 实现对数据库基本的监听操作
 */

public abstract  class BaseDbRepository<Data extends BaseDbModel<Data>> implements DbDataSource<Data>,
        QueryTransaction.QueryResultListCallback<Data>,
        DbHelper.ChangedListener<Data>
{
    private SuccessCallback<List<Data>> callback;
    private final List<Data> dataList = new LinkedList<>();
    private Class<Data> dataClass ;

    @Override
    public void load(SuccessCallback<List<Data>> callback) {
        this.callback = callback;
    }

    @Override
    public void dispose() {
        this.callback = null;
    }

    /**
     * 数据库统一通知的地方 增加或者更改
     * @param list
     */
    @Override
    public void onDataSave(Data... list) {
        boolean isChanged = false ;
        for (Data data : list) {
            if (isRequired(data)){
                insertOrUpdate(data);
                isChanged = true;
            }
        }
        if (isChanged){
            notyfiDataChanged();
        }
    }
    /**
     * 数据库统一通知的地方 删除
     * @param list
     */
    @Override
    public void onDataDelete(Data... list) {

    }

    //DBFLOW框架通知的回调
    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Data> tResult) {
        //数据库加载数据成功
        if (callback!=null){
            callback.onDataLoaded(tResult);
        }
        if (tResult.size()==0){
            dataList.clear();
            notyfiDataChanged();
            return;
        }
        Data[] datas = CollectionUtil.toArray(tResult,dataClass);
        onDataSave(datas);
    }
    private void notyfiDataChanged(){
        SuccessCallback<List<Data>> callback = this.callback;
        if (callback!=null)
        {
            callback.onDataLoaded(dataList);
        }
    }
    protected void insertOrUpdate(Data data){
        int index = indexOf(data);
        if (index>=0){
            //更新
            replace(index,data);
        }else{
            insert(data);
        }

    }
    protected void replace(int index,Data data){
        dataList.remove(index);
        dataList.add(index,data);
    }

    protected void insert(Data data){
        dataList.add(data);
    }
    protected int indexOf(Data newData){
        int index = -1;
        for (Data data1 : dataList) {
            index++;
            if (data1.isSame(newData)){
                return index;
            }
        }
        return -1;
    }
    public abstract boolean isRequired(Data data);
}
