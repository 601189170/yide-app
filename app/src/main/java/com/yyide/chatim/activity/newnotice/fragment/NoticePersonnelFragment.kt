package com.yyide.chatim.activity.newnotice.fragment

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.fastjson.JSON
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.base.BaseMvpFragment
import com.yyide.chatim.databinding.FragmentNoticePersonnelListBinding
import com.yyide.chatim.databinding.ItemNoticePersonnelBinding
import com.yyide.chatim.model.EventMessage
import com.yyide.chatim.model.NoticeBlankReleaseBean
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
    private var total: Int = 0
    private var checkTotalNumber: Int = 0
    private var checkPeopleCount: Int = 0
    private var type: String? = null
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
        type = arguments?.getString("type");
        if (arguments != null) {
            mvpPresenter?.specifieTypeList(type)
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
                mvpPresenter?.specifieTypeList(type)
            }
        }

        viewBinding!!.switchPush.setOnCheckedChangeListener { compoundButton: CompoundButton, isCheck: Boolean ->
            val eventMessage = EventMessage(BaseConstant.TYPE_NOTICE_IS_CONFIRM, "")
            eventMessage.isBoolean = isCheck
            EventBus.getDefault().post(eventMessage)
        }

        viewBinding!!.btnConfirm.setOnClickListener {
            val resultBean = getParams(mAdapter.data as ArrayList<NoticePersonnelBean.ListBean>)
            Log.e(TAG, "btnConfirm>>>>:" + JSON.toJSONString(resultBean))
            EventBus.getDefault().post(EventMessage(BaseConstant.TYPE_NOTICE_PERSONNEL, ""))
        }

        viewBinding!!.cbSelectNumber.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!buttonView.isPressed) {
                Log.e(TAG, "initView: 代码触发，不处理监听事件。")
                return@setOnCheckedChangeListener
            }
            checkTotalNumber = 0
            checkPeopleCount = 0
            recursionChecked(mAdapter.data as ArrayList<NoticePersonnelBean.ListBean>, isChecked)
            checkTotalNumber = getCheckedNumber(mAdapter.data as ArrayList<NoticePersonnelBean.ListBean>)
            if (isChecked) {
                showNoticeScopeNumber(checkTotalNumber)
            } else {
                showNoticeScopeNumber(checkTotalNumber)
            }
            mAdapter.notifyDataSetChanged()
        }
    }

    private fun showNoticeScopeNumber(totalCount: Int) {
        viewBinding!!.cbSelectNumber.text = getString(R.string.notice_chosen_people_number, checkPeopleCount)
        if (totalCount != 0 && totalCount == total) {
            viewBinding!!.cbSelectNumber.isChecked = totalCount == total
        } else {
            viewBinding!!.cbSelectNumber.isChecked = false
        }
    }

    inner class PersonnelAdapter : BaseQuickAdapter<NoticePersonnelBean.ListBean, BaseViewHolder>(R.layout.item_notice_personnel) {

        override fun convert(holder: BaseViewHolder, itemBean: NoticePersonnelBean.ListBean) {
            val view = ItemNoticePersonnelBinding.bind(holder.itemView)
            view.cbCheck.text = itemBean.name
//            if (this.itemCount < holder.adapterPosition - 1) {
//                view.viewLine.visibility = View.VISIBLE
//            } else {
//                view.viewLine.visibility = View.INVISIBLE
//            }
            if (itemBean.list != null && itemBean.list.isNotEmpty()) {
                view.ivUnfold.visibility = View.VISIBLE
                Log.e(TAG, "onBindViewHolder isUnfold: " + itemBean.unfold)
                if (itemBean.unfold) {
                    view.ivUnfold.setImageDrawable(context.resources.getDrawable(R.drawable.icon_up))
                    view.recyclerview.visibility = View.VISIBLE
                } else {
                    view.ivUnfold.setImageDrawable(context.resources.getDrawable(R.drawable.icon_down))
                    view.recyclerview.visibility = View.GONE
                }

                view.recyclerview.layoutManager = LinearLayoutManager(context)
                val adapter = PersonnelAdapter()
                view.recyclerview.adapter = adapter
                adapter.setList(itemBean.list)

                view.root.setOnClickListener { v: View? ->
                    Log.e(TAG, "onBindViewHolder: onclick is unfold:" + view.ivUnfold)
                    itemBean.unfold = !itemBean.unfold
                    notifyDataSetChanged()
                }
            }

            if (viewBinding!!.list.isComputingLayout) {
                viewBinding!!.list.post {
                    view.cbCheck.isChecked = itemBean.check;
                }
            }
            //选中层级
            view.cbCheck.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean ->
//                    itemBean.unfold = isChecked
                if (buttonView != null) {
                    if (!buttonView.isPressed) {
                        return@setOnCheckedChangeListener
                    }
                }
                checkTotalNumber = 0
                checkPeopleCount = 0
                itemBean.check = isChecked
                recursionChecked(itemBean.list, isChecked);
                //reverseElection(mAdapter.data as ArrayList<NoticePersonnelBean.ListBean>)
                showNoticeScopeNumber(getCheckedNumber(mAdapter.data as ArrayList<NoticePersonnelBean.ListBean>))
                notifyDataSetChanged()
            }
        }
    }

    /**
     * 递归实现下级是否选中
     *
     * @param noticeScopeBean
     */
    private fun recursionChecked(noticeScopeBean: ArrayList<NoticePersonnelBean.ListBean>, isChecked: Boolean) {
        if (noticeScopeBean != null) {
            noticeScopeBean.forEachIndexed { index, listBean ->
                listBean.check = isChecked
                noticeScopeBean[index] = listBean
                recursionChecked(listBean.list, isChecked)
            }
        }
    }

    /**
     * 递归实现反选
     *
     * @param noticeScopeBean
     */
    private fun reverseElection(noticeScopeBean: ArrayList<NoticePersonnelBean.ListBean>) {
        if (noticeScopeBean != null) {
            noticeScopeBean.forEachIndexed { index, listBean ->
                var childCount = 0
                listBean.list.forEach() { childItem ->
                    if (childItem.check) {
                        childCount++
                    }
                }
                if (childCount > 0) {
                    listBean.check = childCount == listBean.list.size
                } else {
                    reverseElection(listBean.list)
                }
            }
        }
    }

    /**
     * 获取全部数量
     *
     * @param noticeScopeBean
     */
    private fun getAllCount(noticeScopeBean: ArrayList<NoticePersonnelBean.ListBean>): Int {
        if (noticeScopeBean != null) {
            noticeScopeBean.forEachIndexed { index, listBean ->
                if (TextUtils.isEmpty(listBean.name)) {
                    noticeScopeBean.remove(listBean)
                } else {
                    total++
                    getAllCount(listBean.list)
                }

            }
        }
        return total
    }

    /**
     * 获取当前选中的部门
     *
     * @param noticeScopeBean
     */
    private var checkList: MutableList<NoticeBlankReleaseBean.RecordListBean.ListBean> = mutableListOf()
    private fun getParams(noticeScopeBean: ArrayList<NoticePersonnelBean.ListBean>): NoticeBlankReleaseBean? {
        checkList.clear()
        var recordList: MutableList<NoticeBlankReleaseBean.RecordListBean> = mutableListOf()
        var resultBean = NoticeBlankReleaseBean()
        var recordListBean = NoticeBlankReleaseBean.RecordListBean()
        resultBean.isConfirm = viewBinding!!.switchPush.isChecked
        recordListBean.specifieType = type
        recordListBean.nums = checkPeopleCount
        getCheckList(noticeScopeBean)
        recordList.add(recordListBean)
        if (checkList != null && checkList.size > 0) {
            recordListBean.list = checkList
            resultBean.recordList = recordList
        } else {
            return null
        }
        return resultBean
    }

    //获取已选中的部门
    private fun getCheckList(noticeScopeBean: ArrayList<NoticePersonnelBean.ListBean>) {
        noticeScopeBean.forEachIndexed { index, listBean ->
            if (listBean.check) {
                var item = NoticeBlankReleaseBean.RecordListBean.ListBean()
                item.specifieId = listBean.id
                item.specifieParentId = listBean.parentId
                item.type = listBean.type
                item.nums = listBean.nums
                checkList.add(item)
            }
            getCheckList(listBean.list);
        }
    }

    /**
     * 递归实现获取选中数量
     *
     * @param noticeScopeBean
     */
    private fun getCheckedNumber(noticeScopeBean: ArrayList<NoticePersonnelBean.ListBean>): Int {
        if (noticeScopeBean != null) {
            noticeScopeBean.forEachIndexed { index, listBean ->
                if (listBean.check) {
                    checkTotalNumber++
                    checkPeopleCount += listBean.nums
                }
                getCheckedNumber(listBean.list)
            }
        }
        return checkTotalNumber
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun event(messageEvent: EventMessage) {
        if (BaseConstant.TYPE_NOTICE_IS_CONFIRM == messageEvent.code) {
            viewBinding!!.switchPush.isChecked = messageEvent.isBoolean
        } else if (BaseConstant.TYPE_NOTICE_PERSONNEL == messageEvent.code) {
            val resultBean = getParams(mAdapter.data as ArrayList<NoticePersonnelBean.ListBean>)
            EventBus.getDefault().post(EventMessage(BaseConstant.TYPE_NOTICE_RANGE, JSON.toJSONString(resultBean)))
            activity?.finish()
        }
    }

    override fun getDesignatedPersonnelList(model: NoticePersonnelBean?) {
        if (model != null) {
            if (model.code == BaseConstant.REQUEST_SUCCES2) {
                list = model.data
                mAdapter.setList(model.data)
                total = 0
                total = getAllCount(model.data)
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