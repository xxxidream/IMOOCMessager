package net.qiujuer.italker.common;

/**
 * @author qiujuer
 */

public class Common {
    /**
     * 一些不可变的永恒的参数
     * 通常用户一些配置
     */
    public interface Constant{
        //手机号的正则 11位手机号
        String REGEX_MOBILE = "[1][3,4,5,7,8][0-9]{9}$";
        String API_URL = "http:192.168.32.89:8080/api/";
    }

}
