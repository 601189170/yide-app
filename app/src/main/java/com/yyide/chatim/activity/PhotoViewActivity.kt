package com.yyide.chatim.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.github.chrisbanes.photoview.PhotoView
import com.yyide.chatim.R
import com.yyide.chatim.databinding.ActivityPhotoViewSampleBinding
import com.yyide.chatim.databinding.ItemPhotoViewBinding
import com.yyide.chatim.utils.GlideUtil

class PhotoViewActivity : Activity() {

    private lateinit var viewSampleBinding: ActivityPhotoViewSampleBinding
    private var imgList = mutableListOf<String>()

    companion object {
        @JvmStatic
        fun start(context: Context, path: String) {
            val intent = Intent(context, PhotoViewActivity::class.java)
            intent.putExtra("path", path)
            context.startActivity(intent)
        }

        fun start(context: Context, paths: ArrayList<String>) {
            val intent = Intent(context, PhotoViewActivity::class.java)
            intent.putStringArrayListExtra("paths", paths)
            context.startActivity(intent)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewSampleBinding = ActivityPhotoViewSampleBinding.inflate(layoutInflater)
        setContentView(viewSampleBinding.root)
        initView()
    }

    private fun initView() {
        val path = intent.getStringExtra("path")
        if (intent.getStringArrayListExtra("paths") != null) {
            imgList = intent.getStringArrayListExtra("paths") as MutableList<String>
        }
        if (!TextUtils.isEmpty(path)) {
            if (path != null) {
                imgList.add(path)
            }
        }
        //如果有多个图片就显示数量标
        if (imgList.isNotEmpty() && imgList.size > 1) {
            viewSampleBinding.tvNumber.text =
                getString(R.string.start_number_end_number, 1, imgList.size)
        }
        viewSampleBinding.viewpager2.adapter = SamplePagerAdapter()
        viewSampleBinding.viewpager2.addOnPageChangeListener(object :
            ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                viewSampleBinding.tvNumber.text =
                    getString(R.string.start_number_end_number, position + 1, imgList.size)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })
    }

    inner class SamplePagerAdapter : PagerAdapter() {
        override fun getCount(): Int {
            return imgList.size
        }

        override fun instantiateItem(container: ViewGroup, position: Int): View {
            val itemViewHolder = ItemPhotoViewBinding.inflate(layoutInflater)
            GlideUtil.loadImageRadius(
                baseContext,
                imgList[position],
                itemViewHolder.ivImg,
                SizeUtils.dp2px(6f)
            ) // Now just add PhotoView to ViewPager and return it
            container.addView(
                itemViewHolder.ivImg,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            itemViewHolder.ivImg.setOnClickListener { finish() }
            return itemViewHolder.ivImg
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }
    }

}