package com.yyide.chatim.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.youth.banner.adapter.BannerAdapter
import com.yyide.chatim.R
import com.yyide.chatim.databinding.ItemStudentAttendanceBinding
import com.yyide.chatim.model.AttendanceStudentRsp

/**
 * @ProjectName : yideapp
 * @Author : J
 * @Time : 2022/4/9 10:19
 * @Description : 家长端的考勤
 */
class BannerStudentAttendanceAdapter(datas: MutableList<AttendanceStudentRsp.AttendanceAdapterBean>?) :
    BannerAdapter<AttendanceStudentRsp.AttendanceAdapterBean, BannerStudentAttendanceAdapter.BannerViewHolder>(
        datas
    ), View.OnClickListener {
    lateinit var onItemClickListener: OnStudentItemClickListener
    override fun onCreateHolder(
        parent: ViewGroup?,
        viewType: Int
    ): BannerViewHolder {
        val view = LayoutInflater.from(parent?.context)
            .inflate(R.layout.item_student_attendance, parent, false)
        return BannerViewHolder(view)

    }

    override fun onBindView(
        holder: BannerViewHolder?,
        data: AttendanceStudentRsp.AttendanceAdapterBean?,
        position: Int,
        size: Int
    ) {
        var bind = ItemStudentAttendanceBinding.bind(holder!!.itemView)
        bind.studentName.text = data?.name ?: "0"
        bind.normalOut.text = (data?.normalNumCourse ?: "0") as String?
        bind.normalEvent.text = (data?.normalNumEvent ?: "0") as String?
        bind.normalCourse.text = (data?.normalNumCourse ?: "0") as String?
        bind.unusualOut.text = (data?.errorNumOut ?: "0") as String?
        bind.unusualEvent.text = (data?.errorNumEvent ?: "0") as String?
        bind.unusualCourse.text = (data?.errorNumCourse ?: "0") as String?
    }

    class BannerViewHolder(@NonNull view: View) : RecyclerView.ViewHolder(view) {
    }

    override fun onClick(v: View?) {
        val tag = v!!.getTag(R.id.layout_teacher)
        if (tag == null || tag !is Int) return
        val postion = tag
        if (::onItemClickListener.isInitialized) {
            onItemClickListener.onItemClick(v, postion)
        }
    }

    public fun SetOnStuItemClickListener(onItemClickListener: OnStudentItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    interface OnStudentItemClickListener {
        fun onItemClick(view: View, position: Int);
    }
}