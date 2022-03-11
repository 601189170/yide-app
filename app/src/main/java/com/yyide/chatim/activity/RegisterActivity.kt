package com.yyide.chatim.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.TextView
import com.blankj.utilcode.util.ToastUtils
import com.yyide.chatim.R
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.base.BaseMvpActivity
import com.yyide.chatim.databinding.ActivityRegisterBinding
import com.yyide.chatim.model.ResultBean
import com.yyide.chatim.presenter.RegisterPresenter
import com.yyide.chatim.view.RegisterView

class RegisterActivity : BaseMvpActivity<RegisterPresenter>(), RegisterView {

    lateinit var registerBinding: ActivityRegisterBinding
    private var time: TimeCount? = null

    override fun getContentViewID(): Int {
        return R.layout.activity_register
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(registerBinding.root)
        initView()
        time = TimeCount(120000, 1000, registerBinding.postCode)
    }

    fun initView() {
        registerBinding.top.title.setText(R.string.register)
        registerBinding.top.backLayout.setOnClickListener { finish() }
        registerBinding.tvConfirm.setOnClickListener { submit() }
        registerBinding.postCode.setOnClickListener { getCode() }
        registerBinding.tvAgreement.setOnClickListener {
            WebViewActivity.startTitle(this@RegisterActivity, BaseConstant.AGREEMENT_URL, getString(R.string.agreement_title))
        }
        registerBinding.tvPrivacy.setOnClickListener {
            WebViewActivity.startTitle(this@RegisterActivity, BaseConstant.PRIVACY_URL, getString(R.string.privacy_title))
        }
        registerBinding.eye.setOnClickListener {
            if (!registerBinding.eye.isSelected) {
                registerBinding.eye.isSelected = true
                registerBinding.newPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                registerBinding.eye.isSelected = false
                registerBinding.newPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }
    }

    internal class TimeCount(millisInFuture: Long, countDownInterval: Long, private val postCode: TextView) : CountDownTimer(millisInFuture, countDownInterval) {
        override fun onFinish() { // 计时完毕
            postCode.text = "获取验证码"
            postCode.isClickable = true
        }

        @SuppressLint("SetTextI18n")
        override fun onTick(millisUntilFinished: Long) { // 计时过程
            postCode.isClickable = false //防止重复点击
            postCode.text = "" + millisUntilFinished / 1000 + "s"
        }
    }

    private fun getCode() {
        val mobile: String = registerBinding.phone.text.toString().trim { it <= ' ' }
        if (TextUtils.isEmpty(mobile)) {
            ToastUtils.showShort("请输入手机号码")
        } else {
            mvpPresenter.getSmsCode(mobile)
        }
    }

    private fun submit() {
        val mobile: String = registerBinding.phone.text.toString().trim { it <= ' ' }
        val sms: String = registerBinding.code.text.toString().trim { it <= ' ' }
        val newPwd: String = registerBinding.newPassword.text.toString().trim { it <= ' ' }
        when {
            TextUtils.isEmpty(mobile) -> {
                ToastUtils.showShort("请输入手机号码")
            }
            TextUtils.isEmpty(sms) -> {
                ToastUtils.showShort("请输入验证码")
            }
            TextUtils.isEmpty(newPwd) -> {
                ToastUtils.showShort("请输入新密码")
            }
            else -> {
                mvpPresenter.register(mobile, sms, newPwd)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (time != null) {
            time!!.cancel()
        }
    }

    override fun createPresenter(): RegisterPresenter {
        return RegisterPresenter(this)
    }

    override fun showError() {

    }

    override fun registerSuccess(model: ResultBean?) {
        ToastUtils.showShort(model?.msg)
        if (model?.code == BaseConstant.REQUEST_SUCCESS) {
            finish()
        }
    }

    override fun getSmsSuccess(model: ResultBean) {
        ToastUtils.showShort(model.msg)
        if (model.code == BaseConstant.REQUEST_SUCCESS) {
            time!!.start()
        }
    }

    override fun fail(msg: String?) {
        Log.d("fail", msg)
    }
}