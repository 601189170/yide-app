package com.yyide.chatim_pro.utils

import android.content.Context
import android.content.Intent
import com.blankj.utilcode.util.ToastUtils
import com.yyide.chatim_pro.SpData
import com.yyide.chatim_pro.activity.AppManagerActivity
import com.yyide.chatim_pro.activity.WebViewActivity
import com.yyide.chatim_pro.activity.attendance.teacher.TeacherAttendanceActivity
import com.yyide.chatim_pro.activity.leave.AskForLeaveActivity
import com.yyide.chatim_pro.activity.meeting.MeetingHomeActivity
import com.yyide.chatim_pro.activity.message.MessagePushActivity
import com.yyide.chatim_pro.activity.operation.OperationActivityByParents
import com.yyide.chatim_pro.activity.schedule.SchoolCalendarActivity
import com.yyide.chatim_pro.activity.table.TableActivity
import com.yyide.chatim_pro.base.BaseConstant
import com.yyide.chatim_pro.model.EventMessage
import org.greenrobot.eventbus.EventBus

object JumpUtil {
    val APP_KEY_TRAFFICSTATISTICS = "trafficStatistics"//通行统计
    val APP_KEY_SCHOOLCALENDAR = "schoolcalendar"//校历
    val APP_KEY_FULLCALENDAR = "fullcalendar"//日程
    val APP_KEY_TEACHERATTENDANCE = "teacherAttendance"//教师考勤
    val APP_KEY_STUDENTATTENDANCE = "studentAttendance"//学生考勤

    @JvmStatic
    fun appOpen(mActivity: Context, type: String, url: String?, title: String) {
        when (type) {
            "编辑" -> {
                val intent = Intent(mActivity, AppManagerActivity::class.java)
                mActivity.startActivity(intent)
            }
            "通知公告" -> {
                MessagePushActivity.startGo(mActivity)
            }
            "请假" -> {
                val intent = Intent(mActivity, AskForLeaveActivity::class.java)
                mActivity.startActivity(intent)
            }
            "课表" -> {
                val intent = Intent(mActivity, TableActivity::class.java)
                mActivity.startActivity(intent)
            }
            "待办" -> {
                EventBus.getDefault()
                    .post(EventMessage(BaseConstant.TYPE_SELECT_MESSAGE_TODO, "", 1))
            }
            "日程" -> {
                EventBus.getDefault()
                    .post(EventMessage(BaseConstant.TYPE_HOME_CHECK_SCHEDULE, "", 0))
            }
            "会议" -> {
                mActivity.startActivity(Intent(mActivity, MeetingHomeActivity::class.java))
            }
            "校历" -> {
                mActivity.startActivity(Intent(mActivity, SchoolCalendarActivity::class.java))
            }
            "学生考勤" -> {
                WebViewActivity.startTitle(
                    mActivity,
                    getHttpUrl(BaseConstant.ATTENDANCE_HTML),
                    title
                )
            }
            "教师考勤" -> {
                TeacherAttendanceActivity.startGo(mActivity)
            }
            "通行统计" -> {
                WebViewActivity.startTitle(mActivity, getHttpUrl(BaseConstant.CURRENT_HTML), title)
            }
            "周报" -> {
                WebViewActivity.startTitle(mActivity, getHttpUrl(BaseConstant.WEEKLY_HTML), title)
            }
            "作业" -> {
                mActivity.startActivity(Intent(mActivity, OperationActivityByParents::class.java))
            }
            "信息发布" ->{
                MessagePushActivity.startGo(mActivity)
            }

            else -> if ("#" == url) {
                ToastUtils.showShort("暂无权限")
            } else {
                WebViewActivity.startTitle(mActivity, url + Base64Utils.getData(), title)
            }
        }
    }

    private fun getHttpUrl(url: String): String {
        return if (SpData.getIdentityInfo().staffIdentity()) {
            "$url?identity=tea"
        } else {
            "$url?identity=par"
        }
    }
}