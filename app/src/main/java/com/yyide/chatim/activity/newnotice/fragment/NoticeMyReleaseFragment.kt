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
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.activity.newnotice.NoticeDetailActivity
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.base.BaseMvpFragment
import com.yyide.chatim.databinding.FragmentNoticeMyPushListBinding
import com.yyide.chatim.databinding.ItemNoticeMyPushBinding
import com.yyide.chatim.model.ResultBean
import com.yyide.chatim.presenter.NoticeReceivedPresenter
import com.yyide.chatim.view.NoticeReceivedView
import com.yyide.chatim.widget.itemDocoretion.ItemDecorationPowerful
import java.util.*

/**
 *
 * 我的发布通知列表
 * auther lrz
 * time  2021年6月17日11:00:27
 */
class NoticeMyReleaseFragment : BaseMvpFragment<NoticeReceivedPresenter?>(), NoticeReceivedView {
    private val TAG = NoticeMyReleaseFragment::class.java.simpleName
    private var viewBinding: FragmentNoticeMyPushListBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewBinding = FragmentNoticeMyPushListBinding.inflate(layoutInflater)
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
        viewBinding!!.list.layoutManager = GridLayoutManager(activity, 2)
        viewBinding!!.list.addItemDecoration(ItemDecorationPowerful(ItemDecorationPowerful.GRID_DIV, Color.TRANSPARENT, SizeUtils.dp2px(12f)))
        viewBinding!!.list.adapter = adapter
        adapter.setEmptyView(R.layout.empty)
        adapter.emptyLayout!!.setOnClickListener { v: View? ->
            //点击空数据界面刷新当前页数据
            ToastUtils.showShort("getEmptyLayout To Data")
        }
        adapter.setOnItemClickListener { adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int -> startActivity(Intent(activity, NoticeDetailActivity::class.java)) }
        val list: MutableList<String> = ArrayList()
        for (i in 0..9) {
            list.add("")
        }
        adapter.setList(list)
    }

    private val adapter: BaseQuickAdapter<String, BaseViewHolder> = object : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_notice_my_push) {

        override fun convert(holder: BaseViewHolder, item: String) {
            val view = ItemNoticeMyPushBinding.bind(holder.itemView)
            //            view.tvNoticeTitle.setText("");
//            view.tvNoticeTime.setText("");
            //view.ivNoticeImg
            //view.ivIconImg
        }
    }

    override fun getMyReceivedList(model: ResultBean) {
        if (model.code == BaseConstant.REQUEST_SUCCES2) {
        }
    }

    override fun getMyReceivedFail(msg: String) {
        Log.d(TAG, "getMyReceivedFail>>>>>：$msg")
    }

    override fun onDestroy() {
        super.onDestroy()
        viewBinding = null
    }

    companion object {
        fun newInstance(): NoticeMyReleaseFragment {
            val fragment = NoticeMyReleaseFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}