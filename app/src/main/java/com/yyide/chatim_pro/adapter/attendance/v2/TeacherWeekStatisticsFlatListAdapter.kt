package com.yyide.chatim_pro.adapter.attendance.v2

import android.text.TextUtils
import android.view.View
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim_pro.R
import com.yyide.chatim_pro.model.attendance.EventBean
import com.yyide.chatim_pro.utils.DateUtils

/**
 *
 * @author liu tao
 * @date 2021/12/7 16:01
 * @description 描述
 */
class TeacherWeekStatisticsFlatListAdapter :
    BaseMultiItemQuickAdapter<EventBean, BaseViewHolder>() {
    init {
        addItemType(EventBean.GROUP_ITEM, R.layout.item_statistics_list)
        addItemType(EventBean.CHILD_ITEM, R.layout.item_statistics_child_list)
    }

    override fun convert(baseViewHolder: BaseViewHolder, dataBean: EventBean) {
        when (baseViewHolder.itemViewType) {
            EventBean.GROUP_ITEM -> {
                baseViewHolder.setText(R.id.tv_name, dataBean.userName)
                val string = context.getString(R.string.attendance_time_format)
                baseViewHolder.setText(
                    R.id.tv_attendance_time,
                    String.format(string, dataBean.sectionNumber)
                )
                baseViewHolder.setImageResource(
                    R.id.iv_direction,
                    if (dataBean.checked) R.drawable.icon_arrow_up else R.drawable.icon_arrow_down
                )
                baseViewHolder.setVisible(R.id.child_recyclerview, false)
            }
            EventBean.CHILD_ITEM -> {
                baseViewHolder.itemView.setBackgroundColor(context.resources.getColor(R.color.color_F9FAF9))
                if (!TextUtils.isEmpty(dataBean.sortName)) {
                    val date = DateUtils.formatTime(dataBean.requiredTime, null, "MM.dd")
                    baseViewHolder.setText(R.id.tv_name, date + " " + dataBean.sortName)
                } else if (TextUtils.isEmpty(dataBean.sortName) && !TextUtils.isEmpty(dataBean.eventName)) {
                    val date = DateUtils.formatTime(dataBean.requiredTime, null, "MM.dd")
                    baseViewHolder.setText(R.id.tv_name, date + " " + dataBean.eventName)
                } else {
                    val date = DateUtils.formatTime(dataBean.courseStartTime, null, "MM.dd")
                    if (dataBean.attendanceType == "1" || "6" == dataBean.attendanceType) {
                        baseViewHolder.setText(R.id.tv_name, date + " " + dataBean.courseInfo)
                        baseViewHolder.setVisible(R.id.tv_course_name, true)
                        baseViewHolder.setText(R.id.tv_course_name, dataBean.courseName)
                    } else {
                        baseViewHolder.setVisible(R.id.tv_course_name, true)
                        baseViewHolder.setText(R.id.tv_name, date + " " + dataBean.courseInfo)
                        baseViewHolder.setText(R.id.tv_course_name, dataBean.courseName)
                    }
                }

                var status: String? = dataBean.attendanceType
                if (status == null) {
                    status = ""
                }
                //0正常、1缺勤、2迟到/3早退,4请假）
                //考勤类型 0正常 1缺勤、2迟到 3早退 4 无效打卡 5 请假 6 未打卡
                when (status) {
                    "0" -> {
                        baseViewHolder.setVisible(R.id.tv_attendance_status, true)
                        baseViewHolder.setText(
                            R.id.tv_attendance_status,
                            context.getString(R.string.attendance_normal)
                        )
                        baseViewHolder.getView<View>(R.id.gp_event_time).setVisibility(View.GONE)
                        baseViewHolder.getView<View>(R.id.gp_leave_event_time)
                            .setVisibility(View.GONE)
                    }
                    "1" -> {
                        baseViewHolder.setVisible(R.id.tv_attendance_status, true)
                        baseViewHolder.setText(
                            R.id.tv_attendance_status,
                            context.getString(R.string.attendance_absence)
                        )
                        baseViewHolder.getView<View>(R.id.gp_event_time).setVisibility(View.GONE)
                        baseViewHolder.getView<View>(R.id.gp_leave_event_time)
                            .setVisibility(View.GONE)
                    }
                    "6" -> {
                        val attendanceSignInOut: String? = dataBean.attendanceSignInOut
                        if ("1" == attendanceSignInOut) {
                            baseViewHolder.setText(
                                R.id.tv_attendance_status,
                                context.getString(R.string.attendance_no_logout)
                            )
                        } else {
                            baseViewHolder.setText(
                                R.id.tv_attendance_status,
                                context.getString(R.string.attendance_no_sign_in)
                            )
                        }
                        baseViewHolder.setVisible(R.id.tv_attendance_status, true)
                        baseViewHolder.getView<View>(R.id.gp_event_time).setVisibility(View.GONE)
                        baseViewHolder.getView<View>(R.id.gp_leave_event_time)
                            .setVisibility(View.GONE)
                    }
                    "2" -> {
                        baseViewHolder.getView<View>(R.id.gp_event_time).setVisibility(View.VISIBLE)
                        baseViewHolder.getView<View>(R.id.gp_leave_event_time)
                            .setVisibility(View.GONE)
                        baseViewHolder.setVisible(R.id.tv_attendance_status, false)
                        baseViewHolder.setText(
                            R.id.tv_attendance_status,
                            context.getString(R.string.attendance_late)
                        )
                        if (!TextUtils.isEmpty(dataBean.clockName)) {
                            baseViewHolder.setText(R.id.tv_event_time_title, dataBean.clockName)
                        } else {
                            baseViewHolder.setText(
                                R.id.tv_event_time_title,
                                context.getString(R.string.attendance_event_name)
                            )
                        }
                        val date1 = DateUtils.formatTime(dataBean.signInTime, null, "HH:mm")
                        baseViewHolder.setText(R.id.tv_event_time, date1)
                        baseViewHolder.setTextColor(
                            R.id.tv_event_time,
                            context.resources.getColor(R.color.attendance_time_late)
                        )
                    }
                    "3" -> {
                        baseViewHolder.getView<View>(R.id.gp_event_time).setVisibility(View.VISIBLE)
                        baseViewHolder.getView<View>(R.id.gp_leave_event_time)
                            .setVisibility(View.GONE)
                        baseViewHolder.setVisible(R.id.tv_attendance_status, false)
                        baseViewHolder.setText(
                            R.id.tv_attendance_status,
                            context.getString(R.string.attendance_leave_early)
                        )
                        if (!TextUtils.isEmpty(dataBean.clockName)) {
                            baseViewHolder.setText(R.id.tv_event_time_title, dataBean.clockName)
                        } else {
                            baseViewHolder.setText(
                                R.id.tv_event_time_title,
                                context.getString(R.string.attendance_event_name)
                            )
                        }
                        val date2 = DateUtils.formatTime(dataBean.signInTime, null, "HH:mm")
                        baseViewHolder.setText(R.id.tv_event_time, date2)
                        baseViewHolder.setTextColor(
                            R.id.tv_event_time,
                            context.resources.getColor(R.color.attendance_leave_early)
                        )
                    }
                    "5" -> {
                        baseViewHolder.getView<View>(R.id.gp_event_time).setVisibility(View.GONE)
                        baseViewHolder.getView<View>(R.id.gp_leave_event_time)
                            .setVisibility(View.VISIBLE)
                        baseViewHolder.setText(
                            R.id.tv_attendance_status,
                            context.getString(R.string.attendance_ask_for_leave)
                        )
                        baseViewHolder.setVisible(R.id.tv_attendance_status, false)
                        baseViewHolder.setText(
                            R.id.tv_leave_event_time_title,
                            context.getString(R.string.attendance_leave_time)
                        )
                        baseViewHolder.setTextColor(
                            R.id.tv_leave_event_time,
                            context.resources.getColor(R.color.attendance_time_leave)
                        )
                        val data1 =
                            DateUtils.formatTime(dataBean.leaveStartTime, null, "MM.dd HH:mm")
                        val data2 = DateUtils.formatTime(dataBean.leaveEndTime, null, "MM.dd HH:mm")
                        baseViewHolder.setText(R.id.tv_leave_event_time, "$data1-$data2")
                    }
                    else -> {
                        baseViewHolder.setVisible(R.id.tv_attendance_status, false)
                        baseViewHolder.getView<View>(R.id.gp_event_time).setVisibility(View.GONE)
                        baseViewHolder.getView<View>(R.id.gp_leave_event_time)
                            .setVisibility(View.GONE)
                    }
                }
            }
        }
    }
}