package com.yyide.chatim.activity.newnotice

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.yyide.chatim.R
import com.yyide.chatim.base.BaseActivity
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.base.BaseMvpActivity
import com.yyide.chatim.databinding.ActivityNoticeDetail2Binding
import com.yyide.chatim.model.EventMessage
import com.yyide.chatim.model.NoticeItemBean
import com.yyide.chatim.model.NoticeMyReleaseDetailBean
import com.yyide.chatim.model.ResultBean
import com.yyide.chatim.presenter.NoticeDetailPresenter
import com.yyide.chatim.presenter.NoticeMyReleasePresenter
import com.yyide.chatim.utils.GlideUtil
import com.yyide.chatim.view.NoticeDetailView
import org.greenrobot.eventbus.EventBus

/**
 * desc （通知公告）我的发布通知详情
 * author lrz
 * time 2021年6月19日
 */
class NoticeDetailActivity : BaseMvpActivity<NoticeDetailPresenter>(), NoticeDetailView {
    private var detailBinding: ActivityNoticeDetail2Binding? = null
    private var itemBean: NoticeMyReleaseDetailBean.DataBean? = null

    override fun getContentViewID(): Int {
        return R.layout.activity_notice_detail2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailBinding = ActivityNoticeDetail2Binding.inflate(layoutInflater)
        setContentView(detailBinding!!.root)
        initView()
        val id = intent.getLongExtra("id", -1)
        mvpPresenter.getPublishDetail(id)
    }

    private fun initView() {
        detailBinding!!.include.title.setText(R.string.notice_my_push_title)
        detailBinding!!.include.backLayout.setOnClickListener { finish() }
        detailBinding!!.clReadUnread.setOnClickListener {
            NoticeUnConfirmListActivity.start(this, itemBean?.id ?: 0);
        }
    }

    private fun setDate(item: NoticeMyReleaseDetailBean.DataBean) {
        itemBean = item
        if (item != null) {
            detailBinding!!.tvNoticeTitle.text = item.title
            detailBinding!!.tvNoticeContent.text = item.content

            if (!TextUtils.isEmpty(item.timerDate)) {
                detailBinding!!.tvPushDesc.text = getString(R.string.notice_timing_push) + "\t\t" + item.timerDate
            } else {
                detailBinding!!.tvPushDesc.text = if (item.type == 0) "空白模板" else "模板"  //通知公告类型 0空白模板 1非空白模板
            }

            if (!TextUtils.isEmpty(item.imgpath)) {
                GlideUtil.loadImageRadius(baseContext, item.imgpath, detailBinding!!.ivBg, 8)
            }
            var teacherNumber: Int = 0
            var patriarchNumber: Int = 0
            var brandClassNumber: Int = 0
            var brandSiteNumber: Int = 0

            //通知范围
            if (item.notifyRange == 1) {//指定 部门发布
                var stringBuffer = StringBuffer()
                item.notifies.forEachIndexed { index, notifiesBean ->
                    when (notifiesBean.specifieType) {
                        0 -> {
                            notifiesBean.list.forEachIndexed { index, listBean ->
                                teacherNumber += listBean.nums
                            }
                        }
                        1 -> {
                            notifiesBean.list.forEachIndexed { index, listBean ->
                                patriarchNumber += listBean.nums
                            }
                        }
                        2 -> {
                            if (notifiesBean.list != null) {
                                brandClassNumber = notifiesBean.list.size
                            }
                        }
                        3 -> {
                            if (notifiesBean.list != null) {
                                brandSiteNumber = notifiesBean.list.size
                            }
                        }
                    }

                }
                if (brandClassNumber > 0) {
                    stringBuffer.append(getString(R.string.notice_brand_check_class_number, brandClassNumber)).append("、")
                }
                if (patriarchNumber > 0) {
                    stringBuffer.append(getString(R.string.notice_patriarch_number, patriarchNumber)).append("、")
                }
                if (teacherNumber > 0) {
                    stringBuffer.append(getString(R.string.notice_teacher_number, teacherNumber)).append("、")
                }
                if (brandSiteNumber > 0) {
                    stringBuffer.append(getString(R.string.notice_brand_site_number, brandSiteNumber))
                }
                detailBinding!!.tvNotificationRange.text = stringBuffer.toString()
            } else {
                detailBinding!!.tvNotificationRange.text = "全校"
            }

            //已读/未读 确认
            if (item.isConfirm) {
                detailBinding!!.tvReadDesc.text = getString(R.string.notice_confirm_result)
            } else {
                detailBinding!!.tvReadDesc.text = getString(R.string.notice_read)
            }
            detailBinding!!.tvRead.text = getString(R.string.dividing_line, item.confirmOrReadNum, item.totalNum)


//            detailBinding!!.tvRead.text = item.
            detailBinding!!.btnCommit.isClickable = false
            if (item.isRetract) {//是否已撤回
                detailBinding!!.btnToWithdraw.visibility = View.GONE
                detailBinding!!.btnCommit.visibility = View.VISIBLE
            } else {
                detailBinding!!.btnToWithdraw.visibility = View.VISIBLE
                detailBinding!!.btnCommit.visibility = View.GONE
            }
            detailBinding!!.btnToWithdraw.setOnClickListener {
                mvpPresenter.retract(item.id)
            }
        }
    }

    override fun createPresenter(): NoticeDetailPresenter {
        return NoticeDetailPresenter(this)
    }

    override fun showError() {

    }

    override fun retractSuccess(model: ResultBean?) {
        if (model != null) {
            if (model.code == BaseConstant.REQUEST_SUCCES2) {
                detailBinding!!.btnToWithdraw.visibility = View.GONE
                detailBinding!!.btnCommit.visibility = View.VISIBLE
                detailBinding!!.btnCommit.isClickable = false
                EventBus.getDefault().post(EventMessage(BaseConstant.TYPE_UPDATE_NOTICE_MY_RELEASE, ""))
            }
        }
    }

    override fun getMyReceivedList(model: NoticeMyReleaseDetailBean?) {
        if (model != null) {
            if (model.code == BaseConstant.REQUEST_SUCCES2) {
                setDate(model.data)
            }
        }
    }

    override fun getMyReceivedFail(msg: String?) {
        Log.d("NoticeDetailActivity", "getMyReceivedFail$msg");
    }
}