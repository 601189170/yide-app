package com.yyide.chatim.login

import android.content.Intent
import android.content.pm.PackageManager
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.activity.viewModels
import com.alibaba.fastjson.JSON
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.tencent.mmkv.MMKV
import com.tencent.qcloud.tim.uikit.TUIKit
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack
import com.tencent.qcloud.tim.uikit.utils.ToastUtil
import com.yyide.chatim.NewMainActivity
import com.yyide.chatim.SpData
import com.yyide.chatim.activity.NoteBookActivity
import com.yyide.chatim.activity.schedule.SchoolCalendarActivity
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.base.KTBaseActivity
import com.yyide.chatim.base.MMKVConstant
import com.yyide.chatim.databinding.ActivityNewLoginBinding
import com.yyide.chatim.login.viewmodel.LoginViewModel
import com.yyide.chatim.model.IdentityBean
import com.yyide.chatim.model.SchoolRsp
import com.yyide.chatim.model.UserInfo
import com.yyide.chatim.utils.DemoLog
import com.yyide.chatim.utils.Utils
import com.yyide.chatim.utils.YDToastUtil
import com.yyide.chatim.utils.loge

class NewLoginActivity : KTBaseActivity<ActivityNewLoginBinding>(ActivityNewLoginBinding::inflate) {

    private val TAG = NewLoginActivity::class.java.simpleName
    private val viewModel: LoginViewModel by viewModels()

    override fun initView() {
        super.initView()
        setScreenFull()
        initEdit()
        binding.etUser.setText(MMKV.defaultMMKV().decodeString(MMKVConstant.YD_USERNAME))
        binding.etPwd.setText(MMKV.defaultMMKV().decodeString(MMKVConstant.YD_PASSWORD))
        Utils.checkPermission(this)
        binding.ivDel.setOnClickListener {
            binding.etUser.setText("")
        }

        binding.eye.setOnClickListener {
            eye()
        }

        binding.btnLogin.setOnClickListener {
            login()
        }
        viewModel.loginLiveData.observe(this) {
            val bean = it.getOrNull()
            hideLoading()
            if (it.isSuccess) {
                if (bean != null) {
                    //登陆成功保存账号密码
                    MMKV.defaultMMKV()
                        .encode(MMKVConstant.YD_USERNAME, binding.etUser.text.toString())
                    MMKV.defaultMMKV()
                        .encode(MMKVConstant.YD_PASSWORD, binding.etPwd.text.toString())
                    SPUtils.getInstance().put(SpData.LOGINDATA, JSON.toJSONString(bean))
                    //获取学校信息
                    getSchoolInfo()
                }
            } else {
                it.exceptionOrNull()?.localizedMessage?.let { it1 ->
                    loge(it1)
                    ToastUtils.showLong(it1)
                }
            }
        }
//        startActivity(Intent(this, NoteBookActivity::class.java))
        startActivity(Intent(this, SchoolCalendarActivity::class.java))


    }

    private fun getSchoolInfo() {
        //处理选择学校数据
        viewModel.schoolLiveData.observe(this) {
            hideLoading()
            if (it.isSuccess) {
                val listData = it.getOrNull()
                if (!listData.isNullOrEmpty()) {
                    if (listData.size > 1 || (listData.isNotEmpty() && listData[0].children!!.size > 1)) {
                        IdentitySelectActivity.start(this)
                    } else {
                        if (listData.isNotEmpty() && listData[0].children!!.isNotEmpty()) {
                            identityLogin(listData[0], listData[0].children!![0])
                        } else {
                            YDToastUtil.showMessage("无学校或无身份")
                        }
                    }
                }
            } else {
                it.exceptionOrNull()?.localizedMessage?.let { it1 ->
                    loge(it1)
                    ToastUtils.showLong(it1)
                }
            }
        }
        showLoading()
        viewModel.schoolIdentity()
    }

