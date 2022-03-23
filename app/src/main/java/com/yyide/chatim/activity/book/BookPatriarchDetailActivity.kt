package com.yyide.chatim.activity.book

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.tencent.imsdk.v2.V2TIMConversation
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo
import com.yyide.chatim.BaseApplication
import com.yyide.chatim.R
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.chat.ChatActivity
import com.yyide.chatim.databinding.ActivityBookPatriarchDetailBinding
import com.yyide.chatim.databinding.ActivityPersonInfoBinding
import com.yyide.chatim.model.BookGuardianItem
import com.yyide.chatim.utils.Constants
import com.yyide.chatim.utils.GlideUtil
import com.yyide.chatim.utils.RxPermissionUtils
import com.yyide.chatim.utils.Utils

class BookPatriarchDetailActivity : BaseActivity() {

    private lateinit var viewBinding: ActivityBookPatriarchDetailBinding
//    activity_person_info
//    private lateinit var viewBinding: ActivityPersonInfoBinding
    private var type: Int = 1

    companion object {
        @JvmStatic
        fun start(context: Context, guardianItem: BookGuardianItem) {
            val intent = Intent(context, BookPatriarchDetailActivity::class.java)
            intent.putExtra("guardianItem", guardianItem)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityBookPatriarchDetailBinding.inflate(layoutInflater)
//        viewBinding = ActivityPersonInfoBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        initView()
    }

    override fun getContentViewID(): Int {
        return R.layout.activity_book_patriarch_detail
    }

    private fun initView() {
        val guardianItem: BookGuardianItem =
            intent.getSerializableExtra("guardianItem") as BookGuardianItem
        GlideUtil.loadImageHead(
            this,
            guardianItem.faceInformation,
            viewBinding.ivHead
        )
        viewBinding.top.backLayout.setOnClickListener { finish() }
        viewBinding.top.title.text = getString(R.string.book_title_info_yd)
//        viewBinding.tvName.text = guardianItem.name
        viewBinding.name.text = guardianItem.name
        viewBinding.tvFamilyRelations.text = guardianItem.getRelation()
        viewBinding.tvSingleGuardianship.text = if (guardianItem.singleParent == "1") "是" else "否"
        viewBinding.ivPhone.setOnClickListener {
            RxPermissionUtils.callPhone(this, guardianItem.phone)
        }

        if (TextUtils.isEmpty(guardianItem.phone))
            viewBinding.phone.text = "暂无该学生的手机号码"
        else
            viewBinding.phone.text = Utils.setHideMobile(guardianItem.phone)

        viewBinding.set.text = "显示"
        viewBinding.set.setOnClickListener { v: View? ->
            if (type == 1) {
                type = 2
                viewBinding.set.text = "隐藏"
                viewBinding.phone.text = guardianItem.phone
            } else {
                type = 1
                viewBinding.set.text = "显示"
                viewBinding.phone.text = Utils.setHideMobile(guardianItem.phone)
            }
        }
        viewBinding.clMessage.setOnClickListener {
            val chatInfo = ChatInfo()
            chatInfo.type = V2TIMConversation.V2TIM_C2C
            chatInfo.id = guardianItem.userId
            chatInfo.chatName = guardianItem.name
            val intent = Intent(BaseApplication.getInstance(), ChatActivity::class.java)
            intent.putExtra(Constants.CHAT_INFO, chatInfo)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            BaseApplication.getInstance().startActivity(intent)
        }
    }

}