package com.example.android_dz_1.network

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class WebSocketHelper(private val guid: String, private val cid: String) {

    private val client = OkHttpClient()
    private lateinit var webSocket: WebSocket

    fun connect(listener: WebSocketListener) {
        val request = Request.Builder()
            .url("ws://localhost:3000/$guid/$cid")
            .build()

        webSocket = client.newWebSocket(request, listener)
    }

    fun sendMessage(message: String) {
        webSocket.send(message)
    }

    fun close() {
        webSocket.close(1000, null)
    }
}
