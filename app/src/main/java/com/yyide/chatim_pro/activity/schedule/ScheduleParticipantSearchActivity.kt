package com.yyide.chatim_pro.activity.schedule

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.fastjson.JSON
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim_pro.R
import com.yyide.chatim_pro.base.BaseActivity
import com.yyide.chatim_pro.databinding.ActivityScheduleParticipantSearchBinding
import com.yyide.chatim_pro.model.schedule.ParticipantRsp
import com.yyide.chatim_pro.model.schedule.SearchParticipantRsp
import com.yyide.chatim_pro.model.schedule.toParticipantListBean
import com.yyide.chatim_pro.utils.loge
import com.yyide.chatim_pro.viewmodel.StaffParticipantViewModel

/**
 * @author liu tao
 * @date 2021/11/6 14:05
 * @description 日程参与人搜索
 */
class ScheduleParticipantSearchActivity : BaseActivity() {
    lateinit var scheduleParticipantSearchBinding: ActivityScheduleParticipantSearchBinding
    private val staffParticipantViewModel: StaffParticipantViewModel by viewModels()
    var teacherList = mutableListOf<SearchParticipantRsp.DataBean.TeacherListBean>()
    var studentList = mutableListOf<SearchParticipantRsp.DataBean.StudentListBean>()

