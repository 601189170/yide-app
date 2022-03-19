package com.yyide.chatim.activity.book.fragment

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.yyide.chatim.R
import com.yyide.chatim.SpData
import com.yyide.chatim.activity.book.BookPatriarchDetailActivity
import com.yyide.chatim.activity.book.BookStudentDetailActivity
import com.yyide.chatim.activity.book.adapter.BookClassesAdapter
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.base.BaseMvpFragment
import com.yyide.chatim.databinding.FragmentTeacherBookBinding
import com.yyide.chatim.model.BookClassesItem
import com.yyide.chatim.model.BookRsp
import com.yyide.chatim.presenter.BookPresenter
import com.yyide.chatim.utils.GlideUtil
import com.yyide.chatim.view.BookView

class BookTeacherFragment : BaseMvpFragment<BookPresenter>(), BookView {

    private val TAG = BookTeacherFragment::class.java.simpleName
    private lateinit var mViewBinding: FragmentTeacherBookBinding

    companion object {
        fun newInstance(): BookTeacherFragment {
            return BookTeacherFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mViewBinding = FragmentTeacherBookBinding.inflate(layoutInflater)
        return mViewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun createPresenter(): BookPresenter {
        return BookPresenter(this)
    }

    private lateinit var classesAdapter: BookClassesAdapter
    private fun initView() {
        mViewBinding.root.visibility = View.INVISIBLE
        mViewBinding.bookClassesList.layoutManager = LinearLayoutManager(context)
        classesAdapter = BookClassesAdapter()
        mViewBinding.bookClassesList.adapter = classesAdapter
        classesAdapter.setEmptyView(R.layout.empty)
        mvpPresenter.getAddressBook("2")
        classesAdapter.setOnItemClickListener { adapter, view, position ->
            when {
                adapter.getItemViewType(position) == BookClassesAdapter.ITEM_TYPE_STUDNET -> {
                    context?.let {
                        classesAdapter.getItem(position).student?.let { it1 ->
                            BookStudentDetailActivity.start(
                                it,
                                it1
                            )
                        }
                    }
                }
                adapter.getItemViewType(position) == BookClassesAdapter.ITEM_TYPE_GUARDIAN -> {
                    context?.let {
                        classesAdapter.getItem(position).guardian?.let { it1 ->
                            BookPatriarchDetailActivity.start(
                                it,
                                it1
                            )
                        }
                    }
                }
            }
        }
    }

    override fun getBookList(model: BookRsp?) {
        mViewBinding.root.visibility = View.VISIBLE
        if (model != null) {
            if (model.code == BaseConstant.REQUEST_SUCCESS) {
                //处理没有学校名字
                if (TextUtils.isEmpty(model.data.schoolName_)) {
                    mViewBinding.tvSchoolName.text = SpData.Schoolinfo().schoolName
                } else {
                    mViewBinding.tvSchoolName.text = model.data.schoolName_
                }
                GlideUtil.loadImageHead(
                    context,
                    model.data.schoolBadgeImg,
                    mViewBinding.img
                )
                val classesList = model.data.classesList
                val bookClassesItem = BookClassesItem()
                bookClassesItem.departmentList = model.data.departmentList
                bookClassesItem.itemType = BookClassesAdapter.ITEM_TYPE_DEPARTMENT
                classesList.add(0, bookClassesItem)
                classesAdapter.setList(classesList)
            }
        }
    }

    override fun getBookListFail(msg: String?) {
        mViewBinding.root.visibility = View.VISIBLE
        Log.d(TAG, "getBookListFail：$msg")
    }

}