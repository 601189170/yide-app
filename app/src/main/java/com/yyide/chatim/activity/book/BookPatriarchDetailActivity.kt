package com.yyide.chatim.activity.book

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.tbruyelle.rxpermissions3.RxPermissions
import com.tencent.imsdk.v2.V2TIMConversation
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo
import com.yyide.chatim.BaseApplication
import com.yyide.chatim.R
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.chat.ChatActivity
import com.yyide.chatim.databinding.ActivityBookPatriarchDetailBinding
import com.yyide.chatim.model.BookGuardianItem
import com.yyide.chatim.model.BookTeacherItem
import com.yyide.chatim.utils.Constants
import com.yyide.chatim.utils.GlideUtil
import com.yyide.chatim.utils.Utils

class BookPatriarchDetailActivity : BaseActivity() {

    private lateinit var viewBinding: ActivityBookPatriarchDetailBinding
    private var type: Int = 1

    companion object {
        fun start(context: Context, guardianItem: BookGuardianItem) {
            val intent = Intent(context, BookPatriarchDetailActivity::class.java)
            intent.putExtra("guardianItem", guardianItem)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityBookPatriarchDetailBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        initView()
    }

    override fun getContentViewID(): Int {
        return R.layout.activity_book_patriarch_detail
    }

    private fun initView() {
        val guardianItem: BookGuardianItem =
            intent.getSerializableExtra("guardianItem") as BookGuardianItem
        GlideUtil.loadImageRadius2(
            this,
            guardianItem.faceInformation,
            viewBinding.ivHead,
            SizeUtils.dp2px(2f)
        )
        viewBinding.top.backLayout.setOnClickListener { finish() }
        viewBinding.top.title.text = getString(R.string.book_title_info_yd)
        viewBinding.tvName.text = guardianItem.name
        viewBinding.name.text = guardianItem.name
        viewBinding.tvFamilyRelations.text = guardianItem.getRelation()
        viewBinding.tvSingleGuardianship.text = if (guardianItem.singleParent == "1") "是" else "否"
        viewBinding.tvEmployer.text = if(!TextUtils.isEmpty(guardianItem.workUnit)) guardianItem.workUnit else  "暂无"
        viewBinding.ivPhone.setOnClickListener {
            rxPermission(guardianItem.phone)
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