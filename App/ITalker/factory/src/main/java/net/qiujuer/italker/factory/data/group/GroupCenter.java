package net.qiujuer.italker.factory.data.group;

import net.qiujuer.italker.factory.model.card.GroupCard;
import net.qiujuer.italker.factory.model.card.GroupMemberCard;

/**
 * Created by admin on 2017/9/4.
 */

public interface GroupCenter {
    //群卡片的处理
    void dispatch(GroupCard... cards);
    //群成员的处理
    void dispatch(GroupMemberCard... cards);
}
