package com.yyide.chatim.model

import com.alibaba.fastjson.JSON
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.alibaba.fastjson.annotation.JSONField

/**
 * @Description: java类作用描述
 * @Author: liu ri
 * @CreateDate: 2021/4/13 15:50
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/13 15:50
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
class TodoRsp {
    var total = 0
    var pageSize = 0
    var pageNo = 0

    @JSONField(name = "list")
    var list: List<TodoItemBean>? = null

    data class TodoItemBean(
        @JSONField(name = "id")
        var id: String = "",
        @JSONField(name = "jsonData")
        var jsonData: String = "",
        @JSONField(name = "processInstanceId")
        var processInstanceId: String = "",
        @JSONField(name = "realName")
        var realName: String = "",
        @JSONField(name = "startTime")
        var startTime: String = "",
        @JSONField(name = "status")
        var status: Int = -1,//status; //0 已拒绝  1待审批 2 已通过
        @JSONField(name = "taskId")
        var taskId: String = "",
        @JSONField(name = "taskName")
        var taskName: String = "",
        @JSONField(name = "title")
        var title: String = "",
        @JSONField(name = "identity")
        var identity: String = "",
        @JSONField(name = "type")
        var type: Int = -1
    ) : MultiItemEntity {
        data class LeaveBean(
            @JSONField(name = "endTime")
            var endTime: String = "",
            @JSONField(name = "hours")
            var hours: String = "",
            @JSONField(name = "reason")
            var reason: String = "",
            @JSONField(name = "startTime")
            var startTime: String = "",
            @JSONField(name = "student")
            var student: String = ""
        )

        fun getJsonData(): LeaveBean? {
            return try {
                JSON.parseObject(jsonData, LeaveBean::class.java)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        override val itemType: Int
            get() {
                var type = -1
                //        switch (messageType) {
                //            case ITEM_TYPE_MESSAGE:
                //                type = TodoRsp.ITEM_TYPE_MESSAGE;
                //                break;
                //            case ITEM_TYPE_NOTICE:
                //                type = TodoRsp.ITEM_TYPE_NOTICE;
                //                break;
                //            default:
                //                type = TodoRsp.RecordsBean.ITEM_TYPE_LEAVE;
                //                break;
                //        }
                type = ITEM_TYPE_LEAVE
                return type
            }

        companion object {
            const val IS_TEXT_TYPE = "1"
            const val ITEM_TYPE_MESSAGE = 1
            const val ITEM_TYPE_NOTICE = 2
            const val ITEM_TYPE_LEAVE = 3
        }
    }

}