package com.yyide.chatim.activity.newnotice.fragment

import android.os.Bundle
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
import com.yyide.chatim.databinding.FragmentNoticeBrandPersonnelListBinding
import com.yyide.chatim.databinding.ItemNoticePersonnelBinding
import com.yyide.chatim.model.*
import com.yyide.chatim.presenter.NoticeBrandPersonnelPresenter
import com.yyide.chatim.view.NoticeBrandPersonnelView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 通知公告班牌场地（选择）
 * auther lrz
 * time  2021年6月17日11:00:27
 */
class NoticeBrandPersonnelFragment : BaseMvpFragment<NoticeBrandPersonnelPresenter?>(), NoticeBrandPersonnelView {
    private val TAG = NoticeBrandPersonnelFragment::class.java.simpleName
    private var viewBinding: FragmentNoticeBrandPersonnelListBinding? = null
    private var total: Int = 0
    private var checkTotalNumber: Int = 0
    private var type: String? = null
    private var mAdapter: PersonnelAdapter = PersonnelAdapter()
    private var siteList = mutableListOf<NoticeBrandBean.DataBean>()
    private var brandList = mutableListOf<NoticeBrandBean.DataBean>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        viewBinding = FragmentNoticeBrandPersonnelListBinding.inflate(layoutInflater)
        return viewBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        EventBus.getDefault().register(this)
        initView()
        mvpPresenter?.notificationClasses()
        mvpPresenter?.notificationSite()
    }

    override fun createPresenter(): NoticeBrandPersonnelPresenter {
        return NoticeBrandPersonnelPresenter(this)
    }

    private fun initView() {
        type = "2"
        showNoticeScopeNumber(0)
        //默认选中第一个
        viewBinding!!.list.layoutManager = LinearLayoutManager(activity)
        viewBinding!!.list.adapter = mAdapter
        mAdapter.setEmptyView(R.layout.empty)
        mAdapter.emptyLayout!!.setOnClickListener { v: View? ->
            //点击空数据界面刷新当前页数据
        }

        viewBinding!!.btnConfirm.setOnClickListener {
            val resultBean = getParams(mAdapter.data as ArrayList<NoticeBrandBean.DataBean>)
            Log.e(TAG, "btnConfirm>>>>:" + JSON.toJSONString(resultBean))
            EventBus.getDefault().post(EventMessage(BaseConstant.TYPE_NOTICE_PERSONNEL, ""))
        }

        viewBinding!!.cbSelectNumber.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!buttonView.isPressed) {
                Log.e(TAG, "initView: 代码触发，不处理监听事件。")
                return@setOnCheckedChangeListener
            }
            checkTotalNumber = 0
            recursionChecked(mAdapter.data as ArrayList<NoticeBrandBean.DataBean>, isChecked)
            checkTotalNumber = getCheckedNumber(mAdapter.data as ArrayList<NoticeBrandBean.DataBean>)
            if (isChecked) {
                showNoticeScopeNumber(checkTotalNumber)
            } else {
                showNoticeScopeNumber(checkTotalNumber)
            }
            mAdapter.notifyDataSetChanged()
        }

        viewBinding!!.ctBrand.setOnClickListener {
            type = "2"
            checkTotalNumber = 0
            viewBinding!!.ctBrand.isChecked = true
            viewBinding!!.ctSite.isChecked = false
            viewBinding!!.cbSelectNumber.isChecked = false
            showNoticeScopeNumber(0)
            mAdapter = PersonnelAdapter()
            viewBinding!!.list.adapter = mAdapter
            recursionChecked(brandList as ArrayList<NoticeBrandBean.DataBean>, false)
            total = 0
            total = getAllCount(brandList as ArrayList<NoticeBrandBean.DataBean>)
            mAdapter.setList(brandList)
        }

        viewBinding!!.ctSite.setOnClickListener {
            type = "3"
            checkTotalNumber = 0
            viewBinding!!.ctBrand.isChecked = false
            viewBinding!!.ctSite.isChecked = true
            viewBinding!!.cbSelectNumber.isChecked = false
            showNoticeScopeNumber(0)
            mAdapter = PersonnelAdapter()
            viewBinding!!.list.adapter = mAdapter
            recursionChecked(siteList as ArrayList<NoticeBrandBean.DataBean>, false)
            total = 0
            total = getAllCount(siteList as ArrayList<NoticeBrandBean.DataBean>)
            mAdapter.setList(siteList)

        }
    }

    private fun showNoticeScopeNumber(totalCount: Int) {
        viewBinding!!.cbSelectNumber.text = getString(R.string.notice_brand_class_number, totalCount)
        if (totalCount != 0 && totalCount == total) {
            viewBinding!!.cbSelectNumber.isChecked = totalCount == total
        } else {
            viewBinding!!.cbSelectNumber.isChecked = false
        }
    }

    inner class PersonnelAdapter : BaseQuickAdapter<NoticeBrandBean.DataBean, BaseViewHolder>(R.layout.item_notice_personnel) {

        override fun convert(holder: BaseViewHolder, item: NoticeBrandBean.DataBean) {
            val view = ItemNoticePersonnelBinding.bind(holder.itemView)
            view.cbCheck.text = item.name

            if (viewBinding!!.list.isComputingLayout) {
                viewBinding!!.list.post {
                    view.cbCheck.isChecked = item.check
                }
            }
//            if (this.itemCount < holder.adapterPosition - 1) {
//                view.viewLine.visibility = View.VISIBLE
//            } else {
//                view.viewLine.visibility = View.INVISIBLE
//            }
            if (item.list != null && item.list.isNotEmpty()) {
                view.ivUnfold.visibility = View.VISIBLE
                Log.e(TAG, "onBindViewHolder isUnfold: " + item.unfold)
                if (item.unfold) {
                    view.ivUnfold.setImageDrawable(context.resources.getDrawable(R.drawable.icon_up))
                    view.recyclerview.visibility = View.VISIBLE
                } else {
                    view.ivUnfold.setImageDrawable(context.resources.getDrawable(R.drawable.icon_down))
                    view.recyclerview.visibility = View.GONE
                }

                view.recyclerview.layoutManager = LinearLayoutManager(context)
                val adapter = PersonnelAdapter()
                view.recyclerview.adapter = adapter
                adapter.setList(item.list)

                view.root.setOnClickListener { v: View? ->
                    Log.e(TAG, "onBindViewHolder: onclick is unfold:" + view.ivUnfold)
                    item.unfold = !item.unfold
                    notifyDataSetChanged()
                }
            }

            //选中层级
            view.cbCheck.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean ->
                if (buttonView != null) {
                    if (!buttonView.isPressed) {
                        return@setOnCheckedChangeListener
                    }
                }
                checkTotalNumber = 0
                item.check = isChecked
                if (item.list != null) {
                    recursionChecked(item.list, isChecked);
                }
                //reverseElection(mAdapter.data as ArrayList<NoticeBrandBean.DataBean>)
                showNoticeScopeNumber(getCheckedNumber(mAdapter.data as ArrayList<NoticeBrandBean.DataBean>))
                notifyDataSetChanged()
            }
        }
    }

    /**
     * 递归实现下级是否选中
     *
     * @param noticeScopeBean
     */
    private fun recursionChecked(noticeScopeBean: ArrayList<NoticeBrandBean.DataBean>, isChecked: Boolean) {
        noticeScopeBean.forEachIndexed { index, listBean ->
            listBean.check = isChecked
            noticeScopeBean[index] = listBean
            if (listBean.list != null) {
                recursionChecked(listBean.list, isChecked)
            }
        }
    }

    /**
     * 递归实现反选
     *
     * @param noticeScopeBean
     */
    private fun reverseElection(noticeScopeBean: ArrayList<NoticeBrandBean.DataBean>) {
        noticeScopeBean.forEachIndexed { index, listBean ->
            var childCount = 0
            if (listBean.list != null) {
                listBean.list.forEach() { childItem ->
                    if (childItem.check) {
                        childCount++
                    }
                }
            }
            if (childCount > 0) {
                listBean.check = childCount == listBean.list.size
            } else {
                reverseElection(listBean.list)
            }
        }
    }

    /**
     * 递归实现下级是否选中
     *
     * @param noticeScopeBean
     */
    private fun getAllCount(noticeScopeBean: ArrayList<NoticeBrandBean.DataBean>): Int {
        noticeScopeBean.forEachIndexed { index, listBean ->
            total++
            if (listBean.list != null) {
                getAllCount(listBean.list)
            }
        }
        return total
    }

    /**
     * 获取当前选中的部门
     *
     * @param noticeScopeBean
     */
    private fun getParams(noticeScopeBean: ArrayList<NoticeBrandBean.DataBean>): NoticeBlankReleaseBean? {
        val rangeList: MutableList<NoticeBlankReleaseBean.RecordListBean.ListBean> = mutableListOf()
        val recordList: MutableList<NoticeBlankReleaseBean.RecordListBean> = mutableListOf()
        val resultBean = NoticeBlankReleaseBean()
        val recordListBean = NoticeBlankReleaseBean.RecordListBean()
        recordListBean.specifieType = type
        recordListBean.nums = checkTotalNumber
        getRangeList(noticeScopeBean, rangeList)
        recordList.add(recordListBean)
        if (rangeList != null && rangeList.size > 0) {
            recordListBean.list = rangeList
            resultBean.recordList = recordList
        } else {
            return null
        }
        resultBean.recordList = recordList
        return resultBean
    }

    private fun getRangeList(noticeScopeBean: ArrayList<NoticeBrandBean.DataBean>, rangeList: MutableList<NoticeBlankReleaseBean.RecordListBean.ListBean>) {
        noticeScopeBean.forEachIndexed { index, listBean ->
            if (listBean.check) {
                val item = NoticeBlankReleaseBean.RecordListBean.ListBean()
                if (type == "3") {//场地
                    item.specifieId = listBean.id
                } else {//班级
                    item.specifieId = listBean.siteId
                }
                item.specifieParentId = listBean.parentId
                item.type = listBean.type
                rangeList.add(item)
            }
            if (listBean.list != null) {
                getRangeList(listBean.list as ArrayList<NoticeBrandBean.DataBean>, rangeList);
            }
        }
    }

    /**
     * 递归实现获取选中数量
     *
     * @param noticeScopeBean
     */
    private fun getCheckedNumber(noticeScopeBean: ArrayList<NoticeBrandBean.DataBean>): Int {
        noticeScopeBean.forEachIndexed { index, listBean ->
            if (listBean.check) {
                checkTotalNumber++
                //checkPeopleCount += listBean.
            }
            if (listBean.list != null) {
                getCheckedNumber(listBean.list as ArrayList<NoticeBrandBean.DataBean>)
            }
        }
        return checkTotalNumber;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun event(messageEvent: EventMessage) {
        if (BaseConstant.TYPE_NOTICE_IS_CONFIRM == messageEvent.code) {

        } else if (BaseConstant.TYPE_NOTICE_PERSONNEL == messageEvent.code) {
            val resultBean = getParams(mAdapter.data as ArrayList<NoticeBrandBean.DataBean>)
            EventBus.getDefault().post(EventMessage(BaseConstant.TYPE_NOTICE_RANGE, JSON.toJSONString(resultBean)))
            activity?.finish()
        }
    }

    override fun getBrandPersonnelList(model: NoticeBrandBean?) {
        if (model != null) {
            if (model.code == BaseConstant.REQUEST_SUCCES2) {
                brandList = model.data
                mAdapter.setList(brandList)
                total = getAllCount(brandList as ArrayList<NoticeBrandBean.DataBean>);
            }
        }
    }

    override fun getSitePersonnelList(model: NoticeBrandBean?) {
        if (model != null) {
            if (model.code == BaseConstant.REQUEST_SUCCES2) {
                siteList = model.data
            }
        }
    }

    override fun getBrandPersonnelFail(msg: String?) {
        Log.d(TAG, "getMyReceivedFail>>>>>：$msg")
    }

    override fun onDestroy() {
        super.onDestroy()
        viewBinding = null
        EventBus.getDefault().unregister(this)
    }

    companion object {
        fun newInstance(type: String): NoticeBrandPersonnelFragment {
            val fragment = NoticeBrandPersonnelFragment()
            val args = Bundle()
            args.putString("type", type)
            fragment.arguments = args
            return fragment
        }
    }

}