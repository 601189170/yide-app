package com.yyide.chatim.activity.newnotice

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.blankj.utilcode.util.ActivityUtils
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationManagerKit
import com.yyide.chatim.MainActivity
import com.yyide.chatim.R
import com.yyide.chatim.activity.newnotice.fragment.NoticeMyReceivedFragment
import com.yyide.chatim.activity.newnotice.fragment.NoticeMyReleaseFragment
import com.yyide.chatim.activity.newnotice.fragment.NoticeTemplateReleaseFragment
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.databinding.ActivityNoticeBinding
import com.yyide.chatim.model.EventMessage
import com.yyide.chatim.model.GetAppVersionResponse
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

/**
 * 通知主页
 */
class NewNoticeAnnouncementActivity : BaseActivity() {
    private var noticeBinding: ActivityNoticeBinding? = null
    override fun getContentViewID(): Int {
        return R.layout.activity_notice
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
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
                    0 -> fragment = NoticeMyReceivedFragment.newInstance()
                    1 -> fragment = NoticeMyReleaseFragment.newInstance()
                    2 -> fragment = NoticeTemplateReleaseFragment.newInstance()
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun event(messageEvent: EventMessage) {
        if (BaseConstant.TYPE_NOTICE_PUSH_BLANK == messageEvent.code) {
            noticeBinding!!.viewpager.currentItem = 1
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}