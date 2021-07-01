package com.yyide.chatim.activity.newnotice.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.activity.newnotice.fragment.adaoter.OnCheckBoxChangeListener
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.base.BaseMvpFragment
import com.yyide.chatim.databinding.FragmentNoticePersonnelListBinding
import com.yyide.chatim.databinding.ItemNoticePersonnelBinding
import com.yyide.chatim.model.EventMessage
import com.yyide.chatim.model.NoticePersonnelBean
import com.yyide.chatim.presenter.NoticeDesignatedPersonnelPresenter
import com.yyide.chatim.view.NoticeDesignatedPersonnelView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 通知公告部门（选择）
 * auther lrz
 * time  2021年6月17日11:00:27
 */
class NoticePersonnelFragment : BaseMvpFragment<NoticeDesignatedPersonnelPresenter?>(), NoticeDesignatedPersonnelView {
    private val TAG = NoticePersonnelFragment::class.java.simpleName
    private var viewBinding: FragmentNoticePersonnelListBinding? = null
    private var totalNumber: Int = 0
    private var list: ArrayList<NoticePersonnelBean.ListBean>? = null
    private val mAdapter: PersonnelAdapter = PersonnelAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        viewBinding = FragmentNoticePersonnelListBinding.inflate(layoutInflater)
        return viewBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        EventBus.getDefault().register(this)
        initView()
        if (arguments != null) {
            mvpPresenter?.specifieTypeList(arguments!!.getString("type"))
        }
    }

    override fun createPresenter(): NoticeDesignatedPersonnelPresenter {
        return NoticeDesignatedPersonnelPresenter(this)
    }

    private fun initView() {
        showNoticeScopeNumber(0)
        //默认选中第一个
        viewBinding!!.list.layoutManager = LinearLayoutManager(activity)
        viewBinding!!.list.adapter = mAdapter
        mAdapter.setEmptyView(R.layout.empty)
        mAdapter.emptyLayout!!.setOnClickListener { v: View? ->
            //点击空数据界面刷新当前页数据
            if (arguments != null) {
                mvpPresenter?.specifieTypeList(arguments!!.getString("type"))
            }
        }

        mAdapter.setOnCheckBoxChangeListener(object : OnCheckBoxChangeListener {
            override fun change() {
                mAdapter.notifyDataSetChanged()
            }
        })

        viewBinding!!.switchPush.setOnCheckedChangeListener { compoundButton: CompoundButton, isCheck: Boolean ->
            val eventMessage = EventMessage(BaseConstant.TYPE_NOTICE_IS_CONFIRM, "")
            eventMessage.isBoolean = isCheck
            EventBus.getDefault().post(eventMessage)
        }

        viewBinding!!.btnConfirm.setOnClickListener {
            //confirm()
        }
    }

    private fun showNoticeScopeNumber(total: Int) {
        viewBinding!!.cbSelectNumber.text = getString(R.string.notice_chosen_people_number, total)
        viewBinding!!.cbSelectNumber.setOnCheckedChangeListener { buttonView, isChecked ->
            list?.let { recursionChecked(it, isChecked) }
            if (isChecked) {
                showNoticeScopeNumber(totalNumber)
            } else {
                totalNumber = 0
                showNoticeScopeNumber(totalNumber);
            }
        }
        if (totalNumber != 0) {
            viewBinding!!.cbSelectNumber.isChecked = totalNumber == total
        } else {
            viewBinding!!.cbSelectNumber.isChecked = false
        }
    }

    inner class PersonnelAdapter : BaseQuickAdapter<NoticePersonnelBean.ListBean, BaseViewHolder>(R.layout.item_notice_personnel) {

        override fun convert(holder: BaseViewHolder, itemBean: NoticePersonnelBean.ListBean) {
            val view = ItemNoticePersonnelBinding.bind(holder.itemView)
            view.cbCheck.text = itemBean.name
            if (itemBean.list != null && itemBean.list.isNotEmpty()) {
                view.ivUnfold.visibility = View.VISIBLE
                view.ivUnfold.setImageDrawable(context.resources.getDrawable(R.drawable.icon_down))
                Log.e(TAG, "onBindViewHolder isUnfold: " + itemBean.unfold)
                if (itemBean.unfold) {
                    view.ivUnfold.setImageDrawable(context.resources.getDrawable(R.drawable.icon_up))
                    view.recyclerview.visibility = View.VISIBLE
                } else {
                    view.ivUnfold.setImageDrawable(context.resources.getDrawable(R.drawable.icon_down))
                    view.recyclerview.visibility = View.GONE
                }
                if (this.itemCount < holder.adapterPosition - 1) {
                    view.viewLine.visibility = View.VISIBLE
                } else {
                    view.viewLine.visibility = View.INVISIBLE
                }

                if (viewBinding!!.list.isComputingLayout) {
                    viewBinding!!.list.post {
                        view.cbCheck.isChecked = itemBean.unfold;
                        notifyItemChanged(holder.adapterPosition);
                    };
                }
                
                view.recyclerview.layoutManager = LinearLayoutManager(context)
                val adapter = PersonnelAdapter()
                view.recyclerview.adapter = adapter
                adapter.setList(itemBean.list)
                adapter.setOnCheckBoxChangeListener(object : OnCheckBoxChangeListener {
                    override fun change() {
                        if (onCheckBoxChangeListener != null) {
                            onCheckBoxChangeListener?.change()
                        }
                    }
                })
                view.root.setOnClickListener { v: View? ->
                    Log.e(TAG, "onBindViewHolder: onclick is unfold:" + view.ivUnfold)
                    itemBean.unfold = !itemBean.unfold
                    if (onCheckBoxChangeListener != null) {
                        onCheckBoxChangeListener?.change()
                    }
                }
            }

            //选中层级
            view.cbCheck.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean ->
                recursionChecked(itemBean.list, isChecked)
                if (onCheckBoxChangeListener != null) {
                    onCheckBoxChangeListener!!.change()
                }
                itemBean.unfold = isChecked
            }
        }

        //private boolean unfold = false;
        fun setOnCheckBoxChangeListener(onCheckBoxChangeListener: OnCheckBoxChangeListener) {
            this.onCheckBoxChangeListener = onCheckBoxChangeListener
        }

        private var onCheckBoxChangeListener: OnCheckBoxChangeListener? = null
    }

    /**
     * 递归实现下级是否选中
     *
     * @param noticeScopeBean
     */
    private fun recursionChecked(noticeScopeBean: ArrayList<NoticePersonnelBean.ListBean>, isChecked: Boolean): Int {
        if (noticeScopeBean != null) {
            for (scopeBean in noticeScopeBean) {
                scopeBean.unfold = isChecked
                recursionChecked(scopeBean.list, isChecked)
                totalNumber++;
            }
        }
        return totalNumber;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun event(messageEvent: EventMessage) {
        if (BaseConstant.TYPE_NOTICE_IS_CONFIRM == messageEvent.code) {
            viewBinding!!.switchPush.isChecked = messageEvent.isBoolean
        }
    }

    override fun getDesignatedPersonnelList(model: NoticePersonnelBean?) {
        if (model != null) {
            if (model.code == BaseConstant.REQUEST_SUCCES2) {
                list = model.data
                mAdapter.setList(model.data)
            }
        }
    }

    override fun getDesignatedPersonnelFail(msg: String?) {
        Log.d(TAG, "getMyReceivedFail>>>>>：$msg")
    }

    override fun onDestroy() {
        super.onDestroy()
        viewBinding = null
        EventBus.getDefault().unregister(this)
    }

    companion object {
        fun newInstance(type: String): NoticePersonnelFragment {
            val fragment = NoticePersonnelFragment()
            val args = Bundle()
            args.putString("type", type)
            fragment.arguments = args
            return fragment
        }
    }

}