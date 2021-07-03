package com.yyide.chatim.activity.newnotice.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.yyide.chatim.R
import com.yyide.chatim.activity.newnotice.NoticeConfirmDetailActivity
import com.yyide.chatim.activity.newnotice.fragment.adaoter.NoticeMyReceivedAdapter
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.base.BaseMvpFragment
import com.yyide.chatim.databinding.FragmentNoticeMyReceviedListBinding
import com.yyide.chatim.model.NoticeItemBean
import com.yyide.chatim.presenter.NoticeReceivedPresenter
import com.yyide.chatim.utils.DateUtils
import com.yyide.chatim.view.NoticeReceivedView
import com.yyide.chatim.widget.itemDocoretion.ItemDecorationPowerful

/**
 * 我收到的通知列表
 * auther lrz
 * time  2021年6月17日11:00:27
 */
class NoticeMyReceivedFragment : BaseMvpFragment<NoticeReceivedPresenter?>(), NoticeReceivedView, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private val TAG = NoticeMyReceivedFragment::class.java.simpleName
    private var viewBinding: FragmentNoticeMyReceviedListBinding? = null
    private var pageNum = 1
    private var pageSize = 20
    private val receivedAdapter = NoticeMyReceivedAdapter(R.layout.item_notice_my_recevied)
    private var startData = DateUtils.getDayBegin()
    private var endData = DateUtils.getDayEndDate(System.currentTimeMillis())

    companion object {
        fun newInstance(): NoticeMyReceivedFragment {
            val fragment = NoticeMyReceivedFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewBinding = FragmentNoticeMyReceviedListBinding.inflate(layoutInflater)
        return viewBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        mvpPresenter?.getReceiverNoticeList(startData, endData, pageNum, pageSize);
    }

    override fun createPresenter(): NoticeReceivedPresenter {
        return NoticeReceivedPresenter(this)
    }

    override fun onRefresh() {
        pageNum = 1
        startData = DateUtils.getDayBegin()
        endData = DateUtils.getDayEndDate(System.currentTimeMillis())
        mvpPresenter?.getReceiverNoticeList(startData, endData, pageNum, pageSize);
    }

    private fun initView() {
        //默认选中第一个
        viewBinding!!.tvToday.isChecked = true
        viewBinding!!.tvToday.setTextColor(Color.WHITE)
        viewBinding!!.tvToday.setOnClickListener(this)
        viewBinding!!.tvThisWeek.setOnClickListener(this)
        viewBinding!!.tvThisMonth.setOnClickListener(this)

        viewBinding!!.swipeRefreshLayout.isRefreshing = false
        viewBinding!!.swipeRefreshLayout.setOnRefreshListener(this)
        context?.resources?.getColor(R.color.colorAccent)?.let { viewBinding!!.swipeRefreshLayout.setColorSchemeColors(it) }

        viewBinding!!.list.layoutManager = GridLayoutManager(activity, 2)
        viewBinding!!.list.addItemDecoration(ItemDecorationPowerful(ItemDecorationPowerful.GRID_DIV, Color.TRANSPARENT, SizeUtils.dp2px(10f)))
        viewBinding!!.list.adapter = receivedAdapter
        receivedAdapter.setEmptyView(R.layout.empty)
        receivedAdapter.emptyLayout!!.setOnClickListener {
            //点击空数据界面刷新当前页数据
            //ToastUtils.showShort("getEmptyLayout To Data")
        }
        receivedAdapter.setOnItemClickListener { adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int ->
            var intent = Intent(context, NoticeConfirmDetailActivity::class.java)
            intent.putExtra("id", receivedAdapter.getItem(position).id)
            startActivity(intent)
        }
        receivedAdapter.loadMoreModule.setOnLoadMoreListener {

            //上拉加载时取消下拉刷新
//            mSwipeRefreshLayout.setRefreshing(false);
            receivedAdapter.loadMoreModule.isEnableLoadMore = true
            //请求数据
            pageNum++
            mvpPresenter?.getReceiverNoticeList(startData, endData, pageNum, pageSize);
        }
        receivedAdapter.loadMoreModule.isAutoLoadMore = true
        //当自动加载开启，同时数据不满一屏时，是否继续执行自动加载更多(默认为true)
        receivedAdapter.loadMoreModule.isEnableLoadMoreIfNotFullPage = false
    }

    override fun onClick(v: View) {
        viewBinding!!.tvToday.isChecked = false
        viewBinding!!.tvThisWeek.isChecked = false
        viewBinding!!.tvThisMonth.isChecked = false
        viewBinding!!.tvToday.setTextColor(context!!.resources.getColor(R.color.text_1E1E1E))
        viewBinding!!.tvThisWeek.setTextColor(context!!.resources.getColor(R.color.text_1E1E1E))
        viewBinding!!.tvThisMonth.setTextColor(context!!.resources.getColor(R.color.text_1E1E1E))
        when (v.id) {
            viewBinding!!.tvToday.id -> {
                viewBinding!!.tvToday.isChecked = true
                viewBinding!!.tvToday.setTextColor(Color.WHITE)
                startData = DateUtils.getDayBegin()
                endData = DateUtils.getDayEndDate(System.currentTimeMillis())
                Log.d(TAG, "Day startDate:.>>> :$startData\t endDate>>> $endData")
            }
            viewBinding!!.tvThisWeek.id -> {
                viewBinding!!.tvThisWeek.isChecked = true
                viewBinding!!.tvThisWeek.setTextColor(Color.WHITE)
                startData = DateUtils.getDate(DateUtils.getBeginDayOfWeek().time)
                endData = DateUtils.getDate(System.currentTimeMillis())
                Log.d(TAG, "Week startDate:.>>> :$startData\t endDate>>> $endData")
            }
            viewBinding!!.tvThisMonth.id -> {
                viewBinding!!.tvThisMonth.isChecked = true
                viewBinding!!.tvThisMonth.setTextColor(Color.WHITE)
                startData = DateUtils.getDate(DateUtils.getBeginDayOfMonth().time)
                endData = DateUtils.getDate(System.currentTimeMillis())
                Log.d(TAG, "Month startDate:.>>> :$startData\t endDate>>> $endData")
            }
        }
        pageNum = 1
        mvpPresenter?.getReceiverNoticeList(startData, endData, pageNum, pageSize);
    }

    override fun getMyReceivedList(model: NoticeItemBean?) {
        viewBinding!!.swipeRefreshLayout.isRefreshing = false
        if (model?.code == BaseConstant.REQUEST_SUCCES2) {
            if (pageNum == 1) {
                if (model?.data != null) {
                    receivedAdapter.setList(model.data.records)
                }
            } else {
                if (model?.data != null) {
                    receivedAdapter.addData(model.data.records)
                }
            }
            if (model.data != null && model.data.records != null) {
                if (model.data.records.size < pageSize) {
                    //如果不够一页,显示没有更多数据布局
                    //receivedAdapter.loadMoreModule.loadMoreEnd()
                } else {
                    receivedAdapter.loadMoreModule.loadMoreComplete()
                }
            } else {
                receivedAdapter.loadMoreModule.loadMoreComplete()
            }
        }
    }

    override fun getMyReceivedFail(msg: String) {
        viewBinding!!.swipeRefreshLayout.isRefreshing = false
        Log.d(TAG, "getMyReceivedFail>>>>>：$msg")
    }

    override fun onDestroy() {
        super.onDestroy()
        viewBinding = null
    }

}