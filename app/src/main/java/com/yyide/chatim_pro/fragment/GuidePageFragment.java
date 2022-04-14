package com.yyide.chatim_pro.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.SPUtils;
import com.yyide.chatim_pro.R;
import com.yyide.chatim_pro.base.BaseConstant;
import com.yyide.chatim_pro.databinding.FragmentGuidePageBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GuidePageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GuidePageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "index";

    // TODO: Rename and change types of parameters
    private int index;
    private FragmentGuidePageBinding bind;

    public GuidePageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param index Parameter 1.
     * @return A new instance of fragment GuidePageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GuidePageFragment newInstance(int index) {
        GuidePageFragment fragment = new GuidePageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_PARAM1, 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_guide_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind = FragmentGuidePageBinding.bind(view);
        switch (index) {
            case 0:
                bind.ivImage.setImageResource(R.drawable.guide1);
                break;
            case 1:
                bind.ivImage.setImageResource(R.drawable.guide2);
                break;
            case 2:
                bind.ivImage.setImageResource(R.drawable.guide3);
                break;
            case 3:
                bind.ivImage.setImageResource(R.drawable.guide4);
                bind.ivImage.setOnClickListener(v ->{
                    //去首页
                    SPUtils.getInstance().put(BaseConstant.FIRST_OPEN_APP,false);
                    final Intent intent = getActivity().getIntent();
                    getActivity().setResult(Activity.RESULT_OK,intent);
                    getActivity().finish();
                }
               );
                break;
            default:
                break;
        }
    }
}