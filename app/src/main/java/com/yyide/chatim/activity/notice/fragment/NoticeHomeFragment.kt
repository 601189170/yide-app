package com.yyide.chatim.activity.notice.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yyide.chatim.activity.notice.fragment.viewmodel.NoticeHomeViewModel
import com.yyide.chatim.base.KTBaseFragment
import com.yyide.chatim.databinding.NoticeHomeFragmentBinding

class NoticeHomeFragment :
    KTBaseFragment<NoticeHomeFragmentBinding>(NoticeHomeFragmentBinding::inflate) {

    companion object {
        fun newInstance() = NoticeHomeFragment()
    }

    private lateinit var viewModel: NoticeHomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NoticeHomeViewModel::class.java)

    }



}