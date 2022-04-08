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
        bind.studentName.text = "${data!!.name}"
        bind.normalOut.text = "${data!!.normalNumOut}"
        bind.normalEvent.text = "${data!!.normalNumEvent}"
        bind.normalCourse.text = "${data!!.normalNumCourse}"
        bind.unusualOut.text = "${data!!.errorNumOut}"
        bind.unusualEvent.text = "${data!!.errorNumEvent}"
        bind.unusualCourse.text = "${data!!.errorNumCourse}"
    }

    class BannerViewHolder(@NonNull view: View) : RecyclerView.ViewHolder(view) {
    }
}