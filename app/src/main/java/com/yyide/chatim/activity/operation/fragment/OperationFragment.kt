package com.yyide.chatim.activity.operation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.flyco.tablayout.listener.OnTabSelectListener
import com.yyide.chatim.R
import com.yyide.chatim.activity.operation.viewmodel.OperationViewModel
import com.yyide.chatim.databinding.OperationFragmentBinding

/**
 * @Desc 作业-家长
 * @Data 2022年2月17日13:35:32
 * @Author lrz
 */
class OperationFragment : Fragment() {

    companion object {
        private val mFragments = ArrayList<Fragment>()
        private val mTitles = arrayOf("作业数据", "作业图表")
        fun newInstance() = OperationFragment()
    }

    private lateinit var viewModel: OperationViewModel
    private lateinit var viewBinding: OperationFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = OperationFragmentBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(OperationViewModel::class.java)
        initView()
    }

    private fun initView() {
        viewBinding.top.title.text = getString(R.string.operation_title)
        viewBinding.top.backLayout.setOnClickListener { activity?.finish() }
        viewBinding.top.ivEdit.setOnClickListener { }
        viewBinding.top.ivRight.setOnClickListener { }
        viewBinding.tvClassName.setOnClickListener { }

        viewBinding.layoutTime.tvToDay.setOnClickListener {
            if (!viewBinding.layoutTime.tvToDay.isChecked) {
                setCheck()
                viewBinding.layoutTime.tvToDay.isChecked = true
            }
        }
        viewBinding.layoutTime.tvWeek.setOnClickListener {
            if (!viewBinding.layoutTime.tvWeek.isChecked) {
                setCheck()
                viewBinding.layoutTime.tvWeek.isChecked = true
            }
        }
        viewBinding.layoutTime.tvLastWeek.setOnClickListener {
            if (!viewBinding.layoutTime.tvLastWeek.isChecked) {
                setCheck()
                viewBinding.layoutTime.tvLastWeek.isChecked = true
            }
        }
        viewBinding.layoutTime.tvMonth.setOnClickListener {
            if (!viewBinding.layoutTime.tvMonth.isChecked) {
                setCheck()
                viewBinding.layoutTime.tvMonth.isChecked = true
            }
        }
        viewBinding.layoutTime.tvLastMonth.setOnClickListener {
            if (!viewBinding.layoutTime.tvLastMonth.isChecked) {
                setCheck()
                viewBinding.layoutTime.tvLastMonth.isChecked = true
            }
        }
    }

    private fun setCheck() {
        viewBinding.layoutTime.tvToDay.isChecked = false
        viewBinding.layoutTime.tvWeek.isChecked = false
        viewBinding.layoutTime.tvLastWeek.isChecked = false
        viewBinding.layoutTime.tvMonth.isChecked = false
        viewBinding.layoutTime.tvLastMonth.isChecked = false
    }

    private fun tl_3() {
        val viewPager: ViewPager = viewBinding.viewpager
        viewPager.adapter = MyPagerAdapter(childFragmentManager)
        viewBinding.mTabLayout.setTabData(mTitles)
        viewBinding.mTabLayout.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                viewPager.currentItem = position
            }

            override fun onTabReselect(position: Int) {}
        })
        viewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                viewBinding.mTabLayout.currentTab = position
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        viewPager.currentItem = 1
    }

    private class MyPagerAdapter(fm: FragmentManager?) :
        FragmentPagerAdapter(fm!!) {
        override fun getCount(): Int {
            return mFragments.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mTitles.get(position)
        }

        override fun getItem(position: Int): Fragment {
            return mFragments.get(position)
        }
    }

}