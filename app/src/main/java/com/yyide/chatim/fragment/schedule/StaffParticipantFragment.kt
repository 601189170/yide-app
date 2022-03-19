package com.yyide.chatim.fragment.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.fastjson.JSON
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.SpData
import com.yyide.chatim.activity.schedule.ScheduleParticipantSearchActivity.Companion.guardianName
import com.yyide.chatim.databinding.FragmentStaffParticipantBinding
import com.yyide.chatim.model.schedule.ParticipantRsp
import com.yyide.chatim.model.schedule.SearchParticipantRsp
import com.yyide.chatim.model.schedule.toParticipantListBean
import com.yyide.chatim.utils.DisplayUtils
import com.yyide.chatim.utils.loge
import com.yyide.chatim.view.SpacesFlowItemDecoration
import com.yyide.chatim.viewmodel.ParticipantSharedViewModel
import com.yyide.chatim.viewmodel.StaffParticipantViewModel

/**
 * 参与人选择
 */
class StaffParticipantFragment : Fragment() {
    private var type: Int = 0
    lateinit var staffParticipantBinding: FragmentStaffParticipantBinding
    private val staffParticipantViewModel: StaffParticipantViewModel by viewModels()
    private val participantSharedViewModel: ParticipantSharedViewModel by activityViewModels()

