package com.yyide.chatim.activity.schedule

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.fastjson.JSON
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.flexbox.*
import com.jzxiang.pickerview.listener.OnDateSetListener
import com.tencent.mmkv.MMKV
import com.yyide.chatim.R
import com.yyide.chatim.activity.meeting.MeetingSaveActivity
import com.yyide.chatim.adapter.schedule.ScheduleTodayAdapter
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.base.MMKVConstant
import com.yyide.chatim.databinding.ActivityScheduleSearchBinding
import com.yyide.chatim.dialog.ScheduleSearchFilterPop
import com.yyide.chatim.model.schedule.*
import com.yyide.chatim.utils.DatePickerDialogUtil
import com.yyide.chatim.utils.DisplayUtils
import com.yyide.chatim.utils.loge
import com.yyide.chatim.view.SpaceItemDecoration
import com.yyide.chatim.view.SpacesItemDecoration
import com.yyide.chatim.viewmodel.LabelManageViewModel
import com.yyide.chatim.viewmodel.ScheduleMangeViewModel
import com.yyide.chatim.viewmodel.ScheduleSearchViewModel
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityScheduleSearchBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
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

        scheduleMangeViewModel.selectAllScheduleList()
    }

    override fun getContentViewID(): Int {
        return R.layout.activity_schedule_search
    }

    fun initView() {
        val simpleDateFormat1 = SimpleDateFormat("yyyy-MM", Locale.getDefault())
        viewBinding.tvSearchTime.text = simpleDateFormat1.format(Date())
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
            DatePickerDialogUtil.showDateTime(
                this,
                "选择日程开始时间",
                filterTagCollect.startTime,
                searchTimeListener
            )
        }
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
        viewBinding.recyclerview.addItemDecoration(SpaceItemDecoration(SpacesItemDecoration.dip2px(10f)))
        viewBinding.recyclerview.adapter = scheduleSearchResultListAdapter
        scheduleSearchResultListAdapter.setOnItemClickListener { _, _, position ->
            loge("setOnItemClickListener：$position")
            val scheduleData = scheduleSearchResultList[position]
            if (scheduleData.type.toInt() == Schedule.SCHEDULE_TYPE_CONFERENCE){
                MeetingSaveActivity.jumpUpdate(this@ScheduleSearchActivity,scheduleData.id)
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
            viewBinding.tvSearchTime.visibility = View.VISIBLE
            filterAdapter.setList(null)
            filterTagCollect = FilterTagCollect()
        }
        //初始化过滤条件的布局
        val flexboxLayoutManager = FlexboxLayoutManager(this)
        viewBinding.filterRecyclerView.layoutManager =flexboxLayoutManager
        viewBinding.filterRecyclerView.addItemDecoration(SpacesItemDecoration(SpacesItemDecoration.dip2px(5f)))
        viewBinding.filterRecyclerView.adapter = filterAdapter
        filterAdapter.setList(tagList)
    }

    private val searchTimeListener = OnDateSetListener { _, millSeconds ->
        val simpleDateFormat1 = SimpleDateFormat("yyyy-MM", Locale.getDefault())
        viewBinding.tvSearchTime.text = simpleDateFormat1.format(Date(millSeconds))
        val simpleDateFormat2 = SimpleDateFormat("yyyy-MM", Locale.getDefault())
        val startTime = simpleDateFormat2.format(Date(millSeconds))
        filterTagCollect.startTime = startTime
        //开始查询日程
        scheduleSearchViewModel.searchSchedule(filterTagCollect)
    }


    private fun showFilterIconColor(isShowing: Boolean) {
        if (isShowing) {
            viewBinding.clFilterCondition.visibility = View.VISIBLE
            viewBinding.clSearchHistory.visibility = View.GONE
            viewBinding.tvSearchTime.visibility = View.INVISIBLE
            viewBinding.tvFilter.setTextColor(resources.getColor(R.color.colorPrimary))
            viewBinding.tvFilter.setCompoundDrawablesWithIntrinsicBounds(
                null,
                null,
                resources.getDrawable(R.mipmap.icon_yd_filter_select),
                null
            )
        } else {
            viewBinding.clFilterCondition.visibility = View.GONE
            viewBinding.clSearchHistory.visibility = View.VISIBLE
            viewBinding.tvSearchTime.visibility = View.VISIBLE
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
        val historyList =
            MMKV.defaultMMKV().decodeStringSet(MMKVConstant.YD_SCHEDULE_HISTORY, HashSet())
        if (historyList.isEmpty()) {
            viewBinding.clSearchHistory.visibility = View.GONE
        } else {
            viewBinding.clSearchHistory.visibility = View.VISIBLE
        }
        searchHistoryList.clear()
        searchHistoryList.addAll(historyList)
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
            scheduleSearchViewModel.searchSchedule(filterTagCollect)
        }
    }

    /**
     * 清空搜索历史
     */
    private fun clearHistory() {
        val search_history =
            MMKV.defaultMMKV().decodeStringSet(MMKVConstant.YD_SCHEDULE_HISTORY, HashSet())
        search_history.clear()
        MMKV.defaultMMKV().encode(MMKVConstant.YD_SCHEDULE_HISTORY, search_history)
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
                drawable.setColor(Color.parseColor(item.label?.colorValue))
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
        val decodeStringSet =
            MMKV.defaultMMKV().decodeStringSet(MMKVConstant.YD_SCHEDULE_HISTORY, HashSet())
        decodeStringSet.add(keyWord)
        if (decodeStringSet.isNotEmpty() && decodeStringSet.size > 20) {

        }
        MMKV.defaultMMKV().encode(MMKVConstant.YD_SCHEDULE_HISTORY, decodeStringSet)
    }

}