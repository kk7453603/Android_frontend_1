package com.example.android_dz_1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.android_dz_1.model.Chatroom
import com.example.android_dz_1.model.Message
import com.example.android_dz_1.network.ApiClient
import com.example.android_dz_1.network.ChatService
import com.example.android_dz_1.util.Result
import kotlinx.coroutines.Dispatchers

class ChatViewModel(private val token: String) : ViewModel() {

    private val chatService = ApiClient.getClient(token).create(ChatService::class.java)

    fun getChatrooms() = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val response = chatService.getChatrooms()
            if (response.isSuccessful) {
                val chatrooms = response.body()?.content
                emit(Result.Success(chatrooms))
            } else {
                emit(Result.Failure(Exception("Ошибка загрузки чатов")))
            }
        } catch (e: Exception) {
            emit(Result.Failure(e))
        }
    }

    fun fetchMessages(chatroomId: String) = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val response = chatService.fetchMessages(chatroomId)
            if (response.isSuccessful) {
                val messages = response.body()?.content
                emit(Result.Success(messages))
            } else {
                emit(Result.Failure(Exception("Ошибка загрузки сообщений")))
            }
        } catch (e: Exception) {
            emit(Result.Failure(e))
        }
    }
}