    /**
     * 用户学校身份登录
     */
    private fun identityLogin(
        schoolBean: SchoolRsp,
        identityBean: IdentityBean
    ) {
        viewModel.loginLiveData.observe(this) {
            hideLoading()
            if (it.isSuccess) {
                SPUtils.getInstance()
                    .put(SpData.SCHOOLINFO, JSON.toJSONString(schoolBean))
                SPUtils.getInstance()
                    .put(SpData.IDENTIY_INFO, JSON.toJSONString(identityBean))
                val user = SpData.User()
                user.isLogin = true
                SPUtils.getInstance().put(SpData.LOGINDATA, JSON.toJSONString(user))
                startActivity(Intent(this, NewMainActivity::class.java))
                finish()
            } else {
                it.exceptionOrNull()?.localizedMessage?.let { it1 ->
                    loge(it1)
                    ToastUtils.showLong(it1)
                }
            }
        }
        showLoading()
        viewModel.identityLogin(identityBean.id, schoolBean.id)
    }

    private fun login() {
//        val validateCode: String = vCode.getText().toString().trim { it <= ' ' }
        val mobile: String = binding.etUser.text.toString().trim { it <= ' ' }
        val password: String = binding.etPwd.text.toString().trim { it <= ' ' }
//        if (ll_sms.isShown()) { //处理登录逻辑
//            if (TextUtils.isEmpty(mobile)) {
//                YDToastUtil.showShort("请输入手机号码")
//            } else if (TextUtils.isEmpty(validateCode)) {
//                YDToastUtil.showShort("请输入验证码")
//            } else {
//                tologinBymobile(validateCode, mobile)
//            }
//        } else {
        when {
            TextUtils.isEmpty(mobile) -> {
                YDToastUtil.showMessage("请输入手机号码")
            }
            TextUtils.isEmpty(password) -> {
                YDToastUtil.showMessage("请输入密码")
            }
            else -> {
                showLoading()
                viewModel.login(mobile, password)
            }
        }
//        }
    }

    private fun eye() {
        if (!binding.eye.isSelected) {
            binding.eye.isSelected = true
            binding.etPwd.transformationMethod = HideReturnsTransformationMethod.getInstance()
        } else {
            binding.eye.isSelected = false
            binding.etPwd.transformationMethod = PasswordTransformationMethod.getInstance()
        }
        binding.etPwd.setSelection(binding.etPwd.length())
    }

    private fun initEdit() {
        binding.etUser.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                val s1 = s.toString()
                if (!TextUtils.isEmpty(s1)) {
                    binding.ivDel.visibility = View.VISIBLE
                } else {
                    binding.ivDel.visibility = View.GONE
                }
            }
        })
    }

    private fun initIm(userId: String, userSig: String) {
        TUIKit.login(userId, userSig, object : IUIKitCallBack {
            override fun onError(module: String, code: Int, desc: String) {
                runOnUiThread {
                    //YDToastUtil.toastLongMessage("登录失败, errCode = " + code + ", errInfo = " + desc);
                    SPUtils.getInstance()
                        .put(BaseConstant.LOGINNAME, binding.etUser.text.toString())
                    SPUtils.getInstance()
                        .put(BaseConstant.PASSWORD, binding.etPwd.text.toString())
                    UserInfo.getInstance().setAutoLogin(false)
                    UserInfo.getInstance().userSig = userSig
                    UserInfo.getInstance().userId = userId
                    startActivity(Intent(this@NewLoginActivity, NewMainActivity::class.java))
                    finish()
                    Log.e(TAG, "initIm==>onSuccess: 腾讯IM激活成功")
                }
                DemoLog.i(TAG, "imLogin errorCode = $code, errorInfo = $desc")
                hideLoading()
            }

            override fun onSuccess(data: Any) {
                SPUtils.getInstance().put(BaseConstant.LOGINNAME, binding.etUser.text.toString())
                SPUtils.getInstance().put(BaseConstant.PASSWORD, binding.etPwd.text.toString())
                UserInfo.getInstance().setAutoLogin(true)
                UserInfo.getInstance().userSig = userSig
                UserInfo.getInstance().userId = userId
                startActivity(Intent(this@NewLoginActivity, NewMainActivity::class.java))
                hideLoading()
                Log.e(TAG, "initIm==>onSuccess: 腾讯IM激活成功")
                finish()
            }
        })
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish()
        }
        return super.onKeyDown(keyCode, event)
    }

    /**
     * 系统请求权限回调
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == Utils.REQ_PERMISSION_CODE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                ToastUtil.toastLongMessage("未全部授权，部分功能可能无法使用！")
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
//        startActivity(Intent(this, SchoolCalendarActivity::class.java))
        startActivity(Intent(this, NoteBookActivity::class.java))


    }

}