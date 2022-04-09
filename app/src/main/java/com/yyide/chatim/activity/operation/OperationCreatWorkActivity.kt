package com.yyide.chatim.activity.operation

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.fastjson.JSON
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jzxiang.pickerview.TimePickerDialog
import com.jzxiang.pickerview.listener.OnDateSetListener
import com.yyide.chatim.R
import com.yyide.chatim.activity.PhotoViewActivity
import com.yyide.chatim.activity.operation.viewmodel.OperationPostWorkModel
import com.yyide.chatim.base.KTBaseActivity
import com.yyide.chatim.database.ScheduleDaoUtil.defaultTwoTimeListOfDateTime
import com.yyide.chatim.database.ScheduleDaoUtil.toStringTime
import com.yyide.chatim.databinding.*
import com.yyide.chatim.dialog.deletePop
import com.yyide.chatim.dialog.postClassDataPop
import com.yyide.chatim.model.*
import com.yyide.chatim.utils.DatePickerDialogUtil
import com.yyide.chatim.utils.DateUtils
import com.yyide.chatim.utils.GlideUtil
import com.yyide.chatim.utils.logd
import com.yyide.chatim.view.DialogUtil
import com.yyide.chatim.view.SpacesItemDecoration
import org.joda.time.DateTime
import java.util.*

