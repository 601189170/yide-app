package com.yyide.chatim.dialog

import android.content.Context
import android.view.Gravity
import android.view.View
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.SizeUtils
import com.contrarywind.listener.OnItemSelectedListener
import com.contrarywind.view.WheelView
import com.yyide.chatim.R
import com.yyide.chatim.adapter.table.TableClassWheelAdapter
import com.yyide.chatim.databinding.PopupTableClassBinding
import com.yyide.chatim.model.table.ChildrenItem
import com.yyide.chatim.model.table.ClassInfo
import com.yyide.chatim.model.table.ClassInfoBean
import com.yyide.chatim.utils.asDrawable
import com.yyide.chatim.utils.logd
import razerdp.basepopup.BasePopupWindow

/**
 *
 * @author shenzhibin
 * @date 2022/3/26 10:55
 * @description Table 班级(场地) popUp
 */
class TableClassPopUp : BasePopupWindow {


    lateinit var binding: PopupTableClassBinding
    private var mSubmitCallback: SubmitCallBack? = null
    private var expandableParentAdapter: TableClassWheelAdapter? = null
    private var expandableAdapter: TableClassWheelAdapter? = null

    private val selectData: ClassInfo = ClassInfo()

    private var parentIndex = 0
    private var childrenIndex = 0

    var classList: MutableList<ClassInfoBean> = mutableListOf()
    var classItemList: MutableList<ChildrenItem> = mutableListOf()


    interface SubmitCallBack {
        fun getSubmitData(data: ClassInfo)
    }


    constructor(context: Context) : super(context)

    constructor(fragment: Fragment) : super(fragment)


    init {
        setContentView(R.layout.popup_table_class)
        setBackground(0)
        popupGravity = Gravity.BOTTOM
    }

    override fun onViewCreated(contentView: View) {
        binding = PopupTableClassBinding.bind(contentView)
        /*binding.rvPopupTableClass.setBackgroundResource(R.color.color_F3FCF9)
        binding.rvPopupTableClassItem.setBackgroundResource(R.color.color_F3FCF9)*/
        initListener()
    }

