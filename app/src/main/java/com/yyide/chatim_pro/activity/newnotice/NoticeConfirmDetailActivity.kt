package com.yyide.chatim_pro.activity.newnotice

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim_pro.R
import com.yyide.chatim_pro.activity.PhotoViewActivity
import com.yyide.chatim_pro.base.BaseActivity
import com.yyide.chatim_pro.base.BaseConstant
import com.yyide.chatim_pro.databinding.ActivityNoticeConfirmDetailBinding
import com.yyide.chatim_pro.model.EventMessage
import com.yyide.chatim_pro.model.NoticeMyReleaseDetailBean
import com.yyide.chatim_pro.model.ResultBean
import com.yyide.chatim_pro.net.ApiCallback
import com.yyide.chatim_pro.utils.DateUtils
import com.yyide.chatim_pro.utils.GlideUtil
import io.reactivex.rxjava3.disposables.Disposable
import org.greenrobot.eventbus.EventBus

/**
 * 确认通知/已读通知 详情
 * 2021年6月28日
 * DESC: 是否已确认
 */
class NoticeConfirmDetailActivity : BaseActivity() {
    private lateinit var confirmDetailBinding: ActivityNoticeConfirmDetailBinding
    private lateinit var mAdapter: BaseQuickAdapter<String, BaseViewHolder>
    private val mDataList = java.util.ArrayList<String>()
    private var detailId: Long = 0

    override fun getContentViewID(): Int {
        return R.layout.activity_notice_confirm_detail
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        confirmDetailBinding = ActivityNoticeConfirmDetailBinding.inflate(layoutInflater)
        setContentView(confirmDetailBinding.root)
        initView()
        confirmDetailBinding.btnConfirm.setOnClickListener {
            showLoading()
            confirm()
        }

        confirmDetailBinding.ivNoticeImg.setOnClickListener {
//            NoticeImageDialog.showPreView(
//                this,
//                imgPath
//            )
            PhotoViewActivity.start(this, imgPath)
        }
        confirmDetailBinding.tvNoticeContent.movementMethod =
            ScrollingMovementMethod.getInstance()
        detailId = intent.getLongExtra("id", -1)
        getDetail(detailId)
    }

//    override fun onNewIntent(intent: Intent?) {
//        super.onNewIntent(intent)
//        if (intent != null) {
//            detailId = intent.getLongExtra("id", -1)
//            getDetail(detailId)
//        }
//    }

    private var imgPath = ""
    private fun getDetail(publishId: Long) {
        showLoading()
        addSubscription(
            mDingApiStores.confirmNoticeDetail(publishId),
            object : ApiCallback<NoticeMyReleaseDetailBean?>() {
                override fun onSuccess(model: NoticeMyReleaseDetailBean?) {
                    if (model != null && model.code == BaseConstant.REQUEST_SUCCESS) {
                        if (model.data != null) {
                            setDetailView(model)
                        } else {
                            showMessage()
                        }
                    }
                }

                override fun onFailure(msg: String) {
                    Log.e("onFailure", "---findTargetSnapPosition---")
                }

                override fun onFinish() {
                    hideLoading()
                }

                override fun onSubscribe(d: Disposable?) {
                    addSubscription(d)
                }

            })
    }

    private fun setDetailView(model: NoticeMyReleaseDetailBean) {
        if (model.data.type == 0) { //空白模板显示文本信息
            confirmDetailBinding.tvNoticeTitle.text = model.data.title
            confirmDetailBinding.tvNoticeContent.text =
                model.data.content
        } else {
            imgPath = model.data.imgpath
            confirmDetailBinding.clBlank.visibility = View.GONE
            confirmDetailBinding.clImg.visibility = View.VISIBLE
            GlideUtil.loadImageRadius(
                mActivity,
                model.data.imgpath,
                confirmDetailBinding.ivNoticeImg,
                SizeUtils.dp2px(4f)
            )
        }
        when {
            DateUtils.isToday(
                DateUtils.parseTimestamp(
                    model.data.timerDate,
                    ""
                )
            ) -> {//今天
                confirmDetailBinding.tvPushTime.text = getString(
                    R.string.notice_toDay,
                    DateUtils.formatTime(
                        model.data.timerDate,
                        "yyyy-MM-dd HH:mm",
                        "HH:mm"
                    )
                )
            }
            DateUtils.isYesterday(
                DateUtils.parseTimestamp(
                    model.data.timerDate,
                    ""
                )
            ) -> {//昨天
                confirmDetailBinding.tvPushTime.text = getString(
                    R.string.notice_yesterday,
                    DateUtils.formatTime(
                        model.data.timerDate,
                        "yyyy-MM-dd HH:mm",
                        "HH:mm"
                    )
                )
            }
            else -> {
                confirmDetailBinding.tvPushTime.text =
                    model.data.timerDate
            }
        }
        confirmDetailBinding.tvPushPeople.text = model.data.publisher
        confirmDetailBinding.btnConfirm.visibility = View.VISIBLE
        if (model.data.isConfirm) {
            if (!model.data.confirmOrRead) {
                confirmDetailBinding.btnConfirm.isClickable = true
                confirmDetailBinding.btnConfirm.setBackgroundResource(
                    R.drawable.bg_corners_blue_5
                )
                confirmDetailBinding.btnConfirm.text =
                    getString(R.string.notice_confirm_roger_that)
            } else {
                confirmDetailBinding.btnConfirm.isClickable = false
                confirmDetailBinding.btnConfirm.setBackgroundResource(
                    R.drawable.bg_corners_gray2_22
                )
                confirmDetailBinding.btnConfirm.text =
                    getString(R.string.notice_have_been_confirmed)
            }
        } else {
            confirmDetailBinding.btnConfirm.visibility = View.GONE
            confirm()
        }
    }

