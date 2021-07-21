package com.yyide.chatim.activity.newnotice.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.activity.newnotice.NoticeDetailActivity
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.base.BaseMvpFragment
import com.yyide.chatim.databinding.EmptyBinding
import com.yyide.chatim.databinding.FragmentNoticeMyPushListBinding
import com.yyide.chatim.databinding.ItemNoticeMyPushBinding
import com.yyide.chatim.model.EventMessage
import com.yyide.chatim.model.NoticeItemBean
import com.yyide.chatim.presenter.NoticeMyReleasePresenter
import com.yyide.chatim.utils.DateUtils
import com.yyide.chatim.utils.GlideUtil
import com.yyide.chatim.view.NoticeMyReleasedView
import com.yyide.chatim.widget.itemDocoretion.ItemDecorationPowerful
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 *
 * 我的发布通知列表
 * auther lrz
 * time  2021年6月17日11:00:27
 */
class NoticeMyReleaseFragment : BaseMvpFragment<NoticeMyReleasePresenter?>(), NoticeMyReleasedView, SwipeRefreshLayout.OnRefreshListener {
    private val TAG = NoticeMyReleaseFragment::class.java.simpleName
    private var viewBinding: FragmentNoticeMyPushListBinding? = null
    private var pageNum = 1
    private var pageSize = 20
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        viewBinding = FragmentNoticeMyPushListBinding.inflate(layoutInflater)
        EventBus.getDefault().register(this)
        return viewBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun createPresenter(): NoticeMyReleasePresenter {
        return NoticeMyReleasePresenter(this)
    }

    private fun initView() {
        //默认选中第一个
        viewBinding!!.list.layoutManager = GridLayoutManager(activity, 2)
        viewBinding!!.list.addItemDecoration(ItemDecorationPowerful(ItemDecorationPowerful.GRID_DIV, Color.TRANSPARENT, SizeUtils.dp2px(6f)))
        viewBinding!!.list.adapter = mAdapter
        val emptyBinding = EmptyBinding.inflate(layoutInflater)
        mAdapter.setEmptyView(emptyBinding.root)
        emptyBinding.tvDesc.text = "还没有发布任何通知"
        mAdapter.emptyLayout!!.setOnClickListener {
            //点击空数据界面刷新当前页数据
            pageNum = 1
            mvpPresenter?.getMyNoticeList(pageNum, pageSize)

        }
        mAdapter.setOnItemClickListener { _: BaseQuickAdapter<*, *>?, _: View?, position: Int ->
            val intent = Intent(activity, NoticeDetailActivity::class.java)
            intent.putExtra("id", mAdapter.getItem(position).id);
            startActivity(intent)
        }
        viewBinding!!.swipeRefreshLayout.isRefreshing = false
        viewBinding!!.swipeRefreshLayout.setOnRefreshListener(this)
        context?.resources?.getColor(R.color.colorAccent)?.let { viewBinding!!.swipeRefreshLayout.setColorSchemeColors(it) }
        mAdapter.loadMoreModule.setOnLoadMoreListener {

            //上拉加载时取消下拉刷新
//            mSwipeRefreshLayout.setRefreshing(false);
            mAdapter.loadMoreModule.isEnableLoadMore = true
            //请求数据
            pageNum++
            mvpPresenter?.getMyNoticeList(pageNum, pageSize)
        }
        mAdapter.loadMoreModule.isAutoLoadMore = true
        //当自动加载开启，同时数据不满一屏时，是否继续执行自动加载更多(默认为true)
        mAdapter.loadMoreModule.isEnableLoadMoreIfNotFullPage = false
        mvpPresenter?.getMyNoticeList(pageNum, pageSize)
    }

