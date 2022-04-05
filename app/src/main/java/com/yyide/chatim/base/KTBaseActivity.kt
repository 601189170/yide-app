package com.yyide.chatim.base

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.viewbinding.ViewBinding
import com.yyide.chatim.utils.LoadingTools

open class KTBaseActivity<VB : ViewBinding>(open val block: (LayoutInflater) -> VB) :
    BaseActivity() {
    protected val binding by lazy { block(layoutInflater) }
    private var loading: LoadingTools? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
    }

    fun setScreenFull() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val window: Window = window
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = Color.TRANSPARENT
            window.navigationBarColor = Color.TRANSPARENT
        }
    }

    protected open fun initView() {}

    override fun showLoading() {
        if (loading != null && !isFinishing) {
            loading?.showLoading()
        }
    }

    override fun hideLoading() {
        if (loading != null) {
            loading?.closeLoading()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        loading = null
    }
}