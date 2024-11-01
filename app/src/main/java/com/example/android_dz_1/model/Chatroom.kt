package com.example.android_dz_1.model

import com.google.gson.annotations.SerializedName

data class Chatroom(
    @SerializedName("chatroomId")
    val chatroomId: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("guid")
    val guid: String?
)
