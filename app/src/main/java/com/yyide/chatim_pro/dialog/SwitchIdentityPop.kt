package com.yyide.chatim_pro.dialog

import android.app.Activity
import android.util.Log
import android.view.*
import android.widget.PopupWindow
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tencent.qcloud.tim.uikit.TUIKit
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack
import com.yyide.chatim_pro.R
import com.yyide.chatim_pro.SpData
import com.yyide.chatim_pro.adapter.SwitchIdentityAdapter
import com.yyide.chatim_pro.base.BaseConstant
import com.yyide.chatim_pro.model.EventMessage
import com.yyide.chatim_pro.model.SchoolRsp
import com.yyide.chatim_pro.model.UserInfo
import com.yyide.chatim_pro.utils.DemoLog
import com.yyide.chatim_pro.utils.LoadingTools
import okhttp3.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by Administrator on 2019/5/15.
 */
class SwitchIdentityPop(var context: Activity) : PopupWindow() {
    var popupWindow: PopupWindow? = null
    var mWindow: Window? = null
    var mOkHttpClient = OkHttpClient()
    var loadingTools: LoadingTools? = null
    var identityBean: SchoolRsp.IdentityBean? = null
    private var mOnCheckCallBack: OnCheckCallBack? = null
    fun setOnCheckCallBack(mOnCheckCallBack: OnCheckCallBack?) {
        this.mOnCheckCallBack = mOnCheckCallBack
    }

