package net.qiujuer.italker.factory.presenter.contact;

import net.qiujuer.italker.factory.model.db.User;
import net.qiujuer.italker.factory.presenter.BaseContract;

/**
 * Created by admin on 2017/9/1.
 */

public interface ContactContract {
    //什么都不需要额外定义，开始就是调用start()
    interface Presenter extends BaseContract.Presenter{

    }
    interface View extends  BaseContract.RecyclerView<Presenter,User>{
    }
}
