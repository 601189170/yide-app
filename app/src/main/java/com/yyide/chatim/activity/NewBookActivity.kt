package com.yyide.chatim.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.databinding.ActivityNewBookBinding
import com.yyide.chatim.databinding.ItemNewBookBinding

/**
 * 新版本通讯录
 * @author LRZ
 * @data  2021年8月20日
 */
class NewBookActivity : BaseActivity() {

    private lateinit var mViewBinding: ActivityNewBookBinding

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
        //通讯录列表
        mViewBinding.bookStaffList.layoutManager = LinearLayoutManager(this)
        mViewBinding.bookStaffList.adapter = staffAdapter
//        staffAdapter.setEmptyView(R.layout.empty)
        staffAdapter.setList(setData())

        mViewBinding.bookClassesList.layoutManager = LinearLayoutManager(this)
        mViewBinding.bookClassesList.adapter = classesAdapter
        classesAdapter.setList(setData())
    }

    private fun setData(): List<String> {
        val dataList = mutableListOf<String>()
        for (i in 1..10) {
            dataList.add("李老师${i}")
        }
        return dataList
    }

    private val staffAdapter =
        object : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_new_book) {
            override fun convert(holder: BaseViewHolder, item: String) {
                holder.setText(R.id.name, item)
            }
        }

    private val classesAdapter =
        object : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_new_book) {
            override fun convert(holder: BaseViewHolder, item: String) {
                holder.setText(R.id.name, item)
            }
        }
}