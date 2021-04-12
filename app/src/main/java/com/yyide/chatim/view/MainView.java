package com.yyide.chatim.view;



import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.model.ListAllScheduleByTeacherIdRsp;
import com.yyide.chatim.model.ListScheduleRsp;
import com.yyide.chatim.model.LoginRsp;
import com.yyide.chatim.model.ResultBean;
import com.yyide.chatim.model.SelectSchByTeaidRsp;
import com.yyide.chatim.model.SelectUserRsp;
import com.yyide.chatim.model.UserLogoutRsp;
import com.yyide.chatim.model.VideoEntity;
import com.yyide.chatim.model.addUserEquipmentInfoRsp;
import com.yyide.chatim.model.getUserSigRsp;
import com.yyide.chatim.model.listTimeDataRsp;

import java.util.List;

/**
 * 作者：Rance on 2016/10/25 15:19
 * 邮箱：rance935@163.com
 */
public interface MainView extends BaseView {

    void getData(SelectUserRsp rsp);

    void getDataFail(String msg);

    void UserLogoutData(UserLogoutRsp rsp);

    void UserLogoutDataFail(String msg);

//    void getUserSchool(GetUserSchoolRsp rsp);

    void getUserSchoolDataFail(String rsp);

    void selectSchByTeaid(SelectSchByTeaidRsp rsp);

    void selectSchByTeaidDataFail(String rsp);

    void listAllScheduleByTeacherId(ListAllScheduleByTeacherIdRsp rsp);

    void listAllScheduleByTeacherIdDataFail(String rsp);

    void addUserEquipmentInfo(ResultBean rsp);

    void addUserEquipmentInfoFail(String rsp);


}
