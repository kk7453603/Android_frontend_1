package com.example.android_dz_1.ui

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.android_dz_1.model.Chatroom
import com.example.android_dz_1.model.Message
import com.example.android_dz_1.network.WebSocketManager
import com.example.android_dz_1.util.Result
import com.example.android_dz_1.viewmodel.ChatViewModel
import com.example.android_dz_1.viewmodel.ChatViewModelFactory
import androidx.compose.runtime.livedata.observeAsState
import kotlinx.coroutines.launch

@Composable
fun ChatScreen() {
    val sharedPreferences = LocalContext.current.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val token = sharedPreferences.getString("token", null)
    val guid = sharedPreferences.getString("guid", null)
    val chatViewModel: ChatViewModel = viewModel(factory = ChatViewModelFactory(token ?: ""))

    var selectedChatroom by remember { mutableStateOf<Chatroom?>(null) }
    var messages by remember { mutableStateOf(listOf<Message>()) }
    var messageText by remember { mutableStateOf("") }

    val webSocketManager = remember { mutableStateOf<WebSocketManager?>(null) }
    val coroutineScope = rememberCoroutineScope()

    val chatroomsResult by chatViewModel.getChatrooms().observeAsState(Result.Loading)

    if (selectedChatroom == null) {
        // Отображение списка чатов
        Column {
            Text(text = "Доступные чаты", style = MaterialTheme.typography.h5)
            when (chatroomsResult) {
                is Result.Success<*> -> {
                    val rooms = (chatroomsResult as Result.Success<List<Chatroom>>).data ?: emptyList()
                    LazyColumn {
                        items(rooms) { chatroom ->
                            Text(
                                text = chatroom.name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedChatroom = chatroom
                                        messages = emptyList()

                                        // Загрузка сообщений для выбранного чата
                                        coroutineScope.launch {
                                            chatViewModel.fetchMessages(selectedChatroom!!.chatroomId).observeForever { result ->
                                                if (result is Result.Success) {
                                                    messages = result.data ?: emptyList()
                                                }
                                            }
                                        }

                                        // Инициализация WebSocket
                                        webSocketManager.value = WebSocketManager(guid ?: "", selectedChatroom!!.chatroomId) { message ->
                                            messages = messages + listOf(message)
                                        }
                                        webSocketManager.value?.connect()
                                    }
                                    .padding(8.dp)
                            )
                        }
                    }
                }
                is Result.Failure -> {
                    Text(text = "Ошибка загрузки чатов")
                }
                is Result.Loading -> {
                    CircularProgressIndicator()
                }
            }
        }
    } else {
        // Отображение выбранного чата
        DisposableEffect(Unit) {
            onDispose {
                webSocketManager.value?.close()
            }
        }

        Column(modifier = Modifier.fillMaxSize()) {
            Text(text = "Чат: ${selectedChatroom?.name}", style = MaterialTheme.typography.h5)
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(messages) { message ->
                    Text(
                        text = "${message.senderName}: ${message.content}",
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
            Row(modifier = Modifier.padding(8.dp)) {
                TextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    modifier = Modifier.weight(1f),
                    label = { Text("Сообщение") }
                )
                Button(onClick = {
                    webSocketManager.value?.sendMessage(messageText)
                    messageText = ""
                }) {
                    Text("Отправить")
                }
            }
            Button(onClick = {
                // Вернуться к списку чатов
                selectedChatroom = null
                webSocketManager.value?.close()
                webSocketManager.value = null
            }) {
                Text("Назад к чатам")
            }
        }
    }
}
