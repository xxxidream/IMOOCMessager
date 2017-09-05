package net.qiujuer.italker.factory.data.helper;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;

import net.qiujuer.italker.factory.model.db.AppDatabase;
import net.qiujuer.italker.factory.model.db.Group;
import net.qiujuer.italker.factory.model.db.GroupMember;
import net.qiujuer.italker.factory.model.db.Group_Table;
import net.qiujuer.italker.factory.model.db.Message;
import net.qiujuer.italker.factory.model.db.Session;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by admin on 2017/9/4.
 * 数据库的辅助工具类
 * 辅助完成 增删改
 */

public class DbHelper {
    private static final DbHelper instance;
    static {
        instance = new DbHelper();
    }
    private DbHelper(){
    }

    /**
     * 观察者集合
     * Class<?> 观察的表
     * Set<ChangedListener> 每个表对应的观察者有很多
     */
    private final Map<Class<?>,Set<ChangedListener>> changeListeners = new HashMap<>();

    /**
     * 从所有的监听器中，获取某一个表的所有监听者
     * @param modelClass
     * @param <Model>
     * @return
     */
    private <Model extends BaseModel> Set<ChangedListener> getListeners(Class<Model> modelClass){
        if (changeListeners.containsKey(modelClass))
            return changeListeners.get(modelClass);
        return null;
    }
    /**
     * 添加一个监听
     * @param tClass 对某个表监听
     * @param changedListener 监听者
     * @param <Model> 表的泛型
     */
    public static <Model extends BaseModel> void addChangedListener(final Class<Model> tClass,ChangedListener<Model> changedListener){
        Set<ChangedListener> changedListeners = instance.getListeners(tClass);
        if (changedListeners==null){
            changedListeners = new HashSet<>();
            //添加到总的map中
            instance.changeListeners.put(tClass,changedListeners);
        }
        changedListeners.add(changedListener);

    }

    public static <Model extends BaseModel> void removeChangedListener(final Class<Model> tClass,ChangedListener<Model> changedListener){
        Set<ChangedListener> changedListeners = instance.getListeners(tClass);
        if (changedListeners==null){
            return;
        }
        //从容器中删除你这个监听者
        changedListeners.remove(changedListener);
    }
    /**
     * 新增或者修改的统一方法
     * @param tClass 传递一个class 信息
     * @param models 这个class 对应的实例的数组
     * @param <Model> 这个实例的泛型，限定条件是BaseModel
     */
    public static<Model extends BaseModel> void save(final Class<Model> tClass, final Model...models){
        if (models ==null || models.length==0)
            return;
        DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
        definition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                ModelAdapter<Model> adapter = FlowManager.getModelAdapter(tClass);
//              adapter.saveAll(CollectionUtil.toArrayList(users));
                adapter.saveAll(Arrays.asList(models));
                instance.notifySave(tClass,models);
            }
        }).build().execute();
    }

    /**
     * 删除数据库的统一封装
     * @param tClass 传递一个class 信息
     * @param models 这个class 对应的实例的数组
     * @param <Model> 这个实例的泛型，限定条件是BaseModel
     */
    public static<Model extends BaseModel> void delete(final Class<Model> tClass, final Model...models){
        if (models ==null || models.length==0)
            return;
        DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
        definition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                ModelAdapter<Model> adapter = FlowManager.getModelAdapter(tClass);
//              adapter.saveAll(CollectionUtil.toArrayList(users));
                adapter.deleteAll(Arrays.asList(models));
                instance.notifyDelete(tClass,models);
            }
        }).build().execute();
    }

    /**
     * 进行通知调用
     * @param tClass 通知的类型
     * @param models 通知的model数组
     * @param <Model> 这个实例的泛型，限定条件是BaseModel
     */
    private final <Model extends BaseModel> void notifySave(final Class<Model> tClass,
                                                            final Model...models){
        //找监听器
        final Set<ChangedListener> listeners = getListeners(tClass);
        if (listeners!=null&&listeners.size()>0){
            //通用的通知
            for (ChangedListener<Model> listener : listeners) {
                listener.onDataSave(models);
            }
        }
        //例外情况
        //群成员变更，需要更新对应群信息更新
        //消息变化，应该通知会话列表更新
        if (GroupMember.class.equals(tClass)){
            updateGroup((GroupMember[]) models);
        }else if (Message.class.equals(tClass)){
            updateSession((Message[]) models);
        }

    }

    /**
     * 从成员中找出成员对应的群，并对群进行更新
     * @param members
     */
    private void updateGroup(GroupMember... members){
        final Set<String> groupIds = new HashSet<>();
        for (GroupMember member : members) {
            groupIds.add(member.getGroup().getId());
        }

        //更新群的信息
        DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
        definition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                //找到需要通知的群
                List<Group> groups = SQLite.select()
                        .from(Group.class)
                        .where(Group_Table.id.in(groupIds))
                        .queryList();
               instance.notifySave(Group.class,groups.toArray(new Group[0]));
            }
        }).build().execute();

    }

    /**
     * 从消息列表中，选出对应的会话并进行更新
     * @param messages
     */
    private void updateSession(Message... messages){
        final Set<Session.Identify> identifies = new HashSet<>();
        for (Message message : messages) {
            Session.Identify identify =  Session.createSessionIdentify(message);
            identifies.add(identify);
        }
        //更新群的信息
        DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
        definition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                //找到需要通知的群
                ModelAdapter<Session> adapter = FlowManager.getModelAdapter(Session.class);
                Session[] sessions = new Session[identifies.size()];
                int index = 0;
                for (Session.Identify identify : identifies) {
                    Session session = SessionHelper.findFromLocal(identify.id);
                    if (session==null){
                        session = new Session(identify);//第一次聊天,创建一个你和对方的会话
                    }
                    //刷新会话对应的信息为当前Message的最新状态
                    session.refreshToNow();
                    adapter.save(session);
                    //添加到集合
                    sessions[index++] = session;
                }

                instance.notifySave(Session.class,sessions);
            }
        }).build().execute();


    }

    /**
     * 进行通知调用
     * @param tClass 通知的类型
     * @param models 通知的model数组
     * @param <Model> 这个实例的泛型，限定条件是BaseModel
     */
    private final <Model extends BaseModel> void notifyDelete(final Class<Model> tClass,
                                                              final Model...models){
        //找监听器
        final Set<ChangedListener> listeners = getListeners(tClass);
        if (listeners!=null&&listeners.size()>0){
            //通用的通知
            for (ChangedListener<Model> listener : listeners) {
                listener.onDataDelete(models);
            }
        }
        //例外情况
        //群成员变更，需要更新对应群信息更新
        //消息变化，应该通知会话列表更新
        if (GroupMember.class.equals(tClass)){
            updateGroup((GroupMember[]) models);
        }else if (Message.class.equals(tClass)){
            updateSession((Message[]) models);
        }

    }

    @SuppressWarnings("unused")
    public interface ChangedListener<Data extends BaseModel>{
        void onDataSave(Data... list);
        void onDataDelete(Data... list);
    }
}