    private var staffOpen = true
    private var studentOpen = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scheduleParticipantSearchBinding =
            ActivityScheduleParticipantSearchBinding.inflate(layoutInflater)
        setContentView(scheduleParticipantSearchBinding.root)
        initView()
        initData()
    }

    private fun initData() {
        //教职工列表
        staffParticipantViewModel.teacherList.observe(this) {
            if (it == null || it.isEmpty()) {
                teacherList.clear()
                staffAdapter.setList(teacherList)
                scheduleParticipantSearchBinding.gpStaff.visibility = View.GONE
                return@observe
            }
            teacherList.clear()
            teacherList.addAll(it)
            staffAdapter.setList(teacherList)
            scheduleParticipantSearchBinding.gpStaff.visibility = View.VISIBLE
        }
        //家长列表
        staffParticipantViewModel.studentList.observe(this) {
            if (it == null || it.isEmpty()) {
                studentList.clear()
                scheduleParticipantSearchBinding.gpStudent.visibility = View.GONE
                scheduleParticipantSearchBinding.vLine1.visibility = View.GONE
                studentAdapter.setList(studentList)
                return@observe
            }
            studentList.clear()
            studentList.addAll(it)
            studentAdapter.setList(studentList)
            scheduleParticipantSearchBinding.gpStudent.visibility = View.VISIBLE
            scheduleParticipantSearchBinding.vLine1.visibility =
                if (teacherList.isEmpty()) View.GONE else View.VISIBLE
        }
    }

    private fun initView() {
        scheduleParticipantSearchBinding.top.title.text = "添加参与人"
        scheduleParticipantSearchBinding.top.backLayout.setOnClickListener {
            finish()
        }
        scheduleParticipantSearchBinding.top.tvRight.visibility = View.VISIBLE
        scheduleParticipantSearchBinding.top.tvRight.text = "确定"
        scheduleParticipantSearchBinding.top.tvRight.setTextColor(resources.getColor(R.color.colorPrimary))
        scheduleParticipantSearchBinding.top.tvRight.setOnClickListener {
            //选中的参与人列表
            val participantList = mutableListOf<ParticipantRsp.DataBean.ParticipantListBean>()
            val participantTeacherList = teacherList.filter { it.checked }.map { it.toParticipantListBean() }
            participantList.addAll(participantTeacherList)

            studentList.forEach {
                val participantStudentList = it.guardians.filter{it.checked}.map { it.toParticipantListBean() }
                participantList.addAll(participantStudentList)
            }
            loge("选中的参与人列表：$participantList")
            val intent = intent
            intent.putExtra("data",JSON.toJSONString(participantList))
            setResult(RESULT_OK,intent)
            finish()
        }

        scheduleParticipantSearchBinding.edit.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val inputMethodManager =
                    this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(
                    this@ScheduleParticipantSearchActivity.currentFocus?.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
                val keyWord: String = scheduleParticipantSearchBinding.edit.text.toString()
                if (TextUtils.isEmpty(keyWord)) {
                    ToastUtils.showShort("请输入关键词内容")
                }
                //搜索参与人
                staffParticipantViewModel.searchParticipant(keyWord)
            }
            false
        }

        scheduleParticipantSearchBinding.tvStaff.setOnClickListener {
            if (staffOpen) {
                val drawable =
                    ContextCompat.getDrawable(this, R.drawable.schedule_fold_up_icon)?.apply {
                        setBounds(0, 0, minimumWidth, minimumHeight)
                    }
                scheduleParticipantSearchBinding.tvStaff.setCompoundDrawables(
                    null,
                    null,
                    drawable,
                    null
                )
                scheduleParticipantSearchBinding.rvStaff.visibility = View.GONE
                staffOpen = false
                return@setOnClickListener
            }
            val drawable =
                ContextCompat.getDrawable(this, R.drawable.schedule_fold_down_icon)?.apply {
                    setBounds(0, 0, minimumWidth, minimumHeight)
                }
            scheduleParticipantSearchBinding.tvStaff.setCompoundDrawables(
                null,
                null,
                drawable,
                null
            )
            scheduleParticipantSearchBinding.rvStaff.visibility = View.VISIBLE
            staffOpen = true
        }

        scheduleParticipantSearchBinding.tvStudent.setOnClickListener {
            if (studentOpen) {
                val drawable =
                    ContextCompat.getDrawable(this, R.drawable.schedule_fold_up_icon)?.apply {
                        setBounds(0, 0, minimumWidth, minimumHeight)
                    }
                scheduleParticipantSearchBinding.tvStudent.setCompoundDrawables(
                    null,
                    null,
                    drawable,
                    null
                )
                scheduleParticipantSearchBinding.rvStudent.visibility = View.GONE
                studentOpen = false
                return@setOnClickListener
            }
            val drawable =
                ContextCompat.getDrawable(this, R.drawable.schedule_fold_down_icon)?.apply {
                    setBounds(0, 0, minimumWidth, minimumHeight)
                }
            scheduleParticipantSearchBinding.tvStudent.setCompoundDrawables(
                null,
                null,
                drawable,
                null
            )
            scheduleParticipantSearchBinding.rvStudent.visibility = View.VISIBLE
            studentOpen = true
        }

        //初始化教职工列表
        scheduleParticipantSearchBinding.rvStaff.layoutManager = LinearLayoutManager(this)
        scheduleParticipantSearchBinding.rvStaff.adapter = staffAdapter

        staffAdapter.setOnItemClickListener { _, _, position ->
            loge("选择教职工：${teacherList[position].name}")
            teacherList[position].checked = !teacherList[position].checked
            staffAdapter.setList(teacherList)
        }

        //初始化学生列表
        scheduleParticipantSearchBinding.rvStudent.layoutManager = LinearLayoutManager(this)
        scheduleParticipantSearchBinding.rvStudent.adapter = studentAdapter

    }

    override fun getContentViewID(): Int = R.layout.activity_schedule_participant_search

    private val staffAdapter = object :
        BaseQuickAdapter<SearchParticipantRsp.DataBean.TeacherListBean, BaseViewHolder>(R.layout.item_participant_search_list) {
        override fun convert(
            holder: BaseViewHolder,
            item: SearchParticipantRsp.DataBean.TeacherListBean
        ) {
            holder.setText(R.id.tv_name, "${item.name} (${item.showName})")
            holder.getView<CheckBox>(R.id.checkBox).isEnabled = true
            holder.getView<CheckBox>(R.id.checkBox).isChecked = item.checked
        }
    }

    private val studentAdapter = object :
        BaseQuickAdapter<SearchParticipantRsp.DataBean.StudentListBean, BaseViewHolder>(R.layout.item_participant_search_student_list) {
        override fun convert(
            holder: BaseViewHolder,
            item: SearchParticipantRsp.DataBean.StudentListBean
        ) {
            item.classesName = item.classesName ?: ""
            holder.setText(R.id.tv_student_name, "${item.name} (${item.classesName})")
            val recyclerView = holder.getView<RecyclerView>(R.id.rv_parent_of_student)
            recyclerView.layoutManager = LinearLayoutManager(this@ScheduleParticipantSearchActivity)
            val parentsAdapter = object :
                BaseQuickAdapter<SearchParticipantRsp.DataBean.StudentListBean.GuardiansBean, BaseViewHolder>(
                    R.layout.item_participant_parent_of_student_list
                ) {
                override fun convert(
                    holder: BaseViewHolder,
                    item: SearchParticipantRsp.DataBean.StudentListBean.GuardiansBean
                ) {
                    holder.setText(R.id.tv_name, item.guardianName)
                    holder.getView<CheckBox>(R.id.checkBox).isEnabled = true
                    holder.getView<CheckBox>(R.id.checkBox).isChecked = item.checked

                    holder.setText(R.id.tv_guardian_name,guardianName(item.relation))
//                    holder.itemView.setOnClickListener {
//                        item.checked = !item.checked
//                        holder.getView<CheckBox>(R.id.checkBox).isChecked = item.checked
//                    }
                }
            }
            val guardians = item.guardians
            recyclerView.adapter = parentsAdapter
            parentsAdapter.setList(guardians)

            parentsAdapter.setOnItemClickListener { _, _, position ->
                guardians[position].checked = !guardians[position].checked
                parentsAdapter.setList(guardians)
            }
        }
    }

    companion object{
        fun guardianName(relation:Int):String{
            //0.父亲1.母亲2.爷爷3.奶奶4.外公5.外婆，6其他
            when (relation) {
                1 -> {
                    return "父亲"
                }
                2 -> {
                    return "母亲"
                }
                3 -> {
                    return "爷爷"
                }
                4 -> {
                    return "奶奶"
                }


                else -> {
                }
            }
            return "其他"
        }
    }
}