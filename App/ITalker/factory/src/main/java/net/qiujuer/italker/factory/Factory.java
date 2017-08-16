package net.qiujuer.italker.factory;

import net.qiujuer.italker.common.app.Application;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by admin on 2017/8/16.
 */

public class Factory {
    //单例模式
    private static final Factory instance;
    private final Executor executor;
    static {
        instance = new Factory();
    }
    private Factory(){
        executor = Executors.newFixedThreadPool(4);
    }

    /**
     * 返回全局的Application
     * @return
     */
    public static Application app(){
        return Application.getInstance();
    }

    /**
     * 异步运行的方法
     * @param runnable Runnable
     */
    public static void runOnAsync(Runnable runnable){
        //拿到单例在线程池中异步执行
        instance.executor.execute(runnable);
    }
}
