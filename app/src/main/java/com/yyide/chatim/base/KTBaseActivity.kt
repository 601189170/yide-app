package com.yyide.chatim.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.yyide.chatim.R

open class KTBaseActivity<VB : ViewBinding>(open val block: (LayoutInflater) -> VB) :
    BaseActivity() {
    protected val binding by lazy { block(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
    }

    protected open fun initView(){}
}