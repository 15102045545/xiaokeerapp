package com.example.xiaokeer_app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.xiaokeer_app.ui.navigation.NavigationItem
import com.example.xiaokeer_app.ui.screens.SettingsScreen
import com.example.xiaokeer_app.ui.screens.TodoScreen
import com.example.xiaokeer_app.ui.theme.XiaokeerTheme

@Composable
@Preview
fun App() {
    XiaokeerTheme {
        var selectedItem by remember { mutableStateOf(NavigationItem.TODO) }

        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .safeContentPadding()
                .fillMaxSize()
        ) {
            // 上层：标题栏
            TopBar(
                title = selectedItem.title,
                modifier = Modifier.fillMaxWidth()
            )

            // 中层：内容区域
            ContentArea(
                selectedItem = selectedItem,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            // 下层：底部导航栏
            BottomNavigationBar(
                selectedItem = selectedItem,
                onItemSelected = { selectedItem = it },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun TopBar(
    title: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(56.dp)
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun ContentArea(
    selectedItem: NavigationItem,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (selectedItem) {
            NavigationItem.TODO -> TodoScreen()
            NavigationItem.SETTINGS -> SettingsScreen()
        }
    }
}

@Composable
private fun BottomNavigationBar(
    selectedItem: NavigationItem,
    onItemSelected: (NavigationItem) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) {
        NavigationBarItem(
            selected = selectedItem == NavigationItem.TODO,
            onClick = { onItemSelected(NavigationItem.TODO) },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = NavigationItem.TODO.title
                )
            },
            label = { Text(NavigationItem.TODO.title) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            )
        )

        NavigationBarItem(
            selected = selectedItem == NavigationItem.SETTINGS,
            onClick = { onItemSelected(NavigationItem.SETTINGS) },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = NavigationItem.SETTINGS.title
                )
            },
            label = { Text(NavigationItem.SETTINGS.title) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            )
        )
    }
}
