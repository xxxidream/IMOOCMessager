package net.qiujuer.italker.factory.data.helper;

import net.qiujuer.italker.factory.Factory;
import net.qiujuer.italker.factory.R;
import net.qiujuer.italker.factory.data.DataSource;
import net.qiujuer.italker.factory.model.api.RspModel;
import net.qiujuer.italker.factory.model.api.user.UserUpdateModel;
import net.qiujuer.italker.factory.model.card.UserCard;
import net.qiujuer.italker.factory.model.db.User;
import net.qiujuer.italker.factory.net.NetWork;
import net.qiujuer.italker.factory.net.RemoteService;

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
                    User user = userCard.build();
                    user.save();
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
                    User user = userCard.build();
                    user.save();
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
     * @param callback
     * @return
     */
    public static void refreshContacts(final DataSource.Callback<List<UserCard>> callback){
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
    }

}
