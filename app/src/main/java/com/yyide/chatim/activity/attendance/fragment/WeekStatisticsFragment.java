package com.yyide.chatim.activity.attendance.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.yyide.chatim.R;
import com.yyide.chatim.databinding.FragmentWeekStatisticsBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeekStatisticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeekStatisticsFragment extends Fragment {
    private static final String TAG = "WeekStatisticsFragment";
    private FragmentWeekStatisticsBinding mViewBinding;
    private List<Fragment> fragments = new ArrayList<>();
    public WeekStatisticsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WeekStatisticsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WeekStatisticsFragment newInstance(String param1, String param2) {
        WeekStatisticsFragment fragment = new WeekStatisticsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_week_statistics, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewBinding = FragmentWeekStatisticsBinding.bind(view);
        List<String> listTab = new ArrayList<>();
        listTab.add("缺勤");
        listTab.add("迟到");
        listTab.add("请假");
        listTab.add("早退");
        listTab.add("未签到");
        fragments.add(StatisticsListFragment.newInstance(listTab.get(0)));
        fragments.add(StatisticsListFragment.newInstance(listTab.get(1)));
        fragments.add(StatisticsListFragment.newInstance(listTab.get(2)));
        fragments.add(StatisticsListFragment.newInstance(listTab.get(3)));
        fragments.add(StatisticsListFragment.newInstance(listTab.get(4)));

        mViewBinding.viewpager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        mViewBinding.viewpager.setUserInputEnabled(false);
        mViewBinding.viewpager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return fragments.get(position);
            }

            @Override
            public int getItemCount() {
                return listTab.isEmpty() ? 0 : listTab.size();
            }
        });
        mViewBinding.rgAttendanceType.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId){
                case R.id.rb_absence:
                    mViewBinding.viewpager.setCurrentItem(0);
                    break;
                case R.id.rb_late:
                    mViewBinding.viewpager.setCurrentItem(1);
                    break;
                case R.id.rb_leave:
                    mViewBinding.viewpager.setCurrentItem(2);
                    break;
                case R.id.rb_leave_early:
                    mViewBinding.viewpager.setCurrentItem(3);
                    break;
                case R.id.rb_not_sign_in:
                    mViewBinding.viewpager.setCurrentItem(4);
                    break;
                default:
                    break;
            }
        });

    }
}