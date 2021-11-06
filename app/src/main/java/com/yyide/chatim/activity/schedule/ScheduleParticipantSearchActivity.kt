package com.yyide.chatim.activity.schedule

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yanzhenjie.recyclerview.widget.DefaultItemDecoration
import com.yyide.chatim.R
import com.yyide.chatim.activity.book.BookSearchActivity
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.databinding.ActivityScheduleParticipantSearchBinding
import com.yyide.chatim.model.schedule.ParticipantRsp
import com.yyide.chatim.utils.loge

/**
 * @author liu tao
 * @date 2021/11/6 14:05
 * @description 日程参与人搜索
 */
class ScheduleParticipantSearchActivity : BaseActivity() {
    lateinit var scheduleParticipantSearchBinding: ActivityScheduleParticipantSearchBinding
    val list = mutableListOf<ParticipantRsp.DataBean.ParticipantListBean>()
    val list2 = mutableListOf<ParticipantRsp.DataBean.ParticipantListBean>()
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
        for (i in 1 until 8) {
            val participantListBean = ParticipantRsp.DataBean.ParticipantListBean()
            participantListBean.name = "张三(数学组)--$i"
            participantListBean.realname = "张三(数学组)--$i"
            participantListBean.id = "$i"
            participantListBean.userId = "24232$i"
            participantListBean.checked = false
            list.add(participantListBean)
        }
        staffAdapter.setList(list)

        for (i in 1 until 6){
            val participantListBean = ParticipantRsp.DataBean.ParticipantListBean()
            participantListBean.name = "王小二$i"
            val parentlist = mutableListOf<ParticipantRsp.DataBean.ParticipantListBean.Parents>()
            val parents1 = ParticipantRsp.DataBean.ParticipantListBean.Parents()
            parents1.name = "张洪阳"
            parents1.status = "父亲"
            parentlist.add(parents1)
            val parents2 = ParticipantRsp.DataBean.ParticipantListBean.Parents()
            parents2.name = "李丽华"
            parents2.status = "妈妈"
            parentlist.add(parents2)
            participantListBean.parents = parentlist
            list2.add(participantListBean)
        }
        studentAdapter.setList(list2)
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
            }
            false
        }

        scheduleParticipantSearchBinding.tvStaff.setOnClickListener {
            if (staffOpen){
                val drawable = resources.getDrawable(R.drawable.schedule_fold_up_icon,null)
                drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
                scheduleParticipantSearchBinding.tvStaff.setCompoundDrawables(drawable,null,null,null)
                scheduleParticipantSearchBinding.rvStaff.visibility = View.GONE
                staffOpen = false
                return@setOnClickListener
            }
            val drawable = resources.getDrawable(R.drawable.schedule_fold_down_icon,null)
            drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
            scheduleParticipantSearchBinding.tvStaff.setCompoundDrawables(drawable,null,null,null)
            scheduleParticipantSearchBinding.rvStaff.visibility = View.VISIBLE
            staffOpen = true
        }

        scheduleParticipantSearchBinding.tvStudent.setOnClickListener {
            if (studentOpen){
                val drawable = resources.getDrawable(R.drawable.schedule_fold_up_icon,null)
                drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
                scheduleParticipantSearchBinding.tvStudent.setCompoundDrawables(drawable,null,null,null)
                scheduleParticipantSearchBinding.rvStudent.visibility = View.GONE
                studentOpen = false
                return@setOnClickListener
            }
            val drawable = resources.getDrawable(R.drawable.schedule_fold_down_icon,null)
            drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
            scheduleParticipantSearchBinding.tvStudent.setCompoundDrawables(drawable,null,null,null)
            scheduleParticipantSearchBinding.rvStudent.visibility = View.VISIBLE
            studentOpen = true
        }

        //初始化教职工列表
        scheduleParticipantSearchBinding.rvStaff.layoutManager = LinearLayoutManager(this)
        scheduleParticipantSearchBinding.rvStaff.adapter = staffAdapter

        staffAdapter.setOnItemClickListener { _, _, position ->
           loge("选择教职工：${list[position].name}")
            list[position].checked = !list[position].checked
            staffAdapter.setList(list)
        }

        //初始化学生列表
        scheduleParticipantSearchBinding.rvStudent.layoutManager = LinearLayoutManager(this)
        scheduleParticipantSearchBinding.rvStudent.adapter = studentAdapter

    }

    override fun getContentViewID(): Int = R.layout.activity_schedule_participant_search

    private val staffAdapter = object :
        BaseQuickAdapter<ParticipantRsp.DataBean.ParticipantListBean, BaseViewHolder>(R.layout.item_participant_list) {
        override fun convert(
            holder: BaseViewHolder,
            item: ParticipantRsp.DataBean.ParticipantListBean
        ) {
            holder.setText(R.id.tv_name, item.name)
            holder.getView<CheckBox>(R.id.checkBox).isEnabled = true
            holder.getView<CheckBox>(R.id.checkBox).isChecked = item.checked
        }
    }

    private val studentAdapter = object :
        BaseQuickAdapter<ParticipantRsp.DataBean.ParticipantListBean, BaseViewHolder>(R.layout.item_participant_search_student_list) {
        override fun convert(
            holder: BaseViewHolder,
            item: ParticipantRsp.DataBean.ParticipantListBean
        ) {
            holder.setText(R.id.tv_student_name, item.name)
            val recyclerView = holder.getView<RecyclerView>(R.id.rv_parent_of_student)
            recyclerView.layoutManager = LinearLayoutManager(this@ScheduleParticipantSearchActivity)
            recyclerView.adapter = parentsAdapter
            parentsAdapter.setList(item.parents)
        }
    }

    private val parentsAdapter = object :
        BaseQuickAdapter<ParticipantRsp.DataBean.ParticipantListBean.Parents, BaseViewHolder>(R.layout.item_participant_parent_of_student_list) {
        override fun convert(
            holder: BaseViewHolder,
            item: ParticipantRsp.DataBean.ParticipantListBean.Parents
        ) {
            holder.setText(R.id.tv_name, item.name)
            holder.setText(R.id.tv_guardian_name,item.status)
            holder.getView<CheckBox>(R.id.checkBox).isEnabled = true
            holder.getView<CheckBox>(R.id.checkBox).isChecked = item.checked
        }
    }
}