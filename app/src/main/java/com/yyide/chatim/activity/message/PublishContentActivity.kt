package com.yyide.chatim.activity.message

import android.content.Context
import android.content.Intent
import android.text.Html
import androidx.activity.viewModels
import com.yyide.chatim.activity.message.viewmodel.NoticeContentViewModel
import com.yyide.chatim.activity.message.viewmodel.PublishContentViewModel
import com.yyide.chatim.base.KTBaseActivity
import com.yyide.chatim.databinding.ActivityPublishContentBinding
import com.yyide.chatim.model.message.AcceptMessageItem
import com.yyide.chatim.utils.GlideUtil
import com.yyide.chatim.utils.hide
import com.yyide.chatim.utils.show
import com.yyide.chatim.utils.showShotToast

/**
 *
 * @author shenzhibin
 * @date 2022/4/7 10:50
 * @description 信息发布-发布内容详情
 */
class PublishContentActivity :
    KTBaseActivity<ActivityPublishContentBinding>(ActivityPublishContentBinding::inflate) {


    private var noticeData: AcceptMessageItem? = null

    private val viewModel by viewModels<PublishContentViewModel>()

    companion object {
        fun startGo(context: Context, item: AcceptMessageItem) {
            val intent = Intent(context, PublishContentActivity::class.java)
            intent.putExtra("data", item)
            context.startActivity(intent)
        }
    }

    override fun initView() {
        super.initView()
        noticeData = intent.getParcelableExtra<AcceptMessageItem>("data")
        noticeData?.let {
            if (it.isRetract) {
                binding.publishContentReTopBtn.isEnabled = false
                binding.publishContentRevokeBtn.isEnabled = false
            }
            if (it.contentType == 1) {
                binding.publishContentIv.show()
                binding.publishContentText.hide()
            } else {
                binding.publishContentIv.hide()
                binding.publishContentText.show()
            }
            viewModel.getDetail(it.id, it.contentType, it.messType)
        }

        viewModel.messageInfo.observe(this) {

            val data = it.getOrNull() ?: return@observe
            binding.publishContentTop.title.text = data.title

            val subStr = "${data.source}发布于${data.timerDate}"
            binding.publishContentSubTv.text = subStr

            binding.publishContentSubEndTv.text = "浏览 ${data.viewUsers}"

            if (data.contentType == 1) {
                GlideUtil.loadImage(
                    this,
                    GlideUtil.DataUrl(data.contentImg),
                    binding.publishContentIv
                )
            } else {
                val str =
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        Html.fromHtml(data.content, Html.FROM_HTML_MODE_LEGACY).toString()
                    } else {
                        Html.fromHtml(data.content).toString()
                    }
                binding.publishContentText.text = str
            }
        }


    }

    fun initListener() {
        binding.publishContentTop.backLayout.setOnClickListener {
            finish()
        }

        binding.publishContentRevokeBtn.setOnClickListener {

        }

        binding.publishContentReTopBtn.setOnClickListener {

        }

    }

}