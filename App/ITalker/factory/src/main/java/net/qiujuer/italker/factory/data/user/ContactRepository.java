package net.qiujuer.italker.factory.data.user;

import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import net.qiujuer.italker.factory.data.DataSource;
import net.qiujuer.italker.factory.data.helper.DbHelper;
import net.qiujuer.italker.factory.model.db.User;
import net.qiujuer.italker.factory.model.db.User_Table;
import net.qiujuer.italker.factory.persistence.Account;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by admin on 2017/9/5.
 * 联系人仓库
 */

public class ContactRepository implements ContactDataSource,
        QueryTransaction.QueryResultListCallback<User>,
        DbHelper.ChangedListener<User>{
    private DataSource.SuccessCallback<List<User>> callback;
    @Override
    public void load(DataSource.SuccessCallback<List<User>> callback) {
        this.callback = callback;
        //对数据辅助工具类添加一个数据更新的监听器
        DbHelper.addChangedListener(User.class,this);
        SQLite.select()
                .from(User.class)
                .where(User_Table.isFollow.eq(true))
                .and(User_Table.id.notEq(Account.getUserId()))
                .orderBy(User_Table.name,true)
                .limit(100)
                .async()
                .queryListResultCallback(this).execute();
    }

    @Override
    public void dispose() {
        this.callback = null;
        DbHelper.removeChangedListener(User.class,this);
    }

    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<User> tResult) {
//        users.addAll(tResult);
        //数据库加载数据成功
        if (tResult.size()==0){
            users.clear();
            notyfiDataChanged();
            return;
        }
        User[] users = tResult.toArray(new User[0]);
        onDataSave(users);
    }


    @Override
    public void onDataSave(User... list) {
        boolean isChanged = false ;
        for (User user : list) {
            if (isRequired(user)){
                insertOrUpdate(user);
                isChanged = true;
            }
        }
        if (isChanged){
            notyfiDataChanged();
        }
    }

    @Override
    public void onDataDelete(User... list) {
        boolean isChanged = false;
        for (User user : list) {
           if (users.remove(user)){
               isChanged = true;
           }
        }
        if (isChanged){
            notyfiDataChanged();
        }
    }
    private List <User> users = new LinkedList<>();
    private void insertOrUpdate(User user){
        int index = indexOf(user);
        if (index>=0){
            //更新
            replace(index,user);
        }else{
            insert(user);
        }

    }
    private void replace(int index,User user){
        users.remove(index);
        users.add(index,user);
    }

    private void insert(User user){
        users.add(user);
    }

    private void notyfiDataChanged(){
        if (callback!=null)
        {
            callback.onDataLoaded(users);
        }
    }
    private int indexOf(User user){
        int index = -1;
        for (User user1 : users) {
            index++;
            if (user1.isSame(user)){
                return index;
            }
        }
        return -1;
    }

    private boolean isRequired(User user){
        return user.isFollow() && !user.getId().equals(Account.getUserId());
    }
}
