package com.example.android_dz_1.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android_dz_1.model.Message
import com.example.android_dz_1.network.WebSocketManager

@Composable
fun MessageScreen(
    guid: String,
    chatroomId: String
) {
    var messages by remember { mutableStateOf(listOf<Message>()) }
    var inputText by remember { mutableStateOf("") }
    val webSocketManager = remember {
        WebSocketManager(guid = guid, cid = chatroomId) { message ->
            messages = messages + message
        }
    }

    LaunchedEffect(Unit) {
        webSocketManager.connect()
    }

    DisposableEffect(Unit) {
        onDispose {
            webSocketManager.close()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(messages) { message ->
                Text("${message.senderName}: ${message.content}")
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            TextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier.weight(1f),
                label = { Text("Сообщение") }
            )
            IconButton(onClick = {
                if (inputText.isNotBlank()) {
                    webSocketManager.sendMessage(inputText)
                    inputText = ""
                }
            }) {
                Icon(Icons.Default.Send, contentDescription = "Отправить")
            }
        }
    }
}
