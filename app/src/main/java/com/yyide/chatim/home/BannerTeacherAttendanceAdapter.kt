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
    ), View.OnClickListener {
    lateinit var onItemClickListener: OnItemClickListener
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
        //holder.itemView.tag = ""
            bind.outOrIn.text = data?.normalRateOut?:"0"
            bind.outOrIn.text = data?.normalRateTime?:"0"
            bind.outOrIn.text = data?.normalRateCourse?:"0"


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

    public fun SetOnTeaItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int);
    }

}