package net.qiujuer.italker.factory.persistence;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import net.qiujuer.italker.factory.Factory;
import net.qiujuer.italker.factory.model.api.account.AccountRspModel;
import net.qiujuer.italker.factory.model.db.User;
import net.qiujuer.italker.factory.model.db.User_Table;

/**
 * Created by admin on 2017/8/28.
 */

public class Account {
    private static final String KEY_PUSH_ID="KEY_PUSH_ID";
    private static final String KEY_IS_BIND="KEY_IS_BIND";
    private static final String KEY_TOKEN="KEY_TOKEN";
    private static final String KEY_USER_ID="KEY_USER_ID";
    private static final String KEY_ACCOUNT="KEY_ACCOUNT";
    //设备的推送id
    private static String pushId ;
    private static boolean isBind ;
    private static String token ;
    private static String userId ;
    private static String account ;

    public static void setPushId(String pushId){
        Account.pushId = pushId;
        Account.save(Factory.app());
    }
    public static void load(Context context){
        SharedPreferences sp = context.getSharedPreferences(Account.class.getName(),Context.MODE_PRIVATE);
        pushId = sp.getString(KEY_PUSH_ID,"");
        isBind = sp.getBoolean(KEY_IS_BIND,false);
        token = sp.getString(KEY_TOKEN,"");
        userId = sp.getString(KEY_USER_ID,"");
        account = sp.getString(KEY_ACCOUNT,"");
    }
    public static void save(Context context){
        SharedPreferences sp = context.getSharedPreferences(Account.class.getName(),Context.MODE_PRIVATE);
        sp.edit().putString(KEY_PUSH_ID,pushId)
                .putBoolean(KEY_IS_BIND,isBind)
                .putString(KEY_TOKEN,token)
                .putString(KEY_USER_ID,userId)
                .putString(KEY_ACCOUNT,account)
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

        return !TextUtils.isEmpty(userId)
                && !TextUtils.isEmpty(token);

    }

    /**
     * 是否已经完善了用户信息
     * @return
     */
    public static boolean isComplete(){
        if(isLogin()){
            User self = getUser();
            return !TextUtils.isEmpty(self.getDesc())
                  &&!TextUtils.isEmpty(self.getPortrait())
                    &&self.getSex()!=0 ;
        }
        return false;
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

    /**
     * 保存自己的信息
     * @param model
     */
    public static void login(AccountRspModel model){
        //存储用户的token、用户id方便从数据库中查询我的信息
        Account.token = model.getToken();
        Account.account = model.getAccount();
        Account.userId = model.getUser().getId();
        save(Factory.app());
    }

    public static User getUser(){
        return TextUtils.isEmpty(userId)?new User(): SQLite.select()
                .from(User.class)
                .where(User_Table.id.eq(userId))
                .querySingle();
    }
    public static String getToken(){
        return token;
    }
}
