package net.qiujuer.italker.factory.net;

import android.text.TextUtils;

import net.qiujuer.italker.common.Common;
import net.qiujuer.italker.factory.Factory;
import net.qiujuer.italker.factory.persistence.Account;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by admin on 2017/8/18.
 */

public class NetWork {
    private static NetWork instance;
    private Retrofit retrofit;
    static {
        instance = new NetWork();
    }
    private  NetWork(){
    }

    public static Retrofit getRetrofit(){
        if (instance.retrofit!=null)
            return instance.retrofit;
        //得到一个ok 的client
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        Request.Builder builder = original.newBuilder();
                        if (!TextUtils.isEmpty(Account.getToken())){
                            builder.addHeader("token",Account.getToken());
                        }
                        builder.addHeader("Content-type","application/json");
                        Request newRequest = builder.build();
                        return chain.proceed(newRequest);
                    }

                })
                .build();
        Retrofit.Builder builder = new Retrofit.Builder();
        instance.retrofit = builder.baseUrl(Common.Constant.API_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(Factory.getGson()))
                .build();
        return instance.retrofit;
    }

    public static RemoteService remote(){
        RemoteService service = NetWork.getRetrofit().create(RemoteService.class);
        return service;
    }
}
