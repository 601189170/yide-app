package com.yyide.chatim.activity.newnotice.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.activity.newnotice.NoticeDetailActivity
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.base.BaseMvpFragment
import com.yyide.chatim.databinding.FragmentNoticePersonnelListBinding
import com.yyide.chatim.databinding.ItemNoticePersonnelBinding
import com.yyide.chatim.model.NoticeItemBean
import com.yyide.chatim.model.ResultBean
import com.yyide.chatim.presenter.NoticeReceivedPresenter
import com.yyide.chatim.view.NoticeReceivedView
import java.util.*

/**
 * 通知公告部门（选择）
 * auther lrz
 * time  2021年6月17日11:00:27
 */
class NoticePersonnelFragment : BaseMvpFragment<NoticeReceivedPresenter?>(), NoticeReceivedView {
    private val TAG = NoticePersonnelFragment::class.java.simpleName
    private var viewBinding: FragmentNoticePersonnelListBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewBinding = FragmentNoticePersonnelListBinding.inflate(layoutInflater)
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
        viewBinding!!.list.layoutManager = LinearLayoutManager(activity)
        viewBinding!!.list.adapter = adapter
        adapter.setEmptyView(R.layout.empty)
        adapter.emptyLayout!!.setOnClickListener { v: View? ->
            //点击空数据界面刷新当前页数据
            ToastUtils.showShort("getEmptyLayout To Data")
        }
        adapter.setOnItemClickListener { _: BaseQuickAdapter<*, *>?, _: View?, _: Int -> startActivity(Intent(activity, NoticeDetailActivity::class.java)) }
        val list: MutableList<String> = ArrayList()
        for (i in 0..9) {
            list.add("")
        }
        adapter.setList(list)

        viewBinding!!.switchPush.setOnCheckedChangeListener { compoundButton: CompoundButton, isCheck: Boolean ->
            if (isCheck) {

            } else {

            }
        }

        viewBinding!!.btnConfirm.setOnClickListener {
            confirm()
        }
    }

    private fun confirm(){

    }

    private val adapter: BaseQuickAdapter<String, BaseViewHolder> = object : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_notice_personnel) {
        override fun convert(holder: BaseViewHolder, s: String) {
            val view = ItemNoticePersonnelBinding.bind(holder.itemView)
            view.cbCheck.text = s
            view.cbCheck.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean -> }
            if (this.itemCount > holder.adapterPosition - 1) {
                view.viewLine.visibility = View.VISIBLE
            } else {
                view.viewLine.visibility = View.INVISIBLE
            }
        }
    }

    override fun getMyReceivedList(model: NoticeItemBean?) {
        if (model != null) {
            if (model.code == BaseConstant.REQUEST_SUCCES2) {

            }
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
        fun newInstance(): NoticePersonnelFragment {
            val fragment = NoticePersonnelFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}