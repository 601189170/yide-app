package com.yyide.chatim.database

import android.util.Log
import androidx.room.*
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.yyide.chatim.model.schedule.LabelListRsp
import com.yyide.chatim.model.schedule.ParticipantRsp
import com.yyide.chatim.model.schedule.ScheduleData
import org.json.JSONArray
import org.json.JSONException

/**
 *
 * @author liu tao
 * @date 2021/10/14 14:31
 * @description 描述indices = [Index(value = ["date"],unique = true)]
 */

@Entity(tableName = "schedule",indices = [Index(value = ["id"],unique = true)])
data class ScheduleBean(
    @PrimaryKey(autoGenerate = true)
    val uid: Long = 0,
    var id: String? = null,
    var name: String? = null,
    var type: String? = null,
    var isTop: String? = null,
    var remark: String? = null,
    var filePath: String? = null,
    var isRepeat: String? = null,
    var status: String? = null,
    var rrule: String? = null,
    var remindType: String? = null,
    var remindTypeInfo: String? = null,
    var startTime: String? = null,
    var endTime: String? = null,
    var iconImg: String? = null,
    var isAllDay: String? = null,
    var siteId: String? = null,
    var siteName: String? = null,
    var updateType: String? = null,
    var updateDate: String? = null,
    var dayOfMonth: String? = null,
    var promoterName: String? = null
) {
    @Ignore
    constructor() : this(0)
}

@Entity(tableName = "label")
data class LabelList(
    @PrimaryKey(autoGenerate = true)
    val uid: Long = 0,
    var id: String? = null,
    var labelName: String? = null,
    var colorValue: String? = null,
    var scheduleCreatorId: String? = null
) {
    @Ignore
    constructor() : this(0)
}


@Entity(tableName = "participant")
data class ParticipantList(
    @PrimaryKey(autoGenerate = true)
    val uid: Long = 0,
    var id: String? = null,
    var userId: String? = null,
    var scheduleId: String? = null,
    var type: String? = null,
    var userName: String? = null,
    var scheduleCreatorId: String? = null
) {
    @Ignore
    constructor() : this(0)
}

data class ScheduleWithParticipantAndLabel(
    @Embedded val schedule: ScheduleBean,

    @Relation(
        parentColumn = "id",
        entityColumn = "scheduleCreatorId"
    )
    val participantList: List<ParticipantList>,

    @Relation(
        parentColumn = "id",
        entityColumn = "scheduleCreatorId"
    )
    val labelList: List<LabelList>
)

fun ScheduleWithParticipantAndLabel.scheduleWithParticipantAndLabelToScheduleData(): ScheduleData {
    this.also {
        val scheduleData = ScheduleData()
        scheduleData.id = this.schedule.id
        scheduleData.name = it.schedule.name
        scheduleData.type = it.schedule.type
        scheduleData.isTop = it.schedule.isTop
        scheduleData.remark = it.schedule.remark
        scheduleData.filePath = it.schedule.filePath
        scheduleData.isRepeat = it.schedule.isRepeat
        scheduleData.status = it.schedule.status
        scheduleData.rrule = jsonToMap(it.schedule.rrule ?: "")
        scheduleData.remindType = it.schedule.remindType
        scheduleData.remindTypeInfo = it.schedule.remindTypeInfo
        scheduleData.startTime = it.schedule.startTime
        scheduleData.endTime = it.schedule.endTime
        scheduleData.iconImg = it.schedule.iconImg
        scheduleData.isAllDay = it.schedule.isAllDay
        scheduleData.siteId = it.schedule.siteId
        scheduleData.siteName = it.schedule.siteName
        scheduleData.updateType = it.schedule.updateType
        scheduleData.updateDate = it.schedule.updateDate
        scheduleData.dayOfMonth = it.schedule.dayOfMonth
        scheduleData.promoterName = it.schedule.promoterName
        // private List<LabelListRsp.DataBean> label;
        // private List<ParticipantRsp.DataBean.ParticipantListBean> participant;
        val participantList = mutableListOf<ParticipantRsp.DataBean.ParticipantListBean>()
        it.participantList.forEach {
            val participant = ParticipantRsp.DataBean.ParticipantListBean()
            participant.id = it.id
            participant.type = it.type
            //学生和教职工取值不一
            participant.name = it.userName
            participantList.add(participant)
        }
        scheduleData.participant = participantList
        val labelList = mutableListOf<LabelListRsp.DataBean>()
        it.labelList.forEach {
            val label = LabelListRsp.DataBean()
            label.id = it.id
            label.labelName = it.labelName
            label.colorValue = it.colorValue
            labelList.add(label)
        }
        scheduleData.label = labelList
        return scheduleData
    }
}

