package com.yyide.chatim.activity.book

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.tbruyelle.rxpermissions3.RxPermissions
import com.tencent.imsdk.v2.V2TIMConversation
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo
import com.yyide.chatim.BaseApplication
import com.yyide.chatim.R
import com.yyide.chatim.activity.PersonInfoActivity
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.chat.ChatActivity
import com.yyide.chatim.databinding.ActivityBookTeacherDetailBinding
import com.yyide.chatim.model.BookTeacherItem
import com.yyide.chatim.utils.Constants
import com.yyide.chatim.utils.GlideUtil
import com.yyide.chatim.utils.Utils

class BookTeacherDetailActivity : BaseActivity() {

    private lateinit var viewBinding: ActivityBookTeacherDetailBinding
    private var type: Int = 1

    companion object {
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
        GlideUtil.loadImageRadius2(
            this,
            teacher.faceInformation,
            viewBinding.ivHead,
            SizeUtils.dp2px(2f)
        )
        viewBinding.top.backLayout.setOnClickListener { finish() }
        viewBinding.top.title.text = getString(R.string.book_title_info_yd)
        viewBinding.tvName.text = teacher.name
        viewBinding.name.text = teacher.name
        viewBinding.sex.text = if (teacher.sex == "1") "男" else "女"
        viewBinding.phone.text = Utils.setHideMobile(teacher.phone)
        viewBinding.email.text = if (TextUtils.isEmpty(teacher.email)) "无" else teacher.email
        viewBinding.subjectName.text = teacher.subjectName
        viewBinding.ivPhone.setOnClickListener {
            rxPermission(teacher.phone)
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
            chatInfo.id = teacher.userId
            chatInfo.chatName = teacher.name
            val intent = Intent(BaseApplication.getInstance(), ChatActivity::class.java)
            intent.putExtra(Constants.CHAT_INFO, chatInfo)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            BaseApplication.getInstance().startActivity(intent)
        }
    }

    private fun rxPermission(phone: String) {
        val rxPermissions = RxPermissions(this)
        rxPermissions.request(Manifest.permission.CALL_PHONE).subscribe { granted: Boolean ->
            if (granted) {
                if (!TextUtils.isEmpty(phone)) {
                    val intent = Intent(Intent.ACTION_CALL)
                    val data = Uri.parse("tel:$phone")
                    intent.data = data
                    startActivity(intent)
                } else {
                    ToastUtils.showShort("空手机号，无法拨打电话")
                }
            } else {
                // 权限被拒绝
                AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage(R.string.permission_call_phone)
                    .setPositiveButton(
                        "开启"
                    ) { dialog: DialogInterface?, which: Int ->
                        val localIntent = Intent()
                        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        localIntent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
                        localIntent.data = Uri.fromParts(
                            "package",
                            packageName,
                            null
                        )
                        startActivity(localIntent)
                    }
                    .setNegativeButton("取消", null)
                    .create().show()
            }
        }
    }
}