package net.qiujuer.italker.factory.data.user;

import net.qiujuer.italker.factory.model.card.UserCard;

/**
 * Created by admin on 2017/9/4.
 * 用户中心的基本定义
 */

public interface UserCenter {
    //分发处理一堆卡片并更新到数据库
    void dispatch(UserCard... cards);
}
