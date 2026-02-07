package com.example.xiaokeer_app.data.network

import com.example.xiaokeer_app.data.local.SettingsStorage
import com.example.xiaokeer_app.data.model.AddTodoRequest
import com.example.xiaokeer_app.data.model.AppInvisibleRequest
import com.example.xiaokeer_app.data.model.Todo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

class TodoApiService(private val settingsStorage: SettingsStorage) {

    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .build()

    private val gson = Gson()
    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    private fun buildRequest(path: String, body: String): Request {
        val url = "${settingsStorage.serverUrl}$path"
        val headerKey = settingsStorage.headerKey
        val headerValue = settingsStorage.headerValue

        return Request.Builder()
            .url(url)
            .post(body.toRequestBody(jsonMediaType))
            .apply {
                if (headerKey.isNotBlank() && headerValue.isNotBlank()) {
                    addHeader(headerKey, headerValue)
                }
            }
            .build()
    }

    suspend fun queryAppVisibleTodos(): Result<List<Todo>> = withContext(Dispatchers.IO) {
        try {
            val request = buildRequest("/api/todo/queryAppVisible", "{}")
            val response = client.newCall(request).execute()

            if (response.isSuccessful) {
                val body = response.body?.string() ?: "[]"
                val type = object : TypeToken<List<Todo>>() {}.type
                val todos: List<Todo> = gson.fromJson(body, type)
                Result.success(todos)
            } else {
                Result.failure(Exception("请求失败: ${response.code}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addTodo(todoText: String): Result<Todo> = withContext(Dispatchers.IO) {
        try {
            val addRequest = AddTodoRequest(
                todo = todoText,
                source = Todo.SOURCE_MANUAL,
                appVisible = Todo.VISIBLE
            )
            val body = gson.toJson(addRequest)
            val request = buildRequest("/api/todo/add", body)
            val response = client.newCall(request).execute()

            if (response.isSuccessful) {
                val responseBody = response.body?.string() ?: "{}"
                val todo: Todo = gson.fromJson(responseBody, Todo::class.java)
                Result.success(todo)
            } else {
                Result.failure(Exception("添加失败: ${response.code}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun setAppInvisible(ids: List<Long>): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val invisibleRequest = AppInvisibleRequest(ids = ids)
            val body = gson.toJson(invisibleRequest)
            val request = buildRequest("/api/todo/appInvisible", body)
            val response = client.newCall(request).execute()

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("操作失败: ${response.code}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
