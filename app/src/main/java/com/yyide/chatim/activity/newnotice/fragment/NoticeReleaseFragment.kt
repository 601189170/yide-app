package com.yyide.chatim.activity.newnotice.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.activity.newnotice.NoticeTemplateDetailActivity
import com.yyide.chatim.databinding.FragmentNoticePushBinding
import com.yyide.chatim.widget.itemDocoretion.ItemDecorationPowerful
import java.util.*

/**
 * A fragment representing a list of Items.
 */
class NoticeReleaseFragment : Fragment() {
    private var pushBinding: FragmentNoticePushBinding? = null
    private var pageNum = 1
    private var checkPosition = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        pushBinding = FragmentNoticePushBinding.inflate(layoutInflater)
        return pushBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    var listTabs: MutableList<String> = ArrayList()
    private fun initView() {
        listTabs.add("推荐1")
        listTabs.add("推荐2")
        listTabs.add("推荐3")
        listTabs.add("推荐4")
        listTabs.add("推荐5")
        listTabs.add("推荐6")
        listTabs.add("推荐7")
        listTabs.add("推荐8")
        listTabs.add("推荐9")
        //导航TAB 列表 模板发布
        pushBinding!!.recyclerview.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        pushBinding!!.recyclerview.addItemDecoration(ItemDecorationPowerful(ItemDecorationPowerful.HORIZONTAL_DIV, Color.TRANSPARENT, SizeUtils.dp2px(5f)))
        pushBinding!!.recyclerview.adapter = adapter
        adapter.setList(listTabs)
        adapter.setOnItemClickListener { adapter: BaseQuickAdapter<*, *>, _: View?, position: Int ->
            checkPosition = position
            adapter.notifyDataSetChanged()
            pushBinding!!.recyclerview.smoothScrollToPosition(position)
        }
        //通知模板数据列表
        val mAdapter = NoticeReleaseAdapter(R.layout.item_notice_push)
        pushBinding!!.list.layoutManager = GridLayoutManager(activity, 2)
        pushBinding!!.list.addItemDecoration(ItemDecorationPowerful(ItemDecorationPowerful.GRID_DIV, Color.TRANSPARENT, SizeUtils.dp2px(12f)))
        pushBinding!!.list.adapter = mAdapter
        mAdapter.setList(listTabs)
        mAdapter.setEmptyView(R.layout.empty)
        mAdapter.emptyLayout!!.setOnClickListener { v: View? ->
            //点击空数据界面刷新当前页数据
            ToastUtils.showShort("getEmptyLayout To Data")
        }
        mAdapter.setOnItemClickListener { adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int -> startActivity(Intent(context, NoticeTemplateDetailActivity::class.java)) }
        mAdapter.loadMoreModule.setOnLoadMoreListener {

            //上拉加载时取消下拉刷新
//            mSwipeRefreshLayout.setRefreshing(false);
            mAdapter.loadMoreModule.isEnableLoadMore = true
            //请求数据
            pageNum++
        }
        mAdapter.loadMoreModule.isAutoLoadMore = true
        //当自动加载开启，同时数据不满一屏时，是否继续执行自动加载更多(默认为true)
        mAdapter.loadMoreModule.isEnableLoadMoreIfNotFullPage = false

        //数据加载完之后
//        if (pageNum == 1) {
//            if (model != null && model.getData() != null) {
//                mAdapter.setList(model.getData().getRecords());
//            }
//        } else {
//            if (model != null && model.getData() != null) {
//                mAdapter.addData(model.getData().getRecords());
//            }
//        }
//        if (model.getData() != null && model.getData().getRecords() != null) {
//            if (model.getData().getRecords().size() < pageSize) {
//                //如果不够一页,显示没有更多数据布局
//                mAdapter.getLoadMoreModule().loadMoreEnd();
//            } else {
//                mAdapter.getLoadMoreModule().loadMoreComplete();
//            }
//        }
    }

    private val adapter: BaseQuickAdapter<String, BaseViewHolder> = object : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_notice_textview) {
        override fun convert(holder: BaseViewHolder, item: String) {
            val checkedTextView = holder.getView<CheckedTextView>(R.id.tv_checked_mark)
            checkedTextView.text = item
            if (checkPosition == holder.adapterPosition) {
                checkedTextView.isChecked = true
                checkedTextView.setTextColor(Color.WHITE)
            } else {
                checkedTextView.setTextColor(context.resources.getColor(R.color.text_1E1E1E))
                checkedTextView.isChecked = false
            }
        }
    }

    companion object {
        fun newInstance(): NoticeReleaseFragment {
            val fragment = NoticeReleaseFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}