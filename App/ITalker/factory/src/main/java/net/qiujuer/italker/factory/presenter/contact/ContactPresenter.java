package net.qiujuer.italker.factory.presenter.contact;


import android.support.v7.util.DiffUtil;

import net.qiujuer.italker.common.widget.recycler.RecyclerAdapter;
import net.qiujuer.italker.factory.data.DataSource;
import net.qiujuer.italker.factory.data.helper.UserHelper;
import net.qiujuer.italker.factory.data.user.ContactDataSource;
import net.qiujuer.italker.factory.data.user.ContactRepository;
import net.qiujuer.italker.factory.model.db.User;
import net.qiujuer.italker.factory.presenter.BaseRecyclerPresenter;
import net.qiujuer.italker.factory.utils.DiffUiDataCallback;

import java.util.List;

/**
 * Created by admin on 2017/9/1.
 */

public class ContactPresenter extends BaseRecyclerPresenter<User,ContactContract.View>
implements ContactContract.Presenter,DataSource.SuccessCallback<List<User>>{

    private ContactDataSource mSource;
    public ContactPresenter(ContactContract.View view) {
        super(view);
        mSource = new ContactRepository();
    }

    @Override
    public void start() {
        super.start();
        //加载本地数据,并添加监听
        mSource.load(this);
        //加载网络数据
        UserHelper.refreshContacts();
        //TODO 问题
        //关注后虽然存储数据库，但是没有通知页面更新
        //如果刷新数据库或者从网络刷新，最后刷新的时候都是全局刷新
        //本地刷新和网络刷新，可能出现冲突，导致数据显示异常
        //网络返回的数据可能已经是存在于数据库的，如何识别

    }


    //子线程
    @Override
    public void onDataLoaded(List<User> users) {
        //无论怎么操作，数据变更都会通知到这里来
        final ContactContract.View view = getView();
        if (view ==null)
            return;
        RecyclerAdapter<User> adapter = view.getRecyclerAdapter();
        List<User> oldList = adapter.getItems();
        DiffUtil.Callback  callback = new DiffUiDataCallback<>(oldList,users);
        DiffUtil.DiffResult result =DiffUtil.calculateDiff(callback);
        refreshData(result,users);
    }

    @Override
    public void destory() {
        super.destory();
        //当界面销毁的时候，我们应该把数据监听销毁
        mSource.dispose();
    }
}
