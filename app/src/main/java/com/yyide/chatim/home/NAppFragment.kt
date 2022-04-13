package com.yyide.chatim.home

import NewAppViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.tabs.TabLayout
import com.yyide.chatim.R
import com.yyide.chatim.adapter.NewAppAdapter
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.base.BaseFragment
import com.yyide.chatim.databinding.NappFragmentBinding
import com.yyide.chatim.model.EventMessage
import com.yyide.chatim.model.NewAppRspJ
import org.greenrobot.eventbus.EventBus

/**
 * @ProjectName : yideapp
 * @Author : J
 * @Time : 2022/4/7 15:25
 * @Description : 新的工作台
 */
class NAppFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var binding: NappFragmentBinding
    private val viewModel: NewAppViewModel by viewModels()
    private var mNewAppAdapter: NewAppAdapter? = null
    private var isClick = false
    private val mTitles: MutableList<String> = ArrayList()
    private val mAppList: MutableList<NewAppRspJ> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = NappFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        getData()
    }

    private fun initView() {
        mNewAppAdapter = NewAppAdapter()
        binding.newappRecycle.layoutManager = LinearLayoutManager(mActivity)
        binding.newappRecycle.adapter = mNewAppAdapter
        mNewAppAdapter!!.setEmptyView(R.layout.empty)
        binding.newappTablayout.tabMode = TabLayout.MODE_SCROLLABLE
        binding.swipeRefreshLayout.setColorSchemeColors(this.resources.getColor(R.color.colorPrimary))
        binding.swipeRefreshLayout.setOnRefreshListener(this)
        binding.swipeRefreshLayout.isRefreshing = true
        binding.swipeRefreshLayout.isRefreshing = false
        binding.newappRecycle.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == 0)
                    isClick = false

            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                //if (!isClick) {
                val l = recyclerView.layoutManager as LinearLayoutManager?
                val firstPosition = l!!.findFirstVisibleItemPosition()
                binding.newappTablayout.setScrollPosition(firstPosition, 0f, true)
                //}
            }
        })
        binding.newappTablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                isClick = true
                val position = tab!!.position
                val l = binding.newappRecycle.layoutManager as LinearLayoutManager
                val firstPosition = l.findFirstVisibleItemPosition()
                val lastPosition = l.findLastCompletelyVisibleItemPosition()
                when {
                    position > lastPosition -> {
                        binding.newappRecycle.smoothScrollToPosition(position)
                    }
                    position < firstPosition -> {
                        binding.newappRecycle.smoothScrollToPosition(position)
                    }
                    else -> {
                        val top = binding.newappRecycle.getChildAt(position - firstPosition).top
                        binding.newappRecycle.smoothScrollBy(0, top)
                    }
                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                //第一次进入页面不调用recyclerview滑动设置不走onTabselected
                onTabSelected(tab)
            }

        })
        viewModel.nAppList.observe(requireActivity()) {
            if (it.isSuccess) {
                val data = it.getOrNull()
                data?.let { dataList ->
                    mTitles.clear()
                    binding.newappTablayout.removeAllTabs()
                    mAppList.clear()
                    mAppList.addAll(dataList)
                    /*  if (!dataList.isNullOrEmpty()) {
                          if (dataList.first().categoryName != "常用应用") {
                              val often = NewAppRspJ()
                              often.categoryName = "常用应用"
                              dataList.add(0, often)
                          }
                      }*/
                    mNewAppAdapter?.setList(mAppList)
                    for (i in it.getOrNull()!!) {
                        mTitles.add(i.categoryName)
                        val newTab = binding.newappTablayout.newTab()
                        newTab.text = i.categoryName
                        binding.newappTablayout.addTab(newTab)
                    }
                }
            }
            binding.swipeRefreshLayout.isRefreshing = false

        }

    }

    private fun getData() {
        viewModel.getApplist()
    }


    override fun onRefresh() {
        getData()
        //refresh = true
        //EventBus.getDefault().post(EventMessage(BaseConstant.TYPE_UPDATE_APPCENTER_LIST, ""))
       // getData()
    }


}












