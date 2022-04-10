package com.yyide.chatim.activity.face

import android.content.Intent
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.SpData
import com.yyide.chatim.base.KTBaseActivity
import com.yyide.chatim.databinding.ActivityFaceMultBinding
import com.yyide.chatim.databinding.ItemMultipleStudentBinding
import com.yyide.chatim.model.FaceStudentBean
import com.yyide.chatim.viewmodel.gate.PushSettingViewModel

/**
 * 多学员列表
 */
class FaceMultipleActivity :
    KTBaseActivity<ActivityFaceMultBinding>(ActivityFaceMultBinding::inflate) {
    private val faceViewModel: FaceViewModel by viewModels()
    override fun initView() {
        super.initView()
        binding.layoutTop.title.text = getString(R.string.title_student_info)
        binding.layoutTop.backLayout.setOnClickListener { finish() }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = mAdapter

        faceViewModel.faceStudentListViewModel.observe(this) {
            mAdapter.setList(it)
        }

        SpData.getIdentityInfo().let {
            faceViewModel.getFaceList("${it.id}")
        }
    }

    private val mAdapter =
        object : BaseQuickAdapter<FaceStudentBean, BaseViewHolder>(R.layout.item_multiple_student) {
            override fun convert(holder: BaseViewHolder, item: FaceStudentBean) {
                val bind = ItemMultipleStudentBinding.bind(holder.itemView)
                bind.tvName.text = item.name
                //0男 1女
                bind.tvDesc.text = "${if ("1" == item.gender) "男" else "女"}/${item.className}"
                if (item.isGather) {
                    bind.tvGather.text = "采集成功"
                    bind.tvGather.setCompoundDrawablesWithIntrinsicBounds(
                        context.resources.getDrawable(
                            R.mipmap.icon_gather
                        ), null, null, null
                    )
                    bind.ctCommit.isChecked = true
                    bind.ctCommit.text = "重新采集"
                } else {
                    bind.ctCommit.text = "点击采集"
                    bind.tvGather.text = "未采集"
                    bind.ctCommit.isChecked = false
                    bind.tvGather.setCompoundDrawablesWithIntrinsicBounds(
                        context.resources.getDrawable(
                            R.mipmap.icon_un_gather
                        ), null, null, null
                    )
                }
                bind.ctCommit.setOnClickListener {
                    val intent = Intent(context, FaceCaptureActivity::class.java)
                    intent.putExtra("studentId", "${item.id}")
                    startActivity(intent)
                }
            }

        }
}