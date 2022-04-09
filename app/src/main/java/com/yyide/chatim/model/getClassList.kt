package com.yyide.chatim.model

import com.google.gson.annotations.SerializedName
import com.yyide.chatim.model.schedule.ScheduleData

data class getClassList(

        val children: List<Children>,
        val id: String,
        val name: String,
        val pid: Int

) {

        data class Children(
                val children: List<Any>,
                val id: String,
                val name: String,
                val pid: String
        )
}