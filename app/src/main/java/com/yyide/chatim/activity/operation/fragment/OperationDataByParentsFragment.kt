package com.yyide.chatim.activity.operation.fragment

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
import com.yyide.chatim.R
import com.yyide.chatim.activity.operation.OperationDetailActivity
import com.yyide.chatim.activity.operation.OperationDetailActivityByParents
import com.yyide.chatim.activity.operation.viewmodel.OperationViewModel
import com.yyide.chatim.activity.operation.viewmodel.OperationViewModelByParents
import com.yyide.chatim.databinding.ItemOperationBkBinding
import com.yyide.chatim.databinding.OperationDataFragmentBinding
import com.yyide.chatim.model.ParentWorkList
import com.yyide.chatim.model.TeacherWorkListRsp
import com.yyide.chatim.model.getClassSubjectListRsp

/**
 * @Desc 作业-数据
 * @Data 2022年2月17日13:35:32
 * @Author lrz
 */
class OperationDataByParentsFragment : Fragment() , SwipeRefreshLayout.OnRefreshListener{

    companion object {
        fun newInstance() = OperationDataByParentsFragment()
    }

    private val viewModel: OperationViewModelByParents by activityViewModels()


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
        viewModel.studentId.observe(viewLifecycleOwner){
            Log.e("TAG", "viewModel.studentId.observe: ")
            getParentsData(pageNum,pageSize)
        }

        viewModel.ParentsWorkDatasList.observe(viewLifecycleOwner){

            Log.e("TAG", "selectParentStudentData: "+JSON.toJSONString(it.getOrNull()) )
            viewBinding!!.swipeRefreshLayout.isRefreshing = false
            mAdapter.loadMoreModule.loadMoreComplete()
            if (it.isSuccess){
                val result = it.getOrNull()
                if (result!=null){
                    mAdapter.setList(result.list);
                    if (pageNum == 1) {
                            mAdapter.setList(result.list)
                    } else {
                        mAdapter.addData(result.list)

                    }
                }
            }
        }

    }
    fun getParentsData(pageNo: Int,pageSize:Int){

        val classesId=viewModel.classesId.value
        val studentId=viewModel.studentId.value
        val subjectId=viewModel.subjectId.value
        val startTime=viewModel.startTime.value
        val endTime=viewModel.endTime.value
        Log.e("TAG", "classesId: "+JSON.toJSONString(classesId) )
        Log.e("TAG", "studentId: "+JSON.toJSONString(studentId) )
        Log.e("TAG", "subjectId: "+JSON.toJSONString(subjectId) )
        if (subjectId!=null&&classesId!=null&&studentId!=null&&startTime!=null&&endTime!=null){
            viewModel.selectParentPage(subjectId,studentId,classesId,startTime,endTime,pageNo,pageSize)
        }
    }
    private fun initView() {

        viewBinding.recyclerView.layoutManager = LinearLayoutManager(activity)
        viewBinding.recyclerView.adapter = mAdapter
        viewBinding!!.swipeRefreshLayout.isRefreshing = false
        viewBinding!!.swipeRefreshLayout.setOnRefreshListener(this)
        context?.resources?.getColor(R.color.colorAccent)?.let { viewBinding!!.swipeRefreshLayout.setColorSchemeColors(it) }
        mAdapter.setOnItemClickListener(OnItemClickListener { adapter, view, position ->
            val intent = Intent(context, OperationDetailActivityByParents::class.java)
            intent.putExtra("id", mAdapter.getItem(position).id)
            startActivity(intent)
        })



        mAdapter.loadMoreModule.setOnLoadMoreListener {

            //上拉加载时取消下拉刷新
//            mSwipeRefreshLayout.setRefreshing(false);
            mAdapter.loadMoreModule.isEnableLoadMore = true
            //请求数据
            pageNum++
            getParentsData(pageNum,pageSize)
        }
        mAdapter.loadMoreModule.isAutoLoadMore = true
        //当自动加载开启，同时数据不满一屏时，是否继续执行自动加载更多(默认为true)
        mAdapter.loadMoreModule.isEnableLoadMoreIfNotFullPage = false
    }

    private val mAdapter =
        object :
            BaseQuickAdapter<ParentWorkList.DD, BaseViewHolder>(R.layout.item_operation_bk), LoadMoreModule {
            override fun convert(holder: BaseViewHolder, item: ParentWorkList.DD) {
                val viewBind = ItemOperationBkBinding.bind(holder.itemView)


                viewBind.tvName.text=item.subjectName
                viewBind.tvTime.text=item.releaseTime
                viewBind.tvContent.text=item.title
                //家长视角展示

                if (item.completion==1){
                    viewBind.postStuatsLeft.text="已完成"
                }else{
                    viewBind.postStuatsLeft.text="未完成"
                }
                if (item.completion==1){
                    viewBind.postStuatsRihgt.text="很简单"
                }else if(item.completion==2){
                    viewBind.postStuatsRihgt.text="有难度"
                } else{
                    viewBind.postStuatsRihgt.text="非常难"
                }
                if (item.isFeedback){
                    viewBind.postStuatsLeft.visibility=View.VISIBLE
                    viewBind.postStuatsRihgt.visibility=View.VISIBLE
                    viewBind.noPost.visibility=View.GONE
                }else{
                    viewBind.postStuatsLeft.visibility=View.GONE
                    viewBind.postStuatsRihgt.visibility=View.GONE
                    viewBind.noPost.visibility=View.VISIBLE
                }

                //家长视角隐藏
                viewBind.tvHaveRead.visibility=View.GONE
                viewBind.tvComplete.visibility=View.GONE

            }
        }

    override fun onRefresh() {

        // 这里的作用是防止下拉刷新的时候还可以上拉加载
        mAdapter.loadMoreModule.isEnableLoadMore = false
        pageNum = 1
        getParentsData(pageNum,pageSize)
    }
    override fun onResume(){
        super.onResume()
        getParentsData(pageNum,pageSize)
    }
}