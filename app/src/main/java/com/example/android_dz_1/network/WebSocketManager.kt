package com.example.android_dz_1.network

import com.example.android_dz_1.model.Message
import com.google.gson.Gson
import okhttp3.*

class WebSocketManager(
    private val guid: String,
    private val cid: String,
    private val onMessageReceived: (Message) -> Unit
) {
    private var webSocket: WebSocket? = null
    private val client = OkHttpClient()
    private val gson = Gson()

    fun connect() {
        val request = Request.Builder()
            .url("ws://yourserver.com/ws?guid=$guid&cid=$cid")
            .build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                val message = parseMessage(text)
                onMessageReceived(message)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                // Обработка ошибки
            }
        })
    }

    fun sendMessage(message: String) {
        val messageJson = gson.toJson(Message(
            senderName = "", content = message,
            messageId = TODO(),
            chatroomId = TODO(),
            senderGuid = TODO(),
            image = TODO()
        ))
        webSocket?.send(messageJson)
    }

    fun close() {
        webSocket?.close(1000, null)
    }

    private fun parseMessage(text: String): Message {
        return gson.fromJson(text, Message::class.java)
    }
}
