package com.yyide.chatim.fragment.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.databinding.FragmentStaffParticipantBinding
import com.yyide.chatim.model.schedule.ParticipantRsp
import com.yyide.chatim.utils.DisplayUtils
import com.yyide.chatim.utils.loge
import com.yyide.chatim.view.SpacesFlowItemDecoration
import com.yyide.chatim.viewmodel.ParticipantSharedViewModel

private const val ARG_TYPE = "type"

/**
 *
 */
class StaffParticipantFragment : Fragment() {
    private var type: String? = null
    lateinit var staffParticipantBinding: FragmentStaffParticipantBinding
    private val list = mutableListOf<ParticipantRsp.DataBean>()
    private val participantViewModel: ParticipantSharedViewModel by activityViewModels()

    //临时缓存请求过的数据
    private val listCache = mutableMapOf<String, ParticipantRsp.DataBean>()

    //当前也显示的参与人及部门数据
    private val curList = mutableListOf<ParticipantRsp.DataBean.ParticipantListBean>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = it.getString(ARG_TYPE)
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
        initData()
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
        navAdapter.setList(list)
        staffParticipantBinding.rvTopNavList.adapter = navAdapter
        navAdapter.setOnItemClickListener { _, _, position ->
            loge("查看部门：${list[position].name}")
            // 0,1,2
            if (position == list.size - 1) {
                return@setOnItemClickListener
            }
            while (position + 1 < list.size) {
                //返回到前面的数据
                list.removeAt(position + 1)
                navAdapter.setList(list)
            }
            val dataBean = list[position]
            curList.clear()
            dataBean.departmentList?.let { curList.addAll(it) }
            dataBean.participantList?.let { curList.addAll(it) }
            //和选中的一致
            synParticipantListSelectedStatus()
            staffAdapter.setList(curList)
        }

        //初始化人员列表
        val linearLayoutManager2 = LinearLayoutManager(requireContext())
        staffParticipantBinding.rvList.layoutManager = linearLayoutManager2
        staffAdapter.setList(curList)
        staffParticipantBinding.rvList.adapter = staffAdapter
        staffAdapter.setOnItemClickListener { _, _, position ->
            loge("选择人员：$position")
            val participantListBean = curList[position]
            if (participantListBean.department) {
                val dataBean = listCache[participantListBean.name]
                if (dataBean != null) {
                    curList.clear()
                    dataBean.departmentList?.let { curList.addAll(it) }
                    dataBean.participantList?.let { curList.addAll(it) }
                    synParticipantListSelectedStatus()
                    staffAdapter.setList(curList)
                    list.add(dataBean)
                    navAdapter.setList(list)
                } else {
                    //缓存没数据则需要请求数据
                }
            } else {
                val value = participantViewModel.curStaffParticipantList.value
                if (value?.map { it.name }?.contains(participantListBean.name) == true) {
                    value.remove(participantListBean)
                } else {
                    value?.add(participantListBean)
                }
                participantViewModel.curStaffParticipantList.postValue(value)
            }
        }

        //监听父activity改变当前选中人员数据变化
        participantViewModel.curStaffParticipantList.observe(requireActivity(), { participantList ->
            loge("当前选中参与人数据发生变化：${participantList.size}")
            curList.forEach {
                it.checked = participantList.contains(it)
            }
            staffAdapter.setList(curList)
        })

    }

    private fun synParticipantListSelectedStatus() {
        //和选中的一致
        val value = participantViewModel.curStaffParticipantList.value
        curList.forEach {
            if (value != null) {
                it.checked = value.map { it.name }.contains(it.name)
            }
        }
    }

    private val navAdapter = object :
        BaseQuickAdapter<ParticipantRsp.DataBean, BaseViewHolder>(R.layout.item_participant_top_nav) {
        override fun convert(holder: BaseViewHolder, item: ParticipantRsp.DataBean) {
            holder.setText(R.id.tv_name, item.name?.name)
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

    fun initData() {
        val dataBean = ParticipantRsp.DataBean()
        dataBean.name = ParticipantRsp.DataBean.NameBean(id = "", name = "深圳中学")
        val participantList = mutableListOf<ParticipantRsp.DataBean.ParticipantListBean>()
        participantList.add(ParticipantRsp.DataBean.ParticipantListBean("", "校长"))
        participantList.add(ParticipantRsp.DataBean.ParticipantListBean("", "负责人"))
        val departmentList = mutableListOf<ParticipantRsp.DataBean.ParticipantListBean>()
        departmentList.add(ParticipantRsp.DataBean.ParticipantListBean("", "教务处", true))
        departmentList.add(ParticipantRsp.DataBean.ParticipantListBean("", "后勤部", true))
        departmentList.add(ParticipantRsp.DataBean.ParticipantListBean("", "工会", true))
        departmentList.add(ParticipantRsp.DataBean.ParticipantListBean("", "校长办公室", true))
        dataBean.departmentList = departmentList
        dataBean.participantList = participantList
        list.add(dataBean)
        listCache["深圳中学"] = dataBean
        val dataBean2 = ParticipantRsp.DataBean()
        dataBean2.name = ParticipantRsp.DataBean.NameBean(id = "", name = "教务处")
        val participantList1 = mutableListOf<ParticipantRsp.DataBean.ParticipantListBean>()
        participantList1.add(ParticipantRsp.DataBean.ParticipantListBean("", "教务处长"))
        participantList1.add(ParticipantRsp.DataBean.ParticipantListBean("", "教务处|负责人"))
        val departmentList1 = mutableListOf<ParticipantRsp.DataBean.ParticipantListBean>()
        departmentList1.add(ParticipantRsp.DataBean.ParticipantListBean("", "教学组", true))
        departmentList1.add(ParticipantRsp.DataBean.ParticipantListBean("", "校纪律组", true))
        dataBean2.departmentList = departmentList1
        dataBean2.participantList = participantList1
        listCache["教务处"] = dataBean2

        val dataBean3 = ParticipantRsp.DataBean()
        dataBean3.name = ParticipantRsp.DataBean.NameBean(id = "", name = "教学组")
        val participantList2 = mutableListOf<ParticipantRsp.DataBean.ParticipantListBean>()
        participantList2.add(ParticipantRsp.DataBean.ParticipantListBean("", "教学组|负责人"))
        val departmentList2 = mutableListOf<ParticipantRsp.DataBean.ParticipantListBean>()
        departmentList2.add(ParticipantRsp.DataBean.ParticipantListBean("", "语文教研室", true))
        departmentList2.add(ParticipantRsp.DataBean.ParticipantListBean("", "数学教研室", true))
        dataBean3.departmentList = departmentList2
        dataBean3.participantList = participantList2
        listCache["教学组"] = dataBean3


        val dataBean1 = list[0]
        curList.addAll(dataBean1.departmentList!!)
        curList.addAll(dataBean1.participantList!!)
    }

    companion object {
        @JvmStatic
        fun newInstance(type: String) =
            StaffParticipantFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TYPE, type)
                }
            }
    }
}