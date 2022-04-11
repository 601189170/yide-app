package com.yyide.chatim.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.youth.banner.adapter.BannerAdapter
import com.yyide.chatim.R
import com.yyide.chatim.databinding.ItemTeacherAttendanceBinding
import com.yyide.chatim.model.AttendanceTeacherRsp

/**
 * @ProjectName : yideapp
 * @Author : J
 * @Time : 2022/4/9 10:51
 * @Description : 老师考勤轮播图
 */
class BannerTeacherAttendanceAdapter(datas: MutableList<AttendanceTeacherRsp.AttendanceTeacherAdapterBean>?) :
    BannerAdapter<AttendanceTeacherRsp.AttendanceTeacherAdapterBean, BannerTeacherAttendanceAdapter.BannerViewHolder>(
        datas
    ) {
    override fun onCreateHolder(
        parent: ViewGroup?,
        viewType: Int
    ): BannerViewHolder {
        val view = LayoutInflater.from(parent?.context)
            .inflate(R.layout.item_teacher_attendance, parent, false)
        return BannerViewHolder(view)

    }

    override fun onBindView(
        holder: BannerViewHolder?,
        data: AttendanceTeacherRsp.AttendanceTeacherAdapterBean?,
        position: Int,
        size: Int
    ) {
        val bind = ItemTeacherAttendanceBinding.bind(holder!!.itemView)
        if (data != null) {
            bind.outOrIn.text =  if (data.normalRateOut != null ) "${data!!.normalRateOut}" else "0"
            bind.time.text =  if (data.normalRateTime != null ) "${data!!.normalRateTime}" else "0"
            bind.course.text =  if (data.normalRateCourse != null ) "${data!!.normalRateCourse}" else "0"
        }
    }

    class BannerViewHolder(@NonNull view: View) : RecyclerView.ViewHolder(view) {


    }
}