package com.yyide.chatim.activity.book

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.fastjson.JSON
import com.tencent.imsdk.v2.V2TIMConversation
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo
import com.yyide.chatim.BaseApplication
import com.yyide.chatim.R
import com.yyide.chatim.activity.book.adapter.BookClassesAdapter
import com.yyide.chatim.activity.book.adapter.BookGuardianAdapter
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.chat.ChatActivity
import com.yyide.chatim.databinding.ActivityBookStudentDetailBinding
import com.yyide.chatim.model.BookStudentItem
import com.yyide.chatim.utils.*

class BookStudentDetailActivity : BaseActivity() {

    private lateinit var viewBinding: ActivityBookStudentDetailBinding
    private var type = 1
    private var type2 = 1
    private var type3 = 1
    private lateinit var bookGuardianAdapter: BookGuardianAdapter


    companion object {
        /**
         * type 1 家长  0 老师
         */
        @JvmStatic
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
        if(intent.getSerializableExtra("student") == null) return
        val student = intent.getSerializableExtra("student") as BookStudentItem
        viewBinding.clMessage.visibility = View.VISIBLE
//        GlideUtil.loadImageHead(
//            this,
//            student.faceInformation,
//            viewBinding.ivHead
//        )

        GlideUtil.loadImageHead(
                this,
                student.avatar,
                viewBinding.ivHead
        )
        viewBinding.top.backLayout.setOnClickListener { finish() }
        viewBinding.top.title.text = getString(R.string.book_title_info_yd)
        viewBinding.tvClasses.text =
            if (TextUtils.isEmpty(student.classesName)) "暂无班级星系" else student.classesName
        viewBinding.tvName.text = student.name
        viewBinding.name.text = student.name
//        viewBinding.sex.text = if (student.sex == "1") "男" else "女"
        if (student.sex?.equals("1") == true){
            viewBinding.sex.text="男"
        }else if (student.sex == "0"){
            viewBinding.sex.text="女"
        }else{
            viewBinding.sex.text="未知"
        }
//        viewBinding.sex.text = if (student.sex == "1") "男" else "女"
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
        viewBinding.ivPhone.setOnClickListener {
            RxPermissionUtils.callPhone(this, student.phone)
        }
        Log.e("TAG", "BookStudentDetailActivity==》: "+JSON.toJSONString(student) )
        viewBinding.recylerviewGuardian.layoutManager = LinearLayoutManager(this)

        bookGuardianAdapter = BookGuardianAdapter()

        viewBinding.recylerviewGuardian.adapter=bookGuardianAdapter

        bookGuardianAdapter.setList(student.elternAddBookDTOList)

//        bookGuardianAdapter.setOnItemClickListener { adapter, view, position ->
//            RxPermissionUtils.callPhone(this, bookGuardianAdapter.getItem(position).phone)
//        }


//        if (TextUtils.isEmpty(student.primaryGuardianPhone)) {
//            viewBinding.setMainGuardian.visibility = View.INVISIBLE
//            viewBinding.ivMainGuardian.visibility = View.INVISIBLE
//            viewBinding.mainGuardianPhone.text = "暂无"
//        } else {
//            viewBinding.setMainGuardian.visibility = View.VISIBLE
//            viewBinding.ivMainGuardian.visibility = View.VISIBLE
//            viewBinding.mainGuardianPhone.text = Utils.setHideMobile(student.primaryGuardianPhone)
//        }
//
//        if (TextUtils.isEmpty(student.deputyGuardianPhone)) {
//            viewBinding.setDeputyGuardian.visibility = View.INVISIBLE
//            viewBinding.ivPhoneDeputyGuardian.visibility = View.INVISIBLE
//            viewBinding.phoneDeputyGuardian.text = "暂无"
//        } else {
//            viewBinding.setDeputyGuardian.visibility = View.VISIBLE
//            viewBinding.ivPhoneDeputyGuardian.visibility = View.VISIBLE
//            viewBinding.phoneDeputyGuardian.text = Utils.setHideMobile(student.deputyGuardianPhone)
//        }
//
//
//        viewBinding.ivMainGuardian.setOnClickListener {
//            RxPermissionUtils.callPhone(this, student.primaryGuardianPhone)
//        }
//        viewBinding.ivPhoneDeputyGuardian.setOnClickListener {
//            RxPermissionUtils.callPhone(this, student.deputyGuardianPhone)
//        }
//
//
//        viewBinding.setMainGuardian.setOnClickListener { v: View? ->
//            if (type2 == 1) {
//                type2 = 2
//                viewBinding.setMainGuardian.text = "隐藏"
//                viewBinding.mainGuardianPhone.text = student.primaryGuardianPhone
//            } else {
//                type2 = 1
//                viewBinding.setMainGuardian.text = "显示"
//                viewBinding.mainGuardianPhone.text =
//                    Utils.setHideMobile(student.primaryGuardianPhone)
//            }
//        }
//        viewBinding.setDeputyGuardian.setOnClickListener { v: View? ->
//            if (type3 == 1) {
//                type3 = 2
//                viewBinding.setDeputyGuardian.text = "隐藏"
//                viewBinding.phoneDeputyGuardian.text = student.deputyGuardianPhone
//            } else {
//                type3 = 1
//                viewBinding.setDeputyGuardian.text = "显示"
//                viewBinding.phoneDeputyGuardian.text =
//                    Utils.setHideMobile(student.deputyGuardianPhone)
//            }
//        }

        viewBinding.clMessage.setOnClickListener {
            val chatInfo = ChatInfo()
            chatInfo.type = V2TIMConversation.V2TIM_C2C
            chatInfo.id = student.id
            chatInfo.chatName = student.name
            logd("student id = ${chatInfo.id}")
            val intent = Intent(BaseApplication.getInstance(), ChatActivity::class.java)
            intent.putExtra(Constants.CHAT_INFO, chatInfo)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            BaseApplication.getInstance().startActivity(intent)
        }
    }

}