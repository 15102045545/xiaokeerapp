package com.example.xiaokeer_app.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.xiaokeer_app.data.local.SettingsStorage
import com.example.xiaokeer_app.data.model.Todo
import com.example.xiaokeer_app.data.network.TodoApiService
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@Composable
fun TodoScreen() {
    val context = LocalContext.current
    val settingsStorage = remember { SettingsStorage(context) }
    val apiService = remember { TodoApiService(settingsStorage) }
    val coroutineScope = rememberCoroutineScope()

    var todos by remember { mutableStateOf<List<Todo>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var inputText by remember { mutableStateOf("") }
    var isSending by remember { mutableStateOf(false) }

    val selectedIds = remember { mutableStateListOf<Long>() }
    val listState = rememberLazyListState()

    fun loadTodos() {
        coroutineScope.launch {
            val result = apiService.queryAppVisibleTodos()
            result.onSuccess {
                todos = it
                errorMessage = null
            }.onFailure {
                errorMessage = "无法连接到服务器"
            }
            isLoading = false
        }
    }

    fun addTodo() {
        if (inputText.isBlank() || isSending) return
        isSending = true
        coroutineScope.launch {
            val result = apiService.addTodo(inputText.trim())
            result.onSuccess {
                inputText = ""
                loadTodos()
                Toast.makeText(context, "添加成功", Toast.LENGTH_SHORT).show()
            }.onFailure {
                Toast.makeText(context, "添加失败: ${it.message}", Toast.LENGTH_SHORT).show()
            }
            isSending = false
        }
    }

    fun copySelectedTodos() {
        val selectedTodos = todos.filter { it.id in selectedIds }
        if (selectedTodos.isEmpty()) return

        val textToCopy = selectedTodos.joinToString("\n-----------------\n") { it.todo }
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("todos", textToCopy)
        clipboard.setPrimaryClip(clip)

        Toast.makeText(context, "已复制 ${selectedTodos.size} 条记录", Toast.LENGTH_SHORT).show()
        selectedIds.clear()
    }

    fun completeSelectedTodos() {
        if (selectedIds.isEmpty()) return
        coroutineScope.launch {
            val result = apiService.setAppInvisible(selectedIds.toList())
            result.onSuccess {
                Toast.makeText(context, "已完成 ${selectedIds.size} 条记录", Toast.LENGTH_SHORT).show()
                selectedIds.clear()
                loadTodos()
            }.onFailure {
                Toast.makeText(context, "操作失败: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    LaunchedEffect(Unit) {
        loadTodos()
    }

    LaunchedEffect(Unit) {
        while (isActive) {
            delay(3000)
            if (!isSending) {
                val result = apiService.queryAppVisibleTodos()
                result.onSuccess {
                    todos = it
                    errorMessage = null
                }.onFailure {
                    errorMessage = "无法连接到服务器"
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when {
                    isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    errorMessage != null -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = errorMessage!!,
                                color = MaterialTheme.colorScheme.error,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    todos.isEmpty() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "暂无待办事项",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    else -> {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            reverseLayout = false
                        ) {
                            item { Spacer(modifier = Modifier.height(8.dp)) }
                            items(todos, key = { it.id }) { todo ->
                                TodoMessageItem(
                                    todo = todo,
                                    isSelected = todo.id in selectedIds,
                                    onSelectionChange = { selected ->
                                        if (selected) {
                                            selectedIds.add(todo.id)
                                        } else {
                                            selectedIds.remove(todo.id)
                                        }
                                    }
                                )
                            }
                            item { Spacer(modifier = Modifier.height(if (selectedIds.isNotEmpty()) 72.dp else 8.dp)) }
                        }
                    }
                }
            }

            InputBar(
                text = inputText,
                onTextChange = { inputText = it },
                onSend = { addTodo() },
                isSending = isSending,
                modifier = Modifier.fillMaxWidth()
            )
        }

        AnimatedVisibility(
            visible = selectedIds.isNotEmpty(),
            enter = slideInVertically { it },
            exit = slideOutVertically { it },
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            ActionBar(
                selectedCount = selectedIds.size,
                onCopy = { copySelectedTodos() },
                onComplete = { completeSelectedTodos() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 72.dp)
            )
        }
    }
}

@Composable
private fun TodoMessageItem(
    todo: Todo,
    isSelected: Boolean,
    onSelectionChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.Top
    ) {
        Checkbox(
            checked = isSelected,
            onCheckedChange = onSelectionChange,
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.size(40.dp)
        )

        Spacer(modifier = Modifier.width(4.dp))

        Surface(
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 4.dp,
                bottomStart = 16.dp,
                bottomEnd = 16.dp
            ),
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.widthIn(max = 260.dp)
        ) {
            Text(
                text = todo.todo,
                modifier = Modifier.padding(12.dp),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        SourceAvatar(source = todo.source)
    }
}

@Composable
private fun SourceAvatar(source: String) {
    val (icon, backgroundColor) = when (source) {
        Todo.SOURCE_MANUAL -> Icons.Default.Edit to Color(0xFF4CAF50)
        Todo.SOURCE_SCHEDULED -> Icons.Default.AccessTime to Color(0xFF2196F3)
        Todo.SOURCE_EVENT -> Icons.Default.FlashOn to Color(0xFFFF9800)
        else -> Icons.Default.Edit to Color(0xFF9E9E9E)
    }

    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = source,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
private fun InputBar(
    text: String,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit,
    isSending: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background,
        shadowElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = onTextChange,
                placeholder = { Text("输入待办事项...") },
                modifier = Modifier.weight(1f),
                maxLines = 3,
                shape = RoundedCornerShape(24.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = onSend,
                enabled = text.isNotBlank() && !isSending,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        if (text.isNotBlank() && !isSending)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.surfaceVariant
                    )
            ) {
                if (isSending) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "发送",
                        tint = if (text.isNotBlank()) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun ActionBar(
    selectedCount: Int,
    onCopy: () -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.secondaryContainer,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "已选择 $selectedCount 项",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )

            Row {
                TextButton(onClick = onCopy) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("复制")
                }

                Spacer(modifier = Modifier.width(8.dp))

                TextButton(onClick = onComplete) {
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("完成")
                }
            }
        }
    }
}
