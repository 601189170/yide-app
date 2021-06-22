package com.yyide.chatim.activity.newnotice

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.yyide.chatim.R
import com.yyide.chatim.activity.newnotice.fragment.NoticePersonnelFragment
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.databinding.ActivityNoticeUnConfirmListBinding
import java.util.ArrayList

class NoticeUnConfirmListActivity : BaseActivity() {
    private lateinit var mViewBinding: ActivityNoticeUnConfirmListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding = ActivityNoticeUnConfirmListBinding.inflate(layoutInflater);
        setContentView(mViewBinding.root)
        initView();
    }

    override fun getContentViewID(): Int {
        TODO("Not yet implemented")
        R.layout.activity_notice_un_confirm_list
    }

    private fun initView() {
        mViewBinding!!.top.title.setText(R.string.notice_un_confirm_title)
        mViewBinding!!.top.backLayout.setOnClickListener { v: View? -> finish() }
        val mTitles: MutableList<String> = ArrayList()
        mTitles.add(getString(R.string.notice_tab_teacher))
        mTitles.add(getString(R.string.notice_tab_patriarch))
        mTitles.add(getString(R.string.notice_tab_class_card))
        mViewBinding!!.viewpager.offscreenPageLimit = 3
        mViewBinding!!.viewpager.adapter = object : FragmentPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            override fun getItem(position: Int): Fragment {
                val fragment: Fragment = NoticePersonnelFragment.newInstance()
                when (position) {
                    0, 1, 2 -> {
                    }
                }
                return fragment
            }

            override fun getCount(): Int {
                return mTitles.size
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return mTitles[position]
            }
        }
        mViewBinding!!.slidingTabLayout.setViewPager(mViewBinding!!.viewpager)
        mViewBinding!!.slidingTabLayout.currentTab = 0
    }
}