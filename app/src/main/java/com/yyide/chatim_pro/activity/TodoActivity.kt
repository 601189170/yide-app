package com.yyide.chatim_pro.activity

import com.yyide.chatim_pro.R
import com.yyide.chatim_pro.base.KTBaseActivity
import com.yyide.chatim_pro.databinding.ActivityTodoBinding
import com.yyide.chatim_pro.home.TodoMsgFragment

class TodoActivity : KTBaseActivity<ActivityTodoBinding>(ActivityTodoBinding::inflate) {

    override fun initView() {
        super.initView()
        binding.top.title.text = getString(R.string.todo_title)
        binding.top.backLayout.setOnClickListener { finish() }
        supportFragmentManager.beginTransaction().replace(binding.container.id, TodoMsgFragment())
            .commit()
    }
}