package com.yyide.chatim

import android.Manifest
import android.app.AlertDialog
import android.content.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.alibaba.fastjson.JSON
import com.blankj.utilcode.util.*
import com.blankj.utilcode.util.Utils
import com.heytap.msp.push.HeytapPushManager
import com.huawei.agconnect.config.AGConnectServicesConfig
import com.huawei.hms.aaid.HmsInstanceId
import com.huawei.hms.common.ApiException
import com.shehuan.nicedialog.BaseNiceDialog
import com.shehuan.nicedialog.NiceDialog
import com.shehuan.nicedialog.ViewConvertListener
import com.shehuan.nicedialog.ViewHolder
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.tbruyelle.rxpermissions3.RxPermissions
import com.tencent.imsdk.v2.V2TIMSignalingInfo
import com.tencent.liteav.model.CallModel
import com.tencent.liteav.model.TRTCAVCallImpl
import com.tencent.mmkv.MMKV
import com.tencent.qcloud.tim.uikit.TUIKit
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationManagerKit
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationManagerKit.MessageUnreadWatcher
import com.vivo.push.PushClient
import com.yyide.chatim.alipush.AliasUtil
import com.yyide.chatim.alipush.MyMessageReceiver
import com.yyide.chatim.alipush.NotifyUtil
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.base.KTBaseActivity
import com.yyide.chatim.base.MMKVConstant
import com.yyide.chatim.chat.helper.TUIKitLiveListenerManager
import com.yyide.chatim.databinding.ActivityNewMainBinding
import com.yyide.chatim.fragment.schedule.ScheduleFragment2
import com.yyide.chatim.home.HelpFragment
import com.yyide.chatim.home.HomeFragment
import com.yyide.chatim.home.MessageFragment
import com.yyide.chatim.home.NAppFragment
import com.yyide.chatim.jiguang.ExampleUtil
import com.yyide.chatim.jiguang.LocalBroadcastManager
import com.yyide.chatim.model.*
import com.yyide.chatim.net.AppClient
import com.yyide.chatim.thirdpush.HUAWEIHmsMessageService
import com.yyide.chatim.thirdpush.OPPOPushImpl
import com.yyide.chatim.thirdpush.ThirdPushTokenMgr
import com.yyide.chatim.utils.*
import com.yyide.chatim.view.DialogUtil
import com.yyide.chatim.viewmodel.MainViewModel
import com.yyide.chatim.viewmodel.ScheduleMangeViewModel
import com.yyide.chatim.widget.LoadingButton
import okhttp3.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.joda.time.DateTime
import java.io.File
import java.io.IOException
import java.util.*

/**
 * 2.0首页
 */
