package net.qiujuer.italker.factory.data.message;

import net.qiujuer.italker.factory.model.card.MessageCard;

/**
 * Created by admin on 2017/9/4.
 */

public interface MessageCenter {
    //分发处理一堆卡片并更新到数据库
    void dispatch(MessageCard... cards);
}
