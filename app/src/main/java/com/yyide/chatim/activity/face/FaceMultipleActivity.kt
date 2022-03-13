package com.yyide.chatim.activity.face

import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.base.KTBaseActivity
import com.yyide.chatim.databinding.ActivityFaceMultBinding

/**
 * 多学员列表
 */
class FaceMultipleActivity :
    KTBaseActivity<ActivityFaceMultBinding>(ActivityFaceMultBinding::inflate) {

    override fun initView() {
        super.initView()
        binding.layoutTop.title.text = getString(R.string.title_student_info)
        binding.layoutTop.backLayout.setOnClickListener { finish() }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = mAdapter
    }

    private val mAdapter =
        object : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_multiple_student) {
            override fun convert(holder: BaseViewHolder, item: String) {
            }

        }
}