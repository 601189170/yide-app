package com.yyide.chatim.activity.operation.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.fastjson.JSON
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.activity.operation.viewmodel.OperationTearcherViewModel
import com.yyide.chatim.activity.operation.viewmodel.OperationViewModel
import com.yyide.chatim.databinding.ItemOperationBkBinding
import com.yyide.chatim.databinding.ItemOperationWorkTypeBinding
import com.yyide.chatim.databinding.OperationChildFragmentBinding
import com.yyide.chatim.databinding.OperationDataFragmentBinding
import com.yyide.chatim.fragment.schedule.StaffParticipantFragment
import com.yyide.chatim.kotlin.network.NetworkApi.updateRemind
import com.yyide.chatim.model.TeacherWorkListRsp
import com.yyide.chatim.model.getSchoolWork

class OperationChildFragment : Fragment() {

    companion object {
        private const val ARG_TYPE = "type"
//        fun newInstance() = OperationChildFragment()
        @JvmStatic
        fun newInstance(type: Int) =
        OperationChildFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_TYPE, type)
                    }
                }
    }
    private var type: Int = 0


//    var bean:getSchoolWork?=null
    private val viewModel: OperationTearcherViewModel by activityViewModels()
    private lateinit var viewBinding: OperationChildFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = OperationChildFragmentBinding.inflate(layoutInflater)
        return viewBinding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arguments?.let {
            type = it.getInt(ARG_TYPE)
        }
        viewBinding.recyclerView.layoutManager = LinearLayoutManager(activity)

        viewModel.getSchoolWorkRsp.observe(viewLifecycleOwner){
            if (it.isSuccess){
                val result = it.getOrNull()
                if (result!=null){

                    when(type){
                        0 -> {
                            viewBinding.recyclerView.adapter = mAdapter0
                            viewBinding.topItem.tv1
                            mAdapter0.setList(result.feedbackList)
                        }
                        1 ->{
                            viewBinding.recyclerView.adapter = mAdapter1
                            mAdapter1.setList(result.noFeedbackList)
                            viewBinding.topItem.tv3.text="一键提醒所有"
                            viewBinding.topItem.tv2.visibility=View.GONE
                            viewBinding.topItem.tv3.setTextColor(resources.getColor(R.color.select_circle_color))

                            viewBinding.topItem.tv3.setOnClickListener(View.OnClickListener {
                                //一键提醒所有
                                val  workId=result.classesTimetable.workId
                                val  classesId=result.classesTimetable.classesId

                                if (!TextUtils.isEmpty(workId)&&!TextUtils.isEmpty(classesId)){
                                    viewModel.updateRemind("0",workId,classesId,"")
                                }

                            })
                        }
                        2 ->{
                            viewBinding.recyclerView.adapter = mAdapter2
                            mAdapter2.setList(result.readList)
                            viewBinding.topItem.tv3.visibility=View.GONE
                            viewBinding.topItem.tv2.visibility=View.GONE
                        }
                        3 ->{
                            viewBinding.recyclerView.adapter = mAdapter3
                            mAdapter3.setList(result.noReadList)
                            viewBinding.topItem.tv3.visibility=View.GONE
                            viewBinding.topItem.tv2.visibility=View.GONE
                        }
                    }

                }
            }

        }


    }



    private val mAdapter0 =
            object :
                    BaseQuickAdapter<getSchoolWork.Feedback, BaseViewHolder>(R.layout.item_operation_work_type) {
                override fun convert(holder: BaseViewHolder, item: getSchoolWork.Feedback) {
                    val viewBind = ItemOperationWorkTypeBinding.bind(holder.itemView)
                    viewBind.tv1.text=item.studentName
                    if (getItemPosition(item) == data.size - 1) {
                        viewBind.line.visibility=View.GONE
                    } else {
                        viewBind.line.visibility=View.VISIBLE
                    }

                    if (item.completion==1){
                        viewBind.tv2.text="已完成"
                    }else{
                        viewBind.tv2.text="未完成"
                    }
                    if (item.difficulty==1){
                        viewBind.tv3.text="很简单"
                    }else if((item.difficulty==2)){
                        viewBind.tv3.text="有难度"
                    }else if((item.difficulty==3)){
                        viewBind.tv3.text="非常难"
                    }else{
                        viewBind.tv3.text="未知"

                    }


                }
            }



    private val mAdapter1 =
            object :
                    BaseQuickAdapter<getSchoolWork.NoFeedback, BaseViewHolder>(R.layout.item_operation_work_type) {
                @SuppressLint("ResourceAsColor")
                override fun convert(holder: BaseViewHolder, item: getSchoolWork.NoFeedback) {
                    val viewBind = ItemOperationWorkTypeBinding.bind(holder.itemView)
                    viewBind.tv1.text=item.studentName
                    if (item.isRemind){
                        viewBind.tv3.text="已发送"
                    }else{
                        viewBind.tv3.text="提醒"
                        viewBind.tv3.setTextColor(resources.getColor(R.color.select_circle_color))
                        viewBind.tv3.setOnClickListener(View.OnClickListener {
                            //发送提醒
                            val workId= viewModel.getSchoolWorkRsp.value?.getOrNull()?.classesTimetable?.workId
                            val classesId= viewModel.getSchoolWorkRsp.value?.getOrNull()?.classesTimetable?.classesId
                           if (!TextUtils.isEmpty(workId)&&!TextUtils.isEmpty(classesId)){
                               classesId?.let { it1 -> workId?.let { it2 -> viewModel.updateRemind("1", it2, it1,item.studentId) } }
                           }




                        })
                    }
                    if (getItemPosition(item) == data.size - 1) {
                        viewBind.line.visibility=View.GONE
                    } else {
                        viewBind.line.visibility=View.VISIBLE
                    }
                }
            }

    private val mAdapter2 =
            object :
                    BaseQuickAdapter<getSchoolWork.Read, BaseViewHolder>(R.layout.item_operation_work_type) {
                override fun convert(holder: BaseViewHolder, item: getSchoolWork.Read) {
                    val viewBind = ItemOperationWorkTypeBinding.bind(holder.itemView)
                    viewBind.tv1.text=item.studentName

                    if (getItemPosition(item) == data.size - 1) {
                        viewBind.line.visibility=View.GONE
                    } else {
                        viewBind.line.visibility=View.VISIBLE
                    }
                }
            }

    private val mAdapter3 =
            object :
                    BaseQuickAdapter<getSchoolWork.NoRead, BaseViewHolder>(R.layout.item_operation_work_type) {
                override fun convert(holder: BaseViewHolder, item: getSchoolWork.NoRead) {
                    val viewBind = ItemOperationWorkTypeBinding.bind(holder.itemView)
                    viewBind.tv1.text=item.studentName
                    if (getItemPosition(item) == data.size - 1) {
                        viewBind.line.visibility=View.GONE
                    } else {
                        viewBind.line.visibility=View.VISIBLE
                    }
                }
            }
}