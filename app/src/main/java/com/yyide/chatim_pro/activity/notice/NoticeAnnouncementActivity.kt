package com.yyide.chatim_pro.activity.notice

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import com.tencent.mmkv.MMKV
import com.yyide.chatim_pro.R
import com.yyide.chatim_pro.SpData
import com.yyide.chatim_pro.activity.notice.fragment.NoticeAnnouncementListFragment
import com.yyide.chatim_pro.activity.notice.fragment.PublishNoticAnnouncementListFragment
import com.yyide.chatim_pro.base.BaseConstant
import com.yyide.chatim_pro.base.BaseMvpFragment
import com.yyide.chatim_pro.base.KTBaseActivity
import com.yyide.chatim_pro.databinding.ActivityNoticeAnnouncementBinding
import com.yyide.chatim_pro.model.EventMessage
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 通知公告
 * author lrz
 * time 2021/03/24
 */
class NoticeAnnouncementActivity :
    KTBaseActivity<ActivityNoticeAnnouncementBinding>(ActivityNoticeAnnouncementBinding::inflate) {
    private var hasNoticePermission = false
    private var mTitles = mutableListOf<String>()

    var fragments: MutableList<BaseMvpFragment<*>> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
        hasNoticePermission = MMKV.defaultMMKV().decodeBool("hasNoticePermission", false)
        initViewPager()
    }

    override fun initView() {
        super.initView()
        binding.top.title.setText(R.string.notice_announcement_title)
        binding.top.backLayout.setOnClickListener { finish() }
        binding.fab.setOnClickListener {
            startActivity(Intent(this, NoticeTemplateActivity::class.java))
        }

        mTitles.add("通知公告")
        mTitles.add("新闻资讯")
        mTitles.add("校园风采")
        mTitles.add("失物招领")
    }

    private fun initViewPager() {
        //家长身份不能添加公告
        if (!SpData.getIdentityInfo().staffIdentity() || !hasNoticePermission) {
            binding.fab.visibility = View.GONE
            binding.slidingTabLayout.visibility = View.GONE
        }
        //我的通知
        val my_notice = NoticeAnnouncementListFragment.newInstance("my_notice")
        fragments.add(my_notice)
        if (SpData.getIdentityInfo().staffIdentity() && hasNoticePermission) {
            //我的发布
            val my_release = PublishNoticAnnouncementListFragment.newInstance("my_release")
            fragments.add(my_release)
        }

        binding.viewpager.offscreenPageLimit = fragments.size
        //需要动态数据设置该tab
        binding.viewpager.adapter = object :
            FragmentStatePagerAdapter(
                supportFragmentManager,
                BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
            ) {
            override fun getItem(position: Int): Fragment {
                return fragments[position]
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

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun Event(messageEvent: EventMessage) {
        if (BaseConstant.TYPE_UPDATE_NOTICE_TAB == messageEvent.code) {
            binding.viewpager.currentItem = 1
        }
    }
}