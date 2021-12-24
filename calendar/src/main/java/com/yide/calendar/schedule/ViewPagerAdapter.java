package com.yide.calendar.schedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yide.calendar.R;

import java.util.List;

/**
 * @author liu tao
 * @date 2021/12/24 16:40
 * @description 描述
 */
public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {
    private Context context;
    private List<View> viewList;

    public ViewPagerAdapter(Context context, List<View> viewList) {
        this.context = context;
        this.viewList = viewList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_viewpager,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final View view = viewList.get(position);
        holder.frameLayout.removeAllViews();
        holder.frameLayout.addView(view);
    }

    @Override
    public int getItemCount() {
        return viewList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        FrameLayout frameLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            frameLayout = itemView.findViewById(R.id.calendar_frame);
        }
    }
}
