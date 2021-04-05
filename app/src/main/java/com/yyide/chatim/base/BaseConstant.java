package com.yyide.chatim.base;


import okhttp3.MediaType;

/**
 * Created by Hao on 17/10/26.
 * 常量
 */
public class BaseConstant {
    /**
     * app打开次数
     */
    public static String openTimes = "OPENTIMES";
    /**
     * app登陆成功
     */
    public static String loginSuccess = "LOGINSUCCESS";
    /**
     * 分页加载数量
     */
    public static final int PAGE_SIZE = 20;

    /*请求成功消息编码*/
    public static int REQUEST_SUCCES = 10000;

    public static int REQUEST_SUCCES2 = 200;

    /*账号*/
    public static String LOGINNAME = "LOGINNAME";

    /*密码*/
    public static String PASSWORD = "PASSWORD";
    /**
     * 请求ip==>baseUrl
     */
    public static final String URL_IP = "https://api.uat.edu.1d1j.net";
//    public static final String URL_IP = "http://192.168.3.147:8888";


    /*Base宽*/
    public static int BaseWith =171;

    /*Base高*/
    public static int BaseHeight =177;

    public static final MediaType JSON= MediaType.parse("application/json; charset=utf-8");


    /**
     * request Code 从相册选择照片不裁切
     **/
    public final static int SELECT_ORIGINAL_PIC = 126;
    /**
     * request Code 拍取照片不裁切
     **/
    public final static int REQ_CODE = 127;

    /**
     * EventBus 常量
     */
    public static final String TYPE_CHECK_HELP_CENTER = "type_check_help_center";
    public static final String TYPE_UPDATE_USER_PHONE = "type_update_user_phone";
    public static final String TYPE_UPDATE_USER_EMAIL = "type_update_user_email";
    public static final String TYPE_UPDATE_USER_SEX = "type_update_user_sex";
    public static final String TYPE_UPDATE_USER_BIRTHDAY = "type_update_user_birthday";

    public static final String CheakId = "1";
}
