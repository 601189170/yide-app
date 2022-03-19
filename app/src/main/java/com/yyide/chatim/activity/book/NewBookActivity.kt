package com.yyide.chatim.activity.book

import android.content.Intent
import android.os.Bundle
import com.yyide.chatim.R
import com.yyide.chatim.SpData
import com.yyide.chatim.activity.book.fragment.BookPatriarchFragment
import com.yyide.chatim.activity.book.fragment.BookTeacherFragment
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.databinding.ActivityNewBookBinding
import com.yyide.chatim.model.GetUserSchoolRsp

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