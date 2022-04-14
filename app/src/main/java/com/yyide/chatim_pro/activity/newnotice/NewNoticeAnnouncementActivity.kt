package com.yyide.chatim_pro.activity.newnotice

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.yyide.chatim_pro.R
import com.yyide.chatim_pro.activity.newnotice.fragment.NoticeMyReceivedFragment
import com.yyide.chatim_pro.activity.newnotice.fragment.NoticeMyReleaseFragment
import com.yyide.chatim_pro.activity.newnotice.fragment.NoticeTemplateReleaseFragment
import com.yyide.chatim_pro.base.BaseActivity
import com.yyide.chatim_pro.base.BaseConstant
import com.yyide.chatim_pro.databinding.ActivityNoticeBinding
import com.yyide.chatim_pro.model.*
import com.yyide.chatim_pro.net.ApiCallback
import io.reactivex.rxjava3.disposables.Disposable
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

/**
 * 通知主页
 */
class NewNoticeAnnouncementActivity : BaseActivity() {

    private var noticeBinding: ActivityNoticeBinding? = null
    val mTitles: MutableList<String> = ArrayList()

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
        noticeBinding!!.top.backLayout.setOnClickListener { finish() }
        getPermission()
    }

    private fun initViewpager() {
        noticeBinding!!.viewpager.offscreenPageLimit = 3
        noticeBinding!!.viewpager.adapter = object : FragmentPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            override fun getItem(position: Int): Fragment {
                var fragment: Fragment = NoticeMyReceivedFragment.newInstance()
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

            override fun getPageTitle(position: Int): CharSequence {
                return mTitles[position]
            }
        }
        noticeBinding!!.slidingTabLayout.setViewPager(noticeBinding!!.viewpager)
        noticeBinding!!.slidingTabLayout.currentTab = 0
    }

    private fun getPermission() {
        addSubscription(mDingApiStores.noticePermission(), object : ApiCallback<NoticePermissionBean?>() {
            override fun onSuccess(model: NoticePermissionBean?) {
                if (model != null) {
                    if (model.code == BaseConstant.REQUEST_SUCCESS) {
                        if (!model.data.permission) {
                            mTitles.add(getString(R.string.notice_tab_my_received))
                            noticeBinding!!.slidingTabLayout.visibility = View.GONE
                        } else {
                            mTitles.add(getString(R.string.notice_tab_my_received))
                            mTitles.add(getString(R.string.notice_tab_my_push))
                            mTitles.add(getString(R.string.notice_tab_push))
                        }
                        initViewpager()
                    }
                }
            }

            override fun onFailure(msg: String) {
                Log.e("onFailure", "---findTargetSnapPosition---")
            }

            override fun onFinish() {
                hideLoading()
            }

            override fun onSubscribe(d: Disposable?) {
                addSubscription(d)
            }

        })
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