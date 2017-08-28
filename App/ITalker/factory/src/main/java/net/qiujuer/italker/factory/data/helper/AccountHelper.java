package net.qiujuer.italker.factory.data.helper;

import net.qiujuer.italker.factory.Factory;
import net.qiujuer.italker.factory.R;
import net.qiujuer.italker.factory.data.DataSource;
import net.qiujuer.italker.factory.model.api.RspModel;
import net.qiujuer.italker.factory.model.api.account.AccountRspModel;
import net.qiujuer.italker.factory.model.api.account.RegisterModel;
import net.qiujuer.italker.factory.model.db.User;
import net.qiujuer.italker.factory.net.Network;
import net.qiujuer.italker.factory.net.RemoteService;
import net.qiujuer.italker.factory.persistence.Account;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by admin on 2017/8/18.
 */

public class AccountHelper {
    public static void register(RegisterModel model, final DataSource.Callback<User> callback){
        RemoteService service = Network.getRetrofit().create(RemoteService.class);
        //得到一个call
        Call<RspModel<AccountRspModel>> call = service.accountRegister(model);
        //异步
        call.enqueue(new Callback<RspModel<AccountRspModel>>() {
            @Override
            public void onResponse(Call<RspModel<AccountRspModel>> call,
                                   Response<RspModel<AccountRspModel>> response) {
                //从返回中得到我们的全局model内部是使用gson进行解析
                RspModel<AccountRspModel> rspModel = response.body();
                if(rspModel.success()) {
                    AccountRspModel accountRspModel = rspModel.getResult();
                    if(accountRspModel.isBind()){
                        User user = accountRspModel.getUser();
                        //TODO 数据库写入和缓存绑定
                        //然后返回
                        callback.onDataLoaded(user);
                    }else{
                        bindPush(callback);
                    }

                }else{
                    // 对返回的RspModel中的失败的code进行解析，解析到对应的String
                    Factory.decodeRspCode(rspModel,callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<AccountRspModel>> call, Throwable t) {
                //网络请求失败
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
    }

    /**
     * 对设备id进行绑定
     * @param callback
     */
    public static void bindPush(final DataSource.Callback<User> callback){
        Account.setBind(true);
    }
}