    private fun initListener() {

        binding.viewPopupBlank.setOnClickListener {
            dismiss()
        }

        binding.rvPopupTableClass.setCyclic(false)
        binding.rvPopupTableClassItem.setCyclic(false)

        binding.rvPopupTableClass.setDividerColor(0xffffff)
        binding.rvPopupTableClassItem.setDividerColor(0xffffff)

        binding.rvPopupTableClass.setDividerType(WheelView.DividerType.WRAP)
        binding.rvPopupTableClassItem.setDividerType(WheelView.DividerType.WRAP)

        binding.rvPopupTableClass.setLineSpacingMultiplier(1.4f)
        binding.rvPopupTableClassItem.setLineSpacingMultiplier(1.4f)

        binding.rvPopupTableClass.setTextSize(14f)
        binding.rvPopupTableClassItem.setTextSize(14f)

        /*expandableParentAdapter = object :
            BaseQuickAdapter<ClassInfoBean, BaseViewHolder>(R.layout.item_table_class_popup) {
            override fun convert(holder: BaseViewHolder, item: ClassInfoBean) {
                val binding = ItemTableClassPopupBinding.bind(holder.itemView)
                binding.itemClassPopupFirstTv.text = item.name
                binding.itemClassPopupCl.setBackgroundResource(R.color.color_F3FCF9)
                if (tempSelectData.parentId.isNotEmpty()) {
                    if (item.id == tempSelectData.parentId) {
                        binding.itemClassPopupCl.setBackgroundResource(R.color.colorWhite)
                    }
                }
            }
        }

        expandableAdapter = object :
            BaseQuickAdapter<ChildrenItem, BaseViewHolder>(R.layout.item_table_class_item) {
            override fun convert(holder: BaseViewHolder, item: ChildrenItem) {
                val binding = ItemTableClassItemBinding.bind(holder.itemView)
                binding.itemClassPopupTv.text = item.name
                binding.itemClassPopupCl.setBackgroundResource(R.color.color_F3FCF9)
                if (tempSelectData.parentId.isNotEmpty()) {
                    if (item.id == tempSelectData.parentId) {
                        binding.itemClassPopupCl.setBackgroundResource(R.color.colorWhite)
                    }
                }
            }
        }

        expandableParentAdapter.setOnItemClickListener { adapter, _, position ->
            val temp = adapter.data[position] as ClassInfoBean
            tempSelectData.parentId = temp.id
            tempSelectData.parentName = temp.name
            expandableParentAdapter.notifyDataSetChanged()
            expandableAdapter.setList(temp.children)
        }

        expandableAdapter.setOnItemClickListener { adapter, _, position ->
            val temp = adapter.data[position] as ChildrenItem
            tempSelectData.id = temp.id
            tempSelectData.name = temp.name
            expandableAdapter.notifyDataSetChanged()
        }*/


        /*binding.rvPopupTableClass.adapter = expandableParentAdapter
        binding.rvPopupTableClassItem.adapter = expandableAdapter*/



        binding.rvPopupTableClass.setOnItemSelectedListener {
            val temp = classList[it]
            logd("item parent select = ${temp.name}")
            classItemList.clear()
            classItemList.addAll(temp.children)
            val stringList = mutableListOf<String>()
            classItemList.forEach { childrenItem ->
                stringList.add(childrenItem.name)
            }
            expandableAdapter = TableClassWheelAdapter(stringList)
            binding.rvPopupTableClassItem.adapter = expandableAdapter
        }

        binding.rvPopupTableClassItem.setOnItemSelectedListener {
            val temp = classItemList[it]
            logd("item select = ${temp.name}")
        }




        binding.btnPopupTableClassCancel.setOnClickListener { _ ->
            dismiss()
        }

        binding.btnPopupTableClassSubmit.setOnClickListener { _ ->

            val parentItemIndex = binding.rvPopupTableClass.currentItem
            val itemIndex = binding.rvPopupTableClassItem.currentItem

            logd("currentItem = $parentItemIndex,$itemIndex")
            if (!classList.isNullOrEmpty() &&
                !classItemList.isNullOrEmpty() &&
                classList.size > parentItemIndex &&
                classItemList.size > itemIndex
            ) {

                parentIndex = parentItemIndex;
                childrenIndex = itemIndex

                selectData.parentId = classList[parentItemIndex].id
                selectData.parentName = classList[parentItemIndex].name
                selectData.id = classItemList[itemIndex].id
                selectData.name = classItemList[itemIndex].name
                mSubmitCallback?.getSubmitData(selectData)
            }
            dismiss()
        }
    }

    override fun dismiss() {
        binding.rvPopupTableClass.currentItem = parentIndex
        binding.rvPopupTableClassItem.currentItem = childrenIndex
        super.dismiss()
    }

    /*fun setData(data: List<ChildrenItem>, select: ChildrenItem?) {
        tempSelectData = select ?: data.first()
        selectData = tempSelectData
        expandableAdapter.setList(data)
    }*/

    fun setData(data: List<ClassInfoBean>, select: ClassInfo) {
        // 构造班级名称
        classList.clear()
        classList.addAll(data)

        val stringList = mutableListOf<String>()
        classList.forEach { children ->
            stringList.add(children.name)
        }

        expandableParentAdapter = TableClassWheelAdapter(stringList)
        binding.rvPopupTableClass.adapter = expandableParentAdapter

        //构造班级子名称
        classItemList.clear()
        classItemList.addAll(data[0].children)
        val itemStringList = mutableListOf<String>()
        classItemList.forEach { childrenItem ->
            itemStringList.add(childrenItem.name)
        }

        expandableAdapter = TableClassWheelAdapter(itemStringList)
        binding.rvPopupTableClassItem.adapter = expandableAdapter
    }

    fun setSubmitCallBack(callBack: SubmitCallBack?) {
        mSubmitCallback = callBack
    }
}