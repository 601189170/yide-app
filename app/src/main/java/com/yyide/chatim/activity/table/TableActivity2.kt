package com.yyide.chatim.activity.table

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.yyide.chatim.base.KTBaseActivity
import com.yyide.chatim.databinding.ActivityTableTitle2Binding
import com.yyide.chatim.databinding.ActivityTableTitleBinding
import com.yyide.chatim.fragment.*

/**
 * 班级课表
 *
 * @update 2022年2月21日
 * @Author lrz
 * @Version 2.0
 */
class TableActivity2 : KTBaseActivity<ActivityTableTitle2Binding>(ActivityTableTitle2Binding::inflate) {


    var classesId:String?=""
    var subjectId:String?=""
    var teacherIds:String?=""
    var className:String?=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        classesId= intent.getStringExtra("classesId")
        subjectId= intent.getStringExtra("subjectId")
        teacherIds= intent.getStringExtra("teacherIds")
        className= intent.getStringExtra("className")

        binding.top.title.text = "课表"
        binding.top.backLayout.setOnClickListener { finish() }

        supportFragmentManager.beginTransaction()
                .replace(binding.content.id, ClassTableFragment2()).commit()
    }

    public override fun onResume() {
        super.onResume()
    }

    public override fun onPause() {
        super.onPause()
    }


}