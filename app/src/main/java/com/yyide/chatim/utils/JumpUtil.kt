package com.yyide.chatim.utils

import android.content.Context
import android.content.Intent
import com.blankj.utilcode.util.ToastUtils
import com.tencent.mmkv.MMKV
import com.yyide.chatim.SpData
import com.yyide.chatim.activity.AppManagerActivity
import com.yyide.chatim.activity.table.TableActivity
import com.yyide.chatim.activity.WebViewActivity
import com.yyide.chatim.activity.attendance.AttendanceActivity
import com.yyide.chatim.activity.attendance.StatisticsActivity
import com.yyide.chatim.activity.gate.GateClassTeacherActivity
import com.yyide.chatim.activity.gate.GateDetailInfoActivity
import com.yyide.chatim.activity.gate.GateStudentStaffActivity
import com.yyide.chatim.activity.leave.AskForLeaveActivity
import com.yyide.chatim.activity.meeting.MeetingHomeActivity
import com.yyide.chatim.activity.newnotice.NewNoticeAnnouncementActivity
import com.yyide.chatim.activity.schedule.SchoolCalendarActivity
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.base.MMKVConstant
import com.yyide.chatim.model.EventMessage
import org.greenrobot.eventbus.EventBus

object JumpUtil {

    @JvmStatic
    fun appOpen(mActivity: Context, type: String, url: String?) {
        when (type) {
            "编辑" -> {
                val intent = Intent(mActivity, AppManagerActivity::class.java)
                mActivity.startActivity(intent)
            }
            "通知公告" -> {
                val intent = Intent(mActivity, NewNoticeAnnouncementActivity::class.java)
                mActivity.startActivity(intent)
            }
            "请假" -> {
                val intent = Intent(mActivity, AskForLeaveActivity::class.java)
                mActivity.startActivity(intent)
            }
            "课表" -> {
                val intent = Intent(mActivity, TableActivity::class.java)
                mActivity.startActivity(intent)
            }
            "考勤" -> {
                if (SpData.getClassInfo() != null) {
                    if (!SpData.getIdentityInfo().staffIdentity()) {
                        val intent = Intent(mActivity, StatisticsActivity::class.java)
                        mActivity.startActivity(intent)
                    } else {
                        val intent = Intent(mActivity, AttendanceActivity::class.java)
                        mActivity.startActivity(intent)
                    }
                } else {
                    ToastUtils.showShort("名下无班级考勤");
                }
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
            "通行统计" -> {
                //0全部 1学生 2教职工 3没有权限
                val permission =
                    MMKV.defaultMMKV().decodeString(MMKVConstant.YD_GATE_DATA_ACCESS_PERMISSION)
                when (permission) {
                    "0" -> {
                        mActivity.startActivity(
                            Intent(
                                mActivity,
                                GateStudentStaffActivity::class.java
                            )
                        )
                    }
                    "1" -> {
                        if (SpData.getIdentityInfo().staffIdentity()) {
                            mActivity.startActivity(
                                Intent(
                                    mActivity,
                                    GateClassTeacherActivity::class.java
                                )
                            )
                        } else {
                            mActivity.startActivity(
                                Intent(
                                    mActivity,
                                    GateDetailInfoActivity::class.java
                                )
                            )
                        }
                    }
                    else -> {
                        ToastUtils.showShort("没有权限")
                    }
                }
            }
            else -> if ("#" == url) {
                ToastUtils.showShort("暂无权限")
            } else {
                val intent = Intent(mActivity, WebViewActivity::class.java)
                intent.putExtra("url", url + Base64Utils.getData())
                mActivity.startActivity(intent)
//                ToastUtils.showShort("当前版本不支持该功能")
            }
        }
    }
}