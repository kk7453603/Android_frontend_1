package com.example.android_dz_1.network

import com.example.android_dz_1.model.Chatroom
import com.example.android_dz_1.model.Message
import com.example.android_dz_1.model.ResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ChatService {

    @GET("/message/{cid}")
    suspend fun fetchMessages(
        @Path("cid") chatroomId: String
    ): Response<ResponseModel<List<Message>>>

    @POST("/chatroom/create")
    suspend fun createChatroom(@Body chatroom: Chatroom): Response<ResponseModel<String>>

    @GET("/chatroom/get")
    suspend fun getChatrooms(): Response<ResponseModel<List<Chatroom>>>
}
