package com.yyide.chatim.activity.newnotice.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.yyide.chatim.R
import com.yyide.chatim.activity.newnotice.NoticeConfirmDetailActivity
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.base.BaseMvpFragment
import com.yyide.chatim.databinding.FragmentNoticeMyReceviedListBinding
import com.yyide.chatim.model.ResultBean
import com.yyide.chatim.presenter.NoticeReceivedPresenter
import com.yyide.chatim.view.NoticeReceivedView
import com.yyide.chatim.widget.itemDocoretion.ItemDecorationPowerful
import java.util.*

/**
 * 我收到的通知列表
 * auther lrz
 * time  2021年6月17日11:00:27
 */
class NoticeMyReceivedFragment : BaseMvpFragment<NoticeReceivedPresenter?>(), NoticeReceivedView, View.OnClickListener {
    private val TAG = NoticeMyReceivedFragment::class.java.simpleName
    private var viewBinding: FragmentNoticeMyReceviedListBinding? = null
    private var pageNum = 1

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
    }

    override fun createPresenter(): NoticeReceivedPresenter {
        return NoticeReceivedPresenter(this)
    }

    private fun initView() {
        //默认选中第一个
        viewBinding!!.tvToday.isChecked = true
        viewBinding!!.tvToday.setTextColor(Color.WHITE)
        viewBinding!!.tvToday.setOnClickListener(this)
        viewBinding!!.tvThisWeek.setOnClickListener(this)
        viewBinding!!.tvThisMonth.setOnClickListener(this)
        val receivedAdapter = NoticeMyReceivedAdapter(R.layout.item_notice_my_recevied)
        viewBinding!!.list.layoutManager = GridLayoutManager(activity, 2)
        viewBinding!!.list.addItemDecoration(ItemDecorationPowerful(ItemDecorationPowerful.GRID_DIV, Color.TRANSPARENT, SizeUtils.dp2px(12f)))
        viewBinding!!.list.adapter = receivedAdapter
        receivedAdapter.setEmptyView(R.layout.empty)
        receivedAdapter.emptyLayout!!.setOnClickListener {
            //点击空数据界面刷新当前页数据
            ToastUtils.showShort("getEmptyLayout To Data")
        }
        receivedAdapter.setOnItemClickListener { adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int ->
            var intent = Intent(context, NoticeConfirmDetailActivity::class.java)
            startActivity(intent)
        }
        receivedAdapter.loadMoreModule.setOnLoadMoreListener {

            //上拉加载时取消下拉刷新
//            mSwipeRefreshLayout.setRefreshing(false);
            receivedAdapter.loadMoreModule.isEnableLoadMore = true
            //请求数据
            pageNum++
        }
        receivedAdapter.loadMoreModule.isAutoLoadMore = true
        //当自动加载开启，同时数据不满一屏时，是否继续执行自动加载更多(默认为true)
        receivedAdapter.loadMoreModule.isEnableLoadMoreIfNotFullPage = false
        val list: MutableList<String> = ArrayList()
        for (i in 0..9) {
            list.add("")
        }
        receivedAdapter.setList(list)
    }

    override fun onClick(v: View) {
        viewBinding!!.tvToday.isChecked = false
        viewBinding!!.tvThisWeek.isChecked = false
        viewBinding!!.tvThisMonth.isChecked = false
        viewBinding!!.tvToday.setTextColor(context!!.resources.getColor(R.color.text_1E1E1E))
        viewBinding!!.tvThisWeek.setTextColor(context!!.resources.getColor(R.color.text_1E1E1E))
        viewBinding!!.tvThisMonth.setTextColor(context!!.resources.getColor(R.color.text_1E1E1E))
        if (v.id == viewBinding!!.tvToday.id) {
            viewBinding!!.tvToday.isChecked = true
            viewBinding!!.tvToday.setTextColor(Color.WHITE)
        } else if (v.id == viewBinding!!.tvThisWeek.id) {
            viewBinding!!.tvThisWeek.isChecked = true
            viewBinding!!.tvThisWeek.setTextColor(Color.WHITE)
        } else if (v.id == viewBinding!!.tvThisMonth.id) {
            viewBinding!!.tvThisMonth.isChecked = true
            viewBinding!!.tvThisMonth.setTextColor(Color.WHITE)
        }
    }

    override fun getMyReceivedList(model: ResultBean) {
        if (model.code == BaseConstant.REQUEST_SUCCES2) {
//            if (pageNum == 1) {
//                if (model != null && model.getData() != null) {
//                    adapter.setList(model.getData().getRecords());
//                }
//            } else {
//                if (model != null && model.getData() != null) {
//                    adapter.addData(model.getData().getRecords());
//                }
//            }
//            if (model.getData() != null && model.getData().getRecords() != null) {
//                if (model.getData().getRecords().size() < pageSize) {
//                    //如果不够一页,显示没有更多数据布局
//                    adapter.getLoadMoreModule().loadMoreEnd();
//                } else {
//                    adapter.getLoadMoreModule().loadMoreComplete();
//                }
//            }
        }
    }

    override fun getMyReceivedFail(msg: String) {
        Log.d(TAG, "getMyReceivedFail>>>>>：$msg")
    }

    override fun onDestroy() {
        super.onDestroy()
        viewBinding = null
    }

}