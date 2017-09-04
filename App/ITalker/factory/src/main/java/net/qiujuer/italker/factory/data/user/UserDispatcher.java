package net.qiujuer.italker.factory.data.user;

import android.text.TextUtils;

import net.qiujuer.italker.factory.data.helper.DbHelper;
import net.qiujuer.italker.factory.model.card.UserCard;
import net.qiujuer.italker.factory.model.db.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by admin on 2017/9/4.
 */

public class UserDispatcher implements UserCenter{
    private static UserCenter instance;
    //单线程池，处理卡片一个一个信息进行处理
    private final Executor executor = Executors.newSingleThreadExecutor();

    /**
     * 单例
     * @return
     */
    public static UserCenter instance(){
        if (instance==null){
            synchronized (UserDispatcher.class){
                if (instance==null){
                    instance = new UserDispatcher();
                }
            }
        }
        return instance;
    }
    @Override
    public void dispatch(UserCard... cards) {
        if (cards==null || cards.length==0)
            return;
        //丢到单线程池中
        executor.execute(new UserCardHandler(cards));
    }

    /**
     * xian
     */
    private class UserCardHandler implements  Runnable{
        private final UserCard[] cards;
        UserCardHandler(UserCard[] cards){
           this.cards = cards;
        }
        @Override
        public void run() {
            //单被线程调度的时候触发
            List<User> users = new ArrayList<>();
            for (UserCard card : cards) {
                //进行过滤操作
                if (card==null || TextUtils.isEmpty(card.getId())){
                    continue;
                }
                users.add(card.build());
            }
            DbHelper.save(User.class,users.toArray(new User[0]));
        }
    }
}
