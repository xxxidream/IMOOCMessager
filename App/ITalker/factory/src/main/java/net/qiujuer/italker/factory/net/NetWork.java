package net.qiujuer.italker.factory.net;

import net.qiujuer.italker.common.Common;
import net.qiujuer.italker.factory.Factory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by admin on 2017/8/18.
 */

public class NetWork {
    public static Retrofit getRetrofit(){
        //得到一个ok 的client
        OkHttpClient client = new OkHttpClient.Builder().build();
        Retrofit.Builder builder = new Retrofit.Builder();
        return builder.baseUrl(Common.Constant.API_URL)
                                    .client(client)
                                    .addConverterFactory(GsonConverterFactory.create(Factory.getGson()))
                                    .build();

    }
}
