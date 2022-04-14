package com.yyide.chatim_pro.view;


import com.yyide.chatim_pro.base.BaseView;
import com.yyide.chatim_pro.model.GetUserSchoolRsp;
import com.yyide.chatim_pro.model.ListAllScheduleByTeacherIdRsp;
import com.yyide.chatim_pro.model.NoticeHomeRsp;
import com.yyide.chatim_pro.model.NoticeMyReleaseDetailBean;
import com.yyide.chatim_pro.model.ResultBean;
import com.yyide.chatim_pro.model.SelectSchByTeaidRsp;
import com.yyide.chatim_pro.model.SelectUserRsp;
import com.yyide.chatim_pro.model.TodoRsp;
import com.yyide.chatim_pro.model.UserLogoutRsp;
import com.yyide.chatim_pro.model.WeeklyDescBean;

/**
 * 作者：Rance on 2016/10/25 15:19
 * 邮箱：rance935@163.com
 */
public interface HomeFragmentView extends BaseView{

    void getUserSchool(GetUserSchoolRsp rsp);

    void getFail(String rsp);

}
