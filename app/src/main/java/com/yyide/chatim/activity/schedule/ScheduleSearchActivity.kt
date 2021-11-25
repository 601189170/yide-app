package com.yyide.chatim.activity.schedule

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.fastjson.JSON
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.flexbox.*
import com.jzxiang.pickerview.listener.OnDateSetListener
import com.tencent.mmkv.MMKV
import com.yanzhenjie.recyclerview.OnItemMenuClickListener
import com.yanzhenjie.recyclerview.SwipeMenu
import com.yanzhenjie.recyclerview.SwipeMenuCreator
import com.yanzhenjie.recyclerview.SwipeMenuItem
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import com.yyide.chatim.BaseApplication
import com.yyide.chatim.R
import com.yyide.chatim.SpData
import com.yyide.chatim.activity.meeting.MeetingSaveActivity
import com.yyide.chatim.adapter.schedule.ScheduleTodayAdapter
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.base.MMKVConstant
import com.yyide.chatim.database.ScheduleDaoUtil
import com.yyide.chatim.database.ScheduleDaoUtil.promoterSelf
import com.yyide.chatim.database.ScheduleDaoUtil.toStringTime
import com.yyide.chatim.databinding.ActivityScheduleSearchBinding
import com.yyide.chatim.dialog.ScheduleSearchFilterPop
import com.yyide.chatim.model.schedule.*
import com.yyide.chatim.utils.ColorUtil
import com.yyide.chatim.utils.DatePickerDialogUtil
import com.yyide.chatim.utils.DisplayUtils
import com.yyide.chatim.utils.loge
import com.yyide.chatim.view.SpaceItemDecoration
import com.yyide.chatim.view.SpacesItemDecoration
import com.yyide.chatim.viewmodel.LabelManageViewModel
import com.yyide.chatim.viewmodel.ScheduleEditViewModel
import com.yyide.chatim.viewmodel.ScheduleMangeViewModel
import com.yyide.chatim.viewmodel.ScheduleSearchViewModel
import org.joda.time.DateTime
import java.text.SimpleDateFormat
import java.util.*

class ScheduleSearchActivity : BaseActivity() {
    private val labelManageViewModel: LabelManageViewModel by viewModels()
    private val scheduleSearchViewModel: ScheduleSearchViewModel by viewModels()
    private val scheduleMangeViewModel: ScheduleMangeViewModel by viewModels()
    private lateinit var viewBinding: ActivityScheduleSearchBinding
    private val tagList = mutableListOf<TagType>()
    private var filterTagCollect: FilterTagCollect = FilterTagCollect()
    private val scheduleSearchResultList = mutableListOf<ScheduleData>()
    private lateinit var scheduleSearchResultListAdapter: ScheduleTodayAdapter
    private val searchHistoryList = mutableListOf<String>()
    private var userId: String? = null
    private val mmkv = MMKV.defaultMMKV()
    private var curModifySchedule: ScheduleData? = null
    private var enterDetailPage = false
    private val scheduleEditViewModel: ScheduleEditViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityScheduleSearchBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        userId = SpData.getIdentityInfo().userId
        initView()
        labelManageViewModel.selectLabelList()
        scheduleSearchViewModel.getScheduleSearchResultList().observe(this, {
            if (it.isEmpty()) {
                ToastUtils.showShort("没有搜索到结果~")
            }
            scheduleSearchResultList.clear()
            scheduleSearchResultList.addAll(it)
            scheduleSearchResultListAdapter.setList(scheduleSearchResultList)
        })

