package com.yyide.chatim_pro.activity.newnotice.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim_pro.R
import com.yyide.chatim_pro.base.BaseConstant
import com.yyide.chatim_pro.base.BaseMvpFragment
import com.yyide.chatim_pro.databinding.FragmentNoticeUnreadPersonnelListBinding
import com.yyide.chatim_pro.databinding.ItemNoticeUnreadPersonnelBinding
import com.yyide.chatim_pro.model.EventMessage
import com.yyide.chatim_pro.model.NoticeUnreadPeopleBean
import com.yyide.chatim_pro.presenter.NoticeUnreadPeoplePresenter
import com.yyide.chatim_pro.view.NoticeUnreadPeopleView
import org.greenrobot.eventbus.EventBus

/**
 * 我发布的通知 未读/已读 人员列表
 * auther lrz
 * time  2021年6月17日11:00:27
 */
class NoticeUnreadPersonnelFragment : BaseMvpFragment<NoticeUnreadPeoplePresenter?>(), NoticeUnreadPeopleView {
    private val TAG = NoticeUnreadPersonnelFragment::class.java.simpleName
    private var viewBinding: FragmentNoticeUnreadPersonnelListBinding? = null
    private var type: Int = 0
    private var id: Long = 0
    private var pageNum: Int = 1
    private var pageSize: Int = 20
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        viewBinding = FragmentNoticeUnreadPersonnelListBinding.inflate(layoutInflater)
        return viewBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        if (bundle != null) {
            type = bundle.getInt("type")
            id = bundle.getLong("id")
        }
        initView()
    }

    override fun createPresenter(): NoticeUnreadPeoplePresenter {
        return NoticeUnreadPeoplePresenter(this)
    }

    private fun initView() {
        //默认选中第一个
        viewBinding!!.list.layoutManager = LinearLayoutManager(activity)
        viewBinding!!.list.adapter = adapter
        adapter.setEmptyView(R.layout.empty)
        adapter.emptyLayout!!.setOnClickListener {
            //点击空数据界面刷新当前页数据
            mvpPresenter?.getNoticeUnreadList(id, type, pageNum, pageSize)
        }
        adapter.loadMoreModule.setOnLoadMoreListener {

            //上拉加载时取消下拉刷新
//            mSwipeRefreshLayout.setRefreshing(false);
            adapter.loadMoreModule.isEnableLoadMore = true
            //请求数据
            pageNum++
            mvpPresenter?.getNoticeUnreadList(id, type, pageNum, pageSize)
        }
        adapter.loadMoreModule.isAutoLoadMore = true
        //当自动加载开启，同时数据不满一屏时，是否继续执行自动加载更多(默认为true)
        adapter.loadMoreModule.isEnableLoadMoreIfNotFullPage = false
        //adapter.setOnItemClickListener { _: BaseQuickAdapter<*, *>?, _: View?, _: Int -> startActivity(Intent(activity, NoticeDetailActivity::class.java)) }
        mvpPresenter?.getNoticeUnreadList(id, type, pageNum, pageSize)

    }

    private val adapter: BaseQuickAdapter<NoticeUnreadPeopleBean.DataBean.RecordsBean, BaseViewHolder> = object : BaseQuickAdapter<NoticeUnreadPeopleBean.DataBean.RecordsBean, BaseViewHolder>(R.layout.item_notice_unread_personnel), LoadMoreModule {
        override fun convert(holder: BaseViewHolder, item: NoticeUnreadPeopleBean.DataBean.RecordsBean) {
            val view = ItemNoticeUnreadPersonnelBinding.bind(holder.itemView)
            view.tvName.text = item.name
            if (this.itemCount > holder.adapterPosition - 1) {
                view.viewLine.visibility = View.VISIBLE
            } else {
                view.viewLine.visibility = View.INVISIBLE
            }
        }
    }

    override fun getUnreadPeopleList(model: NoticeUnreadPeopleBean?) {
        if (model != null) {
            if (model.code == BaseConstant.REQUEST_SUCCESS && model.data != null) {
                if (pageNum == 1) {
                    if (model.data != null) {
                        EventBus.getDefault().post(EventMessage(BaseConstant.TYPE_NOTICE_UN_CONFIRM_NUMBER, "", model.data.total))
                        adapter.setList(model.data.records)
                    }
                } else {
                    if (model.data != null) {
                        adapter.addData(model.data.records)
                    }
                }
                if (model.data != null && model.data.records != null) {
                    if (model.data.records.size < pageSize) {
                        //如果不够一页,显示没有更多数据布局
                        adapter.loadMoreModule.loadMoreEnd()
                    } else {
                        adapter.loadMoreModule.loadMoreComplete()
                    }
                }
            }
        }
    }

    override fun getUnreadPeopleFail(msg: String?) {
        Log.d(TAG, "getMyReceivedFail>>>>>：$msg")
    }

    override fun onDestroy() {
        super.onDestroy()
        viewBinding = null
    }

    companion object {
        fun newInstance(id: Long, type: Int): NoticeUnreadPersonnelFragment {
            val fragment = NoticeUnreadPersonnelFragment()
            val args = Bundle()
            args.putLong("id", id)
            args.putInt("type", type)
            fragment.arguments = args
            return fragment
        }
    }

}