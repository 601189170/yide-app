package com.yyide.chatim.activity.schedule

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
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
import com.yyide.chatim.view.SpacesItemDecoration
import java.util.HashSet

class ScheduleSearchActivity : BaseActivity() {


    private lateinit var viewBinding: ActivityScheduleSearchBinding
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