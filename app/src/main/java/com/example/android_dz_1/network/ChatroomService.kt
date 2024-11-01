package com.example.android_dz_1.network

import com.example.android_dz_1.model.*
import retrofit2.Response
import retrofit2.http.*

interface ChatroomService {

    @GET("/chatroom/get")
    suspend fun getChatrooms(): Response<ResponseModel<List<Chatroom>>>

    @POST("/chatroom/create")
    suspend fun createChatroom(
        @Body chatroom: ChatroomDTO
    ): Response<ResponseModel<Unit>>
}
