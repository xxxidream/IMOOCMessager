package net.qiujuer.italker.factory.utils;

import android.support.v7.util.DiffUtil;

import java.util.List;

/**
 * Created by admin on 2017/9/1.
 */

public class DiffUiDataCallback<T extends DiffUiDataCallback.UiDataDiffer<T>> extends DiffUtil.Callback{
    private List<T> mOldList,mNewList;

    public DiffUiDataCallback(List<T> mOldList, List<T> mNewList) {
        this.mOldList = mOldList;
        this.mNewList = mNewList;
    }

    @Override
    public int getOldListSize() {
        //旧的数据大小
        return mOldList.size();
    }

    @Override
    public int getNewListSize() {
        //新的数据大小
        return mOldList.size();
    }

    //两个类是否就是同一个东西，比如id相等的User
    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        T beanOld = mOldList.get(oldItemPosition);
        T beanNew = mNewList.get(newItemPosition);
        return beanNew.isSame(beanOld);
    }
    //在经过相等判断后，进一步判断是否有数据更改
    //比如，同一个用户的两个不同实例，其中的name字段不同
    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        T beanOld = mOldList.get(oldItemPosition);
        T beanNew = mNewList.get(newItemPosition);
        return beanNew.isUiContentSame(beanOld);
    }

    public interface UiDataDiffer<T>{
        //传递一个旧的数据给，问你是否和你标识的是同一个数据

        boolean isSame(T old);
        //你和旧的数据对比，内容是否相同
        boolean isUiContentSame(T old);
    }
}
