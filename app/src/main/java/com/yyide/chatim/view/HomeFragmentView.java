package com.yyide.chatim.view;


import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.GetUserSchoolRsp;
import com.yyide.chatim.model.ListAllScheduleByTeacherIdRsp;
import com.yyide.chatim.model.NoticeHomeRsp;
import com.yyide.chatim.model.NoticeMyReleaseDetailBean;
import com.yyide.chatim.model.ResultBean;
import com.yyide.chatim.model.SelectSchByTeaidRsp;
import com.yyide.chatim.model.SelectUserRsp;
import com.yyide.chatim.model.TodoRsp;
import com.yyide.chatim.model.UserLogoutRsp;
import com.yyide.chatim.model.WeeklyDescBean;

/**
 * 作者：Rance on 2016/10/25 15:19
 * 邮箱：rance935@163.com
 */
public interface HomeFragmentView extends BaseView {

    void confirmNotice(ResultBean model);

    void getNotice(NoticeMyReleaseDetailBean model);

    void getUserSchool(GetUserSchoolRsp rsp);

    void getFail(String rsp);

    void getIndexMyNoticeFail(String rsp);

    void getIndexMyNotice(TodoRsp rsp);

}
