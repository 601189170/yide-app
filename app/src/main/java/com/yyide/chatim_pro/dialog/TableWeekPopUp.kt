package com.yyide.chatim_pro.dialog

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim_pro.R
import com.yyide.chatim_pro.databinding.PopupTableTopBinding

import com.yyide.chatim_pro.model.table.ChildrenItem
import com.yyide.chatim_pro.utils.hide
import com.yyide.chatim_pro.utils.remove
import com.yyide.chatim_pro.utils.show
import razerdp.basepopup.BasePopupWindow

/**
 *
 * @author shenzhibin
 * @date 2022/3/25 10:27
 * @description 课表 Week popup
 */
class TableWeekPopUp : BasePopupWindow {


    lateinit var binding: PopupTableTopBinding
    private var mSubmitCallback: SubmitCallBack? = null
    private lateinit var expandableAdapter: BaseQuickAdapter<ChildrenItem, BaseViewHolder>

    var selectData: ChildrenItem? = null


    interface SubmitCallBack {
        fun getSubmitData(data: ChildrenItem?)
    }


    constructor(context: Context) : super(context)

    constructor(fragment: Fragment) : super(fragment)


    init {
        setContentView(R.layout.popup_table_top)
        setBackground(0)
    }

    override fun onViewCreated(contentView: View) {
        binding = PopupTableTopBinding.bind(contentView)
        initListener()
    }

    private fun initListener() {

        binding.tableTopExpandableCancel.remove()
        binding.tableTopExpandableSubmit.remove()

        binding.viewPopupBlank.setOnClickListener {
            dismiss()
        }

        binding.tableTopExpandableRv.layoutManager = LinearLayoutManager(context)

        expandableAdapter = object : BaseQuickAdapter<ChildrenItem, BaseViewHolder>(R.layout.item_dialog_schedule_remind_list) {
            override fun convert(holder: BaseViewHolder, item: ChildrenItem) {
                holder.setText(R.id.tv_title, item.name)
                holder.getView<View>(R.id.iv_remind).hide()
                if (holder.bindingAdapterPosition == expandableAdapter.data.size - 1){
                    holder.getView<View>(R.id.v_line).hide()
                }
                selectData?.let {
                    if (item.id == it.id) {
                        holder.getView<View>(R.id.iv_remind).show()
                    }
                }

            }
        }

        expandableAdapter.setOnItemClickListener { adapter, _, position ->
            selectData = adapter.data[position] as ChildrenItem
            expandableAdapter.notifyDataSetChanged()
            mSubmitCallback?.getSubmitData(selectData)
            dismiss()
        }

        binding.tableTopExpandableRv.adapter = expandableAdapter
        binding.tableTopExpandableRv.setBackgroundResource(R.drawable.table_expandable_bg)

        /*binding.tableTopExpandableCancel.setOnClickListener { _ ->
            dismiss()
        }

        binding.tableTopExpandableSubmit.setOnClickListener { _ ->
            selectData = tempSelectData
            mSubmitCallback?.getSubmitData(selectData)
            dismiss()
        }*/
    }



    fun setData(data: List<ChildrenItem>, select:ChildrenItem?) {
        selectData = select
        expandableAdapter.setList(data)
    }

    fun setSubmitCallBack(callBack: SubmitCallBack?) {
        mSubmitCallback = callBack
    }


}