package net.qiujuer.italker.factory.persistence;

import android.content.Context;
import android.content.SharedPreferences;

import net.qiujuer.italker.factory.Factory;

/**
 * Created by admin on 2017/8/28.
 */

public class Account {
    private static final String KEY_PUSH_ID="KEY_PUSH_ID";
    private static final String KEY_IS_BIND="KEY_IS_BIND";
    //设备的推送id
    private static String pushId ;
    private static boolean isBind ;


    public static void setPushId(String pushId){
        Account.pushId = pushId;
        Account.save(Factory.app());
    }
    public static void load(Context context){
        SharedPreferences sp = context.getSharedPreferences(Account.class.getName(),Context.MODE_PRIVATE);
        pushId = sp.getString(KEY_PUSH_ID,"");
        isBind = sp.getBoolean(KEY_IS_BIND,false);
    }
    public static void save(Context context){
        SharedPreferences sp = context.getSharedPreferences(Account.class.getName(),Context.MODE_PRIVATE);
        sp.edit().putString(KEY_PUSH_ID,pushId)
                .putBoolean(KEY_IS_BIND,isBind)
                .apply();
    }

    public  static String getPushId(){
        return pushId;
    }

    /**
     * 返回当前账户是否处以登录状态
     * @return
     */
    public static boolean isLogin(){
        return true;
    }
    /**
     * 是否绑定
     * @return
     */
    public static boolean isBind(){
        return isBind;
    }
    public static void setBind(boolean isBind){
        Account.isBind = isBind;
        Account.save(Factory.app());
    }
}
