package com.yyide.chatim.activity.operation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.ViewModelProvider
import com.alibaba.fastjson.JSON
import com.yyide.chatim.R
import com.yyide.chatim.activity.operation.fragment.OperationChildFragment
import com.yyide.chatim.activity.operation.viewmodel.OperationTearcherViewModel
import com.yyide.chatim.activity.operation.viewmodel.OperationViewModel
import com.yyide.chatim.base.KTBaseActivity
import com.yyide.chatim.databinding.ActivityOperationDetailBinding
import com.yyide.chatim.model.BookStudentItem
import com.yyide.chatim.model.Detail

class OperationDetailActivity :
    KTBaseActivity<ActivityOperationDetailBinding>(ActivityOperationDetailBinding::inflate) {
    private lateinit var viewModel: OperationTearcherViewModel
    private lateinit var viewBinding: ActivityOperationDetailBinding
    var id=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(OperationTearcherViewModel::class.java)
        setContentView(viewBinding.root)
        initView()
        initViewPager()

        id = intent.getStringExtra("id")
        viewModel.getSchoolWorkData(id)
        viewModel.getSchoolWorkRsp.observe(this){
            if (it.isSuccess){
                val result = it.getOrNull()
                if (result!=null){
                    Log.e("TAG", "getSchoolWorkRsp: "+JSON.toJSONString(result))
                }
            }

        }

    }

    override fun initView() {
        binding.top.title.text = getString(R.string.operation_detail_title)
        binding.top.backLayout.setOnClickListener { finish() }
    }

    private fun initViewPager() {
        val mTitles: MutableList<String> = ArrayList()
        mTitles.add("已反馈")
        mTitles.add("未反馈")
        mTitles.add("已完成")
        mTitles.add("未读")
        binding.viewpager.offscreenPageLimit = mTitles.size
        //需要动态数据设置该tab
        binding.viewpager.adapter = object :
            FragmentStatePagerAdapter(
                supportFragmentManager,
                BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
            ) {
            override fun getItem(position: Int): Fragment {
                return OperationChildFragment.newInstance()
            }

            override fun getCount(): Int {
                return mTitles.size
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return mTitles[position]
            }
        }
        binding.slidingTabLayout.setViewPager(binding.viewpager)
        binding.slidingTabLayout.currentTab = 0
        binding.slidingTabLayout.notifyDataSetChanged()

    }
}