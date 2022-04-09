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
import com.yyide.chatim.model.message.EventMessageBean
import com.yyide.chatim.utils.*
import org.greenrobot.eventbus.EventBus

/**
 *
 * @author shenzhibin
 * @date 2022/4/6 17:14
 * @description 信息发布详情确认页面
 */
class NoticeContentActivity:KTBaseActivity<ActivityNoticeContentBinding>(ActivityNoticeContentBinding::inflate) {

    private lateinit var noticeData:AcceptMessageItem

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
        viewModel.getDetail(noticeData.receiveId,noticeData.contentType,noticeData.messType)
    }


    override fun initView() {
        super.initView()
        noticeData = intent.getParcelableExtra<AcceptMessageItem>("data") ?: AcceptMessageItem()
        if (noticeData.isConfirm || !noticeData.isNeedConfirm){
            binding.noticeContentSubmitBtn.hide()
        }
        if (noticeData.contentType == 1){
            binding.noticeContentIv.show()
            binding.noticeContentMv.hide()
        }else{
            binding.noticeContentIv.hide()
            binding.noticeContentMv.show()
        }



        viewModel.messageInfo.observe(this){

            val data = it.getOrNull() ?: return@observe
            binding.noticeContentTop.title.text = data.title

            val subStr = "${data.identityUserName}发布于${data.timerDate}"
            binding.noticeContentSubTv.text = subStr

            if (data.contentType == 1){
                GlideUtil.loadImage(this,GlideUtil.DataUrl(data.contentImg),binding.noticeContentIv)
            }else{
                binding.noticeContentMv.loadDataWithBaseURL(
                    null,
                    data.content,
                    "text/html",
                    "utf-8",
                    null
                )
            }
        }

        viewModel.confirmInfo.observe(this){
            hideLoading()
            val result = it.getOrNull()
            if (result == null){
                showShotToast("确认失败")
                return@observe
            }
            noticeData.isConfirm = true
            EventBus.getDefault().post(EventMessageBean(0,noticeData))
            binding.noticeContentSubmitBtn.hide()
        }

    }

    fun initListener(){
        binding.noticeContentTop.backLayout.setOnClickListener {
            finish()
        }

        binding.noticeContentSubmitBtn.setOnClickListener {
            showLoading()
            viewModel.confirmDetail(noticeData.receiveId)
        }

    }



}