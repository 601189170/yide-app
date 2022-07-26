package com.yyide.chatim_pro.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.youth.banner.adapter.BannerAdapter
import com.yyide.chatim_pro.R
import com.yyide.chatim_pro.databinding.ItemHomeBannerBinding
import com.yyide.chatim_pro.model.message.AcceptMessageItem

class BannerExampleAdapter(data: MutableList<AcceptMessageItem>?) :
    BannerAdapter<AcceptMessageItem, BannerExampleAdapter.BannerViewHolder>(data) {
    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): BannerViewHolder {
        val view =
            LayoutInflater.from(parent?.context).inflate(R.layout.item_home_banner, parent, false)
        return BannerViewHolder(view)
    }

    override fun onBindView(
        holder: BannerViewHolder,
        data: AcceptMessageItem,
        position: Int,
        size: Int
    ) {
        val binding = ItemHomeBannerBinding.bind(holder.itemView)
        binding.tvContent.text = data.title
        if (data.identityUserName.isNotEmpty()){
            binding.itemHomeBannerUserTv.text = "${data.identityUserName}发布于${data.timerDate}"
        }

    }

    class BannerViewHolder(@NonNull view: View) : RecyclerView.ViewHolder(view) {
    }

}