        //删除监听
        scheduleEditViewModel.deleteResult.observe(this, {
            if (it) {
                ScheduleDaoUtil.deleteScheduleData(curModifySchedule?.id ?: "")
                for (scheduleData in scheduleSearchResultList) {
                    if (scheduleData.id == curModifySchedule?.id) {
                        scheduleSearchResultList.remove(scheduleData)
                        scheduleSearchResultListAdapter.setList(scheduleSearchResultList)
                        return@observe
                    }
                }
            }
        })
        //修改日程状态监听
        scheduleEditViewModel.changeStatusResult.observe(this, {
            if (it) {
                ToastUtils.showShort("日程修改成功")
                ScheduleDaoUtil.changeScheduleState(
                    curModifySchedule?.id ?: "", curModifySchedule?.status ?: ""
                )
                scheduleSearchViewModel.searchSchedule(filterTagCollect)
            } else {
                ToastUtils.showShort("日程修改失败")
            }
        })
    }

    override fun getContentViewID(): Int {
        return R.layout.activity_schedule_search
    }

    override fun onResume() {
        super.onResume()
        if (enterDetailPage){
            loge("从详情页进入搜索页重新请求数据")
            scheduleSearchViewModel.searchSchedule(filterTagCollect)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        enterDetailPage = false
    }
    fun initView() {
        val simpleDateFormat1 = SimpleDateFormat("yyyy-MM", Locale.getDefault())
//        viewBinding.tvSearchTime.text = simpleDateFormat1.format(Date())
        viewBinding.cancel.setOnClickListener { finish() }
        viewBinding.btnDeleteSearch.setOnClickListener { viewBinding.edit.text = null }
        viewBinding.ivDel.setOnClickListener { clearHistory() }
        //选择过滤条件
        viewBinding.tvFilter.setOnClickListener {
            val scheduleSearchFilterPop = ScheduleSearchFilterPop(
                mActivity,
                labelManageViewModel.getLabelList().value ?: listOf(),
                filterTagCollect
            )
            scheduleSearchFilterPop.setOnSelectListener(object :
                ScheduleSearchFilterPop.OnSelectListener {
                override fun result(tag: ScheduleFilterTag) {
                    loge("过滤条件数据为：$tag")
                    initFilterCondition(tag)
                }
            })
        }
        //根据日期查询
        viewBinding.tvSearchTime.setOnClickListener {
//            DatePickerDialogUtil.showDateTime(
//                this,
//                "选择日程开始时间",
//                filterTagCollect.startTime,
//                searchTimeListener
//            )
        }
        viewBinding.edit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                loge("beforeTextChanged ${s.toString()}")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                loge("onTextChanged ${s.toString()}")
                if (TextUtils.isEmpty(s.toString())) {
                    if (!viewBinding.clFilterCondition.isVisible) {
                        viewBinding.clSearchHistory.visibility = View.VISIBLE
                    }
                } else {
                    viewBinding.clSearchHistory.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {
                loge("afterTextChanged ${s.toString()}")
            }

        })
        viewBinding.edit.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                (v.context
                    .getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
                    .hideSoftInputFromWindow(
                        this@ScheduleSearchActivity.currentFocus?.windowToken,
                        InputMethodManager.HIDE_NOT_ALWAYS
                    )
                val keyWord: String = viewBinding.edit.text.toString().trim()
                if (TextUtils.isEmpty(keyWord)) {
                    ToastUtils.showShort("请输入关键词内容")
                    return@setOnEditorActionListener true
                }
                saveHistory(keyWord)
                //search(keyWord)
                //开始查询日程
                filterTagCollect.name = keyWord
                if (filterTagCollect.startTime == null) {
//                    filterTagCollect.startTime = DateTime.now().toStringTime()
                }
                scheduleSearchViewModel.searchSchedule(filterTagCollect)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        //初始化搜索结果列表
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        viewBinding.recyclerview.layoutManager = linearLayoutManager
        scheduleSearchResultListAdapter = ScheduleTodayAdapter(scheduleSearchResultList)
        viewBinding.recyclerview.setSwipeMenuCreator(mWeekSwipeMenuCreator)
        viewBinding.recyclerview.setOnItemMenuClickListener(mWeekMenuItemClickListener)
        viewBinding.recyclerview.addItemDecoration(
            SpaceItemDecoration(
                SpacesItemDecoration.dip2px(
                    10f
                )
            )
        )
        viewBinding.recyclerview.adapter = scheduleSearchResultListAdapter
        scheduleSearchResultListAdapter.setOnItemClickListener { _, _, position ->
            loge("setOnItemClickListener：$position")
            enterDetailPage = true
            val scheduleData = scheduleSearchResultList[position]
            if (scheduleData.type.toInt() == Schedule.SCHEDULE_TYPE_CONFERENCE) {
                MeetingSaveActivity.jumpUpdate(this@ScheduleSearchActivity, scheduleData.id)
                return@setOnItemClickListener
            }

            if (scheduleData.type.toInt() == Schedule.SCHEDULE_TYPE_CLASS_SCHEDULE) {
                ScheduleTimetableClassActivity.jump(this@ScheduleSearchActivity, scheduleData)
                return@setOnItemClickListener
            }

            val intent = Intent(this@ScheduleSearchActivity, ScheduleEditActivity::class.java)
            intent.putExtra("data", JSON.toJSONString(scheduleData))
            startActivity(intent)
        }
        initSearchHistory()
        //清空选择过滤条件
        viewBinding.ivDelFilter.setOnClickListener {
            tagList.clear()
            viewBinding.clFilterCondition.visibility = View.GONE
//            viewBinding.tvSearchTime.visibility = View.VISIBLE
            filterAdapter.setList(null)
            filterTagCollect = FilterTagCollect()
        }
        //初始化过滤条件的布局
        val flexboxLayoutManager = FlexboxLayoutManager(this)
        viewBinding.filterRecyclerView.layoutManager = flexboxLayoutManager
        viewBinding.filterRecyclerView.addItemDecoration(
            SpacesItemDecoration(
                SpacesItemDecoration.dip2px(
                    5f
                )
            )
        )
        viewBinding.filterRecyclerView.adapter = filterAdapter
        filterAdapter.setList(tagList)
    }

    private val searchTimeListener = OnDateSetListener { _, millSeconds ->
        val simpleDateFormat1 = SimpleDateFormat("yyyy-MM", Locale.getDefault())
//        viewBinding.tvSearchTime.text = simpleDateFormat1.format(Date(millSeconds))
        val simpleDateFormat2 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val startTime = simpleDateFormat2.format(Date(millSeconds))
        filterTagCollect.startTime = startTime
        //开始查询日程
        scheduleSearchViewModel.searchSchedule(filterTagCollect)
    }


    private fun showFilterIconColor(isShowing: Boolean) {
        if (isShowing) {
            viewBinding.clFilterCondition.visibility = View.VISIBLE
            viewBinding.clSearchHistory.visibility = View.GONE
//            viewBinding.tvSearchTime.visibility = View.INVISIBLE
            viewBinding.tvFilter.setTextColor(resources.getColor(R.color.colorPrimary))
            viewBinding.tvFilter.setCompoundDrawablesWithIntrinsicBounds(
                null,
                null,
                resources.getDrawable(R.mipmap.icon_yd_filter_select),
                null
            )
        } else {
            viewBinding.clFilterCondition.visibility = View.GONE
            if (TextUtils.isEmpty(viewBinding.edit.text.toString())) {
                viewBinding.clSearchHistory.visibility = View.VISIBLE
            }
//            viewBinding.tvSearchTime.visibility = View.VISIBLE
            viewBinding.tvFilter.setTextColor(resources.getColor(R.color.text_666666))
            viewBinding.tvFilter.setCompoundDrawablesWithIntrinsicBounds(
                null,
                null,
                resources.getDrawable(R.mipmap.icon_yd_filter),
                null
            )
        }
    }

    private fun initFilterCondition(scheduleFilterTag: ScheduleFilterTag) {
        tagList.clear()
        if (scheduleFilterTag.tags.isEmpty() && scheduleFilterTag.types.isEmpty()) {
            return
        }
        viewBinding.clSearchHistory.visibility = View.GONE
        viewBinding.clFilterCondition.visibility = View.VISIBLE
        if (scheduleFilterTag.types.isNotEmpty()) {
            scheduleFilterTag.types.forEach {
                tagList.add(TagType(0, null, it))
            }
        }

        if (scheduleFilterTag.tags.isNotEmpty()) {
            scheduleFilterTag.tags.forEach {
                tagList.add(TagType(1, it, -1))
            }
        }
        //设置过滤icon的背景
        if (scheduleFilterTag.tags.isEmpty()
            && scheduleFilterTag.types.isEmpty()
            && TextUtils.isEmpty(scheduleFilterTag.endDate)
            && TextUtils.isEmpty(scheduleFilterTag.startDate)
            && scheduleFilterTag.status.isEmpty()
        ) {
            showFilterIconColor(false)
        } else {
            showFilterIconColor(true)
        }
        filterAdapter.setList(tagList)
        //根据日程过滤添加请求日程
        filterTagCollect.startTime = scheduleFilterTag.startDate
        filterTagCollect.endTime = scheduleFilterTag.endDate
        filterTagCollect.status = scheduleFilterTag.status
        filterTagCollect.type = scheduleFilterTag.types
        val labelList = scheduleFilterTag.tags.map { it.id.toString() }
        filterTagCollect.labelId = if (labelList.isEmpty()) null else labelList
        //开始查询日程
        scheduleSearchViewModel.searchSchedule(filterTagCollect)
    }

    private fun initSearchHistory() {
        searchHistoryList.clear()
        val userId = SpData.getIdentityInfo().userId
        val decodeString = mmkv.decodeString(MMKVConstant.YD_SCHEDULE_HISTORY)
        if (!TextUtils.isEmpty(decodeString)) {
            try {
                val searchHistoryBeanList: List<ScheduleSearchHistoryBean> =
                    JSON.parseArray(decodeString, ScheduleSearchHistoryBean::class.java)
                if (searchHistoryBeanList.map { it.userId }.contains(userId)) {
                    searchHistoryBeanList.forEach {
                        if (it.userId == userId) {
                            searchHistoryList.addAll(it.historyList)
                            return@forEach
                        }
                    }
                }
            } catch (e: Exception) {
                loge("" + e.localizedMessage)
            }
        }

        if (searchHistoryList.isEmpty()) {
            viewBinding.clSearchHistory.visibility = View.GONE
        } else {
            viewBinding.clSearchHistory.visibility = View.VISIBLE
        }

        viewBinding.historyRecyclerView.layoutManager = FlexboxLayoutManager(this)
        viewBinding.historyRecyclerView.addItemDecoration(
            SpacesItemDecoration(
                SpacesItemDecoration.dip2px(
                    5f
                )
            )
        )
        viewBinding.historyRecyclerView.adapter = historyAdapter
        historyAdapter.setList(searchHistoryList)
        historyAdapter.setOnItemClickListener { adapter, view, position ->
            //搜索
            filterTagCollect.name = searchHistoryList[position]
            viewBinding.edit.setText(filterTagCollect.name)
            if (filterTagCollect.startTime == null) {
//                filterTagCollect.startTime = DateTime.now().toStringTime()
            }
            scheduleSearchViewModel.searchSchedule(filterTagCollect)
        }
    }

    /**
     * 清空搜索历史
     */
    private fun clearHistory() {
        val decodeString = mmkv.decodeString(MMKVConstant.YD_SCHEDULE_HISTORY)
        if (!TextUtils.isEmpty(decodeString)) {
            try {
                val searchHistoryBeanList: List<ScheduleSearchHistoryBean> =
                    JSON.parseArray(decodeString, ScheduleSearchHistoryBean::class.java)
                if (searchHistoryBeanList.map { it.userId }.contains(userId)) {
                    searchHistoryBeanList.forEach {
                        if (it.userId == userId) {
                            it.historyList.clear()
                            mmkv.encode(
                                MMKVConstant.YD_SCHEDULE_HISTORY,
                                JSON.toJSONString(searchHistoryBeanList)
                            )
                            return@forEach
                        }
                    }
                }
            } catch (e: Exception) {
                loge("" + e.localizedMessage)
                val searchHistoryBeanList = mutableListOf<ScheduleSearchHistoryBean>()
                mmkv.encode(
                    MMKVConstant.YD_SCHEDULE_HISTORY,
                    JSON.toJSONString(searchHistoryBeanList)
                )
            }
        }
        historyAdapter.setList(null)
        searchHistoryList.clear()
        viewBinding.clSearchHistory.visibility = View.GONE
    }

    /**
     * 搜索历史适配器
     */
    private val historyAdapter = object :
        BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_schedule_search_history) {
        override fun convert(holder: BaseViewHolder, item: String) {
            holder.setText(R.id.tv_search_value, item)
        }
    }

    private val filterAdapter = object :
        BaseQuickAdapter<TagType, BaseViewHolder>(R.layout.item_schedule_search_history) {
        override fun convert(holder: BaseViewHolder, item: TagType) {
            if (item.type == 0) {
                val drawable = GradientDrawable()
                drawable.cornerRadius = DisplayUtils.dip2px(context, 2f).toFloat()
                drawable.setColor(context.resources.getColor(R.color.color_f3f3f3))
                holder.getView<TextView>(R.id.tv_search_value).background = drawable
                holder.setTextColor(
                    R.id.tv_search_value,
                    context.resources.getColor(R.color.black9)
                )
                when (item.scheduleType) {
                    //【0：校历日程，1：课表日程，2：事务日程, 3：会议日程】
                    2 -> {
                        holder.setText(R.id.tv_search_value, "事务日程")
                    }
                    0 -> {
                        holder.setText(R.id.tv_search_value, "校历")
                    }
                    3 -> {
                        holder.setText(R.id.tv_search_value, "会议")
                    }
                    1 -> {
                        holder.setText(R.id.tv_search_value, "课表")
                    }
                    else -> {
                    }
                }

            } else {
                val drawable = GradientDrawable()
                drawable.cornerRadius = DisplayUtils.dip2px(context, 2f).toFloat()
                drawable.setColor(ColorUtil.parseColor(item.label?.colorValue))
                holder.getView<TextView>(R.id.tv_search_value).background = drawable
                holder.setTextColor(R.id.tv_search_value, context.resources.getColor(R.color.white))
                holder.setText(R.id.tv_search_value, item.label?.labelName)
            }
        }

    }

    /**
     * 保存搜索历史
     */
    private fun saveHistory(keyWord: String) {
        if (!searchHistoryList.contains(keyWord)) {
            searchHistoryList.add(keyWord)
            historyAdapter.setList(searchHistoryList)
        }
//        if (searchHistoryList.isEmpty()) {
//            viewBinding.clSearchHistory.visibility = View.GONE
//        } else {
//            viewBinding.clSearchHistory.visibility = View.VISIBLE
//        }
        val decodeString = mmkv.decodeString(MMKVConstant.YD_SCHEDULE_HISTORY)
        if (TextUtils.isEmpty(decodeString)) {
            val searchHistoryBeanList = mutableListOf<ScheduleSearchHistoryBean>()
            val historyList = mutableListOf<String>()
            historyList.add(keyWord)
            searchHistoryBeanList.add(ScheduleSearchHistoryBean("", historyList))
            mmkv.encode(MMKVConstant.YD_SCHEDULE_HISTORY, JSON.toJSONString(searchHistoryBeanList))
        } else {
            try {
                val searchHistoryBeanList: List<ScheduleSearchHistoryBean> =
                    JSON.parseArray(decodeString, ScheduleSearchHistoryBean::class.java)
                if (searchHistoryBeanList.map { it.userId }.contains(userId)) {
                    searchHistoryBeanList.forEach {
                        if (it.userId == SpData.getIdentityInfo().userId) {
                            val historyList = it.historyList
                            if (!historyList.contains(keyWord)) {
                                historyList.add(keyWord)
                                mmkv.encode(
                                    MMKVConstant.YD_SCHEDULE_HISTORY,
                                    JSON.toJSONString(searchHistoryBeanList)
                                )
                            }
                            return@forEach
                        }
                    }
                } else {
                    val searchHistoryBeanList = mutableListOf<ScheduleSearchHistoryBean>()
                    val historyList = mutableListOf<String>()
                    historyList.add(keyWord)
                    searchHistoryBeanList.add(ScheduleSearchHistoryBean(userId, historyList))
                    mmkv.encode(
                        MMKVConstant.YD_SCHEDULE_HISTORY,
                        JSON.toJSONString(searchHistoryBeanList)
                    )
                }
            } catch (e: Exception) {
                loge("" + e.localizedMessage)
                val searchHistoryBeanList = mutableListOf<ScheduleSearchHistoryBean>()
                val historyList = mutableListOf<String>()
                historyList.add(keyWord)
                searchHistoryBeanList.add(ScheduleSearchHistoryBean(userId, historyList))
                mmkv.encode(
                    MMKVConstant.YD_SCHEDULE_HISTORY,
                    JSON.toJSONString(searchHistoryBeanList)
                )
            }
        }
    }

    //设置侧滑选项
    val width = DisplayUtils.dip2px(BaseApplication.getInstance(), 63f)
    val height = ViewGroup.LayoutParams.MATCH_PARENT
    private val markCompletedMenuItem: SwipeMenuItem =
        SwipeMenuItem(BaseApplication.getInstance()).setBackground(R.drawable.selector_blue)
            //.setImage(R.drawable.ic_action_delete)
            .setText("标为\n完成")
            .setTextColor(Color.WHITE)
            .setWidth(width)
            .setHeight(height)

    private val markUnCompletedMenuItem: SwipeMenuItem =
        SwipeMenuItem(BaseApplication.getInstance()).setBackground(R.drawable.selector_orange)
            //.setImage(R.drawable.ic_action_delete)
            .setText("标为\n未完成")
            .setTextColor(Color.WHITE)
            .setWidth(width)
            .setHeight(height)

    private val delMenuItem: SwipeMenuItem =
        SwipeMenuItem(BaseApplication.getInstance()).setBackground(R.drawable.selector_red)
            .setText("删除")
            .setTextColor(Color.WHITE)
            .setWidth(width)
            .setHeight(height)

    private val mWeekSwipeMenuCreator: SwipeMenuCreator = object : SwipeMenuCreator {
        override fun onCreateMenu(
            swipeLeftMenu: SwipeMenu,
            swipeRightMenu: SwipeMenu,
            position: Int
        ) {
            loge("position: $position")
            val scheduleData = scheduleSearchResultList[position]
            ////日程类型【0：校历日程，1：课表日程，2：事务日程 3：会议日程】
            val type = scheduleData.type
            val promoterSelf = scheduleData.promoterSelf()
            if (promoterSelf) {
                run {
                    when (type.toInt()) {
                        Schedule.SCHEDULE_TYPE_SCHEDULE -> {
                            if (scheduleData.status == "1") {
                                swipeRightMenu.addMenuItem(markUnCompletedMenuItem)
                            } else {
                                swipeRightMenu.addMenuItem(markCompletedMenuItem)
                            }
                            swipeRightMenu.addMenuItem(delMenuItem)
                        }
                        Schedule.SCHEDULE_TYPE_CONFERENCE -> {
                            swipeRightMenu.addMenuItem(delMenuItem)
                        }
                    }
                }
            }
        }
    }

    /**
     * RecyclerView的Item的Menu点击监听。
     */
    private val mWeekMenuItemClickListener =
        OnItemMenuClickListener { menuBridge, position ->
            menuBridge.closeMenu()
            val direction = menuBridge.direction // 左侧还是右侧菜单。
            val menuPosition = menuBridge.position // 菜单在RecyclerView的Item中的Position。
            if (direction == SwipeRecyclerView.RIGHT_DIRECTION) {
                val scheduleData = scheduleSearchResultList[position]
                val type = scheduleData.type
                when (type.toInt()) {
                    Schedule.SCHEDULE_TYPE_SCHEDULE -> {
                        if (menuPosition == 0) {
                            loge("修改")
                            curModifySchedule = scheduleData
                            scheduleEditViewModel.changeScheduleState(scheduleData)
                            return@OnItemMenuClickListener
                        }
                        if (menuPosition == 1) {
                            loge("删除")
                            curModifySchedule = scheduleData
                            scheduleEditViewModel.deleteScheduleById(scheduleData.id)
                            return@OnItemMenuClickListener
                        }
                    }
                    Schedule.SCHEDULE_TYPE_CONFERENCE -> {
                        if (menuPosition == 0) {
                            loge("删除")
                            curModifySchedule = scheduleData
                            scheduleEditViewModel.deleteScheduleById(scheduleData.id)
                            return@OnItemMenuClickListener
                        }
                    }
                }

            } else if (direction == SwipeRecyclerView.LEFT_DIRECTION) {
                loge("list第$position; 左侧菜单第$menuPosition")
            }
        }
}