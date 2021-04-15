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
    public static String USERSIG = "USERSIG";
    public static String USERNAME = "USERNAME";
    public static String CLASS_INFO = "CLASS_INFO";

    public static LoginRsp User() {
        return JSON.parseObject(SPUtils.getInstance().getString(LOGINDATA, ""), LoginRsp.class);
    }

    public static GetUserSchoolRsp Schoolinfo() {
        return JSON.parseObject(SPUtils.getInstance().getString(SCHOOLINFO, ""), GetUserSchoolRsp.class);
    }

    public static GetUserSchoolRsp.DataBean getIdentityInfo() {
        return JSON.parseObject(SPUtils.getInstance().getString(IDENTIY_INFO, ""), GetUserSchoolRsp.DataBean.class);
    }

    public static GetUserSchoolRsp.DataBean.FormBean getClassInfo() {
        return JSON.parseObject(SPUtils.getInstance().getString(CLASS_INFO, ""), GetUserSchoolRsp.DataBean.FormBean.class);
    }

    public static String UserSig() {
        return SPUtils.getInstance().getString(USERSIG, "");
    }

    public static String UserPhone() {
        return SPUtils.getInstance().getString(USERPHONE, "");
    }

    /**
     * 保存学校信息
     *
     * @param rsp
     */
    public static void setIdentityInfo(GetUserSchoolRsp rsp) {
        //缓存获取学校信息
        if (rsp.data != null) {
            if (rsp.data.size() > 0) {
                GetUserSchoolRsp.DataBean dataBean = null;
                //已选择的身份缓存
                GetUserSchoolRsp.DataBean identityInfo = SpData.getIdentityInfo();
                for (int i = 0; i < rsp.data.size(); i++) {
                    GetUserSchoolRsp.DataBean item = rsp.data.get(i);
                    if (identityInfo != null && item.userId == identityInfo.userId) {
                        SPUtils.getInstance().put(SpData.IDENTIY_INFO, JSON.toJSONString(item));
                        setClassesData(item);
                        dataBean = item;
                        break;
                    }
                }
                //如果没有缓存数据那么就取列表默认身份
                if (dataBean == null) {
                    for (int i = 0; i < rsp.data.size(); i++) {
                        dataBean = rsp.data.get(i);
                        //处理之前选中的身份信息作为默认首选
                        if (dataBean.isCurrentUser) {//保存切换身份信息
                            SPUtils.getInstance().put(SpData.IDENTIY_INFO, JSON.toJSONString(dataBean));
                            setClassesData(dataBean);
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * 保存班级
     *
     * @param dataBean
     */
    private static void setClassesData(GetUserSchoolRsp.DataBean dataBean) {
        if (dataBean != null && dataBean.form != null && dataBean.form.size() > 0) {//保存班级信息
            if (SpData.getClassInfo() != null) {//处理切换班级
                GetUserSchoolRsp.DataBean.FormBean classBean = null;
                for (GetUserSchoolRsp.DataBean.FormBean item : dataBean.form) {
                    if (item.classesId.equals(SpData.getClassInfo().classesId)) {
                        classBean = item;
                    }
                }
                SPUtils.getInstance().put(SpData.CLASS_INFO, JSON.toJSONString(classBean == null ? dataBean.form.get(0) : classBean));
            } else {
                SPUtils.getInstance().put(SpData.CLASS_INFO, JSON.toJSONString(dataBean.form.get(0)));
            }
        } else {//处理切换后没有班级的情况
            SPUtils.getInstance().put(SpData.CLASS_INFO, "");
        }
    }
}
