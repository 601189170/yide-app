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
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.tbruyelle.rxpermissions3.RxPermissions
import com.tencent.imsdk.v2.V2TIMConversation
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo
import com.yyide.chatim.BaseApplication
import com.yyide.chatim.R
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.chat.ChatActivity
import com.yyide.chatim.databinding.ActivityBookStudentDetailBinding
import com.yyide.chatim.model.BookStudentItem
import com.yyide.chatim.utils.Constants
import com.yyide.chatim.utils.GlideUtil
import com.yyide.chatim.utils.Utils

class BookStudentDetailActivity : BaseActivity() {

    private lateinit var viewBinding: ActivityBookStudentDetailBinding
    private var type = 1
    private var type2 = 1
    private var type3 = 1

    companion object {
        fun start(context: Context, student: BookStudentItem) {
            val intent = Intent(context, BookStudentDetailActivity::class.java)
            intent.putExtra("student", student)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityBookStudentDetailBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        initView()
    }

    override fun getContentViewID(): Int {
        return R.layout.activity_book_student_detail
    }

    private fun initView() {
        val student: BookStudentItem = intent.getSerializableExtra("student") as BookStudentItem
        GlideUtil.loadImageRadius2(
            this,
            student.faceInformation,
            viewBinding.ivHead,
            SizeUtils.dp2px(2f)
        )
        viewBinding.top.backLayout.setOnClickListener { finish() }
        viewBinding.top.title.text = getString(R.string.book_title_info_yd)
        viewBinding.tvName.text = student.name
        viewBinding.name.text = student.name
        viewBinding.sex.text = if (student.sex == "1") "男" else "女"
        viewBinding.address.text =
            if (!TextUtils.isEmpty(student.address)) student.address else "暂无"

        if (TextUtils.isEmpty(student.phone)) {
            viewBinding.set.visibility = View.INVISIBLE
            viewBinding.ivPhone.visibility = View.INVISIBLE
            viewBinding.phone.text = "暂无该学生的手机号码"
        } else {
            viewBinding.set.visibility = View.VISIBLE
            viewBinding.ivPhone.visibility = View.VISIBLE
            viewBinding.phone.text = Utils.setHideMobile(student.phone)
        }

        if (TextUtils.isEmpty(student.primaryGuardianPhone)) {
            viewBinding.setMainGuardian.visibility = View.INVISIBLE
            viewBinding.ivMainGuardian.visibility = View.INVISIBLE
            viewBinding.mainGuardianPhone.text = "暂无"
        } else {
            viewBinding.setMainGuardian.visibility = View.VISIBLE
            viewBinding.ivMainGuardian.visibility = View.VISIBLE
            viewBinding.mainGuardianPhone.text = Utils.setHideMobile(student.primaryGuardianPhone)
        }

        if (TextUtils.isEmpty(student.deputyGuardianPhone)) {
            viewBinding.setDeputyGuardian.visibility = View.INVISIBLE
            viewBinding.ivPhoneDeputyGuardian.visibility = View.INVISIBLE
            viewBinding.phoneDeputyGuardian.text = "暂无"
        } else {
            viewBinding.setDeputyGuardian.visibility = View.VISIBLE
            viewBinding.ivPhoneDeputyGuardian.visibility = View.VISIBLE
            viewBinding.phoneDeputyGuardian.text = Utils.setHideMobile(student.deputyGuardianPhone)
        }

        viewBinding.ivPhone.setOnClickListener {
            rxPermission(student.phone)
        }
        viewBinding.ivMainGuardian.setOnClickListener {
            rxPermission(student.primaryGuardianPhone)
        }
        viewBinding.ivPhoneDeputyGuardian.setOnClickListener {
            rxPermission(student.deputyGuardianPhone)
        }

        viewBinding.set.setOnClickListener { v: View? ->
            if (type == 1) {
                type = 2
                viewBinding.set.text = "隐藏"
                viewBinding.phone.text = student.phone
            } else {
                type = 1
                viewBinding.set.text = "显示"
                viewBinding.phone.text = Utils.setHideMobile(student.phone)
            }
        }
        viewBinding.setMainGuardian.setOnClickListener { v: View? ->
            if (type2 == 1) {
                type2 = 2
                viewBinding.setMainGuardian.text = "隐藏"
                viewBinding.mainGuardianPhone.text = student.primaryGuardianPhone
            } else {
                type2 = 1
                viewBinding.setMainGuardian.text = "显示"
                viewBinding.mainGuardianPhone.text =
                    Utils.setHideMobile(student.primaryGuardianPhone)
            }
        }
        viewBinding.setDeputyGuardian.setOnClickListener { v: View? ->
            if (type3 == 1) {
                type3 = 2
                viewBinding.setDeputyGuardian.text = "隐藏"
                viewBinding.phoneDeputyGuardian.text = student.deputyGuardianPhone
            } else {
                type3 = 1
                viewBinding.setDeputyGuardian.text = "显示"
                viewBinding.phoneDeputyGuardian.text =
                    Utils.setHideMobile(student.deputyGuardianPhone)
            }
        }

    }

    private fun rxPermission(phone: String) {
        if (TextUtils.isEmpty(phone)) return
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