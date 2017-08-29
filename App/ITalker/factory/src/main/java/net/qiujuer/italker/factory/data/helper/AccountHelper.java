package net.qiujuer.italker.factory.data.helper;

import android.text.TextUtils;

import net.qiujuer.italker.factory.Factory;
import net.qiujuer.italker.factory.R;
import net.qiujuer.italker.factory.data.DataSource;
import net.qiujuer.italker.factory.model.api.RspModel;
import net.qiujuer.italker.factory.model.api.account.AccountRspModel;
import net.qiujuer.italker.factory.model.api.account.LoginModel;
import net.qiujuer.italker.factory.model.api.account.RegisterModel;
import net.qiujuer.italker.factory.model.db.User;
import net.qiujuer.italker.factory.net.NetWork;
import net.qiujuer.italker.factory.net.RemoteService;
import net.qiujuer.italker.factory.persistence.Account;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by admin on 2017/8/18.
 */

public class AccountHelper {
    public static void register(final RegisterModel model, final DataSource.Callback<User> callback){
        RemoteService service = NetWork.remote();
        //得到一个call
        Call<RspModel<AccountRspModel>> call = service.accountRegister(model);
        //异步
        call.enqueue(new AccountRspCallback(callback));
    }

    /**
     *  登录的调用
     * @param model
     * @param callback
     */
    public static void login(final LoginModel model, final DataSource.Callback<User> callback){
        RemoteService service = NetWork.remote();
        //得到一个call
        Call<RspModel<AccountRspModel>> call = service.accountLogin(model);
        //异步
        call.enqueue(new AccountRspCallback(callback));
    }
    /**
     * 对设备id进行绑定
     * @param callback
     */
    public static void bindPush(final DataSource.Callback<User> callback){
      String pushId = Account.getPushId();
        if(TextUtils.isEmpty(pushId))
            return;
        RemoteService service = NetWork.remote();
        Call<RspModel<AccountRspModel>> call = service.accountBind(pushId);
        call.enqueue(new AccountRspCallback(callback));
    }

    /**
     * 请求的回调封装
     */
    private static class AccountRspCallback implements Callback<RspModel<AccountRspModel>>{
        final DataSource.Callback<User> callback;
        AccountRspCallback(DataSource.Callback<User> callback){
            this.callback = callback;
        }
        @Override
        public void onResponse(Call<RspModel<AccountRspModel>> call,
                               Response<RspModel<AccountRspModel>> response) {
            //从返回中得到我们的全局model内部是使用gson进行解析
            RspModel<AccountRspModel> rspModel = response.body();
            if(rspModel.success()) {
                AccountRspModel accountRspModel = rspModel.getResult();
                User user = accountRspModel.getUser();
                //TODO 数据库写入和缓存绑定
                //第一种直接保存
                user.save();
                //第二种通过ModelAdapter保存
//                        FlowManager.getModelAdapter(User.class)
//                                    .save(user);
                //第三种 事务中
//                        DatabaseDefinition definition = FlowManager.getDatabase(AppDataBase.class);
//                        definition.beginTransactionAsync(new ITransaction() {
//                            @Override
//                            public void execute(DatabaseWrapper databaseWrapper) {
//                                FlowManager.getModelAdapter(User.class)
//                                        .save(user);
//                            }
//                        }).build().execute();
                Account.login(accountRspModel);
                if(accountRspModel.isBind()){
                    //然后返回
                    Account.setBind(true);
                    if (callback!=null){
                        callback.onDataLoaded(user);
                    }
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
            if(callback!=null){
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        }
    }
}
