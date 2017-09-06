package net.qiujuer.italker.factory.presenter.contact;


import android.support.v7.util.DiffUtil;

import net.qiujuer.italker.common.widget.recycler.RecyclerAdapter;
import net.qiujuer.italker.factory.data.DataSource;
import net.qiujuer.italker.factory.data.helper.UserHelper;
import net.qiujuer.italker.factory.data.user.ContactDataSource;
import net.qiujuer.italker.factory.data.user.ContactRepository;
import net.qiujuer.italker.factory.model.db.User;
import net.qiujuer.italker.factory.presenter.BaseSourcePresenter;
import net.qiujuer.italker.factory.utils.DiffUiDataCallback;

import java.util.List;

/**
 * Created by admin on 2017/9/1.
 */

public class ContactPresenter extends BaseSourcePresenter<User,User,ContactDataSource,ContactContract.View>
implements ContactContract.Presenter,DataSource.SuccessCallback<List<User>>{

    public ContactPresenter(ContactContract.View view) {
        super(new ContactRepository(),view);
    }

    @Override
    public void start() {
        super.start();
        //加载网络数据
        UserHelper.refreshContacts();
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
