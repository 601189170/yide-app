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
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.databinding.FragmentStaffParticipantBinding
import com.yyide.chatim.model.schedule.ParticipantRsp
import com.yyide.chatim.utils.DisplayUtils
import com.yyide.chatim.utils.loge
import com.yyide.chatim.view.SpacesFlowItemDecoration
import com.yyide.chatim.viewmodel.ParticipantSharedViewModel
import com.yyide.chatim.viewmodel.StaffParticipantViewModel

/**
 * 参与人选择
 */
class StaffParticipantFragment : Fragment() {
    private var type: String? = null
    lateinit var staffParticipantBinding: FragmentStaffParticipantBinding
    private val staffParticipantViewModel: StaffParticipantViewModel by viewModels()
    private val participantSharedViewModel: ParticipantSharedViewModel by activityViewModels()

    //临时缓存请求过的数据
    private val listCache = mutableMapOf<String, ParticipantRsp.DataBean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = it.getString(ARG_TYPE)
        }
        staffParticipantViewModel.getTeacherParticipant()
        staffParticipantViewModel.getResponseResult().observe(this, {
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
                staffAdapter.setList(list)
                staffParticipantViewModel.getParticipantList().value?.add(it)
                navAdapter.setList(staffParticipantViewModel.getParticipantList().value)
                return@observe
            }
            ToastUtils.showShort("当前部门没有数据")
        })
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
                staffAdapter.setList(curList)
            }
        }

        //初始化人员列表
        val linearLayoutManager2 = LinearLayoutManager(requireContext())
        staffParticipantBinding.rvList.layoutManager = linearLayoutManager2
        staffAdapter.setList(staffParticipantViewModel.curParticipantList.value)
        staffParticipantBinding.rvList.adapter = staffAdapter
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
                        staffAdapter.setList(curList)
                        staffParticipantViewModel.getParticipantList().value?.add(dataBean)
                        navAdapter.setList(staffParticipantViewModel.getParticipantList().value)
                    } else {
                        //缓存没数据则需要请求数据
                        staffParticipantViewModel.getTeacherParticipant(
                            participantListBean.id ?: ""
                        )
                    }
                } else {
                    val value = participantSharedViewModel.curStaffParticipantList.value
                    if (value?.map { it.name }?.contains(participantListBean.name) == true) {
                        value.remove(participantListBean)
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
                loge("当前选中参与人数据发生变化：${participantList.size}")
                staffParticipantViewModel.curParticipantList.value?.also { curList ->
                    curList.forEach {
                        it.checked = participantList.contains(it)
                    }
                    staffAdapter.setList(curList)
                }

            })
    }

    private fun synParticipantListSelectedStatus() {
        //和选中的一致
        val value = participantSharedViewModel.curStaffParticipantList.value
        staffParticipantViewModel.curParticipantList.value?.forEach {
            if (value != null) {
                it.checked = value.map { it.id }.contains(it.id)
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

    companion object {
        private const val ARG_TYPE = "type"
        @JvmStatic
        fun newInstance(type: String) =
            StaffParticipantFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TYPE, type)
                }
            }
    }
}