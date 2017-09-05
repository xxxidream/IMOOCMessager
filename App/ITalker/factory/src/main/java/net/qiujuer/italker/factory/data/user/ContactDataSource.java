package net.qiujuer.italker.factory.data.user;

import net.qiujuer.italker.factory.data.DataSource;
import net.qiujuer.italker.factory.model.db.User;

import java.util.List;

/**
 * Created by admin on 2017/9/5.
 * 联系人数据源
 */

public interface ContactDataSource {
    void load(DataSource.SuccessCallback<List<User>> callback);
    void dispose();
}
