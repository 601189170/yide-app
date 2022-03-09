package com.yyide.chatim.login

import android.annotation.SuppressLint
import android.content.Intent
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.alibaba.fastjson.JSON
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.yyide.chatim.MainActivity
import com.yyide.chatim.SpData
import com.yyide.chatim.base.KTBaseActivity
import com.yyide.chatim.databinding.ActivityIdentitySelectBinding
import com.yyide.chatim.login.banner.IdentityAdapter
import com.yyide.chatim.login.banner.ScaleTransformer
import com.yyide.chatim.login.banner.SchoolAdapter
import com.yyide.chatim.login.viewmodel.IdentitySelectViewModel
import com.yyide.chatim.model.SchoolRsp
import com.yyide.chatim.utils.loge

/**
 * @DESC 身份选择
 * @author lrz
 * @time 2022年3月7日
 */
class IdentitySelectActivity :
    KTBaseActivity<ActivityIdentitySelectBinding>(ActivityIdentitySelectBinding::inflate) {

    val viewModel: IdentitySelectViewModel by viewModels()
    var schoolBean: SchoolRsp? = null
    var identityBean: SchoolRsp.IdentityBean? = null
    var schoolList: List<SchoolRsp>? = null

    override fun initView() {
        super.initView()

        binding.btnConfirm.setOnClickListener {
            nextStep()
        }

        //处理选择学校数据
        viewModel.schoolLiveData.observe(this) {
            hideLoading()
            if (it.isSuccess) {
                val listData = it.getOrNull()
                if (!listData.isNullOrEmpty()) {
                    schoolList = listData
                    //选择学校
                    initSchoolViewPager(binding.vpSchool, binding.constraintLayout1, listData)
                }
            } else {
                it.exceptionOrNull()?.localizedMessage?.let { it1 ->
                    loge(it1)
                    ToastUtils.showLong(it1)
                }
            }
        }
        showLoading()
        viewModel.schoolIdentity()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initSchoolViewPager(
        viewPager: ViewPager,
        viewPagerRootView: ConstraintLayout,
        listData: List<SchoolRsp>
    ) {
        //设置适配器
        viewPager.adapter =
            SchoolAdapter(this, listData)
        //Pager之间的间距
        viewPager.pageMargin = 20

        //预加载
        viewPager.offscreenPageLimit = 3

        //设置两边小 中间大 【切记配合 xml: android:clipChildren="false" 参数使用】
        viewPager.setPageTransformer(
            true,
            ScaleTransformer()
        )
        binding.vpSchool.addOnPageChangeListener(onPageChangeListener)
        //默认第一张图 左右都有图
        if (listData.size > 2) {
            onPageChangeListener.onPageSelected(binding.vpSchool.currentItem)
        } else {
            onPageChangeListener.onPageSelected(binding.vpSchool.currentItem)
        }
        //viewPager左右两边滑动无效的处理
        viewPagerRootView.setOnTouchListener { v, event -> viewPager.onTouchEvent(event) }
    }

    private val onPageChangeListener = object : OnPageChangeListener {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {
            schoolBean = schoolList?.get(position)
            //选择身份
            schoolList?.get(position)?.let {
                initIdentityViewPager(
                    binding.vpIdentity,
                    binding.constraintLayout2,
                    it.children
                )
            }
//            ToastUtils.showShort("onPageSelected:${position}")
        }

        override fun onPageScrollStateChanged(state: Int) {

        }
    }

    private fun initIdentityViewPager(
        viewPager: ViewPager,
        viewPagerRootView: ConstraintLayout,
        listData: List<SchoolRsp.IdentityBean>
    ) {
        //设置适配器
        viewPager.adapter =
            IdentityAdapter(this, listData)

        //Pager之间的间距
        viewPager.pageMargin = 20

        //预加载
        viewPager.offscreenPageLimit = 3

        //设置两边小 中间大 【切记配合 xml: android:clipChildren="false" 参数使用】
        viewPager.setPageTransformer(
            true,
            ScaleTransformer()
        )

        binding.vpIdentity.addOnPageChangeListener(identityOnPageChangeListener)
        if (listData.isNotEmpty()) {
            identityOnPageChangeListener.onPageSelected(0)
        }
        //viewPager左右两边滑动无效的处理
        viewPagerRootView.setOnTouchListener { v, event -> viewPager.onTouchEvent(event) }
    }

    private val identityOnPageChangeListener = object : OnPageChangeListener {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {
            identityBean = schoolBean!!.children[position]
            //ToastUtils.showShort("选择身份:${identityBean!!.identityName}")
        }

        override fun onPageScrollStateChanged(state: Int) {

        }
    }

    private fun nextStep() {
        when {
            schoolBean == null -> {
                ToastUtils.showShort("请选择学校")
            }
            identityBean == null -> {
                ToastUtils.showShort("请选择身份")
            }
            else -> {
                viewModel.loginLiveData.observe(this) {
                    if (it.isSuccess) {
                        SPUtils.getInstance()
                            .put(SpData.SCHOOLINFO, JSON.toJSONString(schoolBean))
                        SPUtils.getInstance()
                            .put(SpData.IDENTIY_INFO, JSON.toJSONString(identityBean))
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        it.exceptionOrNull()?.localizedMessage?.let { it1 ->
                            loge(it1)
                            ToastUtils.showLong(it1)
                        }
                    }
                }
                viewModel.identityLogin(identityBean!!.id, schoolBean!!.id)
            }
        }
    }
}