package com.yyide.chatim.view;


import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.GetAppVersionResponse;
import com.yyide.chatim.model.ListAllScheduleByTeacherIdRsp;
import com.yyide.chatim.model.ResultBean;
import com.yyide.chatim.model.SelectSchByTeaidRsp;
import com.yyide.chatim.model.SelectUserRsp;
import com.yyide.chatim.model.UserLogoutRsp;
import com.yyide.chatim.model.WeeklyDateBean;
import com.yyide.chatim.model.WeeklyDescBean;

/**
 * 作者：Rance on 2016/10/25 15:19
 * 邮箱：rance935@163.com
 */
public interface MainView extends BaseView {

    void getVersionInfo(GetAppVersionResponse rsp);

    void fail(String msg);

    void getCopywriter(WeeklyDescBean model);

    void getWeeklyDate(WeeklyDateBean model);

}
