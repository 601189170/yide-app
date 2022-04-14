package com.yyide.chatim_pro.activity.book

import android.content.Intent
import android.os.Bundle
import com.yyide.chatim_pro.R
import com.yyide.chatim_pro.SpData
import com.yyide.chatim_pro.activity.book.fragment.BookPatriarchFragment
import com.yyide.chatim_pro.activity.book.fragment.BookTeacherFragment
import com.yyide.chatim_pro.base.BaseActivity
import com.yyide.chatim_pro.databinding.ActivityNewBookBinding

/**
 * 新版本通讯录
 * @author LRZ
 * @data  2021年8月20日o
 */
class NewBookActivity : BaseActivity() {

    private lateinit var mViewBinding: ActivityNewBookBinding
    private val TAG = NewBookActivity::class.java.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding = ActivityNewBookBinding.inflate(layoutInflater)
        setContentView(mViewBinding.root)
        initView()
    }

    override fun getContentViewID(): Int {
        return R.layout.activity_new_book
    }

    private fun initView() {
        mViewBinding.top.title.text = getString(R.string.book_title_yd)
        mViewBinding.top.backLayout.setOnClickListener { finish() }
        if (SpData.getIdentityInfo() != null && !SpData.getIdentityInfo().staffIdentity()) {
            supportFragmentManager.beginTransaction().replace(
                mViewBinding.flContent.id,
                BookPatriarchFragment.newInstance()
            ).commit()
        } else {
            supportFragmentManager.beginTransaction().replace(
                mViewBinding.flContent.id,
                BookTeacherFragment.newInstance()
            ).commit()

        }

        mViewBinding.clSearch.setOnClickListener {
            startActivity(Intent(this, BookSearchActivity::class.java))
        }
    }

}