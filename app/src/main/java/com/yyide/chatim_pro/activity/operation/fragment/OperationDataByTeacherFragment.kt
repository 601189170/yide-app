package com.yyide.chatim_pro.activity.operation.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.alibaba.fastjson.JSON
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim_pro.R
import com.yyide.chatim_pro.activity.operation.OperationDetailActivity
import com.yyide.chatim_pro.activity.operation.viewmodel.OperationViewModel
import com.yyide.chatim_pro.databinding.ItemOperationBkBinding
import com.yyide.chatim_pro.databinding.OperationDataFragmentBinding
import com.yyide.chatim_pro.model.TeacherWorkListRsp
import com.yyide.chatim_pro.model.getClassSubjectListRsp

/**
 * @Desc 作业-数据 老师身份
 * @Data 2022年2月17日13:35:32
 * @Author lrz
 */
class OperationDataByTeacherFragment : Fragment() , SwipeRefreshLayout.OnRefreshListener{

    companion object {
        fun newInstance() = OperationDataByTeacherFragment()
    }

    private val viewModel: OperationViewModel by activityViewModels()


    private lateinit var viewBinding: OperationDataFragmentBinding
    private var pageNum = 1
    private var pageSize = 20
    private var listclassssub = mutableListOf<getClassSubjectListRsp>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = OperationDataFragmentBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(OperationViewModel::class.java)

        initView()
        viewModel.rightData.observe(viewLifecycleOwner){
            getTeacherData(pageNum,pageSize)
        }

// 监听开始时间变化
        viewModel.startTime.observe(viewLifecycleOwner) {
            getTeacherData(pageNum,pageSize)
        }
        viewModel.endTime.observe(viewLifecycleOwner) {
            getTeacherData(pageNum,pageSize)
        }

    }
    fun getTeacherData(pageNo: Int,pageSize:Int ){


        val wh=viewModel.leftData.value?.type
        val classesId=viewModel.classesId.value
        val subjectId=viewModel.subjectId.value
        val startTime=viewModel.startTime.value
        val endTime=viewModel.endTime.value
        if (wh!=null&&classesId!=null&&subjectId!=null&&startTime!=null&&endTime!=null){
            viewModel.getTecherWorkList(wh,subjectId,classesId,startTime,endTime,pageNo,pageSize)
        }
    }
    private fun initView() {

        viewBinding.recyclerView.layoutManager = LinearLayoutManager(activity)
        viewBinding.recyclerView.adapter = mAdapter
        viewBinding!!.swipeRefreshLayout.isRefreshing = false
        viewBinding!!.swipeRefreshLayout.setOnRefreshListener(this)
        context?.resources?.getColor(R.color.colorAccent)?.let { viewBinding!!.swipeRefreshLayout.setColorSchemeColors(it) }
        mAdapter.setOnItemClickListener(OnItemClickListener { adapter, view, position ->
            val intent = Intent(context, OperationDetailActivity::class.java)
            intent.putExtra("id", mAdapter.getItem(position).id)
            startActivity(intent)
        })

        viewModel.TeacherWorkListLiveData.observe(viewLifecycleOwner){
            Log.e("TAG", "initView: "+JSON.toJSONString(it) )
            Log.e("TAG", "TeacherWorkListLiveData: "+JSON.toJSONString(it.getOrNull()) )
            viewBinding!!.swipeRefreshLayout.isRefreshing = false
            mAdapter.loadMoreModule.loadMoreComplete()
            if (it.isSuccess){
                val result = it.getOrNull()
                if (result!=null){
//                    mAdapter.setList(result.list);
                    if (pageNum == 1) {
                            mAdapter.setList(result.list)
                    } else {
                        mAdapter.addData(result.list)

                    }
                }
            }else{
                viewBinding.swipeRefreshLayout.setRefreshing(false);
            }
        }

        mAdapter.loadMoreModule.setOnLoadMoreListener {

            //上拉加载时取消下拉刷新
//            mSwipeRefreshLayout.setRefreshing(false);
            mAdapter.loadMoreModule.isEnableLoadMore = true
            //请求数据
            pageNum++
            getTeacherData(pageNum,pageSize)
        }
        mAdapter.loadMoreModule.isAutoLoadMore = true
        //当自动加载开启，同时数据不满一屏时，是否继续执行自动加载更多(默认为true)
        mAdapter.loadMoreModule.isEnableLoadMoreIfNotFullPage = false
    }

    private val mAdapter =
        object :
            BaseQuickAdapter<TeacherWorkListRsp.WorkInfo, BaseViewHolder>(R.layout.item_operation_bk), LoadMoreModule {
            override fun convert(holder: BaseViewHolder, item: TeacherWorkListRsp.WorkInfo) {
                val viewBind = ItemOperationBkBinding.bind(holder.itemView)
                val read = getString(R.string.operation_read_html, item.read, item.total)
                val completed = getString(R.string.operation_completed_html, item.completed, item.total)
                Html.fromHtml(read)
                Html.fromHtml(completed)

                viewBind.tvName.text=item.subjectName
                if (item.isScheduled){
                    viewBind.tvTime.setTextColor(resources.getColor(R.color.colorPrimary))
                    viewBind.tvTime.text=item.releaseTime+"发布"
                }else{
                    viewBind.tvTime.setTextColor(resources.getColor(R.color.textview999))
                    viewBind.tvTime.text=item.releaseTime
                }
                viewBind.tvContent.text=item.title
                viewBind.tvHaveRead.text=Html.fromHtml(read)
                viewBind.tvComplete.text=Html.fromHtml(completed)
            }
        }

    override fun onRefresh() {

        // 这里的作用是防止下拉刷新的时候还可以上拉加载
        mAdapter.loadMoreModule.isEnableLoadMore = false
        pageNum = 1
        getTeacherData(pageNum,pageSize)
    }
    override fun onResume(){
        super.onResume()
        getTeacherData(pageNum,pageSize)
    }
}