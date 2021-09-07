package com.yyide.chatim.utils

import android.content.Context
import android.content.Intent
import com.blankj.utilcode.util.ToastUtils
import com.yyide.chatim.activity.AppManagerActivity
import com.yyide.chatim.activity.TableActivity
import com.yyide.chatim.activity.WebViewActivity
import com.yyide.chatim.activity.attendance.AttendanceActivity
import com.yyide.chatim.activity.leave.AskForLeaveActivity
import com.yyide.chatim.activity.newnotice.NewNoticeAnnouncementActivity
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.model.EventMessage
import org.greenrobot.eventbus.EventBus

object JumpUtil {

    val CODE_10000 = 10000
    val CODE_10001 = 10001
    val CODE_10002 = 10002
    val CODE_10003 = 10003
    val CODE_10004 = 10004
    val CODE_10005 = 10005
    val CODE_10006 = 10006
    val CODE_10007 = 10007
    val CODE_10008 = 10008
    val CODE_10009 = 10009
    val CODE_10010 = 10010

    @JvmStatic
    fun appOpen(mActivity: Context, type: String, url: String) {
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
                val intent = Intent(mActivity, AttendanceActivity::class.java)
                mActivity.startActivity(intent)
            }
            "待办" -> {
                EventBus.getDefault()
                    .post(EventMessage(BaseConstant.TYPE_SELECT_MESSAGE_TODO, "", 1))
            }
            else -> if ("#" == url) {
                ToastUtils.showShort("暂无权限")
            } else {
                val intent = Intent(mActivity, WebViewActivity::class.java)
                intent.putExtra("url", url)
                mActivity.startActivity(intent)
//                ToastUtils.showShort("当前版本不支持该功能")
            }
        }
    }
}