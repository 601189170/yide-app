package com.yyide.chatim_pro.model

import com.google.gson.annotations.SerializedName

data class OpSubjectData(@SerializedName("list")
                           val list: List<SubjectBean>)