fun ScheduleData.scheduleDataToScheduleWithParticipantAndLabel():ScheduleWithParticipantAndLabel {
    this.also {
        val scheduleId = it.id
        val scheduleBean = ScheduleBean()
        scheduleBean.id = it.id
        scheduleBean.name = it.name
        scheduleBean.type = it.type
        scheduleBean.isTop = it.isTop
        scheduleBean.remark = it.remark
        scheduleBean.filePath = it.filePath
        scheduleBean.isRepeat = it.isRepeat
        scheduleBean.status = it.status
        scheduleBean.rrule = JSON.toJSONString(it.rrule)
        scheduleBean.remindType = it.remindType
        scheduleBean.remindTypeInfo = it.remindTypeInfo
        scheduleBean.startTime = it.startTime
        scheduleBean.endTime = it.endTime
        scheduleBean.iconImg = it.iconImg
        scheduleBean.isAllDay = it.isAllDay
        scheduleBean.siteId = it.siteId
        scheduleBean.siteName = it.siteName
        scheduleBean.updateType = it.updateType
        scheduleBean.updateDate = it.updateDate
        scheduleBean.dayOfMonth = it.dayOfMonth
        scheduleBean.promoterName = it.promoterName
        val participantList = mutableListOf<ParticipantList>()
        it.participant.forEach {
            val participant = ParticipantList()
            participant.id = it.id
            participant.userId = it.id
            participant.scheduleId = scheduleId
            participant.type = it.type
            //学生和教职工取值不一
            participant.userName = it.name
            participant.scheduleCreatorId = scheduleId
            participantList.add(participant)
        }
        val labelList = mutableListOf<LabelList>()
        it.label.forEach {
            val label = LabelList()
            label.id = it.id
            label.labelName = it.labelName
            label.colorValue = it.colorValue
            label.scheduleCreatorId = scheduleId
            labelList.add(label)
        }
        return ScheduleWithParticipantAndLabel(scheduleBean,participantList, labelList)
    }
}

/**
 * @param content json字符串
 * @return  如果转换失败返回null,
 */
fun jsonToMap(content: String): MutableMap<String, Any?> {
    var content = content
    content = content.trim { it <= ' ' }
    var result: MutableMap<String, Any?> = mutableMapOf()
    try {
        if (content[0] == '[') {
            val jsonArray = JSONArray(content)
            for (i in 0 until jsonArray.length()) {
                val value: Any = jsonArray.get(i)
                if (value is JSONArray || value is JSONObject) {
                    result.put(
                        i.toString() + "",
                        jsonToMap(value.toString().trim { it <= ' ' })
                    )
                } else {
                    result.put(i.toString() + "", jsonArray.getString(i))
                }
            }
        } else if (content[0] == '{') {
            val jsonObject: org.json.JSONObject = org.json.JSONObject(content)
            val iterator: Iterator<String> = jsonObject.keys()
            while (iterator.hasNext()) {
                val key = iterator.next()
                val value = jsonObject[key]
                if (value is JSONArray || value is JSONObject) {
                    result.put(key, jsonToMap(value.toString().trim { it <= ' ' }))
                } else {
                    result.put(key, value.toString().trim { it <= ' ' })
                }
            }
        } else {
            Log.e("异常", "json2Map: 字符串格式错误")
        }
    } catch (e: JSONException) {
        Log.e("异常", "json2Map: ", e)
    }
    return result
}

