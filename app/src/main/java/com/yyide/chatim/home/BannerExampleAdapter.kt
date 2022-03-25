package com.yyide.chatim.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.youth.banner.adapter.BannerAdapter
import com.yyide.chatim.R
import com.yyide.chatim.databinding.ItemHomeBannerBinding

class BannerExampleAdapter(datas: MutableList<String>?) :
    BannerAdapter<String, BannerExampleAdapter.BannerViewHolder>(datas) {
    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): BannerViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_home_banner, parent, false)
        return BannerViewHolder(view)
    }

    override fun onBindView(
        holder: BannerViewHolder?,
        data: String?,
        position: Int,
        size: Int
    ) {
        var binding = ItemHomeBannerBinding.bind(holder!!.itemView)

    }

    class BannerViewHolder(@NonNull view: View) : RecyclerView.ViewHolder(view) {
    }

}