    //临时缓存请求过的数据
    private val listCache = mutableMapOf<String, ParticipantRsp.DataBean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = it.getInt(ARG_TYPE)
        }
        requestData(null)
        staffParticipantViewModel.getResponseResult().observe(this) {
            if (it != null) {
                listCache[it.name ?: "未知"] = it
                val list = mutableListOf<ParticipantRsp.DataBean.ParticipantListBean>()
                it.departmentList?.let {
                    it.forEach { it.department = true }
                    list.addAll(it)
                }
                it.participantList?.let {
                    it.forEach { it.department = false }
                    list.addAll(it)
                }
                staffParticipantViewModel.curParticipantList.value = list
                synParticipantListSelectedStatus()
                //家长参与人列表有值时，使用有监护人的adapter
                if (type == PARTICIPANT_TYPE_STAFF) {
                    staffAdapter.setList(list)
                } else {
                    if (it.departmentList?.isNotEmpty() == true) {
                        staffAdapter.setList(list)
                    } else {
                        val linearLayoutManager3 = LinearLayoutManager(requireContext())
                        staffParticipantBinding.rvList.layoutManager = linearLayoutManager3
                        staffParticipantBinding.rvList.adapter = studentAdapter
                        studentAdapter.setList(list)
                    }
                }

                staffParticipantViewModel.getParticipantList().value?.add(it)
                navAdapter.setList(staffParticipantViewModel.getParticipantList().value)
                return@observe
            }
            ToastUtils.showShort("当前部门没有数据")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        staffParticipantBinding = FragmentStaffParticipantBinding.inflate(layoutInflater)
        return staffParticipantBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.orientation = RecyclerView.HORIZONTAL
        staffParticipantBinding.rvTopNavList.layoutManager = linearLayoutManager
        staffParticipantBinding.rvTopNavList.addItemDecoration(
            SpacesFlowItemDecoration(
                DisplayUtils.dip2px(
                    requireContext(),
                    6f
                ), 0
            )
        )
        navAdapter.setList(staffParticipantViewModel.getParticipantList().value)
        staffParticipantBinding.rvTopNavList.adapter = navAdapter
        navAdapter.setOnItemClickListener { _, _, position ->
            staffParticipantViewModel.getParticipantList().value?.also {
                loge("查看部门：${it[position].name}")
                if (position == it.size - 1) {
                    return@setOnItemClickListener
                }
                while (position + 1 < it.size) {
                    //返回到前面的数据
                    it.removeAt(position + 1)
                    navAdapter.setList(it)
                }
                val dataBean = it[position]
                val curList = staffParticipantViewModel.curParticipantList.value
                curList?.clear()
                dataBean.departmentList?.let { curList?.addAll(it) }
                dataBean.participantList?.let { curList?.addAll(it) }
                //和选中的一致
                synParticipantListSelectedStatus()
                staffParticipantBinding.rvList.adapter = staffAdapter
                staffAdapter.setList(curList)
            }
        }

        //初始化人员列表
        val linearLayoutManager2 = LinearLayoutManager(requireContext())
        staffParticipantBinding.rvList.layoutManager = linearLayoutManager2
        staffAdapter.setList(staffParticipantViewModel.curParticipantList.value)
        staffParticipantBinding.rvList.adapter = staffAdapter
        //studentAdapter.
        staffAdapter.setOnItemClickListener { _, _, position ->
            loge("选择人员：$position")
            staffParticipantViewModel.curParticipantList.value?.also { curList ->
                val participantListBean = curList[position]
                if (participantListBean.department) {
                    val dataBean = listCache[participantListBean.name]
                    if (dataBean != null) {
                        curList.clear()
                        dataBean.departmentList?.let { curList.addAll(it) }
                        dataBean.participantList?.let { curList.addAll(it) }
                        synParticipantListSelectedStatus()
                        if (type == PARTICIPANT_TYPE_STAFF) {
                            staffAdapter.setList(curList)
                        } else {
                            if (dataBean.departmentList?.isNotEmpty() == true) {
                                staffAdapter.setList(curList)
                            } else {
                                val linearLayoutManager3 = LinearLayoutManager(requireContext())
                                staffParticipantBinding.rvList.layoutManager = linearLayoutManager3
                                staffParticipantBinding.rvList.adapter = studentAdapter
                                studentAdapter.setList(curList)
                            }
                        }

                        staffParticipantViewModel.getParticipantList().value?.add(dataBean)
                        navAdapter.setList(staffParticipantViewModel.getParticipantList().value)
                    } else {
                        //缓存没数据则需要请求数据
                        requestData(participantListBean)
                    }
                } else {
                    val value = participantSharedViewModel.curStaffParticipantList.value
                    if (value?.map { it.userId }?.contains(participantListBean.userId) == true) {
                        //value.remove(participantListBean)
                        val iterator = value.iterator()
                        while (iterator.hasNext()) {
                            val next = iterator.next()
                            if (next.userId == participantListBean.userId) {
                                value.remove(next)
                                break
                            }
                        }
                    } else {
                        value?.add(participantListBean)
                    }
                    participantSharedViewModel.curStaffParticipantList.postValue(value)
                }
            }
        }

        //监听父activity改变当前选中人员数据变化
        participantSharedViewModel.curStaffParticipantList.observe(
            requireActivity(),
            { participantList ->
                loge("当前选中参与人数据发生变化${JSON.toJSONString(participantList)}")
                loge("当前选中参与人数据发生变化：${participantList.size}")
                loge(participantList.map { it.userId }.toString())
                if (type == PARTICIPANT_TYPE_STAFF) {
                    staffParticipantViewModel.curParticipantList.value?.also { curList ->
                        curList.forEach {
                            it.checked = participantList.map { it.userId }.contains(it.userId)
                        }
                        staffAdapter.setList(curList)
                    }
                } else {
                    staffParticipantViewModel.curParticipantList.value?.also { curList ->
                        curList.forEach {
                            it.guardians.forEach {
                                loge("it.userId=${it.userId}")
                                it.checked = participantList.map { it.userId }.contains(it.userId)
                            }
                        }
                        studentAdapter.setList(curList)
                    }
                }
            })
    }

    /**
     * 请求参与人列表数据
     */
    private fun requestData(participantListBean: ParticipantRsp.DataBean.ParticipantListBean?) {
        when (type) {
            PARTICIPANT_TYPE_STAFF -> {
                //请求教职工数据
                staffParticipantViewModel.getTeacherParticipant(participantListBean?.id ?: "")
            }
            PARTICIPANT_TYPE_GUARDIAN -> {
                //请求家长监护人数据
                staffParticipantViewModel.getStudentGuardianParticipant(
                    participantListBean?.id ?: "", participantListBean?.type ?: "0", "1"
                )
            }
            PARTICIPANT_TYPE_STUDENT -> {
                //请求学生数据
                staffParticipantViewModel.getStudentGuardianParticipant(
                    participantListBean?.id ?: "", participantListBean?.type ?: "0", "2"
                )
            }
            else -> {
            }
        }
    }

    private fun synParticipantListSelectedStatus() {
        //和选中的一致
        val value = participantSharedViewModel.curStaffParticipantList.value
        staffParticipantViewModel.curParticipantList.value?.forEach {
            if (value != null) {
                if (type == PARTICIPANT_TYPE_STAFF) {
                    it.checked = value.map { it.userId }.contains(it.userId)
                } else {
                    it.guardians.forEach {
                        it.checked = value.map { it.userId }.contains(it.userId)
                    }
                }
            }
        }
    }

    private val navAdapter = object :
        BaseQuickAdapter<ParticipantRsp.DataBean, BaseViewHolder>(R.layout.item_participant_top_nav) {
        override fun convert(holder: BaseViewHolder, item: ParticipantRsp.DataBean) {
            holder.setText(R.id.tv_name, item.name)
            val list = staffParticipantViewModel.getParticipantList().value
            list?.also {
                if (item == list[list.size - 1]) {
                    //最后一个tab置灰不可点击
                    holder.getView<ImageView>(R.id.iv_right).visibility = View.GONE
                    if (list.size != 1) {
                        holder.setTextColor(R.id.tv_name, resources.getColor(R.color.black11))
                    }
                } else {
                    holder.getView<ImageView>(R.id.iv_right).visibility = View.VISIBLE
                    holder.setTextColor(R.id.tv_name, resources.getColor(R.color.black9))
                }
            }
        }
    }

    private val staffAdapter = object :
        BaseQuickAdapter<ParticipantRsp.DataBean.ParticipantListBean, BaseViewHolder>(R.layout.item_participant_list) {
        override fun convert(
            holder: BaseViewHolder,
            item: ParticipantRsp.DataBean.ParticipantListBean
        ) {
            holder.setText(R.id.tv_name, item.name)
            holder.getView<CheckBox>(R.id.checkBox).isEnabled = !item.department
            holder.getView<CheckBox>(R.id.checkBox).isChecked = item.checked
        }
    }

    private val studentAdapter = object :
        BaseQuickAdapter<ParticipantRsp.DataBean.ParticipantListBean, BaseViewHolder>(R.layout.item_participant_student_guardians_list) {
        override fun convert(
            holder: BaseViewHolder,
            item: ParticipantRsp.DataBean.ParticipantListBean
        ) {
            holder.setText(R.id.tv_student_name, "${item.name}")
            val recyclerView = holder.getView<RecyclerView>(R.id.rv_parent_of_student)
            recyclerView.layoutManager = LinearLayoutManager(requireActivity())
            val parentsAdapter = object :
                BaseQuickAdapter<ParticipantRsp.DataBean.ParticipantListBean.GuardiansBean, BaseViewHolder>(
                    R.layout.item_participant_parent_of_student_list
                ) {
                override fun convert(
                    holder: BaseViewHolder,
                    item: ParticipantRsp.DataBean.ParticipantListBean.GuardiansBean
                ) {
                    holder.setText(R.id.tv_name, item.guardianName)
                    holder.getView<CheckBox>(R.id.checkBox).isEnabled = true
                    holder.getView<CheckBox>(R.id.checkBox).isChecked = item.checked

                    holder.setText(R.id.tv_guardian_name, guardianName(item.relation))
                }
            }
            val guardians = item.guardians
            recyclerView.adapter = parentsAdapter
            parentsAdapter.setList(guardians)

            parentsAdapter.setOnItemClickListener { _, _, position ->
                guardians[position].checked = !guardians[position].checked
                parentsAdapter.setList(guardians)

                //上顶部选中
                val participantListBean = guardians[position].toParticipantListBean()
                val value = participantSharedViewModel.curStaffParticipantList.value
                if (value?.map { it.userId }?.contains(participantListBean.userId) == true) {
                    //value.remove(participantListBean)
                    val iterator = value.iterator()
                    while (iterator.hasNext()) {
                        val next = iterator.next()
                        if (next.userId == participantListBean.userId) {
                            value.remove(next)
                            break
                        }
                    }
                } else {
                    value?.add(participantListBean)
                }
                participantSharedViewModel.curStaffParticipantList.postValue(value)
            }
        }

    }

    companion object {
        private const val ARG_TYPE = "type"

        //教职工
        const val PARTICIPANT_TYPE_STAFF = 1

        //学生
        const val PARTICIPANT_TYPE_STUDENT = 2

        //家长监护人
        const val PARTICIPANT_TYPE_GUARDIAN = 3

        @JvmStatic
        fun newInstance(type: Int) =
            StaffParticipantFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_TYPE, type)
                }
            }
    }
}