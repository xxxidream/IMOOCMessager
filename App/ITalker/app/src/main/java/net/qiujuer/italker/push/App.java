package net.qiujuer.italker.push;

import com.igexin.sdk.PushManager;

import net.qiujuer.italker.common.app.Application;
import net.qiujuer.italker.factory.Factory;

/**
 * Created by 16571 on 2017/8/15.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //调用Factory进行初始化
        Factory.setup();
        //推送进行初始化
        PushManager.getInstance().initialize(this);
    }
}
