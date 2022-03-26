package com.yyide.chatim.dialog

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim.R
import com.yyide.chatim.databinding.PopupTableTopBinding
import com.yyide.chatim.model.table.ChildrenItem
import com.yyide.chatim.utils.hide
import com.yyide.chatim.utils.logd
import com.yyide.chatim.utils.show
import razerdp.basepopup.BasePopupWindow

/**
 *
 * @author shenzhibin
 * @date 2022/3/26 10:55
 * @description Table 班级(场地) popUp
 */
class TableClassPopUp: BasePopupWindow {


    lateinit var binding: PopupTableTopBinding
    private var mSubmitCallback: SubmitCallBack? = null
    private lateinit var expandableAdapter: BaseQuickAdapter<ChildrenItem, BaseViewHolder>

    var selectData: ChildrenItem? = null
    var tempSelectData: ChildrenItem? = null

    interface SubmitCallBack {
        fun getSubmitData(data: ChildrenItem?)
    }


    constructor(context: Context) : super(context)

    constructor(fragment: Fragment) : super(fragment)


    init {
        setContentView(R.layout.popup_table_top)
        setBackground(0)
        //setOutSideDismiss(false)
    }

    override fun onViewCreated(contentView: View) {
        binding = PopupTableTopBinding.bind(contentView)
        binding.tableTopExpandableRv.setBackgroundResource(R.color.color_F3FCF9)
        initListener()
    }

    private fun initListener() {
        binding.tableTopExpandableRv.layoutManager = LinearLayoutManager(context)

        expandableAdapter = object : BaseQuickAdapter<ChildrenItem, BaseViewHolder>(R.layout.item_table_class_popup) {
            override fun convert(holder: BaseViewHolder, item: ChildrenItem) {
                holder.setText(R.id.item_class_popup_firstTv, item.parentName)
                holder.setText(R.id.item_class_popup_secondTv, item.name)
                holder.getView<View>(R.id.item_class_popup_cl).setBackgroundResource(R.color.color_F3FCF9)
                tempSelectData?.let {
                    if (item.id == it.id) {
                        holder.getView<View>(R.id.item_class_popup_cl).setBackgroundResource(R.color.colorWhite)
                    }
                }

            }
        }

        expandableAdapter.setOnItemClickListener { adapter, _, position ->
            tempSelectData = adapter.data[position] as ChildrenItem
            expandableAdapter.notifyDataSetChanged()
        }

        binding.tableTopExpandableRv.adapter = expandableAdapter


        binding.tableTopExpandableCancel.setOnClickListener { _ ->
            dismiss()
        }

        binding.tableTopExpandableSubmit.setOnClickListener { _ ->
            selectData = tempSelectData
            mSubmitCallback?.getSubmitData(selectData)
            dismiss()
        }
    }

    override fun dismiss() {
        super.dismiss()
        tempSelectData = selectData
        expandableAdapter.notifyDataSetChanged()
    }


    fun setData(data: List<ChildrenItem>, select: ChildrenItem?) {
        tempSelectData = select ?: data.first()
        selectData = tempSelectData
        expandableAdapter.setList(data)
    }

    fun setSubmitCallBack(callBack: SubmitCallBack?) {
        mSubmitCallback = callBack
    }
}