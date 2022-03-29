package com.yyide.chatim.fragment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.tencent.qcloud.tim.uikit.utils.ToastUtil
import com.yyide.chatim.R
import com.yyide.chatim.activity.leave.LeaveFlowDetailActivity
import com.yyide.chatim.adapter.TodoAdapter
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.base.KTBaseFragment
import com.yyide.chatim.databinding.DialogTodoLeaveRefuseBinding
import com.yyide.chatim.databinding.FragmentTodoMsgPageBinding
import com.yyide.chatim.fragment.viewmodel.TodoViewModel
import com.yyide.chatim.model.EventMessage
import com.yyide.chatim.model.TodoRsp
import com.yyide.chatim.utils.YDToastUtil
import com.yyide.chatim.utils.loge
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class TodoMsgPageFragment :
    KTBaseFragment<FragmentTodoMsgPageBinding>(FragmentTodoMsgPageBinding::inflate),
    SwipeRefreshLayout.OnRefreshListener {

    private var adapter: TodoAdapter? = null
    private var mParam1 = 0//待办状态 0全部 1待办 2已办
    private var pageNum = 1
    private val pageSize = 15
    private var dialog: Dialog? = null

    private val viewModel: TodoViewModel by viewModels()

    companion object {
        private const val TAG = "TodoMsgFragment"
        private const val ARG_PARAM1 = "param1"
        fun newInstance(param1: Int): TodoMsgPageFragment {
            val fragment = TodoMsgPageFragment()
            val args = Bundle()
            args.putInt(ARG_PARAM1, param1)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = requireArguments().getInt(ARG_PARAM1, -1)
            Log.e(TAG, "mParam1:$mParam1")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onStart() {
        super.onStart()
        if (!EventBus.getDefault().isRegistered(this)) { //加上判断
            EventBus.getDefault().register(this)
        }
    }

    override fun onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) //加上判断
            EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    private fun initView() {
        binding.recyclerview.layoutManager = LinearLayoutManager(activity)
        adapter = TodoAdapter()
        binding.recyclerview.adapter = adapter
        adapter!!.setEmptyView(R.layout.empty)
        binding.swipeRefreshLayout.setColorSchemeColors(requireActivity().resources.getColor(R.color.colorPrimary))
        binding.swipeRefreshLayout.setOnRefreshListener(this)
        binding.swipeRefreshLayout.isRefreshing = false
        adapter!!.loadMoreModule.setOnLoadMoreListener {
            //上拉加载时取消下拉刷新
            adapter!!.loadMoreModule.isEnableLoadMore = true
            //请求数据
            pageNum++
            viewModel.getTodoList(pageNum, pageSize, mParam1.toString())
        }
        adapter!!.setOnItemClickListener { adapter: BaseQuickAdapter<*, *>, view: View?, position: Int ->
            val item = adapter.getItem(position) as TodoRsp.TodoItemBean
            val intent = Intent(context, LeaveFlowDetailActivity::class.java)
            intent.putExtra("type", 2)
            intent.putExtra("id", item.id)
            startActivity(intent)
        }
        adapter!!.loadMoreModule.isAutoLoadMore = true
        //当自动加载开启，同时数据不满一屏时，是否继续执行自动加载更多(默认为true)
        adapter!!.loadMoreModule.isEnableLoadMoreIfNotFullPage = false
        resultData()
        onRefresh()

        adapter!!.setLeaveCallBack(object : TodoAdapter.LeaveCallBack {
            override fun pass(id: String) {
                loading()
                viewModel.leaveRefuseOrPass(id, "2", "")
            }

            override fun reFuse(id: String) {
                dialog = Dialog(requireContext())
                dialog!!.setCancelable(false)
                dialog!!.setCanceledOnTouchOutside(true)
                val vb = DialogTodoLeaveRefuseBinding.inflate(layoutInflater)
                dialog!!.setContentView(vb.root)
                vb.tvCancel.setOnClickListener {
                    dialog!!.dismiss()
                }
                vb.tvConfirm.setOnClickListener {
                    val reason = vb.etInput.text.toString().trim()
                    if (TextUtils.isEmpty(reason)) {
                        YDToastUtil.showMessage("请输入拒绝理由")
                    } else {
                        loading()
                        dialog!!.dismiss()
                        viewModel.leaveRefuseOrPass(id, "0", reason)
                    }
                }
                dialog!!.show()
                dialog!!.window?.setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
                )
            }

        })
    }

    /**
     * 待办列表
     */
    private fun resultData() {
        viewModel.todoLiveData.observe(requireActivity(), Observer {
            val result = it.getOrNull()
            binding.swipeRefreshLayout.isRefreshing = false
            if (it.isSuccess) {
                val dataList = result?.list
                if (dataList != null) {
                    if (result.pageNo == 1) {
                        adapter!!.setList(dataList)
                    } else {
                        adapter!!.addData(dataList)
                    }
                    if (dataList.size < pageSize) {
                        //如果不够一页,显示没有更多数据布局
                        adapter!!.loadMoreModule.loadMoreEnd()
                    } else {
                        adapter!!.loadMoreModule.loadMoreComplete()
                    }
                }
            } else {
                it.exceptionOrNull()?.localizedMessage?.let { it1 ->
                    loge(it1)
                    ToastUtils.showLong(it1)
                }
            }
        })

        viewModel.leaveRefuseOrPassLiveData.observe(requireActivity(), Observer {
            dismiss()
            if (it.isSuccess) {
                if (dialog != null && dialog!!.isShowing) {
                    dialog!!.dismiss()
                }
                YDToastUtil.showMessage("审核完成")
                onRefresh()
            } else {
                it.exceptionOrNull()?.localizedMessage?.let { it1 ->
                    loge(it1)
                    ToastUtils.showLong(it1)
                }
            }
        })

    }

    override fun onRefresh() {
        pageNum = 1
        viewModel.getTodoList(pageNum, pageSize, mParam1.toString())
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun Event(messageEvent: EventMessage) {
        if (BaseConstant.TYPE_LEAVE == messageEvent.code || BaseConstant.TYPE_UPDATE_HOME == messageEvent.code || BaseConstant.TYPE_SELECT_MESSAGE_TODO == messageEvent.code) {
            onRefresh()
        }
    }

}