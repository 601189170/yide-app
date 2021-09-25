package com.yyide.chatim.activity.weekly

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.yyide.chatim.R
import com.yyide.chatim.SpData
import com.yyide.chatim.activity.weekly.details.*
import com.yyide.chatim.activity.weekly.home.SchoolWeeklyFragment
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.databinding.ActivityWeeklyDetailsBinding

/**
 * 周报详情页
 */
class WeeklyDetailsActivity : BaseActivity() {

    private lateinit var viewBinding: ActivityWeeklyDetailsBinding

    companion object {
        private val TYPE = "type"
        const val SCHOOL_ATTENDANCE_TYPE = "school_attendance_type"
        const val SCHOOL_HOMEWORK_TYPE = "school_homework_type"
        const val HEAD_TEACHER_ATTENDANCE_TYPE = "head_teacher_attendance_type"
        const val HEAD_TEACHER_HOMEWORK_TYPE = "head_teacher_homework_type"
        const val TEACHER_ATTENDANCE_TYPE = "teacher_attendance_type"
        const val TEACHER_HOMEWORK_TYPE = "teacher_homework_type"
        const val PARENT_ATTENDANCE_TYPE = "parent_attendance_type"
        const val PARENT_HOMEWORK_TYPE = "parent_homework_type"

        fun jump(context: Context, type: String) {
            val intent = Intent(context, WeeklyDetailsActivity::class.java)
            intent.putExtra(TYPE, type)
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
        viewBinding.back.setOnClickListener { finish() }
        when (type) {
            SCHOOL_ATTENDANCE_TYPE -> {
                supportFragmentManager.beginTransaction().replace(
                    viewBinding.flContent.id,
                    SchoolAttendanceFragment.newInstance()
                ).commit()
            }
            SCHOOL_HOMEWORK_TYPE -> {
                supportFragmentManager.beginTransaction().replace(
                    viewBinding.flContent.id,
                    SchoolHomeworkFragment.newInstance()
                ).commit()
            }
            HEAD_TEACHER_ATTENDANCE_TYPE -> {
                supportFragmentManager.beginTransaction().replace(
                    viewBinding.flContent.id,
                    TeacherAttendanceFragment.newInstance()
                ).commit()
            }
            HEAD_TEACHER_HOMEWORK_TYPE -> {
                supportFragmentManager.beginTransaction().replace(
                    viewBinding.flContent.id,
                    TeacherHomeworkFragment.newInstance()
                ).commit()
            }
            TEACHER_ATTENDANCE_TYPE -> {
                supportFragmentManager.beginTransaction().replace(
                    viewBinding.flContent.id,
                    TeacherAttendanceFragment.newInstance()
                ).commit()
            }
            TEACHER_HOMEWORK_TYPE -> {
                supportFragmentManager.beginTransaction().replace(
                    viewBinding.flContent.id,
                    TeacherHomeworkFragment.newInstance()
                ).commit()
            }
            PARENT_ATTENDANCE_TYPE -> {
                supportFragmentManager.beginTransaction().replace(
                    viewBinding.flContent.id,
                    SchoolWeeklyFragment.newInstance()
                ).commit()
            }
            PARENT_HOMEWORK_TYPE -> {
                supportFragmentManager.beginTransaction().replace(
                    viewBinding.flContent.id,
                    SchoolWeeklyFragment.newInstance()
                ).commit()
            }
        }
    }
}