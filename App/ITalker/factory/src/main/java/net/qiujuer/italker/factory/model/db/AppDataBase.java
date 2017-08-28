package net.qiujuer.italker.factory.model.db;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by admin on 2017/8/28.
 */
@Database(name = AppDataBase.NAME,version = AppDataBase.VERSION)
public class AppDataBase {
    public static final String NAME="AppDataBase";
    public static final int VERSION=  1;

}
