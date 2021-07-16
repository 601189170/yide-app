package com.yyide.chatim.activity.newnotice

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.TabHost
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.alibaba.fastjson.JSON
import com.blankj.utilcode.util.ToastUtils
import com.yyide.chatim.R
import com.yyide.chatim.activity.newnotice.fragment.NoticeUnreadPersonnelFragment
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.base.BaseMvpActivity
import com.yyide.chatim.databinding.ActivityNoticeUnConfirmListBinding
import com.yyide.chatim.model.EventMessage
import com.yyide.chatim.model.NoticeBlankReleaseBean
import com.yyide.chatim.model.ResultBean
import com.yyide.chatim.presenter.NoticeUnreadPresenter
import com.yyide.chatim.view.NoticeUnreadView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*
import androidx.viewpager.widget.ViewPager.OnPageChangeListener as OnPageChangeListener

/**
 * 收到的通知详情未确认人员列表
 * 2021年6月28日
 *
 */
class NoticeUnConfirmListActivity : BaseMvpActivity<NoticeUnreadPresenter>(), NoticeUnreadView {
    private lateinit var mViewBinding: ActivityNoticeUnConfirmListBinding
    private var id: Long = 0
    private var readCount: Int = 0
    private var isRead: Boolean = false

    companion object {
        fun start(context: Context, id: Long, readCount: Int, isRead: Boolean) {
            val intent = Intent(context, NoticeUnConfirmListActivity::class.java)
            intent.putExtra("id", id)
            intent.putExtra("readCount", readCount)
            intent.putExtra("isRead", isRead)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding = ActivityNoticeUnConfirmListBinding.inflate(layoutInflater);
        setContentView(mViewBinding.root)
        EventBus.getDefault().register(this)
        id = intent.getLongExtra("id", 0)
        readCount = intent.getIntExtra("readCount", 0)
        isRead = intent.getBooleanExtra("isRead", false)
        initView()
    }

    override fun getContentViewID(): Int {
        return R.layout.activity_notice_un_confirm_list
    }

    private fun initView() {
        if (isRead) {
            mViewBinding.top.title.setText(R.string.notice_un_confirm_title)
        } else {
            mViewBinding.top.title.setText(R.string.notice_un_read_title)
        }
        mViewBinding.top.backLayout.setOnClickListener { finish() }
        val mTitles: MutableList<String> = ArrayList()
        mTitles.add(getString(R.string.notice_tab_teacher))
        mTitles.add(getString(R.string.notice_tab_patriarch))
        mViewBinding.tvUnConfirmNumber.text = getString(R.string.notice_un_confirm_number, 0)
        mViewBinding.viewpager.offscreenPageLimit = 0
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
        mViewBinding.btnRemind.setOnClickListener {
            mvpPresenter?.unNoticeNotify(id)
        }
    }

    private var unCheckNumber: Int = 0

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun event(messageEvent: EventMessage) {
        if (BaseConstant.TYPE_NOTICE_UN_CONFIRM_NUMBER == messageEvent.code) {
            unCheckNumber += messageEvent.count
            if (isRead) {
                mViewBinding?.tvUnConfirmNumber?.text = getString(R.string.notice_un_confirm_number, unCheckNumber)
            } else {
                mViewBinding?.tvUnConfirmNumber?.text = getString(R.string.notice_un_read_number, unCheckNumber)
            }
        }
    }

    override fun createPresenter(): NoticeUnreadPresenter {
        return NoticeUnreadPresenter(this)
    }

    override fun showError() {
    }

    override fun pushNotice(model: ResultBean?) {
        if (model != null) {
            if (model.code == BaseConstant.REQUEST_SUCCES2) {
                ToastUtils.showShort("已提醒未确认人员")
                finish()
            }
        }
    }

    override fun getPushNoticeFail(msg: String?) {
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

}