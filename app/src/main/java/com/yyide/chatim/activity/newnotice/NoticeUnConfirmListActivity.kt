package com.yyide.chatim.activity.newnotice

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.yyide.chatim.R
import com.yyide.chatim.activity.newnotice.fragment.NoticeUnreadPersonnelFragment
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.databinding.ActivityNoticeUnConfirmListBinding
import java.util.*

/**
 * 收到的通知详情未确认人员列表
 * 2021年6月28日
 *
 */
class NoticeUnConfirmListActivity : BaseActivity() {
    private lateinit var mViewBinding: ActivityNoticeUnConfirmListBinding
    private var id: Long = 0

    companion object {
        fun start(context: Context, id: Long) {
            val intent = Intent(context, NoticeUnConfirmListActivity::class.java)
            intent.putExtra("id", id)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding = ActivityNoticeUnConfirmListBinding.inflate(layoutInflater);
        setContentView(mViewBinding.root)
        initView()
        id = intent.getLongExtra("id", 0)
    }

    override fun getContentViewID(): Int {
        return R.layout.activity_notice_un_confirm_list
    }

    private fun initView() {
        mViewBinding.top.title.setText(R.string.notice_un_confirm_title)
        mViewBinding.top.backLayout.setOnClickListener { finish() }
        val mTitles: MutableList<String> = ArrayList()
        mTitles.add(getString(R.string.notice_tab_teacher))
        mTitles.add(getString(R.string.notice_tab_patriarch))
//        mTitles.add(getString(R.string.notice_tab_class_card))
        mViewBinding.viewpager.offscreenPageLimit = 3
        mViewBinding.viewpager.adapter = object : FragmentPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            override fun getItem(position: Int): Fragment {
                return NoticeUnreadPersonnelFragment.newInstance(id, position)
            }

            override fun getCount(): Int {
                return mTitles.size
            }

            override fun getPageTitle(position: Int): CharSequence {
                return mTitles[position]
            }
        }
        mViewBinding.slidingTabLayout.setViewPager(mViewBinding.viewpager)
        mViewBinding.slidingTabLayout.currentTab = 0
    }

}