    private fun showMessage() {
        val builder = AlertDialog.Builder(this)
            .setTitle("提示")//设置对话框 标题
            .setMessage("该通知已撤回，无法查看")
            .setPositiveButton("确定") { dialog, which ->
                dialog.dismiss()
                finish()
            }
            .create()
            .show()
    }

    private fun confirm() {
        addSubscription(
            mDingApiStores.confirmNotice(detailId),
            object : ApiCallback<ResultBean?>() {
                override fun onSuccess(model: ResultBean?) {
                    if (model != null) {
                        if (model.code == BaseConstant.REQUEST_SUCCESS) {
                            confirmDetailBinding.btnConfirm.isClickable = false
                            confirmDetailBinding.btnConfirm.setBackgroundResource(R.drawable.bg_corners_gray2_22)
                            confirmDetailBinding.btnConfirm.text =
                                getString(R.string.notice_have_been_confirmed)
                            EventBus.getDefault().post(
                                EventMessage(
                                    BaseConstant.TYPE_NOTICE_CONFIRM_RECEIVER,
                                    "" + detailId
                                )
                            )
                        }
                    }
                }

                override fun onFailure(msg: String) {
                    Log.e("onFailure", "---findTargetSnapPosition---")
                }

                override fun onFinish() {
                    hideLoading()
                }

                override fun onSubscribe(d: Disposable?) {
                    addSubscription(d)
                }

            })
    }

