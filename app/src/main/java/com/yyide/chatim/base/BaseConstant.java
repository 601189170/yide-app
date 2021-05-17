package com.yyide.chatim.base;

import com.yyide.chatim.BuildConfig;

import okhttp3.MediaType;

/**
 * Created by Hao on 17/10/26.
 * 常量
 */
public class BaseConstant {
    /**
     * 分页加载数量
     */
    public static final int PAGE_SIZE = 20;

    /*请求成功消息编码*/
    public static int REQUEST_SUCCES = 10000;

    public static int REQUEST_SUCCES2 = 200;

    /**
     * 腾讯IM APPID
     */
    //线上版本APPID
    public static final int SDKAPPID_RELEASE = 1400511951;
    //UAT- APPID
    public static final int SDKAPPID_UAT = 1400521307;

    public static final int SDKAPPID = BuildConfig.DEBUG ? SDKAPPID_UAT : SDKAPPID_RELEASE;

    /*账号*/
    public static String LOGINNAME = "LOGINNAME";

    /*密码*/
    public static String PASSWORD = "PASSWORD";
    /**
     * 请求ip==>baseUrl
     */
//    public static final String API_SERVER_URL_UAT = "http://192.168.3.114:8888";
    public static final String API_SERVER_URL_RELEASE = "https://api.edu.1d1j.cn";
    public static final String API_SERVER_URL_UAT = "https://api.uat.edu.1d1j.net";
    //线上环境
    public static final String API_SERVER_URL = BuildConfig.DEBUG ? API_SERVER_URL_UAT : API_SERVER_URL_RELEASE;


    public static final String API_SERVER_HTML_RELEASE = "https://cloud.edu.1d1j.cn";
    public static final String API_SERVER_HTML_UAT = "https://cloud.uat.edu.1d1j.net";
    public static final String API_SERVER_HTML = BuildConfig.DEBUG ? API_SERVER_HTML_UAT : API_SERVER_HTML_RELEASE;
    //学生作品
    public static final String STUDENT_HONOR_URL = API_SERVER_HTML + "/classcardapp/dist/index.html#/studentWorks";
    //班级相册
    public static final String CLASS_PHOTO_URL = API_SERVER_HTML + "/classcardapp/dist/index.html#/classPhoto";

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
    public static final String TYPE_UPDATE_USER_EMAIL = "type_update_user_email";
    public static final String TYPE_UPDATE_USER_SEX = "type_update_user_sex";
    public static final String TYPE_SELECT_MESSAGE_TODO = "type_select_message_todo";
    public static final String TYPE_MAIN = "type_main";
    public static final String TYPE_IM_LOGIN = "type_im_login";
    public static final String TYPE_UPDATE_HOME = "type_update_home";
    public static final String TYPE_UPDATE_IMG = "type_update_img";
    public static final String TYPE_PREPARES_SAVE = "type_prepares_save";
    public static final String TYPE_UPDATE_APP_MANAGER = "type_update_app_manager";
    public static final String TYPE_UPDATE_APP = "type_update_app";

}