class OperationCreatWorkActivity :
    KTBaseActivity<ActivityOperationPostWorkBinding>(ActivityOperationPostWorkBinding::inflate) {
    private lateinit var viewModel: OperationPostWorkModel



    var mList: MutableList<AddClassBean> = ArrayList()

    var postsubjectList: MutableList<selectSubjectByUserIdRsp> = ArrayList()

    var postclassList: MutableList<getClassList> = ArrayList()

    var classListBean: MutableList<CreateWorkBean.ClassesListDTO> = ArrayList()

    private val requestServerTimeFormat = "yyyy-MM-dd HH:mm:ss"

    private val timeFormat = "MM月dd日 HH:mm E"
    private val showAllTimeFormat = "MM月dd日 E"
    private val allDayTimeFormat = "yyyy-MM-dd"
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(OperationPostWorkModel::class.java)

        viewModel.getSubject()

        viewModel.getClassList()
        //监听科目
        viewModel.subjectData.observe(this){
            if (it.isSuccess){
                if (it.getOrNull()!=null){
                    var  result=it.getOrNull();
                    postsubjectList= result as MutableList<selectSubjectByUserIdRsp>
                    if (postsubjectList.size>0){
                        viewModel.subjectId.value=postsubjectList[0].id
                        binding.subjectName.text=postsubjectList[0].name
//                        binding.workname.text=postsubjectList[0].name+"作业"

                        var  date = DateTime.now()

                        val dateTimes = defaultTwoTimeListOfDateTime(date)
                        val time1 = dateTimes[0]
                        val toptime = time1.toStringTime("MM/dd")
                        binding.workname.text=toptime+postsubjectList[0].name+"作业"
                    }
                }
            }

        }

        //监听选择班级
        viewModel.ClassListData.observe(this){
            if (it.isSuccess){
                if (it.getOrNull()!=null){
                    var  result=it.getOrNull()
                    postclassList= result as MutableList<getClassList>

                }
            }

        }


        // 监听开始时间变化
        viewModel.startTimeLiveData.observe(this) {
            binding.feedbackEndTime.setText(
                    DateUtils.formatTime(
                            it,
                            requestServerTimeFormat,
                            if (viewModel.allDayLiveData.value == true) showAllTimeFormat else timeFormat
                    )
            )
        }


        // 监听结束时间变化
        viewModel.endTimeLiveData.observe(this) {
            binding.releaseTime.setText(
                    DateUtils.formatTime(
                            it,
                            requestServerTimeFormat,
                            if (viewModel.allDayLiveData.value == true) showAllTimeFormat else timeFormat
                    )
            )
        }


        binding.timelayout.setOnClickListener {
            DatePickerDialogUtil.showDateTime(
                    this,
                    getString(R.string.select_begin_time),
                    viewModel.startTimeLiveData.value,
                    startTimeListener,
                    isAllDay = viewModel.allDayLiveData.value == true
            )
            /*val timeDialog = TimeSelectDialog(viewModel.startTimeLiveData.value ?: "")
            timeDialog.setSelectTimeCallBack(startTimeCallBack)
            timeDialog.show(supportFragmentManager, "timeSelect")*/
        }


        binding.timelayout2.setOnClickListener {
            DatePickerDialogUtil.showDateTime(
                    this,
                    getString(R.string.select_begin_time),
                    viewModel.startTimeLiveData.value,
                    startTimeListener,
                    isAllDay = viewModel.allDayLiveData.value == true
            )
            /*val timeDialog = TimeSelectDialog(viewModel.startTimeLiveData.value ?: "")
            timeDialog.setSelectTimeCallBack(startTimeCallBack)
            timeDialog.show(supportFragmentManager, "timeSelect")*/
        }
    }
    private val startTimeListener =
            OnDateSetListener { _: TimePickerDialog?, millSeconds: Long ->
                var startTime = DateUtils.switchTime(Date(millSeconds), requestServerTimeFormat)
                viewModel.allDayLiveData.value?.let {
                    if (it) {
                        startTime =
                                "${DateUtils.switchTime(Date(millSeconds), allDayTimeFormat)} 00:00:00"
                    }
                }
                logd("startTimeListener: $startTime")
                viewModel.startTimeLiveData.value = startTime
            }

    private val endTimeListener =
            OnDateSetListener { _: TimePickerDialog?, millSeconds: Long ->
                var endTime = DateUtils.switchTime(Date(millSeconds), requestServerTimeFormat)
                viewModel.allDayLiveData.value?.let {
                    if (it) {
                        endTime =
                                "${DateUtils.switchTime(Date(millSeconds), allDayTimeFormat)} 23:59:59"
                    }
                }
                logd("endTimeListener: $endTime")
                viewModel.endTimeLiveData.value = endTime
            }

    override fun initView() {
        binding.top.title.text = getString(R.string.operation_post_work_title)
        binding.top.backLayout.setOnClickListener { finish() }
        binding.recyclerView.layoutManager =LinearLayoutManager(this)

//        PhotoViewActivity.start(this, imgPath)
        binding.recyclerView.addItemDecoration(SpacesItemDecoration(SpacesItemDecoration.dip2px(5f)))
        binding.recyclerView.adapter = mAdapter


        binding.recyclerViewImg.layoutManager =GridLayoutManager(this,4)

//        PhotoViewActivity.start(this, imgPath)
        binding.recyclerViewImg.addItemDecoration(SpacesItemDecoration(SpacesItemDecoration.dip2px(5f)))
        binding.recyclerViewImg.adapter = mAdapterimg

        binding.addclass.setOnClickListener(View.OnClickListener {
            val bean = AddClassBean("", "","","","","")
            mList.add(bean)
            mAdapter.setList(mList)
        })
        //选择科目
        binding.subjectName.setOnClickListener(View.OnClickListener {
            DialogUtil.showWorkTypeWorkSelect2(this, DialogUtil.OnPostSubListener {
                binding.subjectName.text=it.name
                viewModel.subjectId.value=it.id
            },postsubjectList,viewModel.subjectId.value)
        })
        binding.btnCommit.setOnClickListener(View.OnClickListener {
            //提交

            var  bean:CreateWorkBean= CreateWorkBean()
            getclassesList();
            bean.title=binding.workname.text.toString()
            bean.content=binding.edit.text.toString()
            //图片地址
            bean.imgPaths=""
            bean.feedbackEndTime="2022-04-09 00:04"
            bean.releaseTime="2022-05-09 00:04"
            bean.subjectId=viewModel.subjectId.value
            bean.classesList=classListBean
            Log.e("TAG", "btnCommit: "+ JSON.toJSONString(bean))
            viewModel.createWork(bean)
        })


    }

    private val mAdapterimg =
            object :
                    BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_operation_work_img) {
                override fun convert(holder: BaseViewHolder, item: String) {
                    val viewBind = ItemOperationWorkImgBinding.bind(holder.itemView)
                    GlideUtil.loadImageRadius(baseContext, item, viewBind.img, SizeUtils.dp2px(4f))
                    viewBind.img.setOnClickListener(View.OnClickListener {
                        PhotoViewActivity.start(this@OperationCreatWorkActivity, item)
                    })

                }
            }
    private val mAdapter =
            object :
                    BaseQuickAdapter<AddClassBean, BaseViewHolder>(R.layout.item_add_class) {
                override fun convert(holder: BaseViewHolder, item: AddClassBean) {
                    val viewBind = ItemAddClassBinding.bind(holder.itemView)
                    //选择班级

                    viewBind.className.text=item.className
                    viewBind.subjectName.text=item.subjectName
                    viewBind.selectlayout1.setOnClickListener(View.OnClickListener {
                        val pop = postClassDataPop(this@OperationCreatWorkActivity,postclassList)
                        pop.setJK(postClassDataPop.SelectDateListener {
                            if (it!=null){
                                viewBind.className.text=it.ljname+it.className
                                item.className=it.ljname+it.className
                                item.classesId=it.classId
                            }
                        })

                    })
                    //选择课表
                    viewBind.selectlayout2.setOnClickListener(View.OnClickListener {
                        //课程表跳转
//                        item.subjectName=
//                        item.subjectId=
//                        item.timetableId=
//                        item.timetableTime=
                    })
                    //刪除
                    viewBind.delete.setOnClickListener(View.OnClickListener {
                        val pop=deletePop(this@OperationCreatWorkActivity)
                        pop.setSelectClasses { deletePop.SelectDateListener {

                                delete(item)

                        } }

                    })
                }
            }


    fun delete(item:AddClassBean){
//        mList.removeAt(index)
        mList.remove(item)
        mAdapter.setList(mList)
        Log.e("TAG", "mAdapter: "+JSON.toJSONString(mList) )
    }



    fun getclassesList(){
        var bean2list: MutableList<CreateWorkBean.ClassesListDTO> = ArrayList()

        mAdapter.data.forEach(){

            if (!TextUtils.isEmpty(it.classesId)&&!TextUtils.isEmpty(it.timetableId)&&!TextUtils.isEmpty(it.timetableTime)){
                var  bean=CreateWorkBean.ClassesListDTO()
                bean.classesId=it.classesId
                bean.timetableId=it.timetableId
                bean.timetableTime=it.timetableTime
                bean2list.add(bean)
                classListBean=bean2list
            }
        }
    }



}