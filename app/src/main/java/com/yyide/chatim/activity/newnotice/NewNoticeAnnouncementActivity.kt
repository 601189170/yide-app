package com.yyide.chatim.activity.newnotice

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.yyide.chatim.R
import com.yyide.chatim.activity.newnotice.fragment.NoticeMyReceivedFragment
import com.yyide.chatim.activity.newnotice.fragment.NoticeMyReleaseFragment
import com.yyide.chatim.activity.newnotice.fragment.NoticeReleaseFragment
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.databinding.ActivityNoticeBinding
import java.util.*

class NewNoticeAnnouncementActivity : BaseActivity() {
    private var noticeBinding: ActivityNoticeBinding? = null
    override fun getContentViewID(): Int {
        return R.layout.activity_notice
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noticeBinding = ActivityNoticeBinding.inflate(layoutInflater)
        setContentView(noticeBinding!!.root)
        initView()
    }

    private fun initView() {
        noticeBinding!!.top.title.setText(R.string.notice_announcement_title)
        noticeBinding!!.top.backLayout.setOnClickListener { v: View? -> finish() }
        val mTitles: MutableList<String> = ArrayList()
        mTitles.add(getString(R.string.notice_tab_my_received))
        mTitles.add(getString(R.string.notice_tab_my_push))
        mTitles.add(getString(R.string.notice_tab_push))
        noticeBinding!!.viewpager.offscreenPageLimit = 3
        noticeBinding!!.viewpager.adapter = object : FragmentPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            override fun getItem(position: Int): Fragment {
                var fragment: Fragment = NoticeMyReceivedFragment.Companion.newInstance()
                when (position) {
                    0 -> fragment = NoticeMyReceivedFragment.Companion.newInstance()
                    1 -> fragment = NoticeMyReleaseFragment.Companion.newInstance()
                    2 -> fragment = NoticeReleaseFragment.Companion.newInstance()
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
        noticeBinding!!.slidingTabLayout.setViewPager(noticeBinding!!.viewpager)
        noticeBinding!!.slidingTabLayout.currentTab = 0
    }
}