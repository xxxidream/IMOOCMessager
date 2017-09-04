package net.qiujuer.italker.factory.presenter.contact;


import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import net.qiujuer.italker.factory.data.helper.UserHelper;
import net.qiujuer.italker.factory.model.db.User;
import net.qiujuer.italker.factory.model.db.User_Table;
import net.qiujuer.italker.factory.persistence.Account;
import net.qiujuer.italker.factory.presenter.BasePresenter;
import net.qiujuer.italker.factory.utils.DiffUiDataCallback;

import java.util.List;

/**
 * Created by admin on 2017/9/1.
 */

public class ContactPresenter extends BasePresenter<ContactContract.View>
implements ContactContract.Presenter{
    public ContactPresenter(ContactContract.View view) {
        super(view);
    }

    @Override
    public void start() {
        super.start();
        //数据库数据
        SQLite.select()
                .from(User.class)
                .where(User_Table.isFollow.eq(true))
                .and(User_Table.id.notEq(Account.getUserId()))
                .orderBy(User_Table.name,true)
                .limit(100)
                .async()
                .queryListResultCallback(new QueryTransaction.QueryResultListCallback<User>() {
                    @Override
                    public void onListQueryResult(QueryTransaction transaction,
                                                  @NonNull List<User> tResult) {
                        getView().getRecyclerAdapter().replace(tResult);
                        getView().onAdapterDataChanged();
                    }
                }).execute();
        //加载网络数据
        UserHelper.refreshContacts();
        /*
        * final List<User> users = new ArrayList<User>();
                for (UserCard userCard:userCards){
                    users.add(userCard.build());
                }
               //丢到事务中保存数据库
                DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
                        definition.beginTransactionAsync(new ITransaction() {
                            @Override
                            public void execute(DatabaseWrapper databaseWrapper) {
                                    FlowManager.getModelAdapter(User.class)
                                            .saveAll(users);


                            }
                        }).build().execute();

                //网络的数据往往是新的，我们需要直接刷新错误
                List<User> old = getView().getRecyclerAdapter().getItems();
                diff(old,users);
                */
        //TODO 问题
        //关注后虽然存储数据库，但是没有通知页面更新
        //如果刷新数据库或者从网络刷新，最后刷新的时候都是全局刷新
        //本地刷新和网络刷新，可能出现冲突，导致数据显示异常
        //网络返回的数据可能已经是存在于数据库的，如何识别

    }

    private void diff(List<User> oldList,List<User> newList){
        DiffUtil.Callback  callback = new DiffUiDataCallback<>(oldList,newList);
        DiffUtil.DiffResult result =DiffUtil.calculateDiff(callback);
        getView().getRecyclerAdapter().replace(newList);//完成对比后进行数据的赋值

        //尝试刷新页面
        result.dispatchUpdatesTo(getView().getRecyclerAdapter());
        //TODO
        getView().onAdapterDataChanged();

    }
}
