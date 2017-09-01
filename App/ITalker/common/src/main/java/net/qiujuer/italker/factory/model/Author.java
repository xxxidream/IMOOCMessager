package net.qiujuer.italker.factory.model;

/**
 * Created by admin on 2017/9/1.
 * 基础用户接口
 */

public interface Author {
    String getId();
    void setId(String id);
    String getName();
    void setName(String name);
    String getPortrait();
    void setPortrait(String portrait);
}
