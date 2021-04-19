package com.yyide.chatim.base;


import android.os.Build;

import com.yyide.chatim.BuildConfig;

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
//    public static final String API_SERVER_URL = "http://192.168.3.114:8888";
    public static final String API_SERVER_URL_UAT = "https://api.uat.edu.1d1j.net";
    //线上环境
    public static final String API_SERVER_URL_RELEASE = "https://api.edu.1d1j.cn";
    public static final String API_SERVER_URL = BuildConfig.DEBUG ? API_SERVER_URL_UAT : API_SERVER_URL_RELEASE;

    /*Base宽*/
    public static int BaseWith = 171;

    /*Base高*/
    public static int BaseHeight = 177;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    /**
     * request Code 从相册选择照片不裁切
     **/
    public final static int SELECT_ORIGINAL_PIC = 126;
    /**
     * request Code 拍取照片不裁切
     **/
    public final static int REQ_CODE = 127;

    public final static String JG_ALIAS_NAME = "aliasName";
    public final static String JG_SEQUENCE = "sequence";

    /**
     * EventBus 常量
     */
    public static final String TYPE_CHECK_HELP_CENTER = "type_check_help_center";
    public static final String TYPE_UPDATE_USER_PHONE = "type_update_user_phone";
    public static final String TYPE_UPDATE_USER_EMAIL = "type_update_user_email";
    public static final String TYPE_UPDATE_USER_SEX = "type_update_user_sex";
    public static final String TYPE_SELECT_MESSAGE_TODO = "type_select_message_todo";

    public static final String TYPE_UPDATE_HOME = "type_update_home";

    public static final String TYPE_UPDATE_IMG = "type_update_img";

    public static final String TYPE_PREPARES_SAVE = "type_prepares_save";
    public static final String TYPE_UPDATE_CLASS_HOME = "type_update_class_home";
    public static final String TYPE_UPDATE_APP_MANAGER = "type_update_app_manager";

    public static final String CheakId = "1";
}
