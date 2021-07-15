package com.yyide.chatim.activity.newnotice

import android.os.Bundle
import android.view.*
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.yyide.chatim.R
import com.yyide.chatim.activity.newnotice.fragment.NoticeBrandPersonnelFragment
import com.yyide.chatim.activity.newnotice.fragment.NoticePersonnelFragment
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.databinding.ActivityNoticeReleasePersonnelBinding
import com.yyide.chatim.model.NoticeBlankReleaseBean
import java.util.*
import kotlin.collections.ArrayList

/**
 * 发布选择指定人员列表
 */
class NoticeDesignatedPersonnelActivity : BaseActivity() {
    private var personnelBinding: ActivityNoticeReleasePersonnelBinding? = null
    override fun getContentViewID(): Int {
        return R.layout.activity_notice_release_personnel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        personnelBinding = ActivityNoticeReleasePersonnelBinding.inflate(layoutInflater)
        setContentView(personnelBinding!!.root)
        initView()
    }

    private fun initView() {
        personnelBinding!!.top.title.setText(R.string.notice_designated_personnel_title)
        personnelBinding!!.top.backLayout.setOnClickListener { finish() }
        val listsBean: ArrayList<NoticeBlankReleaseBean.RecordListBean> = intent.getParcelableArrayListExtra("list")
        val isCheck = intent.getBooleanExtra("isCheck", false)
        val noticeDetail = intent.getBooleanExtra("noticeDetail", false)
        val mTitles: MutableList<String> = ArrayList()
        mTitles.add(getString(R.string.notice_tab_teacher))
        mTitles.add(getString(R.string.notice_tab_patriarch))
        mTitles.add(getString(R.string.notice_tab_class_card))
        var teacherList = ArrayList<NoticeBlankReleaseBean.RecordListBean.ListBean>()
        var patriarchList = ArrayList<NoticeBlankReleaseBean.RecordListBean.ListBean>()
        var brandList = ArrayList<NoticeBlankReleaseBean.RecordListBean.ListBean>()
        var type: String = ""
        //处理已选中的列表数据
        listsBean.forEach { item ->
            when (item.specifieType) {
                "0" -> {
                    teacherList = item.list as ArrayList<NoticeBlankReleaseBean.RecordListBean.ListBean>
                }
                "1" -> {
                    patriarchList = item.list as ArrayList<NoticeBlankReleaseBean.RecordListBean.ListBean>
                }
                "2", "3" -> {
                    brandList = item.list as ArrayList<NoticeBlankReleaseBean.RecordListBean.ListBean>
                    type = item.specifieType
                }
            }
        }

        personnelBinding!!.viewpager.offscreenPageLimit = 3
        personnelBinding!!.viewpager.adapter = object : FragmentPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            override fun getItem(position: Int): Fragment {
                var fragment: Fragment = NoticePersonnelFragment.newInstance(position.toString(), teacherList, isCheck, noticeDetail)
                when (position) {
                    0 -> {
                        fragment = NoticePersonnelFragment.newInstance(position.toString(), teacherList, isCheck, noticeDetail)
                    }
                    1 -> {
                        fragment = NoticePersonnelFragment.newInstance(position.toString(), patriarchList, isCheck, noticeDetail)
                    }
                    2 -> {
                        fragment = NoticeBrandPersonnelFragment.newInstance(type, brandList, isCheck, noticeDetail)
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