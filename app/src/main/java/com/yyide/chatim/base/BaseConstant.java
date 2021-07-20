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
    public static final int SDKAPPID_UAT = 1400511951;//1400537733

    public static final int SDKAPPID = BuildConfig.DEBUG ? SDKAPPID_UAT : SDKAPPID_RELEASE;

    /*账号*/
    public static String LOGINNAME = "LOGINNAME";

    /*密码*/
    public static String PASSWORD = "PASSWORD";

    //第一次打开app
    public static String FIRST_OPEN_APP = "firstOpenApp";

    /**
     * 请求ip==>baseUrl
     */
//    public static final String API_SERVER_URL_UAT = "http://192.168.3.154:8888";//Dev 环境
    public static final String API_SERVER_URL_RELEASE = "https://api.edu.1d1j.cn";
    public static final String API_SERVER_URL_UAT = "https://api.uat.edu.1d1j.net";

    //Http访问地址
    public static String API_SERVER_URL = BuildConfig.DEBUG ? API_SERVER_URL_UAT : API_SERVER_URL_RELEASE;

    //通知模板地址
    public static final String API_SERVER_HTML_RELEASE = "https://cloud.edu.1d1j.cn";
    public static final String API_SERVER_HTML_UAT = "https://cloud.uat.edu.1d1j.net";
    public static final String API_SERVER_HTML = BuildConfig.DEBUG ? API_SERVER_HTML_UAT : API_SERVER_HTML_RELEASE;

    //学生作品
    public static final String STUDENT_HONOR_URL = API_SERVER_HTML + "/classcardapp/dist/index.html#/studentWorks";
    public static final String STUDENT_FAMILY_URL = API_SERVER_HTML + "/classcardapp/dist/index.html#/patriarchProduction";
    //班级相册
    public static final String CLASS_PHOTO_URL = API_SERVER_HTML + "/classcardapp/dist/index.html#/classPhoto";

    public static final String SCAN_URL = API_SERVER_HTML + "/classcardapp/dist/index.html#/QRcodeLogin";

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    //通知公告模板地址
//    public static final String TEMPLATE_URL_DEV = "http://192.168.3.147:8005/#/notice/edit/";
    public static final String TEMPLATE_URL_RELEASE = "https://wap.edu.1d1j.cn/#/notice/edit/";
    public static final String TEMPLATE_URL_DEV = "https://app.uat.edu.1d1j.net/#/notice/edit/";

    public static final String TEMPLATE_URL = BuildConfig.DEBUG ? TEMPLATE_URL_DEV : TEMPLATE_URL_RELEASE;

    //隐私协议
    public static final String PRIVACY_URL = "https://file.1d1j.cn/privacyPolicy.html";

    public static final String AGREEMENT_URL = "https://file.1d1j.cn/serviceAgreement.html";
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

    public static final String SP_PRIVACY = "sp_privacy";

    /**
     * EventBus 常量
     */
    public static final String TYPE_CHECK_HELP_CENTER = "type_check_help_center";
    public static final String TYPE_UPDATE_USER_EMAIL = "type_update_user_email";
    public static final String TYPE_UPDATE_USER_SEX = "type_update_user_sex";
    public static final String TYPE_SELECT_MESSAGE_TODO = "type_select_message_todo";
    public static final String TYPE_MAIN = "type_main";
    public static final String TYPE_IM_LOGIN = "type_im_login";
    public static final String TYPE_MESSAGE_UPDATE = "type_message_update";
    public static final String TYPE_UPDATE_HOME = "type_update_home";
    public static final String TYPE_HOME_CHECK_IDENTITY = "type_home_check_identity";
    public static final String TYPE_UPDATE_NOTICE_TAB = "type_update_notice_tab";
    public static final String TYPE_UPDATE_REMOTE_NOTICE = "type_update_remote_notice";
    public static final String TYPE_MESSAGE_TODO_NUM = "type_message_todo_num";
    public static final String TYPE_NOTICE_NUM = "type_message_num";
    public static final String TYPE_NOTICE_PUSH_BLANK = "type_notice_push_blank";
    public static final String TYPE_NOTICE_IS_CONFIRM = "type_notice_is_confirm";
    public static final String TYPE_NOTICE_PERSONNEL = "type_notice_personnel";
    public static final String TYPE_NOTICE_RANGE = "type_notice_range";
    public static final String TYPE_NOTICE_UN_CONFIRM_NUMBER = "type_notice_un_confirm_number";
    public static final String TYPE_NOTICE_CHECK_NUMBER = "type_notice_check_number";
    public static final String TYPE_NOTICE_CONFIRM_RECEIVER = "type_notice_confirm_receiver";

    public static final String TYPE_LEAVE = "type_leave";
    public static final String TYPE_UPDATE_IMG = "type_update_img";
    public static final String TYPE_PREPARES_SAVE = "type_prepares_save";
    public static final String TYPE_UPDATE_APP_MANAGER = "type_update_app_manager";
    public static final String TYPE_UPDATE_APP = "type_update_app";
    public static final String TYPE_UPDATE_NOTICE_MY_RELEASE = "type_update_notice_my_release";

}