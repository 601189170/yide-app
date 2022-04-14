package com.yyide.chatim_pro.activity.operation

import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
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
import com.yyide.chatim_pro.R
import com.yyide.chatim_pro.activity.PhotoViewActivity
import com.yyide.chatim_pro.activity.operation.viewmodel.OperationPostWorkModel
import com.yyide.chatim_pro.base.KTBaseActivity
import com.yyide.chatim_pro.database.ScheduleDaoUtil.defaultTwoTimeListOfDateTime
import com.yyide.chatim_pro.database.ScheduleDaoUtil.toStringTime
import com.yyide.chatim_pro.databinding.*
import com.yyide.chatim_pro.dialog.deletePop
import com.yyide.chatim_pro.dialog.postClassDataPop
import com.yyide.chatim_pro.model.*
import com.yyide.chatim_pro.view.DialogUtil
import com.yyide.chatim_pro.view.SpacesItemDecoration
import org.joda.time.DateTime
import java.util.*
import com.blankj.utilcode.util.ToastUtils
import com.yyide.chatim_pro.SpData
import com.yyide.chatim_pro.activity.table.TableActivity2
import okhttp3.*
import java.io.File
import com.yyide.chatim_pro.dialog.selectphotoAndCarmer

import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import android.graphics.Bitmap
import android.os.StrictMode
import com.yyide.chatim_pro.utils.*
/**
 * @Desc 添加作业
 * @Data 2022年2月17日13:35:32
 * @Author lrz
 */
