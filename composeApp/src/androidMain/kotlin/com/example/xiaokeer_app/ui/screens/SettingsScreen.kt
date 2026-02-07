package com.example.xiaokeer_app.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.xiaokeer_app.data.local.SettingsStorage

@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val settingsStorage = remember { SettingsStorage(context) }

    var serverUrl by remember { mutableStateOf(settingsStorage.serverUrl) }
    var headerKey by remember { mutableStateOf(settingsStorage.headerKey) }
    var headerValue by remember { mutableStateOf(settingsStorage.headerValue) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "服务器配置",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = serverUrl,
            onValueChange = {
                serverUrl = it
                settingsStorage.serverUrl = it
            },
            label = { Text("服务器地址") },
            placeholder = { Text("例如: http://127.0.0.1:8089") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "请求头配置",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = headerKey,
            onValueChange = {
                headerKey = it
                settingsStorage.headerKey = it
            },
            label = { Text("请求头Key") },
            placeholder = { Text("例如: token") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = headerValue,
            onValueChange = {
                headerValue = it
                settingsStorage.headerValue = it
            },
            label = { Text("请求头Value") },
            placeholder = { Text("例如: 1234567890") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "提示：配置会自动保存",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
