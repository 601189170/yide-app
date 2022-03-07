package com.yyide.chatim.activity.operation.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yyide.chatim.R

class OperationChildFragment : Fragment() {

    companion object {
        fun newInstance() = OperationChildFragment()
    }

    private lateinit var viewModel: OperationChildViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.operation_child_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(OperationChildViewModel::class.java)

    }

}