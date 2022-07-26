package com.yyide.chatim_pro.activity.weekly

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.yyide.chatim_pro.R
import com.yyide.chatim_pro.SpData
import com.yyide.chatim_pro.activity.weekly.details.*
import com.yyide.chatim_pro.activity.weekly.home.WeeklyUtil
import com.yyide.chatim_pro.base.BaseActivity
import com.yyide.chatim_pro.databinding.ActivityWeeklyDetailsBinding
import com.yyide.chatim_pro.model.GetUserSchoolRsp
import com.yyide.chatim_pro.model.WeeklyDateBean

/**
 * 周报详情页
 */
class WeeklyDetailsActivity : BaseActivity() {

    private lateinit var viewBinding: ActivityWeeklyDetailsBinding

    companion object {
        private val TYPE = "type"
        private val STUDNET_ID = "studnet_id"
        private val STUDNET_NAME = "studnet_name"
        private val DATE_TIME = "date_time"
        private val CLASSBEAN = "classbean"

        const val SCHOOL_ATTENDANCE_TYPE = "school_attendance_type"
        const val SCHOOL_HOMEWORK_TYPE = "school_homework_type"
        const val HEAD_TEACHER_ATTENDANCE_TYPE = "head_teacher_attendance_type"
        const val HEAD_TEACHER_HOMEWORK_TYPE = "head_teacher_homework_type"
        const val TEACHER_ATTENDANCE_TYPE = "teacher_attendance_type"
        const val TEACHER_HOMEWORK_TYPE = "teacher_homework_type"
        const val PARENT_ATTENDANCE_TYPE = "parent_attendance_type"
        const val PARENT_HOMEWORK_TYPE = "parent_homework_type"

        fun jump(
            context: Context,
            type: String,
            studentId: String,
            studentName: String,
            dateTime: WeeklyDateBean.DataBean.TimesBean,
            classBean: GetUserSchoolRsp.DataBean.FormBean?
        ) {
            val intent = Intent(context, WeeklyDetailsActivity::class.java)
            intent.putExtra(TYPE, type)
            intent.putExtra(STUDNET_ID, studentId)
            intent.putExtra(STUDNET_NAME, studentName)
            intent.putExtra(DATE_TIME, dateTime)
            intent.putExtra(CLASSBEAN, classBean)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityWeeklyDetailsBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        initView()
    }

    override fun getContentViewID(): Int {
        return R.layout.activity_weekly_details
    }

    private fun initView() {
        val type = intent.getStringExtra(TYPE)
        val studentId = intent.getStringExtra(STUDNET_ID)
        val studentName = intent.getStringExtra(STUDNET_NAME)
        val dateTime = if (intent.getSerializableExtra(DATE_TIME) != null) {
            intent.getSerializableExtra(DATE_TIME) as WeeklyDateBean.DataBean.TimesBean
        } else {
            WeeklyUtil.getDateTime()
        }
        val classBean =
            if (intent.getSerializableExtra(CLASSBEAN) != null) intent.getSerializableExtra(
                CLASSBEAN
            ) as GetUserSchoolRsp.DataBean.FormBean else SpData.getClassInfo()
        viewBinding.back.setOnClickListener { finish() }
        when (type) {
            SCHOOL_ATTENDANCE_TYPE -> {
                viewBinding.tvEventTitle.text = "考勤"
                supportFragmentManager.beginTransaction().replace(
                    viewBinding.flContent.id,
                    SchoolAttendanceFragment.newInstance(dateTime!!)
                ).commit()
            }
            SCHOOL_HOMEWORK_TYPE -> {
                viewBinding.tvEventTitle.text = "作业"
                supportFragmentManager.beginTransaction().replace(
                    viewBinding.flContent.id,
                    SchoolHomeworkFragment.newInstance(dateTime!!)
                ).commit()
            }
            HEAD_TEACHER_ATTENDANCE_TYPE -> {
                viewBinding.tvEventTitle.text = "考勤"
                supportFragmentManager.beginTransaction().replace(
                    viewBinding.flContent.id,
                    TeacherAttendanceHomeFragment.newInstance(dateTime!!, classBean)
                ).commit()
            }
            HEAD_TEACHER_HOMEWORK_TYPE -> {
                viewBinding.tvEventTitle.text = "作业"
                supportFragmentManager.beginTransaction().replace(
                    viewBinding.flContent.id,
                    TeacherHomeworkFragment.newInstance(dateTime!!)
                ).commit()
            }
            TEACHER_ATTENDANCE_TYPE -> {
                viewBinding.tvEventTitle.text = "考勤"
                supportFragmentManager.beginTransaction().replace(
                    viewBinding.flContent.id,
                    TeacherAttendanceHomeFragment.newInstance(dateTime!!, classBean)
                ).commit()
            }
            TEACHER_HOMEWORK_TYPE -> {
                viewBinding.tvEventTitle.text = "作业"
                supportFragmentManager.beginTransaction().replace(
                    viewBinding.flContent.id,
                    TeacherHomeworkFragment.newInstance(dateTime!!)
                ).commit()
            }
            PARENT_ATTENDANCE_TYPE -> {
                viewBinding.tvEventTitle.text = "考勤"
                supportFragmentManager.beginTransaction().replace(
                    viewBinding.flContent.id,
                    ParentsAttendanceDetailFragment.newInstance(studentId, studentName, dateTime!!)
                ).commit()
            }
            PARENT_HOMEWORK_TYPE -> {
                viewBinding.tvEventTitle.text = "作业"
                supportFragmentManager.beginTransaction().replace(
                    viewBinding.flContent.id,
                    TeacherHomeworkFragment.newInstance(dateTime!!)
                ).commit()
            }
        }
    }
}