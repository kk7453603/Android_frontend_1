package com.example.android_dz_1.model

import com.google.gson.annotations.SerializedName

data class Chatroom(
    @SerializedName("chatroom_id")
    val chatroomId: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("guid")
    val ownerGuid: String,
    @SerializedName("is_private")
    val isPrivate: Boolean,
    @SerializedName("participants_limit")
    val participantsLimit: Int
)