class NewMainActivity : KTBaseActivity<ActivityNewMainBinding>(ActivityNewMainBinding::inflate),
    MessageUnreadWatcher {

    private lateinit var viewModel: MainViewModel

    companion object {
        private var mMessageReceiver: MessageReceiver? = null
        const val MESSAGE_RECEIVED_ACTION = "cn.jiguang.demo.jpush.MESSAGE_RECEIVED_ACTION"
        const val KEY_TITLE = "title"
        const val KEY_MESSAGE = "message"
        const val KEY_EXTRAS = "extras"
        val TAG = "NewMainActivity"
    }

    private var isShow = false
    private var firstTime: Long = 0
    private var mCallModel: CallModel? = null
    private var scheduleMangeViewModel: ScheduleMangeViewModel? = null
    private var curDateTime: DateTime? = null

    //身份切换
    private var homeFragment = HomeFragment()
    private var messageFragment = MessageFragment()
    private var scheduleFragment = ScheduleFragment2()
    private var appFragment = NAppFragment()
    private var helpFragment = HelpFragment()


    private val fragmentList = mutableListOf<Fragment>()
    private val homeFragmentStr = "homeFragment"
    private val messageFragmentStr = "messageFragment"
    private val scheduleFragmentStr = "scheduleFragment"
    private val appFragmentStr = "appFragment"
    private val helpFragmentStr = "helpFragment"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {

            fragmentList.clear()
            /*获取保存的fragment  没有的话返回null*/
            homeFragment = supportFragmentManager.getFragment(
                savedInstanceState,
                homeFragmentStr
            ) as HomeFragment
            messageFragment = supportFragmentManager.getFragment(
                savedInstanceState,
                messageFragmentStr
            ) as MessageFragment
            scheduleFragment = supportFragmentManager.getFragment(
                savedInstanceState,
                scheduleFragmentStr
            ) as ScheduleFragment2
            appFragment = supportFragmentManager.getFragment(
                savedInstanceState,
                appFragmentStr
            ) as NAppFragment
            helpFragment = supportFragmentManager.getFragment(
                savedInstanceState,
                helpFragmentStr
            ) as HelpFragment

            addToFragmentList(homeFragment)
            addToFragmentList(messageFragment)
            addToFragmentList(scheduleFragment)
            addToFragmentList(appFragment)
            addToFragmentList(helpFragment)

        } else {
            initFragment()
        }


        setScreenFull()
        EventBus.getDefault().register(this)
        registerMessageReceiver() // used for receive msg
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        //登录IM
        getUserSig()
        //注册极光别名
        AliasUtil.syncAliases()
        //登录IM
        //处理失败时点击切换重新登录IM
        prepareThirdPushToken()
        //ConversationManagerKit.getInstance().addUnreadWatcher(this);
        //离线消息推送处理
        val extras = intent.getStringExtra("extras")
        if (!TextUtils.isEmpty(extras)) {
            binding.content.postDelayed(Runnable {
                val intent =
                    Intent(this, MyMessageReceiver::class.java)
                intent.action = "notification_clicked"
                intent.putExtra("extras", extras)
                sendBroadcast(intent)
            }, 3000)
        }

        //检查是否开启消息通知
        showNotificationPermission()
        //应用更新检测
//        Handler().postDelayed({ mvpPresenter.getVersionInfo() }, 3000)
//        mvpPresenter.copywriter()
//        mvpPresenter.getWeeklyDate()

        //请求日程数据 并保存本地
        scheduleMangeViewModel = ViewModelProvider(this).get(
            ScheduleMangeViewModel::class.java
        )
        scheduleMangeViewModel!!.getAllScheduleList()
        scheduleMangeViewModel!!.curDateTime.observe(this) { dateTime: DateTime ->
            Log.e(TAG, "时间改变: $dateTime")
            if (curDateTime != null) {
                scheduleMangeViewModel!!.curDateTime.value = dateTime
                curDateTime = dateTime
            }
        }
        initView()
        initViewModel()
        viewModel.getTodoList()
    }

    private val HOME_TYPE = 1
    private val MESSAGE_TYPE = 2
    private val SCHEDULE_TYPE = 3
    private val APP_TYPE = 4
    private val HELP_TYPE = 5

    override fun initView() {
        binding.tab1Layout.setOnClickListener {
            binding.ivHome.visibility = View.VISIBLE
            binding.tab1.visibility = View.INVISIBLE
            showFragment(HOME_TYPE, homeFragment)
        }
        binding.tab2Layout.setOnClickListener { showFragment(SCHEDULE_TYPE, scheduleFragment) }
        binding.tab3Layout.setOnClickListener { showFragment(MESSAGE_TYPE, messageFragment) }
        binding.tab4Layout.setOnClickListener { showFragment(APP_TYPE, appFragment) }
        binding.tab5Layout.setOnClickListener { showFragment(HELP_TYPE, helpFragment) }

        //默认选中Home
        //setFragment(HOME_TYPE, homeFragment)
    }

    private fun initFragment() {
        addFragment(homeFragment)
        addFragment(messageFragment)
        addFragment(scheduleFragment)
        addFragment(appFragment)
        addFragment(helpFragment)
        showFragment(HOME_TYPE, homeFragment)
    }

    private fun addToFragmentList(fragment: Fragment?) {
        fragment?.let {
            fragmentList.add(it)
        }
    }

    private fun addFragment(fragment: Fragment) {
        if (!fragment.isAdded) {
            supportFragmentManager.beginTransaction().add(binding.content.id, fragment).commit()
            fragmentList.add(fragment)
        }
    }

    private fun setFragment(type: Int, fragment: Fragment) {
        if (type != HOME_TYPE) {
            binding.ivHome.visibility = View.INVISIBLE
            binding.tab1.visibility = View.VISIBLE
        } else {
            binding.ivHome.visibility = View.VISIBLE
            binding.tab1.visibility = View.INVISIBLE
        }
        binding.tab1.isChecked = type == HOME_TYPE
        binding.tab2.isChecked = type == SCHEDULE_TYPE
        binding.tab3.isChecked = type == MESSAGE_TYPE
        binding.tab4.isChecked = type == APP_TYPE
        binding.tab5.isChecked = type == HELP_TYPE
        supportFragmentManager.beginTransaction().replace(binding.content.id, fragment).commit()
    }

    private fun showFragment(type: Int, fragment: Fragment) {
        if (type != HOME_TYPE) {
            binding.ivHome.visibility = View.INVISIBLE
            binding.tab1.visibility = View.VISIBLE
        } else {
            binding.ivHome.visibility = View.VISIBLE
            binding.tab1.visibility = View.INVISIBLE
        }
        binding.tab1.isChecked = type == HOME_TYPE
        binding.tab2.isChecked = type == SCHEDULE_TYPE
        binding.tab3.isChecked = type == MESSAGE_TYPE
        binding.tab4.isChecked = type == APP_TYPE
        binding.tab5.isChecked = type == HELP_TYPE

        for (frag in fragmentList) {
            if (frag != fragment) {
                supportFragmentManager.beginTransaction().hide(frag).commit()
            }
        }

        supportFragmentManager.beginTransaction().show(fragment).commit()
    }


    private fun prepareThirdPushToken() {
        ThirdPushTokenMgr.getInstance().setPushTokenToTIM()
        when {
            BrandUtil.isBrandHuawei() -> {
                // 华为离线推送
                object : Thread() {
                    override fun run() {
                        try {
                            // read from agconnect-services.json
                            val appId = AGConnectServicesConfig.fromContext(this@NewMainActivity)
                                .getString("client/app_id")
                            val token =
                                HmsInstanceId.getInstance(this@NewMainActivity)
                                    .getToken(appId, "HCM")
                            DemoLog.i(TAG, "huawei get token:$token")
                            if (!TextUtils.isEmpty(token)) {
                                ThirdPushTokenMgr.getInstance().thirdPushToken = token
                                ThirdPushTokenMgr.getInstance().setPushTokenToTIM()
                            }
                        } catch (e: ApiException) {
                            DemoLog.e(TAG, "huawei get token failed, $e")
                        }
                    }
                }.start()
            }
            BrandUtil.isBrandVivo() -> {
                // vivo离线推送
                DemoLog.i(
                    TAG,
                    "vivo support push: " + PushClient.getInstance(applicationContext).isSupport
                )
                PushClient.getInstance(applicationContext).turnOnPush { state ->
                    if (state == 0) {
                        val regId = PushClient.getInstance(
                            applicationContext
                        ).regId
                        DemoLog.i(TAG, "vivopush open vivo push success regId = $regId")
                        ThirdPushTokenMgr.getInstance().thirdPushToken = regId
                        ThirdPushTokenMgr.getInstance().setPushTokenToTIM()
                    } else {
                        // 根据vivo推送文档说明，state = 101 表示该vivo机型或者版本不支持vivo推送，链接：https://dev.vivo.com.cn/documentCenter/doc/156
                        DemoLog.i(TAG, "vivopush open vivo push fail state = $state")
                    }
                }
            }
            HeytapPushManager.isSupportPush() -> {
                // oppo离线推送
                val oppo = OPPOPushImpl()
                oppo.createNotificationChannel(this)
                HeytapPushManager.register(
                    this,
                    PrivateConstants.OPPO_PUSH_APPKEY,
                    PrivateConstants.OPPO_PUSH_APPSECRET,
                    oppo
                )
            }
            BrandUtil.isGoogleServiceSupport() -> {
                // 谷歌推送
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        DemoLog.i(TAG, "onNewIntent")
        mCallModel = intent.getSerializableExtra(Constants.CHAT_INFO) as CallModel
    }

    override fun onStop() {
        ConversationManagerKit.getInstance().destroyConversation()
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        ConversationManagerKit.getInstance().addUnreadWatcher(this)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        //如果是全屏就关闭全屏
        if (GSYVideoManager.backFromWindowFull(this)) {
            return false
        }
        val secondTime = System.currentTimeMillis()
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (secondTime - firstTime < 2000) {
                ActivityUtils.finishAllActivities()
            } else {
                showFragment(HOME_TYPE, homeFragment)
                Toast.makeText(applicationContext, "再按一次返回键退出", Toast.LENGTH_SHORT).show()
                firstTime = System.currentTimeMillis()
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun Event(messageEvent: EventMessage) {
        Log.e(TAG, "Event: " + messageEvent.code)
        if (BaseConstant.TYPE_CHECK_HELP_CENTER == messageEvent.code) {
            showFragment(SCHEDULE_TYPE, scheduleFragment)
        } else if (BaseConstant.TYPE_SELECT_MESSAGE_TODO == messageEvent.code) {
            ActivityUtils.finishToActivity(NewMainActivity::class.java, false)
//            setTab(1, 1)
            showFragment(MESSAGE_TYPE, messageFragment)
        } else if (BaseConstant.TYPE_UPDATE_HOME == messageEvent.code) {
            //registerAlias();
            AliasUtil.syncAliases()
        } else if (BaseConstant.TYPE_REGISTER_UNREAD == messageEvent.code) {
            //ConversationManagerKit.getInstance().addUnreadWatcher(this);
        } else if (BaseConstant.TYPE_MAIN == messageEvent.code) {
            ActivityUtils.finishToActivity(NewMainActivity::class.java, false)
            showFragment(HOME_TYPE, homeFragment)
        } else if (BaseConstant.TYPE_MESSAGE == messageEvent.code) {
            showFragment(MESSAGE_TYPE, messageFragment)
        } else if (BaseConstant.TYPE_SCHEDULE == messageEvent.code) {
            showFragment(APP_TYPE, appFragment)
        } else if (BaseConstant.TYPE_UPDATE_APP == messageEvent.code) {
            //模拟数据测试应用更新
            isShow = true
            //mvpPresenter.getVersionInfo()
        } else if (BaseConstant.TYPE_MESSAGE_TODO_NUM == messageEvent.code) {
            todoCount = messageEvent.count
            setMessageCount(todoCount + messageCount + noticeCount)
        } else if (BaseConstant.TYPE_NOTICE_NUM == messageEvent.code) {
            noticeCount = messageEvent.count
            setMessageCount(todoCount + messageCount + noticeCount)
        } else if (BaseConstant.TYPE_UPDATE_SCHEDULE_LIST_DATA == messageEvent.code) {
            //更新日程数据并存本地
            if (scheduleMangeViewModel != null) {
                scheduleMangeViewModel!!.getAllScheduleList()
            }
        } else if (BaseConstant.TYPE_HOME_CHECK_SCHEDULE == messageEvent.code) {
            showFragment(SCHEDULE_TYPE, scheduleFragment)
        }
    }

    private fun initViewModel() {
        //消息待办数
        viewModel.todoLiveData.observe(this) {
            val result = it.getOrNull()
            if (it.isSuccess) {
                val dataList = result?.list
                if (dataList != null && dataList.isNotEmpty()) {
                    messageCount = result.total
                    setMessageCount(todoCount + messageCount + noticeCount)
                }
            } else {
                it.exceptionOrNull()?.localizedMessage?.let { it1 ->
                    loge(it1)
                }
            }
        }
    }

    private fun registerMessageReceiver() {
        mMessageReceiver = MessageReceiver()
        val filter = IntentFilter()
        filter.priority = IntentFilter.SYSTEM_HIGH_PRIORITY
        filter.addAction(MESSAGE_RECEIVED_ACTION)
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter)
    }

    override fun updateUnread(count: Int) {
        Log.e("Chatim", "updateUnread==>: $count")
        messageCount = count
        setMessageCount(todoCount + messageCount + noticeCount)
        // 华为离线推送角标
        HUAWEIHmsMessageService.updateBadge(this, count)
    }

    private fun handleOfflinePush() {
        val isFromOfflinePush = intent.getBooleanExtra(Constants.IS_OFFLINE_PUSH_JUMP, false)
        if (isFromOfflinePush) {
            val baseLiveListener = TUIKitLiveListenerManager.getInstance().baseCallListener
            baseLiveListener?.handleOfflinePushCall(intent)
        }
    }

    private var messageCount = 0 //消息数量

    private var noticeCount = 0 //消息通知数量

    private var todoCount = 0 //代办数量

    private fun setMessageCount(count: Int) {
        //Log.e("Chatim", "setMessageCount==>: " + count);
        if (count > 0) {
            binding.msgTotalUnread.visibility = View.VISIBLE
        } else {
            binding.msgTotalUnread.visibility = View.GONE
        }
        var unreadStr = "" + count
        if (count >= 100) {
            unreadStr = "99+"
        }
        binding.msgTotalUnread.text = unreadStr
    }

    fun showError() {}

    fun getCopywriter(model: WeeklyDescBean) {
        if (model.code == BaseConstant.REQUEST_SUCCESS2) {
            if (model.data != null && model.data.size > 0) {
                val data = model.data
                Collections.addAll(data) //填充
                val set: Set<String> = HashSet(data)
                MMKV.defaultMMKV().encode(MMKVConstant.YD_WEEKLY_DESC, set)
            }
        }
    }

    fun getWeeklyDate(model: WeeklyDateBean) {
        if (model.code == BaseConstant.REQUEST_SUCCESS2) {
            if (model.data != null) {
                MMKV.defaultMMKV()
                    .encode(MMKVConstant.YD_WEEKLY_DATE, JSON.toJSONString(model.data))
            }
        }
    }

    fun getVersionInfo(rsp: GetAppVersionResponse) {
        Log.e("TAG", "getData==》: " + JSON.toJSONString(rsp))
        if (rsp.code == BaseConstant.REQUEST_SUCCESS2) {
            if (rsp.data != null) {
                download(rsp.data)
            } else if (isShow) {
                ToastUtils.showShort(R.string.newestVersion)
            }
        }
    }

    fun fail(msg: String?) {
        Log.e("TAG", "fail==》: " + JSON.toJSONString(msg))
    }

    inner class MessageReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION == intent.action) {
                    val messge = intent.getStringExtra(KEY_MESSAGE)
                    val extras = intent.getStringExtra(KEY_EXTRAS)
                    val showMsg = StringBuilder()
                    showMsg.append("$KEY_MESSAGE : ").append(messge).append("\n")
                    if (!ExampleUtil.isEmpty(extras)) {
                        showMsg.append("$KEY_EXTRAS : ").append(extras)
                            .append("\n")
                    }
                    Log.e("TAG", "setCustomMsg: $showMsg.toString()")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    override fun onResume() {
        Log.i(TAG, "onResume")
        BaseConstant.isForeground = true
        super.onResume()
        GSYVideoManager.onResume()
        if (mCallModel != null) {
            val impl =
                TRTCAVCallImpl.sharedInstance(BaseApplication.getInstance()) as TRTCAVCallImpl
            impl.stopCall()
            val info = V2TIMSignalingInfo()
            info.inviteID = mCallModel!!.callId
            info.inviteeList = mCallModel!!.invitedList
            info.groupID = mCallModel!!.groupId
            info.inviter = mCallModel!!.sender
            info.data = mCallModel!!.data
            (TRTCAVCallImpl.sharedInstance(BaseApplication.getInstance()) as TRTCAVCallImpl).processInvite(
                info.inviteID, info.inviter, info.groupID, info.inviteeList, info.data
            )
            mCallModel = null
        }
        handleOfflinePush()
    }

    /**
     * 打开应用通知设置
     */
    //@RequiresApi(api = Build.VERSION_CODES.O)
    private fun showNotificationPermission() {
        val enabled = NotifyUtil.checkNotificationsEnabled(this)
        var channelEnabled = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channelEnabled = NotifyUtil.checkNotificationsChannelEnabled(this, "1")
        }
        Log.e(
            TAG,
            "showNotificationPermission: enabled $enabled,channelEnabled $channelEnabled"
        )
        if (!enabled || !channelEnabled) {
            DialogUtil.notificationHintDialog(this, object : DialogUtil.OnClickListener {
                override fun onCancel(view: View) {}
                override fun onEnsure(view: View) {
                    NotifyUtil.openNotificationSettings(this@NewMainActivity)
                }
            })
        }
    }

    override fun onPause() {
        BaseConstant.isForeground = false
        super.onPause()
        GSYVideoManager.onPause()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        if (fg1 != null) {
//            fg1.onActivityResult(requestCode, resultCode, data);
//        }
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver)
        EventBus.getDefault().unregister(this)
        super.onDestroy()
        GSYVideoManager.releaseAllVideos()
    }

    private val mOkHttpClient = OkHttpClient()

    private fun getUserSig() {
        val body = RequestBody.create(BaseConstant.JSON, "")
        //请求组合创建
        val request = Request.Builder()
            .url(BaseConstant.API_SERVER_URL + "/management/cloud-system/im/getUserSig")
            .addHeader("Authorization", SpData.getLogin().getAccessToken())
            .post(body)
            .build()
        //发起请求
        mOkHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                hideLoading()
                Log.e(TAG, "getUserSigonFailure: $e")
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val data = response.body!!.string()
                Log.e(TAG, "getUserSig==>: $data")
                val bean = JSON.parseObject(data, UserSigRsp::class.java)
                if (bean.code == BaseConstant.REQUEST_SUCCESS2) {
                    SPUtils.getInstance().put(SpData.USERSIG, bean.data)
                    initIm(bean.data)
                } else {
                    hideLoading()
                    ToastUtils.showShort(bean.msg)
                }
            }
        })
    }

    private fun initIm(userSig: String) {
        TUIKit.login(SpData.getUserId() + "", userSig, object : IUIKitCallBack {
            override fun onError(module: String, code: Int, desc: String) {
                runOnUiThread {

                    //YDToastUtil.toastLongMessage("登录失败, errCode = " + code + ", errInfo = " + desc);
                    SPUtils.getInstance().put(
                        BaseConstant.LOGINNAME,
                        SPUtils.getInstance().getString(BaseConstant.LOGINNAME)
                    )
                    SPUtils.getInstance().put(
                        BaseConstant.PASSWORD,
                        SPUtils.getInstance().getString(BaseConstant.PASSWORD)
                    )
                    UserInfo.getInstance().isAutoLogin = false
                    UserInfo.getInstance().userSig = userSig
                    UserInfo.getInstance().userId = SpData.getUserId().toString()
                    Log.e(TAG, "initIm==>onSuccess: 腾讯IM激活失败code：$code")
                }
                DemoLog.i(TAG, "imLogin errorCode = $code, errorInfo = $desc")
            }

            override fun onSuccess(data: Any) {
                //腾讯IM离线调度
//                OfflineMessageBean bean = OfflineMessageDispatcher.parseOfflineMessage(getIntent());
//                 if (bean != null) {
//                    OfflineMessageDispatcher.redirect(bean);
//                }
                prepareThirdPushToken()
                SPUtils.getInstance().put(
                    BaseConstant.LOGINNAME,
                    SPUtils.getInstance().getString(BaseConstant.LOGINNAME)
                )
                SPUtils.getInstance().put(
                    BaseConstant.PASSWORD,
                    SPUtils.getInstance().getString(BaseConstant.PASSWORD)
                )
                UserInfo.getInstance().isAutoLogin = true
                UserInfo.getInstance().userSig = userSig
                UserInfo.getInstance().userId = SpData.getUserId().toString()
                Log.e(TAG, "initIm==>onSuccess: 腾讯IM激活成功")
                EventBus.getDefault().post(EventMessage(BaseConstant.TYPE_IM_LOGIN, ""))
            }
        })
    }

    private fun download(data: GetAppVersionResponse.DataBean) {
        LogUtil.d(
            "download",
            "device:" + AppUtils.getAppVersionCode() + "  data:" + data.versionCode
        )
        if (AppUtils.getAppVersionName().compareTo(data.versionCode) < 0) {
            NiceDialog.init().setLayoutId(R.layout.dialog_update)
                .setConvertListener(object : ViewConvertListener() {
                    override fun convertView(holder: ViewHolder, dialog: BaseNiceDialog) {
                        val cancel = holder.getView<TextView>(R.id.tv_cancel)
                        cancel.setOnClickListener { v: View? -> dialog.dismiss() }
                        val btnUpdate = holder.getView<LoadingButton>(R.id.btn_update)
                        downloadOrInstall(btnUpdate, cancel, data)
                        cancel.visibility = if (data.isCompulsory == 0) View.VISIBLE else View.GONE
                    }
                }).setDimAmount(0.5f).setOutCancel(data.isCompulsory == 0)
                .show(supportFragmentManager)
        } else if (isShow) {
            ToastUtils.showShort(R.string.newestVersion)
        }
    }

    private var max1: Long = 0

    private fun downloadOrInstall(
        btnUpdate: LoadingButton,
        cancel: TextView,
        data: GetAppVersionResponse.DataBean
    ) {
        btnUpdate.setOnClickListener { v: View? ->
            val rxPermissions = RxPermissions(this)
            mDisposable =
                rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe { granted: Boolean ->
                        if (granted) {
                            //后台下载APK并更新
                            cancel.visibility = View.GONE
                            btnUpdate.startLoading("")
                            btnUpdate.isClickable = false
                            AppClient.downloadFile(
                                "yide_app",
                                data.filePath,
                                object : AppClient.DownloadListener {
                                    override fun onStart(max: Long) {
                                        max1 = max
                                        //ToastUtils.showShort("开始下载");
                                        runOnUiThread { btnUpdate.stopLoading("start") }
                                    }

                                    override fun onProgress(progress: Long) {
                                        runOnUiThread { btnUpdate.setText("已下载 " + (progress.toFloat() / max1 * 100).toInt() + "%") }
                                    }

                                    override fun onSuccess() {
                                        runOnUiThread {
                                            btnUpdate.stopLoading("点击安装")
                                            btnUpdate.isClickable = true
                                            btnUpdate.setOnClickListener { v1: View? ->
                                                val file = File(
                                                    Utils.getApp()
                                                        .cacheDir,
                                                    "yide_app.apk"
                                                ) // 设置路径
                                                val command =
                                                    arrayOf(
                                                        "chmod",
                                                        "777",
                                                        file.path
                                                    )
                                                val builder =
                                                    ProcessBuilder(*command)
                                                try {
                                                    builder.start()
                                                } catch (e: IOException) {
                                                    e.printStackTrace()
                                                }
                                                val intent =
                                                    installIntent(file.path)
                                                intent?.let { startActivity(it) }
                                            }
                                            btnUpdate.performClick()
                                        }
                                    }

                                    override fun onFailure() {
                                        runOnUiThread {
                                            val file = File(
                                                Utils.getApp().cacheDir,
                                                "yide_app.apk"
                                            )
                                            if (file.exists()) {
                                                file.delete()
                                            }
                                            downloadOrInstall(btnUpdate, cancel, data)
                                            btnUpdate.isClickable = true
                                            btnUpdate.stopLoading("点击重试")
                                        }
                                        ToastUtils.showShort("更新失败")
                                    }
                                })
                        } else {
                            // 权限被拒绝
                            AlertDialog.Builder(this@NewMainActivity)
                                .setTitle("提示")
                                .setMessage(R.string.permission_file)
                                .setPositiveButton(
                                    "开启"
                                ) { dialog: DialogInterface?, which: Int ->
                                    val localIntent =
                                        Intent()
                                    localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    localIntent.action =
                                        "android.settings.APPLICATION_DETAILS_SETTINGS"
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

    private fun installIntent(path: String): Intent? {
        try {
            val file = File(path)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                intent.setDataAndType(
                    FileProvider.getUriForFile(
                        applicationContext, BuildConfig.APPLICATION_ID + ".FileProvider", file
                    ),
                    "application/vnd.android.package-archive"
                )
            } else {
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive")
            }
            return intent
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        supportFragmentManager.putFragment(outState, homeFragmentStr, homeFragment)
        supportFragmentManager.putFragment(outState, messageFragmentStr, messageFragment)
        supportFragmentManager.putFragment(outState, scheduleFragmentStr, scheduleFragment)
        supportFragmentManager.putFragment(outState, appFragmentStr, appFragment)
        supportFragmentManager.putFragment(outState, helpFragmentStr, helpFragment)
        super.onSaveInstanceState(outState)
    }

}