    private fun init() {
        val mView =
            LayoutInflater.from(context).inflate(R.layout.layout_bottom_school_class_pop, null)
        popupWindow = PopupWindow(
            mView, ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT, true
        )
        popupWindow!!.animationStyle = R.style.popwin_anim_style2
        val bg: ConstraintLayout = mView.findViewById(R.id.bg)
        val recyclerView: RecyclerView = mView.findViewById(R.id.listview)
        loadingTools = LoadingTools(context)
        mView.findViewById<View>(R.id.icClose).setOnClickListener { v: View? ->
            if (popupWindow != null && popupWindow!!.isShowing) {
                popupWindow!!.dismiss()
            }
        }

        if (SpData.Schoolinfo() != null && SpData.Schoolinfo().children != null) {
            val children = SpData.Schoolinfo().children
            val mAdapter = SwitchIdentityAdapter()
            recyclerView.layoutManager =
                GridLayoutManager(context, children!!.size)
            recyclerView.adapter = mAdapter
            mAdapter.setList(children)
            mAdapter.setSelectIndex(0)
            mAdapter.setOnItemClickListener { adapter, view, position ->
                mAdapter.setSelectIndex(position)
            }
            identityBean = mAdapter.getSelectItem()
        }

        popupWindow!!.contentView.setOnKeyListener { v: View?, keyCode: Int, event: KeyEvent? ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (popupWindow != null && popupWindow!!.isShowing) {
                    popupWindow!!.dismiss()
                }
                return@setOnKeyListener true
            }
            false
        }
        bg.setOnClickListener { v: View? ->
            if (popupWindow != null && popupWindow!!.isShowing) {
                popupWindow!!.dismiss()
            }
        }
        // 获取当前Activity的window
        val activity = mView.context as Activity
        if (activity != null) {
            //如果设置的值在0 - 1的范围内，则用设置的值，否则用默认值
            mWindow = activity.window
            val params = mWindow!!.attributes
            params.alpha = 0.7f
            mWindow!!.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            mWindow!!.attributes = params
        }
        popupWindow!!.setOnDismissListener {
            //如果设置了背景变暗，那么在dissmiss的时候需要还原
            Log.e("TAG", "onDismiss==>: ")
            if (mWindow != null) {
                val params = mWindow!!.attributes
                params.alpha = 1.0f
                mWindow!!.attributes = params
            }
        }
        popupWindow!!.showAtLocation(mView, Gravity.NO_GRAVITY, 0, 0)
    }

    //    void Tologin(String userId) {
    //        String userName = SPUtils.getInstance().getString(BaseConstant.LOGINNAME);
    //        RequestBody body = new FormBody.Builder()
    //                .add("username", userName)
    //                .add("password", SPUtils.getInstance().getString(BaseConstant.PASSWORD))
    //                .add("userId", userId)
    //                .add("client_id", "yide-cloud")
    //                .add("grant_type", "password")
    //                .add("version", "2")
    //                .add("client_secret", "yide1234567")
    //                .build();
    //        //请求组合创建
    //        Request request = new Request.Builder()
    //                .url(BaseConstant.API_SERVER_URL + "/management/cloud-system/oauth/token")
    //                .post(body)
    //                .build();
    //        //发起请求
    //        mOkHttpClient.newCall(request).enqueue(new Callback() {
    //            @Override
    //            public void onFailure(Call call, IOException e) {
    //                Log.e("TAG", "onFailure: " + e.toString());
    //                loadingTools.closeLoading();
    //            }
    //
    //            @Override
    //            public void onResponse(Call call, Response response) throws IOException {
    //                String data = response.body().string();
    //                Log.e("TAG", "mOkHttpClient==>: " + data);
    //                LoginRsp bean = JSON.parseObject(data, LoginRsp.class);
    //                if (bean.code == BaseConstant.REQUEST_SUCCES2) {
    //                    //存储登录信息
    //                    SPUtils.getInstance().put(SpData.LOGINDATA, JSON.toJSONString(bean));
    //                    //更新日程缓存
    //                    EventBus.getDefault().post(new EventMessage(BaseConstant.TYPE_UPDATE_SCHEDULE_LIST_DATA, ""));
    //                    getUserSchool();
    //                } else {
    //                    ToastUtils.showShort(bean.msg);
    //                    loadingTools.closeLoading();
    //                }
    //            }
    //        });
    //    }
    //    //获取学校信息
    //    void getUserSchool() {
    //        //请求组合创建
    //        Request request = new Request.Builder()
    ////                .url(BaseConstant.API_SERVER_URL + "/management/cloud-system/im/getUserSig")
    //                .url(BaseConstant.API_SERVER_URL + "/management/cloud-system/user/getUserSchoolByApp")
    //                .addHeader("Authorization", SpData.User().data.accessToken)
    //                .build();
    //        //发起请求
    //        mOkHttpClient.newCall(request).enqueue(new Callback() {
    //            @Override
    //            public void onFailure(Call call, IOException e) {
    //                Log.e("TAG", "getUserSigonFailure: " + e.toString());
    //                loadingTools.closeLoading();
    //            }
    //
    //            @Override
    //            public void onResponse(Call call, Response response) throws IOException {
    //                String data = response.body().string();
    //                Log.e("TAG", "getUserSchool333==>: " + data);
    //                GetUserSchoolRsp rsp = JSON.parseObject(data, GetUserSchoolRsp.class);
    //                SPUtils.getInstance().put(SpData.SCHOOLINFO, JSON.toJSONString(rsp));
    //                if (rsp.code == BaseConstant.REQUEST_SUCCES2) {
    //                    getUserSig();
    //                    SpData.setIdentityInfo(rsp);
    //                    if (mOnCheckCallBack != null) {
    //                        mOnCheckCallBack.onCheckCallBack();
    //                    }
    //                } else {
    //                    ToastUtils.showShort(rsp.msg);
    //                    loadingTools.closeLoading();
    //                }
    //            }
    //        });
    //    }
    //计算 UserSig
    //    void getUserSig() {
    //        RequestBody body = RequestBody.create(BaseConstant.JSON, "");
    //        //请求组合创建
    //        Request request = new Request.Builder()
    //                .url(BaseConstant.API_SERVER_URL + "/management/cloud-system/im/getUserSig")
    //                .addHeader("Authorization", SpData.User().data.accessToken)
    //                .post(body)
    //                .build();
    //        //发起请求
    //        mOkHttpClient.newCall(request).enqueue(new Callback() {
    //            @Override
    //            public void onFailure(Call call, IOException e) {
    //                //Log.e(TAG, "getUserSigonFailure: " + e.toString());
    //                loadingTools.closeLoading();
    //            }
    //
    //            @Override
    //            public void onResponse(Call call, Response response) throws IOException {
    //                String data = response.body().string();
    //                //Log.e(TAG, "getUserSig==>: " + data);
    //                UserSigRsp bean = JSON.parseObject(data, UserSigRsp.class);
    //                if (bean.code == BaseConstant.REQUEST_SUCCES2) {
    //                    SPUtils.getInstance().put(SpData.USERSIG, bean.data);
    //                    initIm(bean.data);
    //                } else {
    //                    ToastUtils.showShort(bean.msg);
    //                    loadingTools.closeLoading();
    //                }
    //            }
    //        });
    //    }
    fun initIm(userSig: String?) {
        UserInfo.getInstance().userId = SpData.getUserId() + ""
        // 获取userSig函数
        TUIKit.login(SpData.getUserId() + "", userSig, object : IUIKitCallBack {
            override fun onError(module: String, code: Int, desc: String) {
                loadingTools!!.closeLoading()
                //context.runOnUiThread(() -> YDToastUtil.toastLongMessage("登录失败, errCode = " + code + ", errInfo = " + desc));
                DemoLog.i("TAG", "imLogin errorCode = $code, errorInfo = $desc")
                UserInfo.getInstance().isAutoLogin = false
                UserInfo.getInstance().userSig = userSig
                UserInfo.getInstance().userId = SpData.getUserId() + ""
                Log.e("TAG", "切换成功UserInfo==》: " + UserInfo.getInstance().userId)
                EventBus.getDefault().post(EventMessage(BaseConstant.TYPE_UPDATE_HOME, ""))
                if (mOnCheckCallBack != null) {
                    mOnCheckCallBack!!.onCheckCallBack()
                }
                if (popupWindow != null && popupWindow!!.isShowing) {
                    popupWindow!!.dismiss()
                }
                //                ActivityUtils.finishAllActivities();
//                Intent intent = new Intent(context, MainActivity.class);
//                context.startActivity(intent);
                //刷新首页数据
            }

            override fun onSuccess(data: Any) {
                loadingTools!!.closeLoading()
                UserInfo.getInstance().isAutoLogin = true
                UserInfo.getInstance().userSig = userSig
                UserInfo.getInstance().userId = SpData.getUserId() + ""
                Log.e("TAG", "切换成功UserInfo==》: " + UserInfo.getInstance().userId)
                //                ActivityUtils.finishAllActivities();
//                Intent intent = new Intent(context, MainActivity.class);
//                context.startActivity(intent);
                //刷新首页数据
                EventBus.getDefault().post(EventMessage(BaseConstant.TYPE_UPDATE_HOME, ""))
                EventBus.getDefault().post(EventMessage(BaseConstant.TYPE_HOME_CHECK_IDENTITY, ""))
                if (mOnCheckCallBack != null) {
                    mOnCheckCallBack!!.onCheckCallBack()
                }
                if (popupWindow != null && popupWindow!!.isShowing) {
                    popupWindow!!.dismiss()
                }
            }
        })
    }

    init {
        init()
    }
}