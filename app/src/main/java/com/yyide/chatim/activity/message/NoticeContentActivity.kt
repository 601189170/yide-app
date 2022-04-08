package com.yyide.chatim.activity.message

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import androidx.activity.viewModels
import com.yyide.chatim.activity.message.viewmodel.NoticeContentViewModel
import com.yyide.chatim.base.KTBaseActivity
import com.yyide.chatim.databinding.ActivityNoticeContentBinding
import com.yyide.chatim.model.message.AcceptMessageItem
import com.yyide.chatim.utils.*

/**
 *
 * @author shenzhibin
 * @date 2022/4/6 17:14
 * @description 信息发布详情确认页面
 */
class NoticeContentActivity:KTBaseActivity<ActivityNoticeContentBinding>(ActivityNoticeContentBinding::inflate) {

    private var noticeData:AcceptMessageItem ?= null

    private val viewModel by viewModels<NoticeContentViewModel>()

    companion object{
        fun startGo(context: Context,item:AcceptMessageItem) {
            val intent = Intent(context, NoticeContentActivity::class.java)
            intent.putExtra("data",item)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initListener()
    }


    override fun initView() {
        super.initView()
        noticeData = intent.getParcelableExtra<AcceptMessageItem>("data")
        noticeData?.let {
            if (it.isConfirm){
                binding.noticeContentSubmitBtn.hide()
            }
            if (it.contentType == 1){
                binding.noticeContentIv.show()
                binding.noticeContentText.hide()
            }else{
                binding.noticeContentIv.hide()
                binding.noticeContentText.show()
            }
            viewModel.getDetail(it.receiveId,it.contentType,it.messType)
        }

        viewModel.messageInfo.observe(this){

            val data = it.getOrNull() ?: return@observe
            binding.noticeContentTop.title.text = data.title

            val subStr = "${data.source}发布于${data.timerDate}"
            binding.noticeContentSubTv.text = subStr

            if (data.contentType == 1){
                GlideUtil.loadImage(this,GlideUtil.DataUrl(data.contentImg),binding.noticeContentIv)
            }else{
                val str = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    Html.fromHtml(data.content,Html.FROM_HTML_MODE_LEGACY).toString()
                } else {
                    Html.fromHtml(data.content).toString()
                }
                binding.noticeContentText.text = str
            }
        }

        viewModel.confirmInfo.observe(this){
            val result = it.getOrNull()
            if (result == null){
                showShotToast("确认失败")
                return@observe
            }
            binding.noticeContentSubmitBtn.hide()
        }

    }

    fun initListener(){
        binding.noticeContentTop.backLayout.setOnClickListener {
            finish()
        }

        binding.noticeContentSubmitBtn.setOnClickListener {
            viewModel.confirmDetail(noticeData?.receiveId ?: 0)
        }

    }



}