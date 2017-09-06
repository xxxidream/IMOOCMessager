package net.qiujuer.italker.factory;

import android.support.annotation.StringRes;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import net.qiujuer.italker.common.app.Application;
import net.qiujuer.italker.factory.data.DataSource;
import net.qiujuer.italker.factory.data.group.GroupCenter;
import net.qiujuer.italker.factory.data.group.GroupDispatcher;
import net.qiujuer.italker.factory.data.message.MessageCenter;
import net.qiujuer.italker.factory.data.message.MessageDispatcher;
import net.qiujuer.italker.factory.data.user.UserCenter;
import net.qiujuer.italker.factory.data.user.UserDispatcher;
import net.qiujuer.italker.factory.model.api.PushModel;
import net.qiujuer.italker.factory.model.api.RspModel;
import net.qiujuer.italker.factory.model.card.GroupCard;
import net.qiujuer.italker.factory.model.card.GroupMemberCard;
import net.qiujuer.italker.factory.model.card.MessageCard;
import net.qiujuer.italker.factory.model.card.UserCard;
import net.qiujuer.italker.factory.persistence.Account;
import net.qiujuer.italker.factory.utils.DBFlowExclusionStrategy;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by admin on 2017/8/16.
 */

public class Factory {
    private static final String TAG = Factory.class.getSimpleName();
    //单例模式
    private static final Factory instance;
    private final Executor executor;
    //全局的gson
    private final Gson gson;
    static {
        instance = new Factory();
    }
    private Factory(){
        executor = Executors.newFixedThreadPool(4);
        gson = new GsonBuilder()
                //设置事件格式
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                .setExclusionStrategies(new DBFlowExclusionStrategy())
                .create();
    }

    /**
     * Factory 中的初始化
     */
    public static void setup(){
        FlowManager.init(new FlowConfig.Builder(app()).
                openDatabasesOnInit(true)
                .build());
        Account.load(app());
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
    public static Gson getGson(){
        return instance.gson;
    }


    /**
     * 进行错误Code的解析，
     * 把网络返回的Code值进行统一的规划并返回为一个String资源
     *
     * @param model    RspModel
     * @param callback DataSource.FailedCallback 用于返回一个错误的资源Id
     */
    public static void decodeRspCode(RspModel model, DataSource.FailedCallback callback) {
        if (model == null)
            return;

        // 进行Code区分
        switch (model.getCode()) {
            case RspModel.SUCCEED:
                return;
            case RspModel.ERROR_SERVICE:
                decodeRspCode(R.string.data_rsp_error_service, callback);
                break;
            case RspModel.ERROR_NOT_FOUND_USER:
                decodeRspCode(R.string.data_rsp_error_not_found_user, callback);
                break;
            case RspModel.ERROR_NOT_FOUND_GROUP:
                decodeRspCode(R.string.data_rsp_error_not_found_group, callback);
                break;
            case RspModel.ERROR_NOT_FOUND_GROUP_MEMBER:
                decodeRspCode(R.string.data_rsp_error_not_found_group_member, callback);
                break;
            case RspModel.ERROR_CREATE_USER:
                decodeRspCode(R.string.data_rsp_error_create_user, callback);
                break;
            case RspModel.ERROR_CREATE_GROUP:
                decodeRspCode(R.string.data_rsp_error_create_group, callback);
                break;
            case RspModel.ERROR_CREATE_MESSAGE:
                decodeRspCode(R.string.data_rsp_error_create_message, callback);
                break;
            case RspModel.ERROR_PARAMETERS:
                decodeRspCode(R.string.data_rsp_error_parameters, callback);
                break;
            case RspModel.ERROR_PARAMETERS_EXIST_ACCOUNT:
                decodeRspCode(R.string.data_rsp_error_parameters_exist_account, callback);
                break;
            case RspModel.ERROR_PARAMETERS_EXIST_NAME:
                decodeRspCode(R.string.data_rsp_error_parameters_exist_name, callback);
                break;
            case RspModel.ERROR_ACCOUNT_TOKEN:
                Application.showToast(R.string.data_rsp_error_account_token);
                instance.logout();
                break;
            case RspModel.ERROR_ACCOUNT_LOGIN:
                decodeRspCode(R.string.data_rsp_error_account_login, callback);
                break;
            case RspModel.ERROR_ACCOUNT_REGISTER:
                decodeRspCode(R.string.data_rsp_error_account_register, callback);
                break;
            case RspModel.ERROR_ACCOUNT_NO_PERMISSION:
                decodeRspCode(R.string.data_rsp_error_account_no_permission, callback);
                break;
            case RspModel.ERROR_UNKNOWN:
            default:
                decodeRspCode(R.string.data_rsp_error_unknown, callback);
                break;
        }
    }
    private static void decodeRspCode(@StringRes final int resId,
                                      final DataSource.FailedCallback callback) {
        if (callback != null)
            callback.onDataNotAvailable(resId);
    }
    /**
     * 收到账户退出的消息需要进行账户退出重新登录
     */
    private void logout() {

    }
    /**
     * 处理推送来的消息
     *
     * @param str 消息
     */
    public static void dispatchPush(String str) {
        if (!Account.isLogin()){
            return;
        }
        PushModel model = PushModel.decode(str);
        if (model==null)
            return;
        Log.e(TAG,model.toString());
        for (PushModel.Entity entity : model.getEntities()) {
            switch (entity.type){
                case PushModel.ENTITY_TYPE_LOGOUT:
                   instance.logout();
                    return;
                case PushModel.ENTITY_TYPE_MESSAGE:{
                    //普通消息
                    MessageCard card = getGson().fromJson(entity.content,MessageCard.class);
                    getMessageCenter().dispatch(card);
                    break;
                }
                case PushModel.ENTITY_TYPE_ADD_FRIEND:{
                    UserCard card = getGson().fromJson(entity.content,UserCard.class);
                    getUserCenter().dispatch(card);
                    break;
                }
                case PushModel.ENTITY_TYPE_ADD_GROUP:{
                    GroupCard card = getGson().fromJson(entity.content,GroupCard.class);
                    getGroupCenter().dispatch(card);
                    break;
                }
                //群成员变更，回来的是一个群成员的列表
                case PushModel.ENTITY_TYPE_MODIFY_GROUP_MEMBERS:
                case PushModel.ENTITY_TYPE_ADD_GROUP_MEMBERS:{
                    Type type = new TypeToken<List<GroupMemberCard>>(){}.getType();
                    List<GroupMemberCard> card = getGson().fromJson(entity.content,type);
                    getGroupCenter().dispatch(card.toArray(new GroupMemberCard[0]));
                    break;
                }
                case PushModel.ENTITY_TYPE_EXIT_GROUP_MEMBERS:{
                    //TODO
                    //成员退出的推送
                }

            }
        }


    }

    /**
     * 获取一个用户中心的实现类
     * 用户中心的规范接口
     * @return
     */
    public static UserCenter getUserCenter(){
        return UserDispatcher.instance();
    }

    /**
     * 获取一个消息中心的实现类
     * @return
     */
    public static MessageCenter getMessageCenter(){
        return MessageDispatcher.instance();
    }


    /**
     * 获取一个群中心的实现类
     * @return
     */
    public static GroupCenter getGroupCenter(){
        return GroupDispatcher.instance();
    }

}
