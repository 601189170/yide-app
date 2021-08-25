package com.yyide.chatim.activity.book

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.SizeUtils
import com.yyide.chatim.R
import com.yyide.chatim.SpData
import com.yyide.chatim.activity.BookSearchActivity
import com.yyide.chatim.activity.book.adapter.BookClassesAdapter
import com.yyide.chatim.activity.book.adapter.BookStaffAdapter
import com.yyide.chatim.base.BaseConstant
import com.yyide.chatim.base.BaseFragment
import com.yyide.chatim.base.BaseMvpFragment
import com.yyide.chatim.databinding.FragmentTeacherBookBinding
import com.yyide.chatim.model.BookRsp
import com.yyide.chatim.model.GetUserSchoolRsp
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

    private var staffAdapter = BookStaffAdapter()
    private lateinit var classesAdapter: BookClassesAdapter
    private fun initView() {
        mViewBinding.nestedScrollView.visibility = View.INVISIBLE
        //通讯录列表
        mViewBinding.bookStaffList.layoutManager = LinearLayoutManager(context)
        mViewBinding.bookStaffList.adapter = staffAdapter
//        staffAdapter.setEmptyView(R.layout.empty)
        mViewBinding.bookClassesList.layoutManager = LinearLayoutManager(context)
        classesAdapter = BookClassesAdapter(R.layout.item_new_book)
        mViewBinding.bookClassesList.adapter = classesAdapter
        mvpPresenter.getAddressBook("2")
    }

    override fun getBookList(model: BookRsp?) {
        if (model != null) {
            if (model.code == BaseConstant.REQUEST_SUCCES2) {
                mViewBinding.nestedScrollView.visibility = View.VISIBLE
                //处理没有学校名字
                if (TextUtils.isEmpty(model.data.schoolName_)) {
                    mViewBinding.tvSchoolName.text = SpData.getIdentityInfo().schoolName
                } else {
                    mViewBinding.tvSchoolName.text = model.data.schoolName_
                }
                GlideUtil.loadImageRadius2(
                    context,
                    model.data.schoolBadgeImg,
                    mViewBinding.img,
                    SizeUtils.dp2px(2f)
                )
                staffAdapter.setList(model.data.departmentList)
                classesAdapter.setList(model.data.classesList)
            }
        }
    }

    override fun getBookListFail(msg: String?) {
        Log.d(TAG, "getBookListFail：$msg")
    }
}