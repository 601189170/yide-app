package com.yyide.chatim.activity.newnotice

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.yyide.chatim.R
import com.yyide.chatim.activity.newnotice.fragment.NoticePersonnelFragment
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.databinding.ActivityNoticeReleasePersonnelBinding
import java.util.*

class NoticeDesignatedPersonnelActivity : BaseActivity() {
    private var personnelBinding: ActivityNoticeReleasePersonnelBinding? = null
    override fun getContentViewID(): Int {
        return R.layout.activity_notice_release_personnel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        personnelBinding = ActivityNoticeReleasePersonnelBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_notice_release_personnel)
        initView()
    }

    private fun initView() {
        personnelBinding!!.top.title.setText(R.string.notice_designated_personnel_title)
        personnelBinding!!.top.backLayout.setOnClickListener { v: View? -> finish() }
        val mTitles: MutableList<String> = ArrayList()
        mTitles.add(getString(R.string.notice_tab_teacher))
        mTitles.add(getString(R.string.notice_tab_patriarch))
        mTitles.add(getString(R.string.notice_tab_class_card))
        personnelBinding!!.viewpager.offscreenPageLimit = 3
        personnelBinding!!.viewpager.adapter = object : FragmentPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
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
        personnelBinding!!.slidingTabLayout.setViewPager(personnelBinding!!.viewpager)
        personnelBinding!!.slidingTabLayout.currentTab = 0
    }
}