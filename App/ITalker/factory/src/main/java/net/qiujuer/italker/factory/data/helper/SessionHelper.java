package net.qiujuer.italker.factory.data.helper;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import net.qiujuer.italker.factory.model.db.Session;
import net.qiujuer.italker.factory.model.db.Session_Table;

/**
 * Created by admin on 2017/9/4.
 * 会话辅助工具类
 */

public class SessionHelper {
    public static Session findFromLocal(String id) {
        return SQLite.select()
                .from(Session.class)
                .where(Session_Table.id.eq(id))
                .querySingle();
    }
}
