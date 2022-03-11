package com.yyide.chatim.activity.newnotice

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.blankj.utilcode.util.SizeUtils
import com.yyide.chatim.R
import com.yyide.chatim.activity.PhotoViewActivity
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.base.BaseMvpActivity
import com.yyide.chatim.databinding.ActivityNoticeDetail2Binding
import com.yyide.chatim.model.EventMessage
import com.yyide.chatim.model.NoticeBlankReleaseBean
import com.yyide.chatim.model.NoticeMyReleaseDetailBean
import com.yyide.chatim.model.ResultBean
import com.yyide.chatim.presenter.NoticeDetailPresenter
import com.yyide.chatim.utils.DateUtils
import com.yyide.chatim.utils.GlideUtil
import com.yyide.chatim.view.NoticeDetailView
import org.greenrobot.eventbus.EventBus

/**
 * desc （通知公告）我的发布通知详情
 * author lrz
 * time 2021年6月19日
 */
class NoticeDetailActivity : BaseMvpActivity<NoticeDetailPresenter>(), NoticeDetailView {
    private lateinit var detailBinding: ActivityNoticeDetail2Binding
    private var itemBean: NoticeMyReleaseDetailBean.DataBean? = null
    private val rangeList = ArrayList<NoticeBlankReleaseBean.RecordListBean>()

    override fun getContentViewID(): Int {
        return R.layout.activity_notice_detail2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailBinding = ActivityNoticeDetail2Binding.inflate(layoutInflater)
        setContentView(detailBinding.root)
        initView()
        val id = intent.getLongExtra("id", -1)
        mvpPresenter.getPublishDetail(id)
    }