class OperationCreatWorkActivity :
    KTBaseActivity<ActivityOperationPostWorkBinding>(ActivityOperationPostWorkBinding::inflate),selectphotoAndCarmer.SelectDateListener{
    private lateinit var viewModel: OperationPostWorkModel



    var mList: MutableList<AddClassBean> = ArrayList()

    var postsubjectList: MutableList<selectSubjectByUserIdRsp> = ArrayList()

    var postclassList: MutableList<getClassList> = ArrayList()

    var classListBean: MutableList<CreateWorkBean.ClassesListDTO> = ArrayList()

    private val requestServerTimeFormat = "yyyy-MM-dd HH:mm"

    private val timeFormat = "MM月dd日 HH:mm E"
    private val showAllTimeFormat = "MM月dd日 E"
    private val allDayTimeFormat = "yyyy-MM-dd HH:mm"

    var RESULT_LOAD_IMAGE=1

    var RESULT_SELECT_SUBJECT=2

    var RESULT_CARMER=3

    var kbID:String=""

    var imglist: MutableList<String> = ArrayList()

    var index:Int=-1;

    val FileList: MutableList<File> = ArrayList()

    var SD_CARD_PATH = Environment.getExternalStorageDirectory().absolutePath

    var photoUrl =""

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(OperationPostWorkModel::class.java)

        viewModel.getSubject()

        viewModel.getClassList()
        initPhotoError()
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
        viewModel.UploadRsp.observe(this){
            if (it.isSuccess){
                if (it.getOrNull()!=null){
                    var  result=it.getOrNull();
                    Log.e("TAG", "图片上传成功2: "+JSON.toJSONString(result) )
                    if (result!=null){
                        val sb=StringBuffer()
                        for (i in result.indices) {
                            if (i==result.size-1){
                                sb.append(result[i].url)
                            }else{
                                sb.append(result[i].url+",")
                            }
                        }
                        postData(sb.toString())
                    }


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
                        binding.workname.setText(toptime+postsubjectList[0].name+"作业")
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

    fun initPhotoError(){


        val builder =StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

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
                binding.workname.setText(toptime+it.name+"作业")
            },postsubjectList,viewModel.subjectId.value)
        })

        binding.imglayout.setOnClickListener(View.OnClickListener {
            val pop = selectphotoAndCarmer(this@OperationCreatWorkActivity)
            pop.setJK(this)

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
        var bean2list: MutableList<CreateWorkBean.ClassesListDTO> = ArrayList()
        if (mAdapter.data.size==0){
            ToastUtils.showShort("请选择班级")
            return
        }else{
            mAdapter.data.forEach(){

                if (!TextUtils.isEmpty(it.classesId)){
                    var  bean=CreateWorkBean.ClassesListDTO()
                    bean.classesId=it.classesId
                    bean.timetableId=it.timetableId
                    bean.timetableTime=it.timetableTime
                    bean2list.add(bean)
                    classListBean=bean2list
                }else{
                    ToastUtils.showShort("请选择班级")
                    return
                }
            }
        }
        if (imglist.size>0){
            imglist.forEach(){
                showPicFileByLuban(File(it))
            }
        }else{
            postData("")
        }


    }

    fun postData(sb:String) {
        var  bean= CreateWorkBean()
        bean.title=binding.workname.text.toString()
        bean.content=binding.edit.text.toString()

        //图片地址
        bean.imgPaths=sb
        if (!binding.feedbackEndTime.text.toString().equals("无")){
            bean.feedbackEndTime=binding.feedbackEndTime.text.toString()
        }
        if (!binding.releaseTime.text.toString().equals("无")){
            bean.releaseTime=binding.releaseTime.text.toString()
        }
        bean.subjectId=viewModel.subjectId.value
        bean.classesList=classListBean
        viewModel.createWork(bean)
    }

    private fun showPicFileByLuban(file: File) {
        Luban.with(this)
                .load(file)
                .ignoreBy(100) //.putGear(Luban.THIRD_GEAR)//压缩等级
                .setTargetDir(Environment.getExternalStorageDirectory().absolutePath)
                .filter { path: String -> !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif")) }
                .setCompressListener(object : OnCompressListener {
                    override fun onStart() {
                        showLoading()
                    }

                    override fun onSuccess(file: File) {
                        FileList.add(file)
                        Log.e("TAG", "上传进度: " +FileList.size+"/"+imglist.size)
                        if (FileList.size==imglist.size){
                            viewModel.upPohto(FileList)
                        }else{
                            Log.e("TAG", "压缩完: "+FileList.size )
                        }
                    }

                    override fun onError(e: Throwable) {
                        hideLoading()
                        ToastUtils.showShort("图片压缩失败请重试")
                    }
                }).launch()
    }
    public val mAdapterimg =
            object :
                    BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_operation_work_img2) {
                override fun convert(holder: BaseViewHolder, item: String) {
                    val viewBind = ItemOperationWorkImg2Binding.bind(holder.itemView)
                    viewBind.delete.setOnClickListener(View.OnClickListener {

                        deleteImg(item)

                    })
                    GlideUtil.loadImageRadius(baseContext, item, viewBind.img, SizeUtils.dp2px(4f))
                    viewBind.img.setOnClickListener(View.OnClickListener {
                        PhotoViewActivity.start(this@OperationCreatWorkActivity, item)
                    })


                }
            }


    fun deleteImg(item:String){
        imglist.remove(item)
        mAdapterimg.setList(imglist)

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
                        if(!TextUtils.isEmpty(item.classesId)){
                            val intent = Intent(this@OperationCreatWorkActivity, TableActivity2::class.java)
                            intent.putExtra("classesId",item.classesId)
                            //保证当前账号的当前身份
                            val userid = SpData.getUser().identityUserId
                            intent.putExtra("teacherIds",userid)
                            intent.putExtra("subjectId",viewModel.subjectId.value)
                            intent.putExtra("className",item.className)
                            index=getItemPosition(item)
                            startActivityForResult(intent,RESULT_SELECT_SUBJECT)
                        }else {
                            ToastUtils.showShort("请选择班级")
                        }
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
                imglist.add(picturePath)
                mAdapterimg.setList(imglist)
                cursor.close()
            }



        }else if (requestCode == RESULT_SELECT_SUBJECT && resultCode == RESULT_OK && null != data){

            val timetableId: String = data.getStringExtra("timetableId")
            val timetableTime: String = data.getStringExtra("timetableTime")
            val timetableName: String = data.getStringExtra("timetableName")
            if (index!=-1){
                for (i in mAdapter.data.indices) {
                    mAdapter.data[index].timetableId=timetableId
                    mAdapter.data[index].subjectName=timetableName
                    mAdapter.data[index].timetableTime=timetableTime
                }
            }

            mAdapter.setList(mAdapter.data)




        }else if (requestCode == RESULT_CARMER && resultCode == RESULT_OK && null != data) {

            val bundle= data.getExtras()
            // 获取相机返回的数据，并转换为Bitmap图片格式，这是缩略图
            val bitmap = bundle!!["data"] as Bitmap?

            var path=Utils.getFile(bitmap).absolutePath
            imglist.add(path)
            mAdapterimg.setList(imglist)

        }
    }

    override fun onphoto() {
        val i = Intent(
                Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(i, RESULT_LOAD_IMAGE)
    }

    override fun oncarmer() {


        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
////
        startActivityForResult(intent, RESULT_CARMER)
//path为保存图片的路径，执行完拍照以后能保存到指定的路径下

//        photoUrl=SD_CARD_PATH+System.currentTimeMillis() + ".jpg";
//
//        val file = File(photoUrl)
//
//        val imageUri = Uri.fromFile(file)
//
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
//
//        startActivityForResult(intent, RESULT_CARMER)

    }




}