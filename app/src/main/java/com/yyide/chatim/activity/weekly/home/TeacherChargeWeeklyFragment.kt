package com.yyide.chatim.activity.weekly.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.databinding.DialogWeekMessgeBinding
import com.yyide.chatim.databinding.FragmentTeacherChargeWeeklyBinding
import com.yyide.chatim.dialog.WeeklyTopPop


/**
 *
 * 班主任/教师 周报统计界面
 * date 2021年9月15日15:11:01
 * author LRZ
 */
class TeacherChargeWeeklyFragment : Fragment() {

    private lateinit var viewBinding: FragmentTeacherChargeWeeklyBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentTeacherChargeWeeklyBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TeacherChargeWeeklyFragment().apply {}
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        viewBinding.tvEvent.setOnClickListener {
            val dialog = WeeklyTopPop(activity, viewBinding.tvEvent)
            dialog.setOnSelectDialogMenu(object : WeeklyTopPop.DialogMenu {
                override fun onClickMenuListener(id: Long) {
                    ToastUtils.showShort("Event")
                }
            })
        }
        viewBinding.tvTime.setOnClickListener {
            val dialog = WeeklyTopPop(activity, viewBinding.tvTime)
            dialog.setOnSelectDialogMenu(object : WeeklyTopPop.DialogMenu {
                override fun onClickMenuListener(id: Long) {
                    ToastUtils.showShort("Time")
                }
            })
        }
        initChart()
    }

    private fun initChart() {
        viewBinding.attendance.layoutCharts.recyclerview.layoutManager =
            LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        viewBinding.attendance.layoutCharts.recyclerview.adapter = chartsAdapter
        chartsAdapter.setOnItemClickListener { adapter, view, position ->

            selectPosition = if (selectPosition != position) {
                position
            } else {
                -1
            }
            chartsAdapter.notifyDataSetChanged()
            //show(view, chartsAdapter.getItem(position), 60)
        }
        val datas = mutableListOf<String>()
        datas.add("dfsafd1")
        datas.add("dfsafd2")
        datas.add("dfsafd3")
        datas.add("dfsafd4")
        datas.add("dfsafd5")
        datas.add("dfsafd1")
        datas.add("dfsafd2")
        datas.add("dfsafd3")
        datas.add("dfsafd4")
        datas.add("dfsafd5")
        chartsAdapter.setList(datas)
    }

    @SuppressLint("ResourceAsColor", "ResourceType")
    private fun show(view: View, desc: String, number: Int) {
        val inflate = DialogWeekMessgeBinding.inflate(layoutInflater)
        val popWindow = PopupWindow(
            inflate.root,
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true
        )
        inflate.tvName.text = desc
        inflate.tvProgress.text = "${number}%"
        popWindow.setBackgroundDrawable(context?.getDrawable(android.R.color.transparent))
        popWindow.setOnDismissListener {
            selectPosition = -1
            chartsAdapter.notifyDataSetChanged()
        }
        //获取需要在其上方显示的控件的位置信息
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        //在控件上方显示
        popWindow.showAtLocation(
            view,
            Gravity.NO_GRAVITY,
            (location[0]) - inflate.root.width / 2,
            location[1] - inflate.root.height
        )
    }

    private var selectPosition = -1
    private val chartsAdapter =
        object : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_weekly_charts_vertical) {
            override fun convert(holder: BaseViewHolder, item: String) {
                val rodom = (10..100).random()
                val progressBar = holder.getView<ProgressBar>(R.id.progressbar)
                val constraintLayout = holder.getView<ConstraintLayout>(R.id.constraintLayout)
                progressBar.progress = rodom
                holder.setText(R.id.tv_week, item)
                holder.setText(R.id.tv_progress, "${rodom}%")
                constraintLayout.setBackgroundColor(context.resources.getColor(R.color.transparent))
                if (selectPosition == holder.bindingAdapterPosition) {
                    constraintLayout.setBackgroundColor(context.resources.getColor(R.color.charts_bg))
                }
                //when (holder.bindingAdapterPosition) {
//                0 -> {
//                    holder.setText(R.id.tv_week, "周一")
//                }
//                1 -> {
//                    holder.setText(R.id.tv_week, "周二")
//                }
//                2 -> {
//                    holder.setText(R.id.tv_week, "周三")
//                }
//                3 -> {
//                    holder.setText(R.id.tv_week, "周四")
//                }
//                4 -> {
//                    holder.setText(R.id.tv_week, "周五")
//                }
//                5 -> {
//                    holder.setText(R.id.tv_week, "周六")
//                }
//                6 -> {
//                    holder.setText(R.id.tv_week, "周日")
//                }
            }
        }

}