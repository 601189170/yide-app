package com.yyide.chatim.activity.weekly.home

import android.animation.ValueAnimator
import android.widget.ProgressBar
import com.alibaba.fastjson.JSON
import com.tencent.mmkv.MMKV
import com.yyide.chatim.base.MMKVConstant
import com.yyide.chatim.model.WeeklyDateBean

object WeeklyUtil {

    fun getDesc(): String {
        val decodeStringSet = MMKV.defaultMMKV().decodeStringSet(MMKVConstant.YD_WEEKLY_DESC)
        val list = mutableListOf<String>()
        var desc = ""
        list.addAll(decodeStringSet)
        if (list.size > 0) {
            val random = (0 until list.size).random()
            desc = list[random]
        }
        return desc
    }

    fun getDateTime(): WeeklyDateBean.DataBean.TimesBean? {
        val json = MMKV.defaultMMKV().decodeString(MMKVConstant.YD_WEEKLY_DATE)
        try {
            val dataBean = JSON.parseObject(json, WeeklyDateBean.DataBean::class.java)
            if (dataBean != null && dataBean.time != null) {
                return dataBean.time
            }
        } catch (e: Exception) {
            return WeeklyDateBean.DataBean.TimesBean()
        }
        return WeeklyDateBean.DataBean.TimesBean()
    }

    fun getDateTimes(): List<WeeklyDateBean.DataBean.TimesBean> {
        var dataTimes = mutableListOf<WeeklyDateBean.DataBean.TimesBean>()
        try {
            val json = MMKV.defaultMMKV().decodeString(MMKVConstant.YD_WEEKLY_DATE)
            val dataBean = JSON.parseObject(json, WeeklyDateBean.DataBean::class.java)
            if (dataBean != null && dataBean.times != null) {
                dataTimes = dataBean.times
                return dataTimes
            }
        } catch (e: Exception) {
            return dataTimes
        }
        return dataTimes
    }

    fun setAnimation(view: ProgressBar, mProgressBar: Int) {
        val animator = ValueAnimator.ofInt(0, mProgressBar).setDuration(600)
        animator.addUpdateListener { valueAnimator: ValueAnimator ->
            view.progress = valueAnimator.animatedValue as Int
        }
        animator.start()
    }
}