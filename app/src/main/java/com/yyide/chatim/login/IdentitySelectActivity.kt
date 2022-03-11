package com.yyide.chatim.login

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.alibaba.fastjson.JSON
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.yyide.chatim.NewMainActivity
import com.yyide.chatim.SpData
import com.yyide.chatim.adapter.SwitchIdentityAdapter
import com.yyide.chatim.base.KTBaseActivity
import com.yyide.chatim.databinding.ActivityIdentitySelectBinding
import com.yyide.chatim.login.banner.ScaleTransformer
import com.yyide.chatim.login.banner.SchoolAdapter
import com.yyide.chatim.login.viewmodel.LoginViewModel
import com.yyide.chatim.model.LoginRsp
import com.yyide.chatim.model.SchoolRsp
import com.yyide.chatim.utils.loge

/**
 * @DESC 身份选择
 * @author lrz
 * @time 2022年3月7日
 */
class IdentitySelectActivity :
    KTBaseActivity<ActivityIdentitySelectBinding>(ActivityIdentitySelectBinding::inflate) {

    val viewModel: LoginViewModel by viewModels()
    var schoolBean: SchoolRsp? = null
    var identityBean: SchoolRsp.IdentityBean? = null
    var schoolList: List<SchoolRsp>? = null

    companion object {
        fun start(context: Context, loginRsp: LoginRsp) {
            val intent = Intent(context, IdentitySelectActivity::class.java)
            intent.putExtra("loginRsp", loginRsp)
            context.startActivity(intent)
        }
    }

    override fun initView() {
        super.initView()
        binding.btnConfirm.setOnClickListener {
            nextStep()
        }

        binding.schoolLeft.setOnClickListener {
            val currentItem = binding.vpSchool.currentItem
            if (schoolList != null && schoolList!!.isNotEmpty()) {
                binding.vpSchool.currentItem = currentItem - 1
            }
        }

        binding.schoolRight.setOnClickListener {
            val currentItem = binding.vpSchool.currentItem
            if (schoolList != null && schoolList!!.isNotEmpty()) {
                binding.vpSchool.currentItem = currentItem + 1
            }
        }
        getSchoolInfo()
    }

    private fun getSchoolInfo() {
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
                if (it.children.isNotEmpty()) {
                    initAdapter(it.children)
                }
            }
        }

        override fun onPageScrollStateChanged(state: Int) {

        }
    }

    private fun initAdapter(listData: List<SchoolRsp.IdentityBean>) {
        val mAdapter = SwitchIdentityAdapter()
        binding.recyclerView.layoutManager =
            GridLayoutManager(this@IdentitySelectActivity, listData.size)
        binding.recyclerView.adapter = mAdapter
        mAdapter.setList(listData)
        mAdapter.setSelectIndex(0)
        mAdapter.setOnItemClickListener { adapter, view, position ->
            mAdapter.setSelectIndex(position)
        }
        identityBean = mAdapter.getSelectItem()
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
                    hideLoading()
                    if (it.isSuccess) {
                        SPUtils.getInstance()
                            .put(SpData.SCHOOLINFO, JSON.toJSONString(schoolBean))
                        SPUtils.getInstance()
                            .put(SpData.IDENTIY_INFO, JSON.toJSONString(identityBean))
                        SPUtils.getInstance().put(SpData.LOGINDATA, JSON.toJSONString(intent.getSerializableExtra("loginRsp")))
                        startActivity(Intent(this, NewMainActivity::class.java))
                        finish()
                    } else {
                        it.exceptionOrNull()?.localizedMessage?.let { it1 ->
                            loge(it1)
                            ToastUtils.showLong(it1)
                        }
                    }
                }
                showLoading()
                viewModel.identityLogin(identityBean!!.id, schoolBean!!.id)
            }
        }
    }
}