package net.qiujuer.italker.factory.model.api.account;

import net.qiujuer.italker.factory.model.db.User;

/**
 * Created by admin on 2017/8/18.
 */

public class AccountRspModel {
    private User user;
    private String account;
    private String token;
    //标识是否已经绑定到设备pushId
    private boolean isBind;

    public boolean isBind() {
        return isBind;
    }

    public void setBind(boolean bind) {
        isBind = bind;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "AccountRspModel{" +
                "user=" + user +
                ", account='" + account + '\'' +
                ", token='" + token + '\'' +
                ", isBind=" + isBind +
                '}';
    }
}
