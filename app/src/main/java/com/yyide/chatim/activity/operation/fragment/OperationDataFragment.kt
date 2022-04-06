package com.yyide.chatim.activity.operation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.fastjson.JSON
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.activity.book.BookStudentDetailActivity
import com.yyide.chatim.activity.operation.OperationDetailActivity
import com.yyide.chatim.activity.operation.viewmodel.OperationTearcherViewModel
import com.yyide.chatim.activity.operation.viewmodel.OperationViewModel
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.constant.ResultCodeStr
import com.yyide.chatim.databinding.ItemOperationBkBinding
import com.yyide.chatim.databinding.ItemWeeklyAttendanceBinding
import com.yyide.chatim.databinding.OperationDataFragmentBinding
import com.yyide.chatim.model.EventMessage
import com.yyide.chatim.model.TeacherWorkListRsp
import com.yyide.chatim.viewmodel.ScheduleEditViewModel
import com.yyide.chatim.viewmodel.ScheduleMangeViewModel
import org.greenrobot.eventbus.EventBus

/**
 * @Desc 作业-数据
 * @Data 2022年2月17日13:35:32
 * @Author lrz
 */
class OperationDataFragment : Fragment() {

    companion object {
        fun newInstance() = OperationDataFragment()
    }

//    private lateinit var viewModel: OperationTearcherViewModel
    private lateinit var viewModel: OperationViewModel
//    private val viewModel by view<OperationViewModel>()

//    private val viewModel: OperationViewModel by viewModels()
    private lateinit var viewBinding: OperationDataFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = OperationDataFragmentBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(OperationViewModel::class.java)

        initView()

    }

    private fun initView() {

        viewBinding.recyclerView.layoutManager = LinearLayoutManager(activity)
        viewBinding.recyclerView.adapter = mAdapter
        mAdapter.setOnItemChildClickListener(OnItemChildClickListener { adapter, view, position ->
            val intent = Intent(context, OperationDetailActivity::class.java)
            intent.putExtra("id", mAdapter.getItem(position).id)
            startActivity(intent)
        })
        viewModel.TeacherWorkListLiveData.observe(viewLifecycleOwner){
            Log.e("TAG", "initView: "+JSON.toJSONString(it) )
            Log.e("TAG", "TeacherWorkListLiveData: "+JSON.toJSONString(it.getOrNull()) )
            if (it.isSuccess){
                val result = it.getOrNull()
                if (result!=null){
                    mAdapter.setList(result.data.list);
                }
            }
        }
    }

    private val mAdapter =
        object :
            BaseQuickAdapter<TeacherWorkListRsp.DataB.WorkInfo, BaseViewHolder>(R.layout.item_operation_bk) {
            override fun convert(holder: BaseViewHolder, item: TeacherWorkListRsp.DataB.WorkInfo) {
                val viewBind = ItemOperationBkBinding.bind(holder.itemView)
                val read = getString(R.string.operation_read_html, 0, 40)
                val completed = getString(R.string.operation_completed_html, 0, 40)
                Html.fromHtml(read)
                Html.fromHtml(completed)

                viewBind.tvName.text=item.subjectName
                viewBind.tvTime.text=item.releaseTime
                viewBind.tvContent.text=item.title
                viewBind.tvHaveRead.text=Html.fromHtml(read)
                viewBind.tvComplete.text=Html.fromHtml(completed)
            }
        }

}