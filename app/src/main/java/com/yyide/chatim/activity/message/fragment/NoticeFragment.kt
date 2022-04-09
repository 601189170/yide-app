package com.yyide.chatim.activity.message.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.activity.message.NoticeContentActivity
import com.yyide.chatim.activity.message.PublishContentActivity
import com.yyide.chatim.activity.message.viewmodel.NoticeViewModel
import com.yyide.chatim.base.KTBaseFragment
import com.yyide.chatim.databinding.FragmentMessageNoticeBinding
import com.yyide.chatim.databinding.ItemMessageContentBinding
import com.yyide.chatim.dialog.TableWeekPopUp
import com.yyide.chatim.model.message.AcceptMessageItem
import com.yyide.chatim.model.message.EventMessageBean
import com.yyide.chatim.model.table.ChildrenItem
import com.yyide.chatim.utils.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import razerdp.basepopup.BasePopupWindow
import kotlin.math.log


/**
 *
 * @author shenzhibin
 * @date 2022/4/6 15:35
 * @description 信息发布-通知公告
 */
class NoticeFragment :
    KTBaseFragment<FragmentMessageNoticeBinding>(FragmentMessageNoticeBinding::inflate) {

    private lateinit var dataAdapter: BaseQuickAdapter<AcceptMessageItem, BaseViewHolder>
    private val viewModel by viewModels<NoticeViewModel>()

    private val size = 20
    private var current = 1


    private val contentPopUp: TableWeekPopUp by lazy {
        TableWeekPopUp(this)
    }

    private val contentTimePopUp: TableWeekPopUp by lazy {
        TableWeekPopUp(this)
    }

    companion object {
        fun newInstance(): NoticeFragment {
            val fragment = NoticeFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initView()
        initListener()
        request()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventMessageHandle(item: EventMessageBean) {
        if (item.type != 0 || dataAdapter.data.isNullOrEmpty()) {
            return
        }
        val index = dataAdapter.data.indexOfFirst { it.id == item.item.id }
        dataAdapter.data[index] = item.item
        if (index != -1) {
            dataAdapter.notifyItemChanged(index, null)
        }

    }

    private fun initData() {
        viewModel.mContentList.clear()
        viewModel.mContentList.add(ChildrenItem("我收到的", id = viewModel.noticeTypeByReceive))
        viewModel.mContentList.add(ChildrenItem("我发布的", id = viewModel.noticeTypeByPublish))
        //viewModel.mContentList.add(ChildrenItem("所有发布", id = "2"))

        viewModel.mContentTimeList.clear()
        viewModel.mContentTimeList.add(ChildrenItem("今日", id = viewModel.timeByToday))
        viewModel.mContentTimeList.add(ChildrenItem("本周", id = viewModel.timeByWeek))
        viewModel.mContentTimeList.add(ChildrenItem("本月", id = viewModel.timeByMonth))
        //viewModel.mContentTimeList.add(ChildrenItem("本学期", id = "3"))

        viewModel.selectContent = viewModel.mContentList.first()
        viewModel.selectContentTime = viewModel.mContentTimeList.first()
    }

    private fun initView() {

        if (!viewModel.isTeacher) {
            binding.messageNoticeContentTv.hide()
            binding.messageNoticeContentIv.hide()
        }

        dataAdapter =
            object :
                BaseQuickAdapter<AcceptMessageItem, BaseViewHolder>(R.layout.item_message_content),
                LoadMoreModule {
                override fun convert(holder: BaseViewHolder, item: AcceptMessageItem) {
                    val viewBind = ItemMessageContentBinding.bind(holder.itemView)
                    if (viewModel.selectContent?.id != viewModel.noticeTypeByReceive && item.isTop) {
                        viewBind.itemMessageContentTopIv.show()
                    }
                    viewBind.itemMessageContentTitleTv.text = item.title
                    val subStr = "${item.identityUserName}发布于"
                    viewBind.itemMessageContentSubTv.text = subStr
                    viewBind.itemMessageContentSubTimeTv.text = item.timerDate
                    when (viewModel.selectContent?.id) {
                        viewModel.noticeTypeByReceive -> {
                            if (item.isView) {
                                viewBind.itemMessageContentTitleIv.remove()
                            }else{
                                viewBind.itemMessageContentTitleIv.show()
                            }
                            if (item.isNeedConfirm) {
                                if (item.isConfirm) {
                                    viewBind.itemMessageContentStateIv.hide()
                                    viewBind.itemMessageContentStateTv.text = "已确认"
                                    viewBind.itemMessageContentStateTv.setTextColor(R.color.black11.asColor())
                                } else {
                                    viewBind.itemMessageContentStateIv.show()
                                    viewBind.itemMessageContentStateTv.text = "去确认"
                                    viewBind.itemMessageContentStateTv.setTextColor(R.color.punch_normal.asColor())
                                }
                            }else{
                                viewBind.itemMessageContentStateTv.remove()
                                viewBind.itemMessageContentStateIv.remove()
                            }
                        }
                        viewModel.noticeTypeByPublish -> {
                            viewBind.itemMessageContentStateTv.show()
                            viewBind.itemMessageContentStateIv.show()
                            viewBind.itemMessageContentTitleIv.remove()
                            if (TimeUtil.isDateOver3(item.timerDate)) {
                                viewBind.itemMessageContentStateIv.hide()
                                viewBind.itemMessageContentStateTv.text = "已发布"
                                viewBind.itemMessageContentStateTv.setTextColor(R.color.black11.asColor())
                                viewBind.itemMessageContentSubTimeTv.setTextColor(R.color.black10.asColor())
                            } else {
                                viewBind.itemMessageContentStateIv.hide()
                                viewBind.itemMessageContentStateTv.text = "待发布"
                                viewBind.itemMessageContentStateTv.setTextColor(R.color.not_publish_color.asColor())
                                viewBind.itemMessageContentSubTimeTv.setTextColor(R.color.not_publish_color.asColor())
                            }
                        }
                    }
                }
            }

        dataAdapter.loadMoreModule.isAutoLoadMore = true
        dataAdapter.loadMoreModule.isEnableLoadMoreIfNotFullPage = true
        binding.messageNoticeRv.layoutManager = LinearLayoutManager(activity)
        binding.messageNoticeRv.adapter = dataAdapter
        dataAdapter.setEmptyView(R.layout.empty)
        dataAdapter.loadMoreModule.setOnLoadMoreListener {
            logd("current = $current")
            dataAdapter.loadMoreModule.isEnableLoadMore = true
            current++
            request()
        }


        dataAdapter.setOnItemClickListener { adapter, view, position ->
            val jumpData = adapter.data[position] as AcceptMessageItem
            if (viewModel.selectContent?.id == viewModel.noticeTypeByReceive) {
                jumpData.isView = true
                dataAdapter.notifyItemChanged(position, null)
                NoticeContentActivity.startGo(requireContext(), jumpData)
            } else {
                PublishContentActivity.startGo(requireContext(), jumpData)
            }
        }


        contentPopUp.popupGravity = Gravity.BOTTOM
        contentPopUp.onDismissListener = object : BasePopupWindow.OnDismissListener() {
            override fun onDismiss() {
                binding.messageNoticeContentTv.setTextColor(R.color.black9.asColor())
                binding.messageNoticeContentIv.setImageResource(R.mipmap.table_week_button)
            }
        }


        contentTimePopUp.popupGravity = Gravity.BOTTOM
        contentTimePopUp.onDismissListener = object : BasePopupWindow.OnDismissListener() {
            override fun onDismiss() {
                binding.messageNoticeContentTimeTv.setTextColor(R.color.black9.asColor())
                binding.messageNoticeContentTimeIv.setImageResource(R.mipmap.table_week_button)
            }
        }

        contentPopUp.setData(viewModel.mContentList, viewModel.selectContent)
        binding.messageNoticeContentTv.text = viewModel.selectContent?.name ?: "我收到的"
        contentTimePopUp.setData(viewModel.mContentTimeList, viewModel.selectContentTime)
        binding.messageNoticeContentTimeTv.text = viewModel.selectContentTime?.name ?: "今日"


        viewModel.acceptMessage.observe(requireActivity()) {
            binding.messageNoticeSfl.isRefreshing = false
            if (current == 1) {
                dataAdapter.setList(it.acceptMessage)
            } else {
                dataAdapter.addData(it.acceptMessage)
            }
            if (it.acceptMessage.size < size) {
                //如果不够一页,显示没有更多数据布局
                dataAdapter.loadMoreModule.loadMoreEnd()
            } else {
                dataAdapter.loadMoreModule.loadMoreComplete()
            }
        }

        binding.messageNoticeSfl.setOnRefreshListener {
            current = 1
            request()
        }
        binding.messageNoticeSfl.setColorSchemeColors(requireActivity().resources.getColor(R.color.colorPrimary))


    }

    private fun initListener() {

        binding.messageNoticeContentTv.setOnClickListener {
            if (contentPopUp.isShowing) {
                contentPopUp.dismiss()
            } else {
                binding.messageNoticeContentTv.setTextColor(R.color.colorAccent.asColor())
                binding.messageNoticeContentIv.setImageResource(R.mipmap.table_week_button_pack)
                contentPopUp.showPopupWindow(it)
            }
        }
        binding.messageNoticeContentIv.setOnClickListener {
            if (contentPopUp.isShowing) {
                contentPopUp.dismiss()
            } else {
                binding.messageNoticeContentTv.setTextColor(R.color.colorAccent.asColor())
                binding.messageNoticeContentIv.setImageResource(R.mipmap.table_week_button_pack)
                contentPopUp.showPopupWindow(it)
            }
        }


        binding.messageNoticeContentTimeTv.setOnClickListener {
            if (contentTimePopUp.isShowing) {
                contentTimePopUp.dismiss()
            } else {
                binding.messageNoticeContentTimeTv.setTextColor(R.color.colorAccent.asColor())
                binding.messageNoticeContentTimeIv.setImageResource(R.mipmap.table_week_button_pack)
                contentTimePopUp.showPopupWindow(it)
            }
        }
        binding.messageNoticeContentTimeIv.setOnClickListener {
            if (contentTimePopUp.isShowing) {
                contentTimePopUp.dismiss()
            } else {
                binding.messageNoticeContentTimeTv.setTextColor(R.color.colorAccent.asColor())
                binding.messageNoticeContentTimeIv.setImageResource(R.mipmap.table_week_button_pack)
                contentTimePopUp.showPopupWindow(it)
            }
        }

        contentPopUp.setSubmitCallBack(object : TableWeekPopUp.SubmitCallBack {
            override fun getSubmitData(data: ChildrenItem?) {
                if (data != null) {
                    viewModel.selectContent = data
                    binding.messageNoticeContentTv.text = data.name
                    current = 1
                    request()
                }
            }
        })


        contentTimePopUp.setSubmitCallBack(object : TableWeekPopUp.SubmitCallBack {
            override fun getSubmitData(data: ChildrenItem?) {
                if (data != null) {
                    viewModel.selectContentTime = data
                    binding.messageNoticeContentTimeTv.text = data.name
                    current = 1
                    request()
                }
            }
        })
    }

    fun request() {
        when (viewModel.selectContent?.id) {
            viewModel.noticeTypeByReceive -> {
                logd("current = $current")
                viewModel.requestAcceptMessage(current, size)
            }
            viewModel.noticeTypeByPublish -> {
                viewModel.requestPublishMessage(current, size)
            }
            "2" -> {

            }
        }
    }

}