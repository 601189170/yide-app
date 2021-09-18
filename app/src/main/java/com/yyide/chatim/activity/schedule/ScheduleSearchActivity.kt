package com.yyide.chatim.activity.schedule

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.flexbox.FlexboxLayoutManager
import com.tencent.mmkv.MMKV
import com.yyide.chatim.R
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.base.MMKVConstant
import com.yyide.chatim.databinding.ActivityScheduleSearchBinding
import com.yyide.chatim.dialog.ScheduleSearchFilterPop
import com.yyide.chatim.model.schedule.ScheduleFilterTag
import com.yyide.chatim.model.schedule.TagType
import com.yyide.chatim.utils.DisplayUtils
import com.yyide.chatim.utils.loge
import com.yyide.chatim.view.SpacesItemDecoration
import javassist.bytecode.stackmap.TypeTag
import java.util.HashSet

class ScheduleSearchActivity : BaseActivity() {


    private lateinit var viewBinding: ActivityScheduleSearchBinding
    val tagList = mutableListOf<TagType>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityScheduleSearchBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        initView()
    }

    override fun getContentViewID(): Int {
        return R.layout.activity_schedule_search
    }

    fun initView() {
        viewBinding.cancel.setOnClickListener { finish() }
        viewBinding.btnDeleteSearch.setOnClickListener { viewBinding.edit.text = null }
        viewBinding.ivDel.setOnClickListener { clearHistory() }
        viewBinding.tvFilter.setOnClickListener {
            val scheduleSearchFilterPop = ScheduleSearchFilterPop(mActivity)
            scheduleSearchFilterPop.setOnSelectListener(object :
                ScheduleSearchFilterPop.OnSelectListener {
                override fun result(tag: ScheduleFilterTag) {
                    loge("过滤条件数据为：$tag")
                    initFilterCondition(tag)
                }
            })
            if (scheduleSearchFilterPop.isShowing) {
                viewBinding.tvFilter.setTextColor(resources.getColor(R.color.colorPrimary))
                viewBinding.tvFilter.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    resources.getDrawable(R.mipmap.icon_yd_filter_select),
                    null
                )
            } else {
                viewBinding.tvFilter.setTextColor(resources.getColor(R.color.text_666666))
                viewBinding.tvFilter.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    resources.getDrawable(R.mipmap.icon_yd_filter),
                    null
                )
            }
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
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        //初始化搜索结果列表
        viewBinding.recyclerview.layoutManager = LinearLayoutManager(this)
        viewBinding.recyclerview.adapter = searchAdapter
        initSearchHistory()

        //清空选择过滤条件
        viewBinding.ivDelFilter.setOnClickListener {
            tagList.clear()
            viewBinding.clFilterCondition.visibility = View.GONE
            filterAdapter.setList(null)
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

        viewBinding.filterRecyclerView.layoutManager = FlexboxLayoutManager(this)
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

    private fun initSearchHistory() {
        val historyList =
            MMKV.defaultMMKV().decodeStringSet(MMKVConstant.YD_SCHEDULE_HISTORY, HashSet())
        if (historyList.isEmpty()) {
            viewBinding.tvHistoryOrFilter.visibility = View.GONE
            viewBinding.ivDel.visibility = View.GONE
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
        historyAdapter.setList(historyList)
        historyAdapter.setOnItemClickListener { adapter, view, position ->
            //搜索
            ToastUtils.showShort("搜索")
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
                when (item.scheduleType) {
                    //类型1日程2校历3会议4课表
                    1 -> {
                        holder.setText(R.id.tv_search_value, "事务日程")
                    }
                    2 -> {
                        holder.setText(R.id.tv_search_value, "校历")
                    }
                    3 -> {
                        holder.setText(R.id.tv_search_value, "会议")
                    }
                    4 -> {
                        holder.setText(R.id.tv_search_value, "课表")
                    }
                    else -> {
                    }
                }

            } else {
                val drawable = GradientDrawable()
                drawable.cornerRadius = DisplayUtils.dip2px(context, 2f).toFloat()
                drawable.setColor(Color.parseColor(item.label?.color))
                holder.getView<TextView>(R.id.tv_search_value).background = drawable
                holder.setTextColor(R.id.tv_search_value,context.resources.getColor(R.color.white))
                holder.setText(R.id.tv_search_value, item.label?.title)
            }
        }

    }

    /**
     * 搜索结果适配器
     */
    private val searchAdapter = object :
        BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_schedule_search) {
        override fun convert(holder: BaseViewHolder, item: String) {

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