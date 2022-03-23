package com.yyide.chatim;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.SPUtils;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.model.LoginRsp;
import com.yyide.chatim.model.SchoolRsp;
import com.yyide.chatim.model.UserBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


/**
 * Created by Hao on 2017/11/16.
 */

public class SpData {

    /*用户信息，登录返回，登录信息版本号*/
    public static final String LOGINDATA = "LOGINDATA_NEW2";
    public static final String USER = "USER2";
    public static final String SCHOOLINFO = "SCHOOLINFO2";
    public static final String IDENTIY_INFO = "IDENTIY_INFO2";
    public static final String USERSIG = "USERSIG2";
    public static final String USERNAME = "USERNAME2";
    public static final String CLASS_INFO = "CLASS_INFO2";

    public static LoginRsp getLogin() {
        return JSON.parseObject(SPUtils.getInstance().getString(LOGINDATA, ""), LoginRsp.class);
    }

    public static UserBean getUser() {
        return JSON.parseObject(SPUtils.getInstance().getString(USER, ""), UserBean.class);
    }

    public static void clearUser() {
        SPUtils.getInstance().remove(LOGINDATA);
    }

    public static SchoolRsp Schoolinfo() {
        return JSON.parseObject(SPUtils.getInstance().getString(SCHOOLINFO, ""), SchoolRsp.class);
    }

    public static SchoolRsp.IdentityBean getIdentityInfo() {
        return JSON.parseObject(SPUtils.getInstance().getString(IDENTIY_INFO, ""), SchoolRsp.IdentityBean.class);
    }

    public static GetUserSchoolRsp.DataBean.FormBean getClassInfo() {
        return JSON.parseObject(SPUtils.getInstance().getString(CLASS_INFO, ""), GetUserSchoolRsp.DataBean.FormBean.class);
    }

    public static String getUserId() {
        if (SpData.getUser() != null) {
            return SpData.getUser().getId() + "";
        }
        return "";
    }

    //去重
    public static ArrayList<GetUserSchoolRsp.DataBean.FormBean> removeList(List<GetUserSchoolRsp.DataBean.FormBean> persons) {
        Set<GetUserSchoolRsp.DataBean.FormBean> personSet = new TreeSet<>((o1, o2) -> o1.classesId.compareTo(o2.classesId));
        personSet.addAll(persons);
        return new ArrayList<>(personSet);
    }

    /**
     * 班级列表去重
     *
     * @return
     */
    public static ArrayList<GetUserSchoolRsp.DataBean.FormBean> getDuplicationClassList() {
//        if (SpData.getIdentityInfo() != null && SpData.getIdentityInfo().form != null) {
//            return removeList(SpData.getIdentityInfo().form);
//        }
        return new ArrayList<>();
    }

    /**
     * 班级列表（未去重）
     *
     * @return
     */
    public static ArrayList<GetUserSchoolRsp.DataBean.FormBean> getClassList() {
//        if (SpData.getIdentityInfo() != null && SpData.getIdentityInfo().form != null) {
//            return (ArrayList<GetUserSchoolRsp.DataBean.FormBean>) SpData.getIdentityInfo().form;
//        }
        return new ArrayList<>();
    }

    /**
     * 获取用户信息选择的班级名或者班级学生名
     *
     * @return
     */
    public static String getClassesStudentName() {
        if (getClassInfo() == null) {
            return null;
        }
        if (SpData.getIdentityInfo().staffIdentity()) {
            return getClassInfo().classesName;
        }
        return getClassInfo().classesStudentName;
    }

    public static String UserSig() {
        return SPUtils.getInstance().getString(USERSIG, "");
    }

