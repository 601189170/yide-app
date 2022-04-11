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
    ) {
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
        if (data != null) {
            bind.studentName.text = if (data.name != null) "${data.name}" else ""
            bind.normalOut.text = if (data.normalNumOut != null) "${data.normalNumOut}" else "0"
            bind.normalEvent.text =
                if (data.normalNumEvent != null) "${data.normalNumEvent}" else "0"
            bind.normalCourse.text =
                if (data.normalNumCourse != null) "${data.normalNumCourse}" else "0"
            bind.unusualOut.text = if (data.errorNumOut != null) "${data.errorNumOut}" else "0"
            bind.unusualEvent.text =
                if (data.errorNumEvent != null) "${data.errorNumEvent}" else "0"
            bind.unusualCourse.text =
                if (data.errorNumCourse != null) "${data.errorNumCourse}" else "0"
        }
    }

    class BannerViewHolder(@NonNull view: View) : RecyclerView.ViewHolder(view) {
    }
}