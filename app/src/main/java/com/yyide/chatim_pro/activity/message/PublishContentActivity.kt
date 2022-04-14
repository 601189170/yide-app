package com.yyide.chatim_pro.activity.message

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.yyide.chatim_pro.R
import com.yyide.chatim_pro.activity.message.viewmodel.PublishContentViewModel
import com.yyide.chatim_pro.base.KTBaseActivity
import com.yyide.chatim_pro.databinding.ActivityPublishContentBinding
import com.yyide.chatim_pro.model.message.AcceptMessageItem
import com.yyide.chatim_pro.utils.*

/**
 *
 * @author shenzhibin
 * @date 2022/4/7 10:50
 * @description 信息发布-发布内容详情
 */
class PublishContentActivity :
    KTBaseActivity<ActivityPublishContentBinding>(ActivityPublishContentBinding::inflate) {


    private lateinit var noticeData: AcceptMessageItem
    private val viewModel by viewModels<PublishContentViewModel>()
    private var isTop = false

    companion object {
        fun startGo(context: Context, item: AcceptMessageItem) {
            val intent = Intent(context, PublishContentActivity::class.java)
            intent.putExtra("data", item)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initListener()
        viewModel.getDetail(noticeData.id, noticeData.contentType, noticeData.messType)
    }

    override fun initView() {
        super.initView()
        noticeData = intent.getParcelableExtra<AcceptMessageItem>("data") ?: AcceptMessageItem()
        if (noticeData.isRetract) {
            binding.publishContentRevokeBtn.isEnabled = false
            binding.publishContentRevokeBtn.text = getString(R.string.already_reBack)
        }

        isTop = noticeData.isTop
        if (noticeData.isTop) {
            binding.publishContentReTopBtn.text = getString(R.string.cancel_reTop)
        }
        if (noticeData.contentType == 1) {
            binding.publishContentIv.show()
            binding.publishContentWv.hide()
        } else {
            binding.publishContentIv.hide()
            binding.publishContentWv.show()
        }

        binding.publishContentRangeTv.text = noticeData.notifyUsersInfo
        binding.publishContentPeopleTv.text = "${noticeData.confirmUsers}/${noticeData.receiveUsers}"

        viewModel.messageInfo.observe(this) {

            val data = it.getOrNull() ?: return@observe

            if (!data.isNeedConfirm){
                binding.notifyRangeLine.hide()
                binding.publishContentNotifyPeopleCl.hide()
            }

            binding.publishContentTop.title.text = data.title

            val subStr = "${data.identityUserName}发布于${data.timerDate}"
            binding.publishContentSubTv.text = subStr

            binding.publishContentSubEndTv.text = "浏览 ${data.viewUsers}"



            if (data.contentType == 1) {
                Glide.with(this).load(GlideUtil.DataUrl(data.contentImg)).centerInside().into(binding.publishContentIv)
            } else {
                binding.publishContentWv.loadDataWithBaseURL(
                    null,
                    data.content,
                    "text/html",
                    "utf-8",
                    null
                )
            }
        }


        viewModel.reTopResult.observe(this) {
            val data = it.getOrNull() ?: return@observe
            if (data) {
                if (isTop) {
                    binding.publishContentReTopBtn.text = getString(R.string.reTop)
                    isTop = false
                } else {
                    binding.publishContentReTopBtn.text = getString(R.string.cancel_reTop)
                    isTop = true
                }
            }
            noticeData.isTop = isTop
        }

        viewModel.revokeResult.observe(this){
            noticeData.isRetract = true
            binding.publishContentRevokeBtn.isEnabled = false
            binding.publishContentRevokeBtn.text = getString(R.string.already_reBack)
        }


    }

    fun initListener() {
        binding.publishContentTop.backLayout.setOnClickListener {
            finish()
        }

        binding.publishContentRevokeBtn.setOnClickListener {
            //viewModel.revokePublishMessage(noticeData.id)
        }

        binding.publishContentReTopBtn.setOnClickListener {
            //viewModel.reTopPublishMessage(noticeData.id)
        }

        binding.publishContentNotifyRangeCl.setOnClickListener {
            MessageNotifyActivity.startGo(this, noticeData.id )
        }

        binding.publishContentNotifyPeopleCl.setOnClickListener {

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding.publishContentWv.destroy()
    }

}