package com.example.android_dz_1.model

import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("message_id")
    val messageId: String,
    @SerializedName("chatroom_id")
    val chatroomId: String,
    @SerializedName("sender_guid")
    val senderGuid: String,
    @SerializedName("sender_name")
    val senderName: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("image")
    val image: Boolean
)