    private val mAdapter = object : BaseQuickAdapter<NoticeItemBean.DataBean.RecordsBean, BaseViewHolder>(R.layout.item_notice_my_push), LoadMoreModule {

        @RequiresApi(Build.VERSION_CODES.N)
        override fun convert(holder: BaseViewHolder, item: NoticeItemBean.DataBean.RecordsBean) {
            val view = ItemNoticeMyPushBinding.bind(holder.itemView)
            view.tvNoticeTitle.text = item.title
            if (item.type == 0) {//空白模板文本
                view.clTemplate.visibility = View.INVISIBLE
                view.clBlank.visibility = View.VISIBLE
                view.tvTitle.text = item.title
                view.tvContent.text = item.content
            } else {
                view.clBlank.visibility = View.GONE
                view.clTemplate.visibility = View.VISIBLE
                GlideUtil.loadImageRadius(context, item.imgpath, view.ivNoticeImg, 2, true)
            }

            if (item.isRetract) {//撤回
                view.tvNoticeConfirm.text = getString(R.string.notice_has_withdrawn)
                view.tvNoticeConfirm.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, R.mipmap.icon_notice_withdraw), null, null, null)
            } else {
                var time: Long = 0
                if (!TextUtils.isEmpty(item.timerDate)) {
                    time = DateUtils.parseTimestamp(item.timerDate, "yyyy-MM-dd HH:mm:ss")
                }
                val typeString = if (item.isConfirm) getString(R.string.notice_confirm_count) else getString(R.string.notice_have_read)
                val html = typeString + "：<b><font color=\"#2C8AFF\">" + item.confirmOrReadNum + "</font><font color=\"#999999\">/" + item.totalNum + "</font></b>";
                if (item.totalNum == 0) {
                    view.tvNoticeConfirm.visibility = View.INVISIBLE
                } else {
                    view.tvNoticeConfirm.visibility = View.VISIBLE
                    if (item.isConfirm) {//已确认
                        view.tvNoticeConfirm.text = Html.fromHtml(html, Html.FROM_HTML_OPTION_USE_CSS_COLORS)
                        view.tvNoticeConfirm.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, R.mipmap.icon_notice_confirm), null, null, null)
                    } else {//已读
                        view.tvNoticeConfirm.text = Html.fromHtml(html, Html.FROM_HTML_OPTION_USE_CSS_COLORS)
                        view.tvNoticeConfirm.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, R.mipmap.icon_notice_read), null, null, null)
                    }
                }

                if (time > System.currentTimeMillis()) {//必须大于当前时间才处理
                    if (item.isTimer) {//定时发布
                        val times = DateUtils.dateDiff(item.timerDate)
                        var hours = times[1]
                        if (hours <= 0) {
                            view.tvNoticeConfirm.text = getString(R.string.notice_time_release, times[2])
                        } else {
                            view.tvNoticeConfirm.text = getString(R.string.notice_time_hours_release, hours)
                        }
                        view.tvNoticeConfirm.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, R.mipmap.icon_notice_timer), null, null, null)
                    }
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun event(messageEvent: EventMessage) {
        if (BaseConstant.TYPE_UPDATE_NOTICE_MY_RELEASE == messageEvent.code) {
            pageNum = 1
            mvpPresenter?.getMyNoticeList(pageNum, pageSize)
        }
    }

    override fun getMyReceivedList(model: NoticeItemBean) {
        viewBinding!!.swipeRefreshLayout.isRefreshing = false
        if (model.code == BaseConstant.REQUEST_SUCCES2) {
            if (pageNum == 1) {
                if (model?.data != null) {
                    mAdapter.setList(model.data.records)
                }
            } else {
                if (model?.data != null) {
                    mAdapter.addData(model.data.records)
                }
            }
            mAdapter.loadMoreModule.loadMoreComplete()
//            if (model.data != null && model.data.records != null) {
//                if (model.data.records.size < pageSize) {
//                    //如果不够一页,显示没有更多数据布局
//                    //mAdapter.loadMoreModule.loadMoreEnd()
//                } else {
//                    mAdapter.loadMoreModule.loadMoreComplete()
//                }
//            } else {
//                mAdapter.loadMoreModule.loadMoreComplete()
//            }
        }
    }

    override fun getMyReceivedFail(msg: String) {
        viewBinding!!.swipeRefreshLayout.isRefreshing = false
        Log.d(TAG, "getMyReceivedFail>>>>>：$msg")
    }

    override fun onDestroy() {
        super.onDestroy()
        viewBinding = null
        EventBus.getDefault().unregister(this)
    }

    companion object {
        fun newInstance(): NoticeMyReleaseFragment {
            val fragment = NoticeMyReleaseFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onRefresh() {
        pageNum = 1
        mvpPresenter?.getMyNoticeList(pageNum, pageSize);
    }
}