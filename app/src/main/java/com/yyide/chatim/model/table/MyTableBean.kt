package com.yyide.chatim.model.table

import com.yyide.chatim.model.sitetable.SiteTableRsp

data class MyTableBean(val weekList: SiteTableRsp.DataBean.WeekListBean,
                       val thisWeekEndDate: String = "",
                       val semesterYear: String = "",
                       val weekTotal: Int = 0,
                       val semesterYearName: String = "",
                       val thisWeek: Int = 0,
                       val thisWeekStartDate: String = "",
                       val list: List<MyTableListItem>?)