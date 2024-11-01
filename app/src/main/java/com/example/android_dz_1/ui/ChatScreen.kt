package com.example.android_dz_1.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.android_dz_1.model.Chatroom
import com.example.android_dz_1.model.ChatroomDTO
import com.example.android_dz_1.viewmodel.ChatViewModel
import com.example.android_dz_1.viewmodel.ChatViewModelFactory
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Send
import androidx.compose.foundation.lazy.items

@Composable
fun ChatScreen(
    chatViewModelFactory: ChatViewModelFactory,
    onChatSelected: (Chatroom) -> Unit
) {
    val chatViewModel: ChatViewModel = viewModel(
        factory = chatViewModelFactory
    )
    val chatroomsResult by chatViewModel.getChatrooms().observeAsState(initial = null)

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Чаты") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                // Логика создания нового чата
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Добавить чат")
            }
        }
    ) { innerPadding ->
        if (chatroomsResult == null) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        } else {
            val result = chatroomsResult!!
            if (result.isSuccess) {
                val chatrooms = result.getOrNull() ?: emptyList()
                LazyColumn(
                    contentPadding = innerPadding
                ) {
                    items(chatrooms) { chatroom ->
                        ChatroomItem(chatroom, onChatSelected)
                    }
                }
            } else {
                val exception = result.exceptionOrNull()
                Text(
                    text = "Ошибка: ${exception?.message}",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}


@Composable
fun ChatroomItem(chatroom: Chatroom, onChatSelected: (Chatroom) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onChatSelected(chatroom) }
            .padding(16.dp)
    ) {
        Text(chatroom.name ?: "Без названия", style = MaterialTheme.typography.h6)
        Text("ID: ${chatroom.chatroomId}", style = MaterialTheme.typography.body2)
    }
}
