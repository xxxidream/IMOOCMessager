package net.qiujuer.italker.factory.data.helper;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import net.qiujuer.italker.factory.Factory;
import net.qiujuer.italker.factory.R;
import net.qiujuer.italker.factory.data.DataSource;
import net.qiujuer.italker.factory.model.api.RspModel;
import net.qiujuer.italker.factory.model.api.user.UserUpdateModel;
import net.qiujuer.italker.factory.model.card.UserCard;
import net.qiujuer.italker.factory.model.db.User;
import net.qiujuer.italker.factory.model.db.User_Table;
import net.qiujuer.italker.factory.net.NetWork;
import net.qiujuer.italker.factory.net.RemoteService;
import net.qiujuer.italker.utils.CollectionUtil;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 16571 on 2017/8/30.
 */

public class UserHelper {
    public static void update(UserUpdateModel model, final DataSource.Callback<UserCard> callback){
        RemoteService service = NetWork.remote();
        //得到一个call
        Call<RspModel<UserCard>> call = service.userUpdate(model);
        //异步
        call.enqueue(new Callback<RspModel<UserCard>>() {
            @Override
            public void onResponse(Call<RspModel<UserCard>> call, Response<RspModel<UserCard>> response) {
                RspModel<UserCard> rspModel = response.body();
                if (rspModel.success()){
                    UserCard userCard = rspModel.getResult();
                    //保存数据库，先转换成User
                    Factory.getUserCenter().dispatch(userCard);
                    callback.onDataLoaded(userCard);
                }else{
                    Factory.decodeRspCode(rspModel,callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<UserCard>> call, Throwable t) {
                if(callback!=null){
                    callback.onDataNotAvailable(R.string.data_network_error);
                }
            }
        });
    }

    public static Call search(String name, final DataSource.Callback<List<UserCard>> callback){
        RemoteService service = NetWork.remote();
        //得到一个call
        Call<RspModel<List<UserCard>>> call = service.userSearch(name);
        //异步
        call.enqueue(new Callback<RspModel<List<UserCard>>>() {
            @Override
            public void onResponse(Call<RspModel<List<UserCard>>> call, Response<RspModel<List<UserCard>>> response) {
                RspModel<List<UserCard>> rspModel = response.body();
                if (rspModel.success()){
                    List<UserCard> userCards = rspModel.getResult();
                    callback.onDataLoaded(userCards);
                }else{
                    Factory.decodeRspCode(rspModel,callback);
                }
            }
            @Override
            public void onFailure(Call<RspModel<List<UserCard>>> call, Throwable t) {
                if(callback!=null){
                    callback.onDataNotAvailable(R.string.data_network_error);
                }
            }
        });
        return call;
    }

    /**
     * 关注的网络请求
     * @param id
     * @param callback
     */
    public static void follow(String id, final DataSource.Callback<UserCard> callback) {
        RemoteService service = NetWork.remote();
        //得到一个call
        Call<RspModel<UserCard>> call = service.userFollow(id);
        //异步
        call.enqueue(new Callback<RspModel<UserCard>>() {
            @Override
            public void onResponse(Call<RspModel<UserCard>> call, Response<RspModel<UserCard>> response) {
                RspModel<UserCard> rspModel = response.body();
                if (rspModel.success()){
                    UserCard userCard= rspModel.getResult();
                    Factory.getUserCenter().dispatch(userCard);
                    //TODO 通知联系人列表刷新
                    callback.onDataLoaded(userCard);
                }else{
                    Factory.decodeRspCode(rspModel,callback);
                }
            }
            @Override
            public void onFailure(Call<RspModel<UserCard>> call, Throwable t) {
                if(callback!=null){
                    callback.onDataNotAvailable(R.string.data_network_error);
                }
            }
        });
    }

    /**
     * 刷新联系人的操作
     * 不需要返回Callback ，直接存储到数据库并通过数据库观察者进行通知界面更新
     * 界面更新的时候进行对比，然后差异更新
     */
    public static void refreshContacts(){
        RemoteService service = NetWork.remote();
        //得到一个call
        Call<RspModel<List<UserCard>>> call = service.userContacts();
        //异步
        call.enqueue(new Callback<RspModel<List<UserCard>>>() {
            @Override
            public void onResponse(Call<RspModel<List<UserCard>>> call, Response<RspModel<List<UserCard>>> response) {
                RspModel<List<UserCard>> rspModel = response.body();
                if (rspModel.success()){
                    List<UserCard> userCards = rspModel.getResult();
                    if (userCards==null||userCards.size()==0)
                        return;
//                  Factory.getUserCenter().dispatch(userCards.toArray(new UserCard[0]));
                    Factory.getUserCenter().dispatch(CollectionUtil.toArray(userCards,UserCard.class));
                }else{
                    Factory.decodeRspCode(rspModel,null);
                }
            }
            @Override
            public void onFailure(Call<RspModel<List<UserCard>>> call, Throwable t) {
                //do nothing
            }
        });
    }

    public static User findFromLocal(String id){
       return SQLite.select()
                .from(User.class)
                .where(User_Table.id.eq(id))
                .querySingle();
    }


    public static User findFromNet(String id){
        RemoteService remoteService = NetWork.remote();
        try {
            Response<RspModel<UserCard>> response = remoteService.userFind(id).execute();
            UserCard card = response.body().getResult();
            if (card!=null){
                //数据库的刷新
               Factory.getUserCenter().dispatch(card);
                //TODO
                return card.build();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 搜索 用户
     * 优先本地缓存，没有就从网络拉取
     * @param id
     * @return
     */
    public static User search(String id){
        User user = findFromLocal(id);
        if (user==null){
            return findFromNet(id);
        }
        return user;
    }
    /**
     * 搜索 用户
     * 优先网络拉取，没有就从本地
     * @param id
     * @return
     */
    public static User searchFirstOfN (String id){
        User user = findFromNet(id);
        if (user==null){
            return findFromLocal(id);
        }
        return user;

    }

}
