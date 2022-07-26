package com.yyide.chatim_pro.activity.operation

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.fastjson.JSON
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim_pro.R
import com.yyide.chatim_pro.activity.PhotoViewActivity
import com.yyide.chatim_pro.activity.operation.viewmodel.OperationParentsViewModel
import com.yyide.chatim_pro.base.KTBaseActivity
import com.yyide.chatim_pro.databinding.ActivityOperationDetail2Binding
import com.yyide.chatim_pro.databinding.ItemOperationWorkImgBinding
import com.yyide.chatim_pro.utils.GlideUtil
import com.yyide.chatim_pro.view.SpacesItemDecoration

class OperationDetailActivityByParents :
    KTBaseActivity<ActivityOperationDetail2Binding>(ActivityOperationDetail2Binding::inflate) {
    private lateinit var viewModel: OperationParentsViewModel
    var id=""
    val fragments = mutableListOf<Fragment>()
    var ispost=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(OperationParentsViewModel::class.java)

        id = intent.getStringExtra("id")
        viewModel.getParensWorkInfo(id)

        binding.recyclerView.layoutManager =GridLayoutManager(this, 4)
//        PhotoViewActivity.start(this, imgPath)
        binding.recyclerView.addItemDecoration(SpacesItemDecoration(SpacesItemDecoration.dip2px(5f)))
        viewModel.ispost.observe(this){
            if (it.isSuccess){
                ToastUtils.showShort("反馈成功")
                finish()
            }else{
                ToastUtils.showShort("反馈失败")
            }
        }


        binding.tvExpand.setOnClickListener(View.OnClickListener {
            setvisibility()
        })
        binding.recyclerView.adapter = mAdapter

        viewModel.ParentsWorkInfoData.observe(this){
            if (it.isSuccess){
                val result = it.getOrNull()
                if (result!=null){
                    setCheckTop()
                    setCheckDonw()
                    ispost=result.feedbackData.isFeedback
                    Log.e("TAG", "getSchoolWorkRsp: "+JSON.toJSONString(result))
                    if (!TextUtils.isEmpty(result.classesTimetable.subjectName)){
                        binding.tvClassName.text=result.classesTimetable.classesName+result.classesTimetable.subjectName
                        binding.tvGl.text="关联课程:"+result.classesTimetable.timetableTime+"第"+result.classesTimetable.sequence+"节"+result.classesTimetable.startTime
                    }else{
                        binding.tvGl.visibility=View.GONE
                        binding.tvClassName.text=result.classesTimetable.classesName
                    }
                    binding.tvTitle.text=result.title
                    binding.tvContent.text=result.content
//                    android:text="关联课程：03-13 第一节 08:30"

                    if (result.feedbackData.isFeedback){
                        //已反馈
                        binding.compelete.isEnabled=false
                        binding.nocompelete.isEnabled=false
                        binding.nd1.isEnabled=false
                        binding.nd2.isEnabled=false
                        binding.nd3.isEnabled=false
                        binding.btnCommit.visibility=View.GONE
                        binding.btnCommitGray.visibility=View.VISIBLE

                        if (result.feedbackData.completion==1){
                            binding.compelete.isChecked=true
                        }else{
                            binding.nocompelete.isChecked=true
                        }
                        if (result.feedbackData.difficulty==1){
                            binding.nd1.isChecked=true
                        }else if (result.feedbackData.difficulty==2){
                            binding.nd2.isChecked=true
                        }else if (result.feedbackData.difficulty==3){
                            binding.nd3.isChecked=true
                        }
                    }else{
                        binding.compelete.isEnabled=true
                        binding.nocompelete.isEnabled=true
                        binding.nd1.isEnabled=true
                        binding.nd2.isEnabled=true
                        binding.nd3.isEnabled=true
                        binding.btnCommit.visibility=View.VISIBLE
                        binding.btnCommitGray.visibility=View.GONE

                    }

                    //设置图片
                    if(!TextUtils.isEmpty(result.imgPaths))
                    mAdapter.setList(result.imgPaths.split(","))

                }
            }

        }

        binding.compelete.setOnClickListener(View.OnClickListener {
            setCheckTop()
            binding.compelete.isChecked=true
        })

        binding.nocompelete.setOnClickListener(View.OnClickListener {
            setCheckTop()
            binding.nocompelete.isChecked=true
        })
        binding.nd1.setOnClickListener(View.OnClickListener {
            setCheckDonw()
            binding.nd1.isChecked=true
        })
        binding.nd2.setOnClickListener(View.OnClickListener {
            setCheckDonw()
            binding.nd2.isChecked=true
        })
        binding.nd3.setOnClickListener(View.OnClickListener {
            setCheckDonw()
            binding.nd3.isChecked=true
        })
        binding.btnCommit.setOnClickListener(View.OnClickListener {
            var compelete=0
            var difficulty=0
            if (binding.compelete.isChecked){
                compelete=1
            }else{
                compelete=2
            }
            if (binding.nd1.isChecked){
                difficulty=1
            }else if (binding.nd2.isChecked){
                difficulty=2
            }else if (binding.nd3.isChecked){
                difficulty=3
            }
            viewModel.CommitFeedback(id,compelete,difficulty,ispost)
//            difficulty?.let { it1 -> ispost?.let { it2 -> viewModel.CommitFeedback(id,compelete, it1, it2) } }
        })
        initParentsLayaout()

    }

    private fun setCheckTop() {
        binding.compelete.isChecked = false
        binding.nocompelete.isChecked = false
    }

    private fun setCheckDonw() {
        binding.nd1.isChecked = false
        binding.nd2.isChecked = false
        binding.nd3.isChecked = false
    }
    fun initParentsLayaout(){
        binding.tvExpand.visibility=View.GONE
        binding.imExpand.visibility=View.GONE
    }
    fun setvisibility(){
        if (binding.tvExpand.getText().toString().equals("展开")){
            binding.tvTitle.visibility=View.VISIBLE
            binding.tvContent.visibility=View.VISIBLE
            binding.recyclerView.visibility=View.VISIBLE
            binding.tvExpand.setText("收起")
            binding.imExpand.setImageResource(R.drawable.icon_up2)
        }else{
            binding.tvTitle.visibility=View.VISIBLE
            binding.tvContent.visibility=View.GONE
            binding.recyclerView.visibility=View.GONE
            binding.tvExpand.setText("展开")
            binding.imExpand.setImageResource(R.drawable.icon_down2)
        }

    }
    override fun initView() {
        binding.top.title.text = getString(R.string.operation_detail_title)
        binding.top.backLayout.setOnClickListener { finish() }
        binding.top.ivRight.visibility=View.GONE


    }



    private val mAdapter =
            object :
                    BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_operation_work_img) {
                override fun convert(holder: BaseViewHolder, item: String) {
                    val viewBind = ItemOperationWorkImgBinding.bind(holder.itemView)
                    GlideUtil.loadImageRadius(baseContext, item, viewBind.img, SizeUtils.dp2px(4f))
                    viewBind.img.setOnClickListener(View.OnClickListener {
                        PhotoViewActivity.start(this@OperationDetailActivityByParents, item)
                    })
                }
            }
}