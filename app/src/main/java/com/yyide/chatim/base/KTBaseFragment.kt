package com.yyide.chatim.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

abstract class KTBaseFragment<VB : ViewBinding>(
    val bindingBlock: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : BaseFragment() {
    private lateinit var _binding: VB
    protected val binding get() = _binding
//    override fun getLayoutId() = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingBlock(inflater, container, false)
        return _binding.root
    }
}