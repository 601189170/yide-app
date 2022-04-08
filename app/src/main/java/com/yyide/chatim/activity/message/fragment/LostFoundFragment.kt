package com.yyide.chatim.activity.message.fragment

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
import com.yyide.chatim.activity.message.viewmodel.LostFoundViewModel
import com.yyide.chatim.base.KTBaseFragment
import com.yyide.chatim.databinding.FragmentMessageLoseBinding
import com.yyide.chatim.databinding.FragmentMessageNoticeBinding
import com.yyide.chatim.databinding.ItemMessageContentBinding
import com.yyide.chatim.dialog.TableWeekPopUp
import com.yyide.chatim.model.message.AcceptMessageItem
import com.yyide.chatim.model.table.ChildrenItem
import com.yyide.chatim.utils.*
import razerdp.basepopup.BasePopupWindow

/**
 *
 * @author shenzhibin
 * @date 2022/4/6 15:35
 * @description 信息发布-失物招领
 */
class LostFoundFragment :
    KTBaseFragment<FragmentMessageNoticeBinding>(FragmentMessageNoticeBinding::inflate) {


    private lateinit var dataAdapter: BaseQuickAdapter<AcceptMessageItem, BaseViewHolder>
    private val viewModel by viewModels<LostFoundViewModel>()

    private val size = 20
    private var current = 1


    private val contentPopUp: TableWeekPopUp by lazy {
        TableWeekPopUp(this)
    }

    private val contentTimePopUp: TableWeekPopUp by lazy {
        TableWeekPopUp(this)
    }

    companion object {
        fun newInstance(): LostFoundFragment {
            val fragment = LostFoundFragment()
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
                    viewBind.itemMessageContentTitleTv.text = item.title
                    val subStr = "${item.identityUserName}发布于${item.timerDate}"
                    viewBind.itemMessageContentSubTv.text = subStr
                    viewBind.itemMessageContentStateIv.hide()
                    viewBind.itemMessageContentStateTv.hide()
                    when(viewModel.selectContent?.id){
                        viewModel.noticeTypeByPublish -> {
                            viewBind.itemMessageContentStateTv.show()
                            if (TimeUtil.isDateOver3(item.timerDate)) {
                                viewBind.itemMessageContentStateTv.text = "已发布"
                                viewBind.itemMessageContentStateTv.setTextColor(R.color.black11.asColor())
                            } else {
                                viewBind.itemMessageContentStateTv.text = "待发布"
                                viewBind.itemMessageContentStateTv.setTextColor(R.color.not_publish_color.asColor())
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
            dataAdapter.loadMoreModule.isEnableLoadMore = true
            current++
            request()
        }
        dataAdapter.setOnItemClickListener { adapter, view, position ->
            val jumpData = adapter.data[position] as AcceptMessageItem
            if (viewModel.selectContent?.id == viewModel.noticeTypeByReceive) {
                NoticeContentActivity.startGo(requireContext(), jumpData)
            }else{
                PublishContentActivity.startGo(requireContext(), jumpData)
            }
        }


        contentPopUp.popupGravity = Gravity.BOTTOM
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
        contentPopUp.onDismissListener = object : BasePopupWindow.OnDismissListener() {
            override fun onDismiss() {
                binding.messageNoticeContentTv.setTextColor(R.color.black9.asColor())
                binding.messageNoticeContentIv.setImageResource(R.mipmap.table_week_button)
            }
        }


        contentTimePopUp.popupGravity = Gravity.BOTTOM
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
        contentTimePopUp.onDismissListener = object : BasePopupWindow.OnDismissListener() {
            override fun onDismiss() {
                binding.messageNoticeContentTimeTv.setTextColor(R.color.black9.asColor())
                binding.messageNoticeContentTimeIv.setImageResource(R.mipmap.table_week_button)
            }
        }

        contentPopUp.setData(viewModel.mContentList, viewModel.selectContent)
        contentTimePopUp.setData(viewModel.mContentTimeList, viewModel.selectContentTime)

        viewModel.acceptMessage.observe(requireActivity()) {
            if (current == 1) {
                dataAdapter.setList(it.acceptMessage)
            } else {
                dataAdapter.addData(it.acceptMessage)
            }
            if (it.acceptMessage.size < size || it.total == current) {
                //如果不够一页,显示没有更多数据布局
                dataAdapter.loadMoreModule.loadMoreEnd()
            } else {
                dataAdapter.loadMoreModule.loadMoreComplete()
            }
        }


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
    }

    fun request() {
        when (viewModel.selectContent?.id) {
            viewModel.noticeTypeByReceive -> {
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