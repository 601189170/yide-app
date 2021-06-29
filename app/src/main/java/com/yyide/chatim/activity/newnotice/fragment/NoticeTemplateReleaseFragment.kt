package com.yyide.chatim.activity.newnotice.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.activity.newnotice.NoticeReleaseActivity
import com.yyide.chatim.activity.newnotice.NoticeTemplateDetailActivity
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.base.BaseMvpFragment
import com.yyide.chatim.databinding.FragmentNoticePushBinding
import com.yyide.chatim.databinding.ItemNoticePushBinding
import com.yyide.chatim.model.NoticeReleaseTemplateBean
import com.yyide.chatim.presenter.NoticeReleaseTemplatePresenter
import com.yyide.chatim.utils.GlideUtil
import com.yyide.chatim.view.NoticeReleasedTemplateView
import com.yyide.chatim.widget.itemDocoretion.ItemDecorationPowerful

/**
 * DESC 发布吗模板列表
 * Time 2021年6月24日
 * Author lrz
 */
class NoticeTemplateReleaseFragment : BaseMvpFragment<NoticeReleaseTemplatePresenter>(), NoticeReleasedTemplateView, SwipeRefreshLayout.OnRefreshListener {
    private val TAG = NoticeTemplateReleaseFragment.javaClass.simpleName
    private var pushBinding: FragmentNoticePushBinding? = null
    private var pageNum = 1
    private var pageSize = 20
    private var checkPosition = 0
    private var messageTemplateTypeId: Long? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        pushBinding = FragmentNoticePushBinding.inflate(layoutInflater)
        return pushBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        mvpPresenter.templateNoticeClassifyList(1, 50)
    }

    override fun onRefresh() {
        // 这里的作用是防止下拉刷新的时候还可以上拉加载
        mAdapter.loadMoreModule.isEnableLoadMore = false
        pageNum = 1
        messageTemplateTypeId?.let { mvpPresenter.templateNoticeList(it, pageNum, pageSize) }
        mvpPresenter.templateNoticeClassifyList(1, 50)
    }

    private fun initView() {
        //刷新
        pushBinding?.swipeRefreshLayout?.isRefreshing = false
        pushBinding?.swipeRefreshLayout?.setOnRefreshListener(this)
        context?.resources?.getColor(R.color.colorAccent)?.let { pushBinding?.swipeRefreshLayout?.setColorSchemeColors(it) }
        //导航TAB 列表 模板发布
        pushBinding?.tabList?.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        pushBinding?.tabList?.addItemDecoration(ItemDecorationPowerful(ItemDecorationPowerful.HORIZONTAL_DIV, Color.TRANSPARENT, SizeUtils.dp2px(5f)))
        pushBinding?.tabList?.adapter = tabAdapter
        tabAdapter.setOnItemClickListener { adapter: BaseQuickAdapter<*, *>, _: View?, position: Int ->
            checkPosition = position
            adapter.notifyDataSetChanged()
            pushBinding!!.tabList.smoothScrollToPosition(position)
            messageTemplateTypeId = tabAdapter.getItem(position).id
            pageNum = 1
            mvpPresenter.templateNoticeList(tabAdapter.getItem(position).id, pageNum, pageSize)
        }
        //通知模板数据列表
        pushBinding?.list?.layoutManager = GridLayoutManager(activity, 2)
        pushBinding?.list?.addItemDecoration(ItemDecorationPowerful(ItemDecorationPowerful.GRID_DIV, Color.TRANSPARENT, SizeUtils.dp2px(12f)))
        pushBinding?.list?.adapter = mAdapter
        mAdapter.setEmptyView(R.layout.empty)
        mAdapter.emptyLayout!!.setOnClickListener {
            //点击空数据界面刷新当前页数据
            ToastUtils.showShort("getEmptyLayout To Data")
        }
        mAdapter.setOnItemClickListener { adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int ->
            val item: NoticeReleaseTemplateBean.DataBean.RecordsBean = mAdapter.getItem(position)
            if (item.type == 0) {
                startActivity(Intent(context, NoticeReleaseActivity::class.java))
            } else {
                startActivity(Intent(context, NoticeTemplateDetailActivity::class.java))
            }
        }
        mAdapter.loadMoreModule.setOnLoadMoreListener {
            //上拉加载时取消下拉刷新
//            mSwipeRefreshLayout.setRefreshing(false);
            mAdapter.loadMoreModule.isEnableLoadMore = true
            //请求数据
            pageNum++
            messageTemplateTypeId?.let { mvpPresenter.templateNoticeList(it, pageNum, pageSize) }
        }
        mAdapter.loadMoreModule.isAutoLoadMore = true
        //当自动加载开启，同时数据不满一屏时，是否继续执行自动加载更多(默认为true)
        mAdapter.loadMoreModule.isEnableLoadMoreIfNotFullPage = false
    }

    /**
     * 末班发布页面分类
     */
    private val tabAdapter = object : BaseQuickAdapter<NoticeReleaseTemplateBean.DataBean.RecordsBean, BaseViewHolder>(R.layout.item_notice_textview) {
        override fun convert(holder: BaseViewHolder, item: NoticeReleaseTemplateBean.DataBean.RecordsBean) {
            val checkedTextView = holder.getView<CheckedTextView>(R.id.tv_checked_mark)
            checkedTextView.text = item.name
            if (checkPosition == holder.adapterPosition) {
                checkedTextView.isChecked = true
                checkedTextView.setTextColor(Color.WHITE)
            } else {
                checkedTextView.setTextColor(context.resources.getColor(R.color.text_1E1E1E))
                checkedTextView.isChecked = false
            }
        }
    }

    private val mAdapter = object : BaseQuickAdapter<NoticeReleaseTemplateBean.DataBean.RecordsBean, BaseViewHolder>(R.layout.item_notice_push), LoadMoreModule {
        override fun convert(holder: BaseViewHolder, itemBean: NoticeReleaseTemplateBean.DataBean.RecordsBean) {
            val pushBinding = ItemNoticePushBinding.bind(holder.itemView)
            pushBinding.tvNoticeTitle.text = itemBean.name
            if (itemBean.type == 0) {
                pushBinding.ivIconImg.setBackgroundResource(R.mipmap.icon_notice_add)
            } else {//1非空白模板
                GlideUtil.loadImageRadius(context, itemBean.coverImg, pushBinding.ivIconImg, SizeUtils.dp2px(2f))
            }
        }
    }

    companion object {
        fun newInstance(): NoticeTemplateReleaseFragment {
            val fragment = NoticeTemplateReleaseFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun createPresenter(): NoticeReleaseTemplatePresenter {
        return NoticeReleaseTemplatePresenter(this)
    }

    override fun getNoticeReleasedList(model: NoticeReleaseTemplateBean?) {
        pushBinding?.swipeRefreshLayout?.isRefreshing = false
        if (model != null) {
            if (model.code == BaseConstant.REQUEST_SUCCES2) {
                //数据加载完之后
                if (pageNum == 1) {
                    if (model.data != null && model.data.records != null) {
                        mAdapter.setList(model.data.records)
                    }
                    addItem()
                } else {
                    if (model.data != null) {
                        mAdapter.addData(model.data.records)
                    }
                }
                if (model.data != null && model.data.records != null) {
                    if (model.data.records.size < pageSize) {
                        //如果不够一页,显示没有更多数据布局
                        mAdapter.loadMoreModule.loadMoreEnd()
                    } else {
                        mAdapter.loadMoreModule.loadMoreComplete()
                    }
                }
            }
        }
    }

    private fun addItem() {
        val item = NoticeReleaseTemplateBean.DataBean.RecordsBean()
        item.type = 0
        if (mAdapter.itemCount <= 0) {
            mAdapter.addData(item)
        } else {
            mAdapter.addData(0, item)
        }
    }

    override fun getNoticeReleasedClassifyList(model: NoticeReleaseTemplateBean?) {
        pushBinding?.swipeRefreshLayout?.isRefreshing = false
        if (model != null) {
            if (model.code == BaseConstant.REQUEST_SUCCES2) {
                tabAdapter.setList(model.data.records)
                if (model.data.records != null && model.data.records.size > 0) {
                    messageTemplateTypeId = model.data.records[0].id
                    pageNum = 1
                    pushBinding!!.list.smoothScrollToPosition(0)
                    mvpPresenter.templateNoticeList(messageTemplateTypeId!!, pageNum, pageSize)
                }
            }
        }
    }

    override fun getNoticeReleasedFail(msg: String?) {
        pushBinding?.swipeRefreshLayout?.isRefreshing = false
        Log.d(TAG, "getNoticeReleasedFail>>>>>：$msg")
    }

}