package com.example.xiaokeer_app.data.model

data class Todo(
    val id: Long,
    val todo: String,
    val source: String,
    val appVisible: String,
    val createTime: String,
    val updateTime: String
) {
    companion object {
        const val SOURCE_MANUAL = "手动录入"
        const val SOURCE_SCHEDULED = "定时录入"
        const val SOURCE_EVENT = "事件触发"

        const val VISIBLE = "可见"
        const val INVISIBLE = "不可见"
    }
}
