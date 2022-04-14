package com.yyide.chatim_pro.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.yyide.chatim_pro.base.BaseActivity
import com.yyide.chatim_pro.fragment.GuidePageFragment
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.blankj.utilcode.util.SPUtils
import com.yyide.chatim_pro.R
import com.yyide.chatim_pro.base.BaseConstant
import com.yyide.chatim_pro.databinding.ActivityGuidePageBinding
import java.util.ArrayList

class GuidePageActivity : BaseActivity() {
    private var pageBinding: ActivityGuidePageBinding? = null
    private val fragments: MutableList<Fragment> = ArrayList()
    override fun getContentViewID(): Int {
        return R.layout.activity_guide_page
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageBinding = ActivityGuidePageBinding.inflate(layoutInflater)
        setContentView(pageBinding!!.root)
        initViewPager()
    }

    private fun initViewPager() {
        fragments.add(GuidePageFragment.newInstance(0))
        fragments.add(GuidePageFragment.newInstance(1))
        fragments.add(GuidePageFragment.newInstance(2))
        fragments.add(GuidePageFragment.newInstance(3))
        pageBinding!!.viewpager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        pageBinding!!.viewpager.adapter = object : FragmentStateAdapter(this) {
            override fun createFragment(position: Int): Fragment {
                return fragments[position]
            }

            override fun getItemCount(): Int {
                return if (fragments.isEmpty()) 0 else fragments.size
            }
        }
    }

    override fun onBackPressed() {
        val firstOpenApp = SPUtils.getInstance().getBoolean(BaseConstant.FIRST_OPEN_APP, true)
        if (firstOpenApp) {
            val intent = intent
            intent.putExtra("interrupt", true)
            setResult(RESULT_OK, intent)
            finish()
        } else {
            super.onBackPressed()
        }
    }
}