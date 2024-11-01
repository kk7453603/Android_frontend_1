package com.example.android_dz_1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.android_dz_1.model.Chatroom
import com.example.android_dz_1.model.ChatroomDTO
import com.example.android_dz_1.network.ApiClient
import com.example.android_dz_1.network.ChatroomService
import kotlinx.coroutines.Dispatchers

class ChatViewModel(private val authToken: String) : ViewModel() {

    private val chatroomService = ApiClient.getClient(authToken).create(ChatroomService::class.java)

    fun getChatrooms() = liveData(Dispatchers.IO) {
        try {
            val response = chatroomService.getChatrooms()
            if (response.isSuccessful) {
                val chatrooms = response.body()?.content
                emit(Result.success(chatrooms))
            } else {
                emit(Result.failure(Exception("Ошибка получения чатов")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    fun createChatroom(chatroomDTO: ChatroomDTO) = liveData(Dispatchers.IO) {
        try {
            val response = chatroomService.createChatroom(chatroomDTO)
            if (response.isSuccessful) {
                emit(Result.success(response.body()))
            } else {
                emit(Result.failure(Exception("Ошибка создания чата")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}
