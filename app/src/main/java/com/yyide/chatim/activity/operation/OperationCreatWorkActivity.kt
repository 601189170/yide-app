package com.yyide.chatim.activity.operation

import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
import com.yyide.chatim.widget.WheelView
import org.joda.time.DateTime
import java.util.*
import android.R.attr.data
import android.widget.Toast
import com.blankj.utilcode.util.ToastUtils
import com.yyide.chatim.SpData
import com.yyide.chatim.activity.table.TableActivity2


class OperationCreatWorkActivity :
    KTBaseActivity<ActivityOperationPostWorkBinding>(ActivityOperationPostWorkBinding::inflate) {
    private lateinit var viewModel: OperationPostWorkModel



    var mList: MutableList<AddClassBean> = ArrayList()

    var postsubjectList: MutableList<selectSubjectByUserIdRsp> = ArrayList()

    var postclassList: MutableList<getClassList> = ArrayList()

    var classListBean: MutableList<CreateWorkBean.ClassesListDTO> = ArrayList()

    private val requestServerTimeFormat = "yyyy-MM-dd"

    private val timeFormat = "MM月dd日 HH:mm E"
    private val showAllTimeFormat = "MM月dd日 E"
    private val allDayTimeFormat = "yyyy-MM-dd"

    var RESULT_LOAD_IMAGE=1

    var RESULT_SELECT_SUBJECT=2
    var kbID:String=""

    var imglist: MutableList<String> = ArrayList()

    var index:Int=-1;

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(OperationPostWorkModel::class.java)

        viewModel.getSubject()

        viewModel.getClassList()


        viewModel.ispost.observe(this){
            if (it.isSuccess){
                var  result=it.getOrNull()
                if (!TextUtils.isEmpty(result)){
                    ToastUtils.showShort("发布作业成功")
                    finish()
                }else{
                    ToastUtils.showShort("发布作业失败")
                }
            }
        }
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
                    Log.e("TAG", "postclassList: "+JSON.toJSONString(result) )
                    postclassList= result as MutableList<getClassList>
                    Log.e("TAG", "postclassList: "+JSON.toJSONString(postclassList) )
                }
            }

        }


        // 监听开始时间变化
        viewModel.startTimeLiveData.observe(this) {
            binding.feedbackEndTime.text=it
        }


        // 监听结束时间变化
        viewModel.endTimeLiveData.observe(this) {
            binding.releaseTime.text=it

        }


        binding.timelayout.setOnClickListener {
            DatePickerDialogUtil.showDateTime(
                    this,
                    getString(R.string.select_begin_time2),
                    viewModel.startTimeLiveData.value,
                    startTimeListener,
                    isAllDay = viewModel.allDayLiveData.value == true
            )

        }


        binding.timelayout2.setOnClickListener {
            DatePickerDialogUtil.showDateTime(
                    this,
                    getString(R.string.select_begin_time2),
                    viewModel.startTimeLiveData.value,
                    startTimeListener2,
                    isAllDay = viewModel.allDayLiveData.value == true
            )
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
                viewModel.startTimeLiveData.value = startTime
            }
    private val startTimeListener2 =
            OnDateSetListener { _: TimePickerDialog?, millSeconds: Long ->
                var startTime = DateUtils.switchTime(Date(millSeconds), requestServerTimeFormat)
                viewModel.allDayLiveData.value?.let {
                    if (it) {
                        startTime =
                                "${DateUtils.switchTime(Date(millSeconds), allDayTimeFormat)} 00:00:00"
                    }
                }
                viewModel.endTimeLiveData.value = startTime
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
                var  date = DateTime.now()
                val dateTimes = defaultTwoTimeListOfDateTime(date)
                val time1 = dateTimes[0]
                val toptime = time1.toStringTime("MM/dd")
                binding.workname.text=toptime+it.name+"作业"
            },postsubjectList,viewModel.subjectId.value)
        })

        binding.img.setOnClickListener(View.OnClickListener {
            val i = Intent(
                    Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

            startActivityForResult(i, RESULT_LOAD_IMAGE)
        })
        binding.btnCommit.setOnClickListener(View.OnClickListener {
            //提交
            commit()
        })


    }

    fun commit(){
        if(TextUtils.isEmpty(binding.workname.text.toString())){
            ToastUtils.showShort("请输入作业标题")
            return
        }
        if(TextUtils.isEmpty(binding.edit.text.toString())){
            ToastUtils.showShort("请输入作业内容")
            return
        }

        var  bean= CreateWorkBean()
        getclassesList();
        bean.title=binding.workname.text.toString()
        bean.content=binding.edit.text.toString()
        //图片地址
//        bean.imgPaths=imglist.toString()
        bean.feedbackEndTime=binding.feedbackEndTime.text.toString()
        bean.releaseTime=binding.releaseTime.text.toString()
        bean.subjectId=viewModel.subjectId.value
        bean.classesList=classListBean
        viewModel.createWork(bean)
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
                        if(!TextUtils.isEmpty(item.classesId)){
                            val intent = Intent(this@OperationCreatWorkActivity, TableActivity2::class.java)
                            intent.putExtra("classesId",item.classesId)

                            var teacherids= SpData.getIdentityInfo().id.toString()
                            intent.putExtra("teacherIds",teacherids)
                            intent.putExtra("subjectId",viewModel.subjectId.value)
                            intent.putExtra("className",item.className)
                            index=getItemPosition(item)
                            startActivityForResult(intent,RESULT_SELECT_SUBJECT)
                        }else {
                            ToastUtils.showShort("请选择班级")
                        }

//                        val i = Intent(
//                                Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//
//                        startActivityForResult(i, RESULT_LOAD_IMAGE)
                    })
                    //刪除
                    viewBind.delete.setOnClickListener(View.OnClickListener {
                        val pop=deletePop(this@OperationCreatWorkActivity)
                        pop.setSelectClasses {
                            Log.e("TAG", "setSelectClasses: " )
                            delete(item)
                        }


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
            }else{
                ToastUtils.showShort("请关联班级或科目")
                return
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            val selectedImage: Uri? = data.data
            val filePathColumn = arrayOf<String>(MediaStore.Images.Media.DATA)
            val cursor: Cursor? = selectedImage?.let {
                contentResolver.query(it,
                        filePathColumn, null, null, null)
            }
            if (cursor != null) {
                cursor.moveToFirst()
                val columnIndex: Int = cursor.getColumnIndex(filePathColumn[0])
                val picturePath: String = cursor.getString(columnIndex)
                Log.e("TAG", "onActivityResult: "+picturePath )
                imglist.add(picturePath)
                mAdapterimg.setList(imglist)
                cursor.close()
            }



        }else if (requestCode == RESULT_SELECT_SUBJECT && resultCode == RESULT_OK && null != data){

            val timetableId: String = data.getStringExtra("timetableId")
            val timetableTime: String = data.getStringExtra("timetableTime")
            val timetableName: String = data.getStringExtra("timetableName")
            Log.e("TAG", "onActivityResult: "+JSON.toJSONString(timetableName) )
            Log.e("TAG", "onActivityResult: "+JSON.toJSONString(timetableTime) )
            Log.e("TAG", "onActivityResult: "+JSON.toJSONString(timetableId) )
            if (index!=-1){
                for (i in mAdapter.data.indices) {
                    mAdapter.data[index].timetableId=timetableId
                    mAdapter.data[index].subjectName=timetableName
                    mAdapter.data[index].timetableTime=timetableTime
                }
            }
            Log.e("TAG", "mAdapterDATA==》: "+JSON.toJSONString(mAdapter.data) )

            mAdapter.setList(mAdapter.data)






        }
    }

}