    /**
     * 保存学校信息
     *
     * @param rsp
     */
    public static void setIdentityInfo(GetUserSchoolRsp rsp) {
//        //缓存获取学校信息
//        if (rsp.data != null) {
//            if (rsp.data.size() > 0) {
//                GetUserSchoolRsp.DataBean dataBean = null;
//                //已选择的身份缓存
//                GetUserSchoolRsp.DataBean identityInfo = SpData.getIdentityInfo();
//                for (int i = 0; i < rsp.data.size(); i++) {
//                    GetUserSchoolRsp.DataBean item = rsp.data.get(i);
//                    if (identityInfo != null && item.userId.equals(identityInfo.userId)) {
//                        SPUtils.getInstance().put(SpData.IDENTIY_INFO, JSON.toJSONString(item));
//                        setClassesData(item);
//                        dataBean = item;
//                        break;
//                    }
//                }
//                //如果没有缓存数据那么就取列表默认身份
//                if (dataBean == null) {
//                    for (int i = 0; i < rsp.data.size(); i++) {
//                        dataBean = rsp.data.get(i);
//                        //处理之前选中的身份信息作为默认首选
//                        if (dataBean.isCurrentUser) {//保存切换身份信息
//                            SPUtils.getInstance().put(SpData.IDENTIY_INFO, JSON.toJSONString(dataBean));
//                            setClassesData(dataBean);
//                            break;
//                        }
//                    }
//                }
//            }
//        }
    }

    /**
     * 保存班级
     *
     * @param dataBean
     */
    public static void setClassesData(GetUserSchoolRsp.DataBean dataBean) {
//        if (dataBean != null && dataBean.form != null && dataBean.form.size() > 0) {//保存班级信息
//            GetUserSchoolRsp.DataBean.FormBean classInfo = SpData.getClassInfo();
//            if (classInfo != null) {//处理切换班级
//                GetUserSchoolRsp.DataBean.FormBean classBean = null;
//                for (GetUserSchoolRsp.DataBean.FormBean item : dataBean.form) {
//                    if (GetUserSchoolRsp.DataBean.TYPE_PARENTS.equals(dataBean.status)) {
//                        //家长默认选择班级
//                        if (!TextUtils.isEmpty(classInfo.classesStudentName)
//                                && classInfo.classesStudentName.equals(item.classesStudentName)) {
//                            classBean = item;
//                            break;
//                        }
//                    } else {
//                        //默认选择班级
//                        if (!TextUtils.isEmpty(SpData.getClassInfo().classesId)
//                                && SpData.getClassInfo().classesId.equals(item.classesId)) {
//                            classBean = item;
//                            break;
//                        }
//                    }
//                }
//                SPUtils.getInstance().put(SpData.CLASS_INFO, JSON.toJSONString(classBean == null ? dataBean.form.get(0) : classBean));
//            } else {
//                SPUtils.getInstance().put(SpData.CLASS_INFO, JSON.toJSONString(dataBean.form.get(0)));
//            }
//        } else {//处理切换后没有班级的情况
//            SPUtils.getInstance().remove(SpData.CLASS_INFO);
//        }
    }

    /**
     * 更新本地缓存班级数据
     *
     * @param classesInfo
     */
    public static void setClassesInfo(GetUserSchoolRsp.DataBean.FormBean classesInfo) {
//        GetUserSchoolRsp.DataBean identityInfo = getIdentityInfo();
//        if (classesInfo != null && identityInfo != null && identityInfo.form != null) {
//            List<GetUserSchoolRsp.DataBean.FormBean> lists = new ArrayList<>();
//            for (GetUserSchoolRsp.DataBean.FormBean classesBean : identityInfo.form) {
//                if (!TextUtils.isEmpty(classesBean.classesStudentName) && classesBean.classesStudentName.equals(classesInfo.classesStudentName)) {
//                    classesBean = classesInfo;
//                }
//                lists.add(classesBean);
//            }
//            identityInfo.form.clear();
//            identityInfo.form.addAll(lists);
//            SPUtils.getInstance().put(SpData.IDENTIY_INFO, JSON.toJSONString(identityInfo));
//        }
    }
}
