package com.yyide.chatim.fragment.gate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.yyide.chatim.databinding.FragmentGateThroughListBinding


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class PeopleThroughListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var fragmentGateThroughListBinding: FragmentGateThroughListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentGateThroughListBinding = FragmentGateThroughListBinding.inflate(layoutInflater)
        return fragmentGateThroughListBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentGateThroughListBinding.tvTitle.text = param1
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PeopleThroughListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}