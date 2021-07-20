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
    private var checkType: String = ""
    private var isConfirm: Boolean = false
    private var isCheck: Boolean = false
    private var noticeDetail: Boolean = false
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
            checkType = arguments?.getString("type").toString()
            isCheck = arguments?.getBoolean("isCheck", false) == true
            noticeDetail = arguments?.getBoolean("noticeDetail", false) == true
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
        if (noticeDetail) {
            viewBinding!!.constraintLayout.visibility = View.GONE
        }
        showNoticeScopeNumber(0)
        type = "2"
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
            checkCountAll = 0
            recursionChecked(mAdapter.data as ArrayList<NoticeBrandBean.DataBean>, isChecked)
            showNoticeScopeNumber(getCheckedNumber(mAdapter.data as ArrayList<NoticeBrandBean.DataBean>))
            mAdapter.notifyDataSetChanged()
        }

        viewBinding!!.ctBrand.setOnClickListener {
            type = "2"
            checkTotalNumber = 0
            checkCountAll = 0
            viewBinding!!.ctBrand.isChecked = true
            viewBinding!!.ctSite.isChecked = false
            viewBinding!!.cbSelectNumber.isChecked = false
            showNoticeScopeNumber(0)
            mAdapter = PersonnelAdapter()
            checkLists = ArrayList()
            viewBinding!!.list.adapter = mAdapter
            total = 0
            //recursionChecked(siteList as ArrayList<NoticeBrandBean.DataBean>, false)
            total = getAllCount(brandList as ArrayList<NoticeBrandBean.DataBean>)
            mAdapter.setList(brandList)
        }

        viewBinding!!.ctSite.setOnClickListener {
            type = "3"
            checkTotalNumber = 0
            checkCountAll = 0
            viewBinding!!.ctBrand.isChecked = false
            viewBinding!!.ctSite.isChecked = true
            viewBinding!!.cbSelectNumber.isChecked = false
            mAdapter = PersonnelAdapter()
            total = 0
            total = getAllCount(siteList as ArrayList<NoticeBrandBean.DataBean>)
            reverseElection(siteList as ArrayList<NoticeBrandBean.DataBean>)
            showNoticeScopeNumber(getCheckedNumber(siteList as ArrayList<NoticeBrandBean.DataBean>))
            checkLists = ArrayList()
            viewBinding!!.list.adapter = mAdapter
            mAdapter.setList(siteList)
        }
    }

    private var teacherNumber = 0
    private var patriarchNumber = 0

    private fun showNoticeScopeNumber(totalCount: Int) {
        viewBinding!!.tvCheckedNumber.text = getString(R.string.notice_brand_class_number, teacherNumber, patriarchNumber, checkTotalNumber)
        if (totalCount != 0) {
            viewBinding!!.cbSelectNumber.isChecked = totalCount == total
        } else {
            viewBinding!!.cbSelectNumber.isChecked = false
        }
    }

    inner class PersonnelAdapter : BaseQuickAdapter<NoticeBrandBean.DataBean, BaseViewHolder>(R.layout.item_notice_personnel) {

        override fun convert(holder: BaseViewHolder, item: NoticeBrandBean.DataBean) {
            val view = ItemNoticePersonnelBinding.bind(holder.itemView)
            view.cbCheck.text = item.name
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
                    mAdapter.notifyDataSetChanged()
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
                checkCountAll = 0
                checked(item, isChecked);
                showNoticeScopeNumber(getCheckedNumber(mAdapter.data as ArrayList<NoticeBrandBean.DataBean>))
                mAdapter.notifyDataSetChanged()
            }

//            if (viewBinding!!.list.isComputingLayout) {
//                viewBinding!!.list.post {
            view.cbCheck.isChecked = item.check
//                }
//            }
        }
    }

    /**
     * 递归实现下级是否选中
     * @param noticeScopeBean
     */
    private fun recursionChecked(noticeScopeBean: ArrayList<NoticeBrandBean.DataBean>, isChecked: Boolean) {
        noticeScopeBean.forEachIndexed { index, listBean ->
            listBean.check = isChecked
            if (listBean.list != null) {
                recursionChecked(listBean.list, isChecked)
            }
        }
    }

    private fun checked(node: NoticeBrandBean.DataBean, isChecked: Boolean) {
        node.check = isChecked
        node.unfold = true
        if (node.list != null)
            recursionChecked(node.list, isChecked)
        setParentCheck(node, isChecked)
    }

    /**
     * 选中父节点
     */
    private fun setParentCheck(node: NoticeBrandBean.DataBean, isChecked: Boolean) {
//		有一个子节点选中，则父节点选中
        if (node.parentBean != null && isChecked) {
            node.parentBean.check = isChecked
            node.parentBean.unfold = isChecked
            setParentCheck(node.parentBean, isChecked)
        }

        //全部子节点取消选中，则父节点取消选中
        if (node.parentBean != null && node.parentBean.list != null && !isChecked) {
            if (node.parentBean.list != null) {
                node.parentBean.list.forEachIndexed { i, listBean ->
                    if (listBean.parentBean.list[i].check) {
                        setParentCheck(listBean.parentBean, !isChecked)
                        return
                    }
                }
            }
            node.parentBean.check = isChecked
            setParentCheck(node.parentBean, isChecked)
        }
    }

    /**
     * 递归实现下级是否选中
     *
     * @param noticeScopeBean
     */
    private fun getAllCount(noticeScopeBean: ArrayList<NoticeBrandBean.DataBean>): Int {
        noticeScopeBean.forEachIndexed { _, listBean ->
            if (TextUtils.isEmpty(listBean.name)) {//处理没有名称的部门
                noticeScopeBean.remove(listBean)
            } else {
                total++
                if (listBean.list != null) {
                    //设置父节点数据
                    listBean.list.forEach { it.parentBean = listBean }
                    getAllCount(listBean.list)
                }
            }
        }
        return total
    }

    //反选
    private fun reverseElection(noticeScopeBean: ArrayList<NoticeBrandBean.DataBean>) {
        if (checkType == type) {
            checkLists.forEach {
                checkedRE(noticeScopeBean, it)
            }
        }
    }

    private fun checkedRE(noticeScopeBean: ArrayList<NoticeBrandBean.DataBean>, it: NoticeBlankReleaseBean.RecordListBean.ListBean) {
        noticeScopeBean.forEach { listBean ->
            if (type == "3" && listBean.id == it.specifieId) {
                listBean.check = true
                listBean.unfold = true
                setParentCheck(listBean, true)
            } else if (type == "2" && listBean.siteId == it.specifieId) {
                listBean.check = true
                listBean.unfold = true
                setParentCheck(listBean, true)
            }
            if (listBean.list != null) {
                checkedRE(listBean.list, it)
            }
        }
    }

    /**
     * 获取当前选中的部门
     *
     * @param noticeScopeBean
     */
    private fun getParams(noticeScopeBean: ArrayList<NoticeBrandBean.DataBean>): NoticeBlankReleaseBean {
        checkLists.clear()
        val rangeList: MutableList<NoticeBlankReleaseBean.RecordListBean.ListBean> = mutableListOf()
        val recordList: MutableList<NoticeBlankReleaseBean.RecordListBean> = mutableListOf()
        val resultBean = NoticeBlankReleaseBean()
        val recordListBean = NoticeBlankReleaseBean.RecordListBean()
        resultBean.isConfirm = isConfirm
        recordListBean.specifieType = type
        recordListBean.nums = checkTotalNumber
        getCheckList(noticeScopeBean, rangeList)
        recordList.add(recordListBean)
        if (rangeList.size > 0) {
            recordListBean.list = rangeList
            resultBean.recordList = recordList
        }
        return resultBean
    }

    private fun getCheckList(noticeScopeBean: ArrayList<NoticeBrandBean.DataBean>, rangeList: MutableList<NoticeBlankReleaseBean.RecordListBean.ListBean>) {
        noticeScopeBean.forEachIndexed { _, listBean ->
            if (listBean.check) {
                val item = NoticeBlankReleaseBean.RecordListBean.ListBean()
                if (type == "3" && listBean.parendId > 0) {
                    item.specifieId = listBean.id
                    item.specifieParentId = listBean.parendId
                    item.type = listBean.type
                    rangeList.add(item)
                } else if (type == "2" && listBean.siteId > 0) {
                    item.specifieId = listBean.siteId
                    item.specifieParentId = listBean.parendId
                    item.type = listBean.type
                    rangeList.add(item)
                }
            }
            if (listBean.list != null) {
                getCheckList(listBean.list as ArrayList<NoticeBrandBean.DataBean>, rangeList);
            }
        }
    }

    /**
     * 递归实现获取选中数量
     *
     * @param noticeScopeBean
     */
    var checkCountAll = 0
    private fun getCheckedNumber(noticeScopeBean: ArrayList<NoticeBrandBean.DataBean>): Int {
        noticeScopeBean.forEachIndexed { _, listBean ->
            if (listBean.check) {
                if (type == "3" && listBean.parendId > 0) {
                    checkTotalNumber++
                } else if (type == "2" && listBean.siteId > 0) {
                    checkTotalNumber++
                }
                checkCountAll++
            }
            if (listBean.list != null) {
                getCheckedNumber(listBean.list)
            }
        }
        return checkCountAll
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun event(messageEvent: EventMessage) {
        when {
            BaseConstant.TYPE_NOTICE_IS_CONFIRM == messageEvent.code -> {
                isConfirm = messageEvent.isBoolean
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
                viewBinding!!.tvCheckedNumber.text = getString(R.string.notice_brand_class_number, teacherNumber, patriarchNumber, checkTotalNumber)
            }
        }
    }

    override fun getBrandPersonnelList(model: NoticeBrandBean?) {
        if (model != null) {
            if (model.code == BaseConstant.REQUEST_SUCCES2 && model.data != null) {
                brandList = model.data
                total = getAllCount(model.data as ArrayList<NoticeBrandBean.DataBean>)
                reverseElection(model.data as ArrayList<NoticeBrandBean.DataBean>)
                showNoticeScopeNumber(getCheckedNumber(model.data as ArrayList<NoticeBrandBean.DataBean>))
                mAdapter.setList(model.data)
            }
        }
    }

    override fun getSitePersonnelList(model: NoticeBrandBean?) {
        if (model != null) {
            if (model.code == BaseConstant.REQUEST_SUCCES2 && model.data != null) {
                getData(model.data as ArrayList<NoticeBrandBean.DataBean>)
                siteList = model.data
            }
        }
    }

    private fun getData(list: ArrayList<NoticeBrandBean.DataBean>) {
        list.forEach { listBean ->
            if (listBean.siteEntityFormList != null) {
                listBean.list = listBean.siteEntityFormList
            }
            if (listBean.list != null) {
                getData(listBean.list as ArrayList<NoticeBrandBean.DataBean>)
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
        fun newInstance(type: String, brandList: ArrayList<NoticeBlankReleaseBean.RecordListBean.ListBean>, isCheck: Boolean, noticeDetail: Boolean): NoticeBrandPersonnelFragment {
            val fragment = NoticeBrandPersonnelFragment()
            val args = Bundle()
            args.putString("type", type)
            args.putParcelableArrayList("list", brandList)
            args.putBoolean("isCheck", isCheck)
            args.putBoolean("noticeDetail", noticeDetail)
            fragment.arguments = args
            return fragment
        }
    }

}