    private fun initView() {
        confirmDetailBinding.include.title.setText(R.string.notice_announcement_title)
        confirmDetailBinding.include.backLayout.setOnClickListener { finish() }
        confirmDetailBinding.btnConfirm.visibility = View.INVISIBLE

        for (index in 0..20) {
            if (index % 2 == 0) {
                mDataList.add(
                    "$index 近期，国内多地出现散发病例，疫情动态再度牵动人心。为保障全校师生安全，根据上级部门指示精神，经学校领导研究决定，为有效防控疫情，现把学校疫情防控相关要求布置如下，敬请全校师生、来访家长遵照执行： 1、 全校教师、学生、来访家长及相关工作人员入校时必须佩戴口罩，查验体温正常后方可入校； 2、 各班级要做好二次测体温制度，体温正常者方可进入班级学习； 3、 各班做好请假登记制度，对班上请假学生请假原因和返校时间做好登记，如有异常情况立即报告学校政务处； 4、 严格执行离市报告制度：全校师生如要离开本市外的地方，教师提前向教务主任报告，学生提前向本班班主任报告，返回工作（学习）岗位时应查验行程码和健康绿码后方可入校。 疫情防控无小事，为了全校师生的生命健康，让我们一起克服困难，共克时艰。疫情防控无小事，为了全校师生的生命健康，让我们一起克服困难，共克时艰。疫情防控无小事，为了全校师生的生命健康，让我们一起克服困难，共克时艰。疫情防控无小事，为了全校师生的生命健康，让我们一起克服困难，共克时艰。近期，国内多地出现散发病例，疫情动态再度牵动人心。为保障全校师生安全，根据上级部门指示精神，经学校领导研究决定，为有效防控疫情，现把学校疫情防控相关要求布置如下，敬请全校师生、来访家长遵照执行：\n" +
                            "1、 全校教师、学生、来访家长及相关工作人员入校时必须佩戴口罩，查验体温正常后方可入校；\n" +
                            "2、 各班级要做好二次测体温制度，体温正常者方可进入班级学习；\n" +
                            "3、 各班做好请假登记制度，对班上请假学生请假原因和返校时间做好登记，如有异常情况立即报告学校政务处；\n" +
                            "4、 严格执行离市报告制度：全校师生如要离开本市外的地方，教师提前向教务主任报告，学生提前向本班班主任报告，返回工作（学习）岗位时应查验行程码和健康绿码后方可入校。\n" +
                            "疫情防控无小事，为了全校师生的生命健康，让我们一起克服困难，共克时艰。疫情防控无小事，为了全校师生的生命健康，让我们一起克服困难，共克时艰。疫情防控无小事，为了全校师生的生命健康，让我们一起克服困难，共克时艰。疫情防控无小事，为了全校师生的生命健康，让我们一起克服困难，共克时艰。\n"
                )
            } else {
                mDataList.add("$index 近期，国内多地出现散发病例，疫情动态再度牵动人心。为保障全校师生安全，根据上级部门指示精神，经学校领导研究决定，为有效防控疫情，现把学校疫情防控相关要求布置如下，敬请全校师生、来访家长遵照执行： 1、 全校教师、学生、来访家长及相关工作人员入校时必须佩戴口罩，查验体温正常后方可入校； 2、 各班级要做好二次测体温制度，体温正常者方可进入班级学习； 3、 各班做好请假登记制度，对班上请假学生请假原因和返校时间做好登记，如有异常情况立即报告学校政务处； 4、 严格执行离市报告制度：全校师生如要离开本市外的地方，教师提前向教务主任报告，学生提前向本班班主任报告，返回工作（学习）岗位时应查验行程码和健康绿码后方可入校。 疫情防控无小事，为了全校师生的生命健康，让我们一起克服困难，共克时艰。疫情防控无小事，为了全校师生的生命健康，让我们一起克服困难，共克时艰。疫情防控无小事，为了全校师生的生命健康，让我们一起克服困难，共克时艰。疫情防控无小事，为了全校师生的生命健康，让我们一起克服困难，共克时艰。近期，国内多地出现散发病例，疫情动态再度牵动人心。为保障全校师生安全，根据上级部门指示精神，经学校领导研究决定，为有效防控疫情，现把学校疫情防控相关要求布置如下，敬请全校师生、来访家长遵照执行：\n")
            }
        }


        // ---RecyclerView---
//        confirmDetailBinding?.recyclerView?.isNestedScrollingEnabled = false
        // PagerSnapHelper
        val snapHelper: PagerSnapHelper = object : PagerSnapHelper() {
            // 在 Adapter的 onBindViewHolder 之后执行
            override fun findTargetSnapPosition(
                layoutManager: RecyclerView.LayoutManager,
                velocityX: Int,
                velocityY: Int
            ): Int {
                // TODO 找到对应的Index
                Log.e("xiaxl: ", "---findTargetSnapPosition---")
                val targetPos = super.findTargetSnapPosition(layoutManager, velocityX, velocityY)
                Log.e("xiaxl: ", "targetPos: $targetPos")
//                Toast.makeText(this@NoticeConfirmDetailActivity, "滑到到 " + targetPos + "位置", Toast.LENGTH_SHORT)
//                        .show()
                return targetPos
            }

            // 在 Adapter的 onBindViewHolder 之后执行
            override fun findSnapView(layoutManager: RecyclerView.LayoutManager): View {
                // TODO 找到对应的View
                Log.e("xiaxl: ", "---findSnapView---")
                val view = super.findSnapView(layoutManager)
                Log.e("xiaxl: ", "tag: " + view!!.tag)
                return view
            }
        }
        snapHelper.attachToRecyclerView(confirmDetailBinding.recyclerView)
        // ---布局管理器---
        val linearLayoutManager = LinearLayoutManager(this)
        // 默认是Vertical (HORIZONTAL则为横向列表)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        confirmDetailBinding.recyclerView.layoutManager = linearLayoutManager
        // TODO 这么写是为了获取RecycleView的宽高
        confirmDetailBinding.recyclerView.viewTreeObserver?.addOnGlobalLayoutListener(object :
            OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                confirmDetailBinding.recyclerView.viewTreeObserver.removeOnGlobalLayoutListener(
                    this
                )
                /**
                 * 这么写是为了获取RecycleView的宽高
                 */
            }
        })
        // 创建Adapter，并指定数据集
        mAdapter =
            object : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_notice_confirm_detail) {
                override fun convert(holder: BaseViewHolder, item: String) {
                    holder.setText(R.id.tv_notice_content, item)
                        .setText(R.id.tv_notice_title, "节假日通知")
                    val text = holder.getView<TextView>(R.id.tv_notice_content);
                    text.movementMethod = ScrollingMovementMethod.getInstance()
                    val btnConfirm = holder.getView<Button>(R.id.btn_confirm)

                    btnConfirm.setOnClickListener {

                    }
                }
            }
        // 设置Adapter
        confirmDetailBinding.recyclerView.adapter = mAdapter
        mAdapter.setList(mDataList)
    }
}