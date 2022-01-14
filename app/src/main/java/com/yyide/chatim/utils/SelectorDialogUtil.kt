package com.yyide.chatim.utils

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import com.yyide.chatim.R
import com.yyide.chatim.database.ScheduleDaoUtil
import com.yyide.chatim.databinding.DialogShowDateYearAndMonthBinding
import com.yyide.chatim.model.gate.SiteBean
import com.yyide.chatim.widget.WheelView
import org.joda.time.DateTime

/**
 *
 * @author liu tao
 * @date 2022/1/12 15:44
 * @description 描述
 */
object SelectorDialogUtil {
    /**
     * 选择场地
     * @param column 列
     */
    fun showSiteSelector(
        context: Context,
        title: String = "切换场地",
        curSiteId: String?,
        siteList: List<SiteBean>,
        column: Int = 2,
        onDateSetListener: (siteBean: SiteBean, childBean: SiteBean.ChildrenBean?) -> Unit
    ) {
        val binding = DialogShowDateYearAndMonthBinding.inflate(LayoutInflater.from(context))
        val rootView = binding.root
        val mDialog = Dialog(context, R.style.dialog)
        mDialog.setContentView(rootView)
        var firstIndex = 0 //第一层
        var secondIndex = 0 //第二次
        //找到上次选择
        if (column == 2) {
            if (!curSiteId.isNullOrEmpty()) {
                for (index in siteList.indices) {
                    for (index2 in siteList[index].children.indices) {
                        if (siteList[index].children[index2].id == curSiteId) {
                            firstIndex = index
                            secondIndex = index2
                            break
                        }
                    }
                }
            }
        } else {
            if (!curSiteId.isNullOrEmpty()) {
                for (index in siteList.indices) {
                    if (siteList[index].id == curSiteId) {
                        firstIndex = index
                        break
                    }
                }
            }
        }

        binding.tvTitle.text = title
        binding.yearWv.setData(siteList.map { "${it.name}" })
        binding.yearWv.setDefault(firstIndex)
        if (column == 2) {
            binding.monthWv.visibility = View.VISIBLE
            binding.vLine.visibility = View.VISIBLE
            binding.monthWv.setData(siteList[firstIndex].children.map { "${it.name}" })
            binding.monthWv.setDefault(secondIndex)
        } else {
            binding.monthWv.visibility = View.GONE
            binding.vLine.visibility = View.GONE
        }
        binding.yearWv.setOnSelectListener(object : WheelView.OnSelectListener {
            override fun endSelect(id: Int, text: String?) {
                loge("id=$id,选中${text}")
                if (id != firstIndex) {
                    firstIndex = id
                    if (column == 2){
                        binding.monthWv.setData(siteList[firstIndex].children.map { "${it.name}" })
                        secondIndex = 0
                        binding.monthWv.setDefault(secondIndex)
                    }
                }
            }

            override fun selecting(id: Int, text: String?) {
                loge("selecting id=$id,text=$text")
            }
        })
        if (column == 2) {
            binding.monthWv.setOnSelectListener(object : WheelView.OnSelectListener {
                override fun endSelect(id: Int, text: String?) {
                    secondIndex = id
                }

                override fun selecting(id: Int, text: String?) {
                }

            })
        }

        binding.tvCancel.setOnClickListener {
            mDialog.dismiss()
        }
        binding.tvEnsure.setOnClickListener {
            mDialog.dismiss()
            val siteBean = siteList[firstIndex]
            if (column == 2){
                val childrenBean = siteBean.children[secondIndex]
                onDateSetListener(siteBean, childrenBean)
                return@setOnClickListener
            }
            onDateSetListener(siteBean, null)
        }
        val dialogWindow = mDialog.window
        dialogWindow!!.setGravity(Gravity.BOTTOM)
        dialogWindow.setWindowAnimations(R.style.popwin_anim_style2)
        val lp = dialogWindow.attributes
        lp.width = context.resources.displayMetrics.widthPixels
        lp.height = DisplayUtils.dip2px(context, 380f)
        rootView.measure(0, 0)
        lp.dimAmount = 0.75f
        dialogWindow.attributes = lp
        mDialog.setCancelable(true)
        mDialog.show()
    }
}