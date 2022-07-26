package com.yyide.chatim_pro.activity.book.fragment

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.yyide.chatim_pro.R
import com.yyide.chatim_pro.SpData
import com.yyide.chatim_pro.activity.book.BookStudentDetailActivity
import com.yyide.chatim_pro.activity.book.BookTeacherDetailActivity
import com.yyide.chatim_pro.base.BaseConstant
import com.yyide.chatim_pro.base.BaseMvpFragment
import com.yyide.chatim_pro.databinding.FragmentBookPartriachBinding
import com.yyide.chatim_pro.databinding.ItemNewBookPrariarchTeacherBinding
import com.yyide.chatim_pro.databinding.ItemNewBookPrtriarchBinding
import com.yyide.chatim_pro.databinding.ItemNewBookPrtriarchStudentBinding
import com.yyide.chatim_pro.model.BookClassesItem
import com.yyide.chatim_pro.model.BookRsp
import com.yyide.chatim_pro.model.BookStudentItem
import com.yyide.chatim_pro.model.BookTeacherItem
import com.yyide.chatim_pro.presenter.BookPresenter
import com.yyide.chatim_pro.utils.GlideUtil
import com.yyide.chatim_pro.view.BookView

/**
 * 家长视角通讯录列表
 */
class BookPatriarchFragment : BaseMvpFragment<BookPresenter>(), BookView {

    private val TAG = BookPatriarchFragment::class.java.simpleName
    private lateinit var viewBinding: FragmentBookPartriachBinding

    companion object {
        fun newInstance(): BookPatriarchFragment {
            return BookPatriarchFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentBookPartriachBinding.inflate(layoutInflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    fun initView() {
        viewBinding.linearlayout.visibility = View.INVISIBLE
        viewBinding.recyclerView.layoutManager = LinearLayoutManager(context)
        viewBinding.recyclerView.adapter = adapter
        mvpPresenter.getAddressBook("1")
    }

    override fun createPresenter(): BookPresenter {
        return BookPresenter(this)
    }

    override fun getBookList(model: BookRsp?) {
        if (model != null) {
            if (model.code == BaseConstant.REQUEST_SUCCESS) {
                viewBinding.linearlayout.visibility = View.VISIBLE
                if (TextUtils.isEmpty(model.data.schoolName_)) {
                    viewBinding.tvSchoolName.text = SpData.Schoolinfo().schoolName
                } else {
                    viewBinding.tvSchoolName.text = model.data.schoolName_
                }
                GlideUtil.loadImageHead(
                    context,
                    model.data.schoolBadgeImg,
                    viewBinding.img
                )
                adapter.setList(model.data.classesList)
            }
        }
    }

    override fun getBookListFail(msg: String?) {
        Log.d(TAG, "getBookListFail：$msg")
    }

    private val adapter =
        object :
            BaseQuickAdapter<BookClassesItem, BaseViewHolder>(R.layout.item_new_book_prtriarch) {
            override fun convert(holder: BaseViewHolder, item: BookClassesItem) {
                val view = ItemNewBookPrtriarchBinding.bind(holder.itemView)
                view.tvName.text = item.name
                view.ivUnfold.setImageDrawable(context.resources.getDrawable(R.mipmap.icon_fold))

                if (item.studentList != null && item.studentList!!.isNotEmpty()) {
                    Log.e(TAG, "onBindViewHolder isUnfold: " + item.unfold)
                    if (item.unfold) {
                        view.ivUnfold.setImageDrawable(context.resources.getDrawable(R.mipmap.icon_unfold))
                        view.teacherList.visibility = View.VISIBLE
                        view.studentList.visibility = View.VISIBLE
                        view.tvStudent.visibility = View.VISIBLE
                        view.tvTeacher.visibility = View.VISIBLE
                        val teacherAdapter = TeacherAdapter()
                        view.teacherList.layoutManager = LinearLayoutManager(context)
                        view.teacherList.adapter = teacherAdapter
                        teacherAdapter.setList(item.teacherList)
                        teacherAdapter.setOnItemClickListener { adapter, view, position ->
                            BookTeacherDetailActivity.start(
                                context,
                                teacherAdapter.getItem(position)
                            )
                        }

                        val studentAdapter = StudentAdapter()
                        view.studentList.layoutManager = LinearLayoutManager(context)
                        view.studentList.adapter = studentAdapter
                        studentAdapter.setList(item.studentList)
                        studentAdapter.setOnItemClickListener { adapter, view, position ->
//                        if (studentAdapter.getItem(position).isOwnChild == "1") {
                            BookStudentDetailActivity.start(
                                context,
                                studentAdapter.getItem(position)
                            )
//                        }
                        }
                    } else {
                        view.ivUnfold.setImageDrawable(context.resources.getDrawable(R.mipmap.icon_fold))
                        view.teacherList.visibility = View.GONE
                        view.studentList.visibility = View.GONE
                        view.tvStudent.visibility = View.GONE
                        view.tvTeacher.visibility = View.GONE
                    }
                    view.root.setOnClickListener { v: View? ->
                        item.unfold = !item.unfold
                        notifyItemChanged(holder.adapterPosition)
                    }
                }
            }
        }

    /**
     * 学生适配器
     */
    inner class StudentAdapter :
        BaseQuickAdapter<BookStudentItem, BaseViewHolder>(R.layout.item_new_book_prtriarch_student) {
        override fun convert(holder: BaseViewHolder, item: BookStudentItem) {
            val view = ItemNewBookPrtriarchStudentBinding.bind(holder.itemView)
            view.tvName.text = item.name
            GlideUtil.loadImageHead(
                context,
                item.faceInformation,
                view.img
            )
        }
    }

    /**
     * 教师适配器
     */
    inner class TeacherAdapter :
        BaseQuickAdapter<BookTeacherItem, BaseViewHolder>(R.layout.item_new_book_prariarch_teacher) {
        override fun convert(holder: BaseViewHolder, item: BookTeacherItem) {
            val view = ItemNewBookPrariarchTeacherBinding.bind(holder.itemView)
            view.tvName.text = item.name + " (${item.teachingSubjects})"
            GlideUtil.loadImageHead(
                context,
                item.faceInformation,
                view.img
            )
        }
    }

}