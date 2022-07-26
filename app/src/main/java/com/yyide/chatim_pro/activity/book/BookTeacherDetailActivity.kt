package com.yyide.chatim_pro.activity.book

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.tencent.imsdk.v2.V2TIMConversation
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo
import com.yyide.chatim_pro.R
import com.yyide.chatim_pro.base.BaseActivity
import com.yyide.chatim_pro.chat.ChatActivity
import com.yyide.chatim_pro.databinding.ActivityBookTeacherDetailBinding
import com.yyide.chatim_pro.model.BookTeacherItem
import com.yyide.chatim_pro.utils.*

class BookTeacherDetailActivity : BaseActivity() {

    private lateinit var viewBinding: ActivityBookTeacherDetailBinding
    private var type: Int = 1

    companion object {
        @JvmStatic
        fun start(context: Context, teacher: BookTeacherItem) {
            val intent = Intent(context, BookTeacherDetailActivity::class.java)
            intent.putExtra("teacher", teacher)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityBookTeacherDetailBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        initView()
    }

    override fun getContentViewID(): Int {
        return R.layout.activity_book_teacher_detail
    }

    private fun initView() {
        val teacher: BookTeacherItem = intent.getSerializableExtra("teacher") as BookTeacherItem
//        GlideUtil.loadImageHead(
//            this,
//            teacher.faceInformation,
//            viewBinding.ivHead
//        )
        GlideUtil.loadImageHead(
                this,
                teacher.avatar,
                viewBinding.ivHead
        )

        if ("1" == teacher.whitelist) {
            viewBinding.ivPhone.visibility = View.GONE
            viewBinding.set.visibility = View.GONE
        } else {
            viewBinding.ivPhone.visibility = View.VISIBLE
            viewBinding.set.visibility = View.VISIBLE
        }
        viewBinding.top.backLayout.setOnClickListener { finish() }
        viewBinding.top.title.text = getString(R.string.book_title_info_yd)
        viewBinding.tvName.text = teacher.name
        viewBinding.name.text = teacher.name
//        viewBinding.sex.text = if (teacher.gender == "1") "男" else "女"

        if (teacher.gender == "1"){
            viewBinding.sex.text="男"
        }else if (teacher.gender == "0"){
            viewBinding.sex.text="女"
        }else{
            viewBinding.sex.text="未知"
        }
        viewBinding.phone.text = Utils.setHideMobile(teacher.phone)
        viewBinding.email.text = if (TextUtils.isEmpty(teacher.email)) "无" else teacher.email
        viewBinding.subjectName.text =
            if (!TextUtils.isEmpty(teacher.subjectName)) teacher.subjectName else "暂无"
        viewBinding.ivPhone.setOnClickListener {
            RxPermissionUtils.callPhone(this, teacher.phone)
        }
        viewBinding.set.text = "显示"
        viewBinding.set.setOnClickListener { v: View? ->
            if (type == 1) {
                type = 2
                viewBinding.set.text = "隐藏"
                viewBinding.phone.text = teacher.phone
            } else {
                type = 1
                viewBinding.set.text = "显示"
                viewBinding.phone.text = Utils.setHideMobile(teacher.phone)
            }
        }
        viewBinding.clMessage.setOnClickListener {
            val chatInfo = ChatInfo()
            chatInfo.type = V2TIMConversation.V2TIM_C2C
            chatInfo.id = teacher.id
            chatInfo.chatName = teacher.name
            logd("teacher id = ${chatInfo.id}")
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra(Constants.CHAT_INFO, chatInfo)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

}