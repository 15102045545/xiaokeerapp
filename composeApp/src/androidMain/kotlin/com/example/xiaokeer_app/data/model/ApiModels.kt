package com.example.xiaokeer_app.data.model

data class AddTodoRequest(
    val todo: String,
    val source: String,
    val appVisible: String
)

data class AppInvisibleRequest(
    val ids: List<Long>
)

class QueryAppVisibleRequest
