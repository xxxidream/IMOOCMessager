package net.qiujuer.italker.factory.net;

import net.qiujuer.italker.factory.model.api.RspModel;
import net.qiujuer.italker.factory.model.api.account.AccountRspModel;
import net.qiujuer.italker.factory.model.api.account.LoginModel;
import net.qiujuer.italker.factory.model.api.account.RegisterModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * 网络请求的所有接口
 * Created by admin on 2017/8/18.
 */

public interface RemoteService {
    /**
     * 注册接口
     * @param model RegisterModel
     * @return RspModel<AccountRspModel>
     */
    @POST("account/register")
    Call<RspModel<AccountRspModel>> accountRegister(@Body RegisterModel model);

    /**
     * 登录接口
     * @param model LoginModel
     * @return
     */
    @POST("account/login")
    Call<RspModel<AccountRspModel>> accountLogin(@Body LoginModel model);
    /**
     * //绑定设备id
     * @param pushId
     * @return
     */
    @POST("account/bind/{pushId}")
    Call<RspModel<AccountRspModel>> accountBind(@Path(encoded = true,value ="pushId") String pushId);
}
