package com.example.android_dz_1.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import com.example.android_dz_1.network.WebSocketHelper
import okhttp3.WebSocketListener
import okhttp3.Response
import okhttp3.WebSocket
import okio.ByteString

@Composable
fun MessageScreen(
    authToken: String,
    chatroomId: String
) {
    var messages by remember { mutableStateOf(listOf<String>()) }
    var inputText by remember { mutableStateOf("") }
    val webSocketHelper = remember { WebSocketHelper(guid = "", cid = chatroomId) }

    LaunchedEffect(Unit) {
        webSocketHelper.connect(object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                messages = messages + text
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                // Обработка бинарных сообщений (если нужно)
            }
        })
    }

    DisposableEffect(Unit) {
        onDispose {
            webSocketHelper.close()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(messages) { message ->
                Text(message)
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
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = {
                if (inputText.isNotBlank()) {
                    webSocketHelper.sendMessage(inputText)
                    inputText = ""
                }
            }) {
                Icon(Icons.Default.Send, contentDescription = "Отправить")
            }
        }
    }
}
