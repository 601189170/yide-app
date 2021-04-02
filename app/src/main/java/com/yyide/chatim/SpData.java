package com.yyide.chatim;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.SPUtils;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.model.LoginRsp;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;


/**
 * Created by Hao on 2017/11/16.
 */

public class SpData {

    /*用户信息，登录返回，登录信息版本号*/
    public static String LOGINDATA = "LOGINDATA";
    public static String USERPHONE = "USERPHONE";
    public static String SCHOOLINFO = "SCHOOLINFO";
    public static String IDENTIY_INFO = "IDENTIY_INFO";
    public static String SCHOOLID = "SCHOOLID";
    public static String USERSIG = "USERSIG";
    public static String USERNAME = "USERNAME";
    public static String USERPHOTO = "USERPHOTO";
    public static String USERID = "USERID";
    public static String CLASS_INFO = "CLASS_INFO";


    public static LoginRsp User() {
        return JSON.parseObject(SPUtils.getInstance().getString(LOGINDATA, ""), LoginRsp.class);
    }

    public static GetUserSchoolRsp Schoolinfo() {
        return JSON.parseObject(SPUtils.getInstance().getString(SCHOOLINFO, ""), GetUserSchoolRsp.class);
    }

    public static GetUserSchoolRsp.DataBean getIdentityInfo(){
        return JSON.parseObject(SPUtils.getInstance().getString(IDENTIY_INFO, ""), GetUserSchoolRsp.DataBean.class);
    }

    public static String SchoolId() {
        return SPUtils.getInstance().getString(SCHOOLID, "");
    }

    public static GetUserSchoolRsp.DataBean.FormBean getClassInfo() {
        return JSON.parseObject(SPUtils.getInstance().getString(CLASS_INFO, ""), GetUserSchoolRsp.DataBean.FormBean.class);
    }

    public static String UserSig() {
        return SPUtils.getInstance().getString(USERSIG, "");
    }

    public static String UserName() {
        return SPUtils.getInstance().getString(USERNAME, "");
    }

    public static String UserPhoto() {
        return SPUtils.getInstance().getString(USERPHOTO, "");
    }

    public static String UserPhone() {
        return SPUtils.getInstance().getString(USERPHONE, "");
    }

    public static int getUserId() {
        return SPUtils.getInstance().getInt(USERID);
    }

}
