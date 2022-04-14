package com.yyide.chatim_pro.activity.operation

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.fastjson.JSON
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim_pro.R
import com.yyide.chatim_pro.activity.PhotoViewActivity
import com.yyide.chatim_pro.activity.operation.fragment.OperationChildFragment
import com.yyide.chatim_pro.activity.operation.viewmodel.OperationTearcherViewModel
import com.yyide.chatim_pro.base.KTBaseActivity
import com.yyide.chatim_pro.databinding.ActivityOperationDetailBinding
import com.yyide.chatim_pro.databinding.ItemOperationWorkImgBinding
import com.yyide.chatim_pro.utils.GlideUtil
import com.yyide.chatim_pro.view.SpacesItemDecoration

class OperationDetailActivity :
    KTBaseActivity<ActivityOperationDetailBinding>(ActivityOperationDetailBinding::inflate) {
    private lateinit var viewModel: OperationTearcherViewModel
    var id=""
    val fragments = mutableListOf<Fragment>()
    var titleIndex=0;
    var two=false;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(OperationTearcherViewModel::class.java)

        id = intent.getStringExtra("id")
        viewModel.getSchoolWorkData(id)

        binding.recyclerView.layoutManager =GridLayoutManager(this, 4)
//        PhotoViewActivity.start(this, imgPath)
        binding.recyclerView.addItemDecoration(SpacesItemDecoration(SpacesItemDecoration.dip2px(5f)))



        binding.tvExpand.setOnClickListener(View.OnClickListener {
            setvisibility()
        })


        binding.recyclerView.adapter = mAdapter
        viewModel.getSchoolWorkRsp.observe(this){
            if (it.isSuccess){
                val result = it.getOrNull()
                if (result!=null){
                    Log.e("TAG", "getSchoolWorkRsp: "+JSON.toJSONString(result))
                    if (!TextUtils.isEmpty(result.classesTimetable.subjectName)){
                        binding.tvGl.text="关联课程:"+result.classesTimetable.timetableTime+"第"+result.classesTimetable.sequence+"节"+result.classesTimetable.startTime
                        binding.tvClassName.text=result.classesTimetable.classesName+result.classesTimetable.subjectName
                    }else{
                        binding.tvGl.visibility=View.GONE
                        binding.tvClassName.text=result.classesTimetable.classesName
                    }

                    binding.textView38.text=result.releaseTime+"发布"


                    binding.tvTitle.text=result.title
                    binding.tvContent.text=result.content
                    val type0=result.feedbackList.size
                    val type1=result.noFeedbackList.size
                    val type2=result.readList.size
                    val type3=result.noReadList.size
                    initViewPager(type0,type1,type2,type3)
                    //设置图片
                    if(!TextUtils.isEmpty(result.imgPaths))
                    mAdapter.setList(result.imgPaths.split(","))

                }
            }

        }
        //删除监听
        viewModel.deleteWorkResult.observe(this){
            if (it.isSuccess){
                ToastUtils.showShort("删除成功")
                finish()
            }else{
                ToastUtils.showShort("删除失败")
            }
        }
        //提醒监听
        viewModel.updateRemindResult.observe(this){
            if (it.isSuccess){
                ToastUtils.showShort("提醒成功")
                viewModel.getSchoolWorkData(id)
            }else{
                ToastUtils.showShort("提醒失败")
            }
        }
//        if (SpData.getIdentityInfo().staffIdentity()) {
//            binding.tvExpand.visibility=View.GONE
//            binding.imExpand.visibility=View.GONE
//        }else{
//
//        }


    }

    fun setIndex(boolean: Boolean){
        two=boolean
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
        binding.top.ivRight.visibility=View.VISIBLE

        binding.top.ivRight.setOnClickListener(View.OnClickListener {

            viewModel.getSchoolWorkRsp.value?.getOrNull()?.id?.let { it1 -> viewModel.deleteWork(it1) }
        })
    }

    private fun initViewPager(type0:Int,type1: Int,type2: Int,type3: Int) {
        val mTitles: MutableList<String> = ArrayList()
        fragments.add(OperationChildFragment.newInstance(0))
        fragments.add(OperationChildFragment.newInstance(1))
        fragments.add(OperationChildFragment.newInstance(2))
        fragments.add(OperationChildFragment.newInstance(3))
        mTitles.add("已反馈"+"("+type0+")")
        mTitles.add("未反馈"+"("+type1+")")
        mTitles.add("已读"+"("+type2+")")
        mTitles.add("未读"+"("+type3+")")

        binding.viewpager.offscreenPageLimit = mTitles.size
        //需要动态数据设置该tab
        binding.viewpager.adapter = object :
            FragmentStatePagerAdapter(
                supportFragmentManager,
                BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
            ) {
            override fun getItem(position: Int): Fragment {
                return fragments[position]
            }

            override fun getCount(): Int {
                return mTitles.size
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return mTitles[position]
            }
        }

        binding.slidingTabLayout.setViewPager(binding.viewpager,mTitles.toTypedArray())
        if (!two){
            binding.slidingTabLayout.currentTab = 0
        }else{
            binding.slidingTabLayout.currentTab = 1
        }
        binding.slidingTabLayout.notifyDataSetChanged()

    }

    private val mAdapter =
            object :
                    BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_operation_work_img) {
                override fun convert(holder: BaseViewHolder, item: String) {
                    val viewBind = ItemOperationWorkImgBinding.bind(holder.itemView)
                    GlideUtil.loadImageRadius(baseContext, item, viewBind.img, SizeUtils.dp2px(4f))
                    viewBind.img.setOnClickListener(View.OnClickListener {
                        PhotoViewActivity.start(this@OperationDetailActivity, item)
                    })
                }
            }
}