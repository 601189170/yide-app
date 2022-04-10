package com.yyide.chatim.adapter;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.yyide.chatim.R;
import com.yyide.chatim.database.ScheduleDaoUtil;
import com.yyide.chatim.model.sitetable.SiteTableRsp;
import com.yyide.chatim.utils.VHUtil;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/3/29.
 */

public class TableItemAdapter2 extends BaseAdapter {
    public List<SiteTableRsp.DataBean.TimetableListBean> list = new ArrayList<>();

    public int position = -1;
    public int sl = -1;
    private OnItemClickListener mOnItemClickListener;

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public SiteTableRsp.DataBean.TimetableListBean getItem(int position) {
        return list.get(position);
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null)
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.course_card, viewGroup, false);
        TextView text_view = VHUtil.ViewHolder.get(view, R.id.text_view);
        ImageView img = VHUtil.ViewHolder.get(view, R.id.img);
        ConstraintLayout layout = VHUtil.ViewHolder.get(view, R.id.layout);

//        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, SizeUtils.dp2px(80));
//        layout.setLayoutParams(layoutParams);
        //text_view.setText(getItem(position).subjectName + "\n" + getItem(position).fromDateTime + "\n" + getItem(position).toDateTime);
        String subjectName = getItem(position).getSubjectName();
        if (subjectName == null || subjectName.isEmpty()) {
            subjectName = "/";
        }
        text_view.setText(subjectName);

        if (!subjectName.equals("/")) {
            String finalSubjectName = subjectName;
            text_view.setOnClickListener(v -> {

                if (mOnItemClickListener != null){
                    img.setVisibility(View.VISIBLE);
                    sl=position;
                    if (position==sl){
                        img.setVisibility(View.VISIBLE);
                    }else {
                        img.setVisibility(View.GONE);
                    }
                    mOnItemClickListener.onItemClick(v, finalSubjectName,position);
                }
            });
        }


        if (position % 7 < this.position) {
            text_view.setTextColor(view.getContext().getResources().getColor(R.color.black11));
        } else {
            text_view.setTextColor(view.getContext().getResources().getColor(R.color.black9));
        }

        if (position % 7 == this.position) {
            layout.setBackground(view.getContext().getResources().getDrawable(R.drawable.bg_table_ls));
        } else {
            layout.setBackground(view.getContext().getResources().getDrawable(R.drawable.bg_white2));
        }
        final SiteTableRsp.DataBean.TimetableListBean item = getItem(position);
        final String kssj = item.getStartTime();
        final String jssj = item.getEndTime();
        final String date = item.getDate();
        if (!TextUtils.isEmpty(date) && !TextUtils.isEmpty(kssj) && !TextUtils.isEmpty(jssj)) {
            DateTime startTime = ScheduleDaoUtil.INSTANCE.toDateTime(date + " " + kssj, "yyyy-MM-dd HH:mm");
            DateTime endTime = ScheduleDaoUtil.INSTANCE.toDateTime(date + " " + jssj, "yyyy-MM-dd HH:mm");
            final DateTime now = DateTime.now();
            if (now.compareTo(startTime) >= 0 && now.compareTo(endTime) <= 0) {
                //当前课程
                layout.setBackground(view.getContext().getResources().getDrawable(R.drawable.bg_table_current));
                text_view.setTextColor(view.getContext().getResources().getColor(R.color.white));
            }
        }
        return view;
    }

    public void notifyData(List<SiteTableRsp.DataBean.TimetableListBean> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void setIndex(int position) {
        this.position = position;
        notifyDataSetChanged();
    }
    public void setSelectSubject(int sl) {
        this.sl = sl;
        notifyDataSetChanged();
    }
    // 自定义点击事件
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, String content,int index);
    }

}
