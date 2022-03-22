package com.yyide.chatim.activity

import com.yyide.chatim.R
import com.yyide.chatim.base.KTBaseActivity
import com.yyide.chatim.databinding.ActivityTodoBinding
import com.yyide.chatim.home.TodoMsgFragment

class TodoActivity : KTBaseActivity<ActivityTodoBinding>(ActivityTodoBinding::inflate) {

    override fun initView() {
        super.initView()
        binding.top.title.text = getString(R.string.todo_title)
        binding.top.backLayout.setOnClickListener { finish() }
        supportFragmentManager.beginTransaction().replace(binding.container.id, TodoMsgFragment())
            .commit()
    }
}