    private fun initView() {
        detailBinding.include.title.setText(R.string.notice_my_push_title)
        detailBinding.include.backLayout.setOnClickListener { finish() }
        detailBinding.clReadUnread.setOnClickListener {
            itemBean?.isConfirm?.let { it1 ->
                NoticeUnConfirmListActivity.start(
                    this, itemBean?.id
                        ?: 0, itemBean?.confirmOrReadNum
                        ?: 0, it1
                )
            }
        }

        detailBinding.ivBg.setOnClickListener {
            itemBean?.imgpath?.let { it1 ->
                PhotoViewActivity.start(this, it1)
            }
        }
        detailBinding.clRange.setOnClickListener {
            val intent =
                Intent(NoticeGeneralActivity@ this, NoticeDesignatedPersonnelActivity::class.java)
            intent.putParcelableArrayListExtra("list", rangeList)
            intent.putExtra("noticeDetail", true)
            startActivity(intent)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setDate(item: NoticeMyReleaseDetailBean.DataBean) {
        itemBean = item
        setRangeList()
        detailBinding.tvNoticeTitle.text = item.title
        detailBinding.tvNoticeContent.text = item.content
        detailBinding.tvNoticeContent.movementMethod = ScrollingMovementMethod.getInstance()
        val timeDate = when {
            DateUtils.isToday(DateUtils.parseTimestamp(item.timerDate, "")) -> {//今天
                getString(
                    R.string.notice_toDay,
                    DateUtils.formatTime(item.timerDate, "yyyy-MM-dd HH:mm:ss", "HH:mm")
                )
            }
            DateUtils.isYesterday(DateUtils.parseTimestamp(item.timerDate, "")) -> {//昨天
                getString(
                    R.string.notice_yesterday,
                    DateUtils.formatTime(item.timerDate, "yyyy-MM-dd HH:mm:ss", "HH:mm")
                )
            }
            else -> {
                item.timerDate
            }
        }
        if (item.isTimer) {
            detailBinding.tvPushDesc.text =
                getString(R.string.notice_timing_push) + "\t\t" + timeDate
        } else {
            detailBinding.tvPushDesc.text =
                getString(R.string.notice_immediately_push) + "\t\t" + timeDate //通知公告类型 0空白模板 1非空白模板
        }
        if (item.type == 0) {
            detailBinding.constraintLayout.visibility = View.VISIBLE
        } else {
            detailBinding.constraintLayout.visibility = View.GONE
            if (!TextUtils.isEmpty(item.imgpath)) {
                GlideUtil.loadImageRadius(baseContext, item.imgpath, detailBinding!!.ivBg, SizeUtils.dp2px(4f))
            }
        }
        var teacherNumber: Int = 0
        var patriarchNumber: Int = 0
        var brandClassNumber: Int = 0
        //通知范围
        if (item.notifyRange == 1) {//指定 部门发布
            val stringBuffer = StringBuffer()
            item.notifies.forEachIndexed { _, notifiesBean ->
                when (notifiesBean.specifieType) {
                    0 -> {
                        notifiesBean.list.forEachIndexed { _, listBean ->
                            teacherNumber += listBean.nums
                        }
                    }
                    1 -> {
                        notifiesBean.list.forEachIndexed { _, listBean ->
                            patriarchNumber += listBean.nums
                        }
                    }
                    2, 3 -> {
                        if (notifiesBean.list != null) {
                            brandClassNumber = notifiesBean.list.size
                        }
                    }
                }
            }
            if (teacherNumber > 0) {
                stringBuffer.append(getString(R.string.notice_teacher_number, teacherNumber))
                    .append("、")
            }
            if (patriarchNumber > 0) {
                stringBuffer.append(getString(R.string.notice_patriarch_number, patriarchNumber))
                    .append("、")
            }
            if (brandClassNumber > 0) {
                stringBuffer.append(
                    getString(
                        R.string.notice_brand_check_class_number,
                        brandClassNumber
                    )
                )
            }
            if (!TextUtils.isEmpty(stringBuffer.toString()) && stringBuffer.toString()
                    .endsWith("、")
            ) {
                detailBinding.tvNotificationRange.text = stringBuffer.toString().removeSuffix("、")
            } else {
                detailBinding.tvNotificationRange.text = stringBuffer.toString()
            }
        } else {
            detailBinding.tvNotificationRange.text = "全校"
        }

        //已读/未读 确认
        if (item.isConfirm) {
            detailBinding.tvReadDesc.text = getString(R.string.notice_confirm_result)
        } else {
            detailBinding.tvReadDesc.text = getString(R.string.notice_read)
        }
        detailBinding.tvRead.text = getString(
            R.string.dividing_line,
            item.confirmOrReadNum,
            item.totalNum
        )

//            detailBinding!!.tvRead.text = item.
        detailBinding.btnCommit.isClickable = false
        if (item.isRetract) {//是否已撤回
            detailBinding.btnToWithdraw.visibility = View.GONE
            detailBinding.btnCommit.visibility = View.VISIBLE
            detailBinding.clReadUnread.visibility = View.GONE
        } else {
            if (item.isTimer) {//定时发布处理 未发布的不展示确认人数
                val times = DateUtils.parseTimestamp(item.timerDate, "yyyy-MM-dd HH:mm:ss")
                if (times >= System.currentTimeMillis())
                    detailBinding.clReadUnread.visibility = View.GONE
            }
            if (item.totalNum <= 0)
                detailBinding.clReadUnread.visibility = View.GONE

            detailBinding.btnToWithdraw.visibility = View.VISIBLE
            detailBinding.btnCommit.visibility = View.GONE
        }
        detailBinding.btnToWithdraw.setOnClickListener {
            showMessage(item.id)
        }
    }

    private fun setRangeList() {
        if (itemBean?.notifies != null) {
            itemBean?.notifies?.forEach { item ->
                val paramsItem = NoticeBlankReleaseBean.RecordListBean()
                paramsItem.specifieType = item.specifieType.toString()
                val listBean = ArrayList<NoticeBlankReleaseBean.RecordListBean.ListBean>()
                if (item.list != null) {
                    item.list.forEach { childItem ->
                        val item = NoticeBlankReleaseBean.RecordListBean.ListBean()
                        item.specifieId = childItem.specifieId
                        item.specifieParentId = childItem.specifieParentId
                        item.type = childItem.type.toString()
                        item.nums = childItem.nums
                        listBean.add(item)
                    }
                }
                paramsItem.list = listBean
                rangeList.add(paramsItem)
            }
        }
    }

    private fun showMessage(id: Long) {
        val builder = AlertDialog.Builder(this)
            .setTitle("提示")//设置对话框 标题
            .setMessage("通知撤回后，接收方将不展示通知内容，请确定撤回？")
            .setPositiveButton("确定") { dialog, which ->
                mvpPresenter.retract(id)
                dialog.dismiss()
            }
            .setNegativeButton("取消") { dialog, which -> dialog.dismiss() }
            .create()
            .show()
    }

    override fun createPresenter(): NoticeDetailPresenter {
        return NoticeDetailPresenter(this)
    }

    override fun showError() {

    }

    override fun retractSuccess(model: ResultBean?) {
        if (model != null) {
            if (model.code == BaseConstant.REQUEST_SUCCESS) {
                detailBinding.btnToWithdraw.visibility = View.GONE
                detailBinding.btnCommit.visibility = View.VISIBLE
                detailBinding.btnCommit.isClickable = false
                EventBus.getDefault()
                    .post(EventMessage(BaseConstant.TYPE_UPDATE_NOTICE_MY_RELEASE, ""))
            }
        }
    }

    override fun getMyReceivedList(model: NoticeMyReleaseDetailBean?) {
        if (model != null) {
            if (model.code == BaseConstant.REQUEST_SUCCESS) {
                setDate(model.data)
            }
        }
    }

    override fun getMyReceivedFail(msg: String?) {
        Log.d("NoticeDetailActivity", "getMyReceivedFail$msg");
    }
}