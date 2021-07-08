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
    private var type: String = ""
    private var isCheck: Boolean = false
    private var checkLists = ArrayList<NoticeBlankReleaseBean.RecordListBean.ListBean>()
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
        if (arguments != null) {
            type = arguments?.getString("type").toString()
            isCheck = arguments?.getBoolean("isCheck", false) == true
            checkLists = arguments?.getParcelableArrayList<NoticeBlankReleaseBean.RecordListBean.ListBean>("list") as ArrayList<NoticeBlankReleaseBean.RecordListBean.ListBean>
        }

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
        viewBinding!!.switchPush.isChecked = isCheck
        viewBinding!!.switchPush.setOnCheckedChangeListener { compoundButton: CompoundButton, isCheck: Boolean ->
            val eventMessage = EventMessage(BaseConstant.TYPE_NOTICE_IS_CONFIRM, "")
            eventMessage.isBoolean = isCheck
            EventBus.getDefault().post(eventMessage)
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
            checkLists = ArrayList()
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
            mAdapter = PersonnelAdapter()
            total = 0
            total = getAllCount(siteList as ArrayList<NoticeBrandBean.DataBean>)
            showNoticeScopeNumber(getCheckedNumber(siteList as ArrayList<NoticeBrandBean.DataBean>))
            checkLists = ArrayList()
            recursionChecked(siteList as ArrayList<NoticeBrandBean.DataBean>, false)
            viewBinding!!.list.adapter = mAdapter
            mAdapter.setList(siteList)
        }
    }

    private var teacherNumber = 0
    private var patriarchNumber = 0

    private fun showNoticeScopeNumber(totalCount: Int) {
        viewBinding!!.tvCheckedNumber.text = getString(R.string.notice_brand_class_number, teacherNumber, patriarchNumber, totalCount)
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
                item.unfold = isChecked
                item.check = isChecked
                if (item.list != null) {
                    recursionChecked(item.list, isChecked);
                }
                showNoticeScopeNumber(getCheckedNumber(mAdapter.data as ArrayList<NoticeBrandBean.DataBean>))
                mAdapter.notifyDataSetChanged()
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
            if (TextUtils.isEmpty(listBean.name)) {//处理没有名称的部门
                noticeScopeBean.remove(listBean)
            } else {
                total++
                checkLists.forEach {
                    if (type == "3") {//场地
                        if (listBean.siteId == it.specifieId) {
                            listBean.check = true
                        }
                    } else {//班级
                        if (listBean.id == it.specifieId) {
                            listBean.check = true
                        }
                    }

                }
                if (listBean.list != null) {
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
    private fun getParams(noticeScopeBean: ArrayList<NoticeBrandBean.DataBean>): NoticeBlankReleaseBean? {
        val rangeList: MutableList<NoticeBlankReleaseBean.RecordListBean.ListBean> = mutableListOf()
        val recordList: MutableList<NoticeBlankReleaseBean.RecordListBean> = mutableListOf()
        val resultBean = NoticeBlankReleaseBean()
        val recordListBean = NoticeBlankReleaseBean.RecordListBean()
        resultBean.isConfirm = viewBinding!!.switchPush.isChecked
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
                    item.specifieId = listBean.siteId
                } else {//班级
                    item.specifieId = listBean.id
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
            }
            if (listBean.list != null) {
                getCheckedNumber(listBean.list as ArrayList<NoticeBrandBean.DataBean>)
            }
        }
        return checkTotalNumber;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun event(messageEvent: EventMessage) {
        when {
            BaseConstant.TYPE_NOTICE_IS_CONFIRM == messageEvent.code -> {
                viewBinding!!.switchPush.isChecked = messageEvent.isBoolean
            }
            BaseConstant.TYPE_NOTICE_PERSONNEL == messageEvent.code -> {
                val resultBean = getParams(mAdapter.data as ArrayList<NoticeBrandBean.DataBean>)
                EventBus.getDefault().post(EventMessage(BaseConstant.TYPE_NOTICE_RANGE, JSON.toJSONString(resultBean), type))
                activity?.finish()
            }
            BaseConstant.TYPE_NOTICE_CHECK_NUMBER == messageEvent.code -> {
                if (messageEvent.message == "0") {
                    teacherNumber = messageEvent.count
                } else {
                    patriarchNumber = messageEvent.count
                }
                viewBinding!!.tvCheckedNumber.text = getString(R.string.notice_brand_class_number, teacherNumber, patriarchNumber, getCheckedNumber(mAdapter.data as ArrayList<NoticeBrandBean.DataBean>))
            }
        }
    }

    override fun getBrandPersonnelList(model: NoticeBrandBean?) {
        if (model != null) {
            if (model.code == BaseConstant.REQUEST_SUCCES2) {
                brandList = model.data
                total = getAllCount(brandList as ArrayList<NoticeBrandBean.DataBean>)
                showNoticeScopeNumber(getCheckedNumber(brandList as ArrayList<NoticeBrandBean.DataBean>))
                mAdapter.setList(brandList)
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
        fun newInstance(type: String, brandList: ArrayList<NoticeBlankReleaseBean.RecordListBean.ListBean>, isCheck: Boolean): NoticeBrandPersonnelFragment {
            val fragment = NoticeBrandPersonnelFragment()
            val args = Bundle()
            args.putString("type", type)
            args.putParcelableArrayList("list", brandList)
            args.putBoolean("isCheck", isCheck)
            fragment.arguments = args
            return fragment
        }
    }

}