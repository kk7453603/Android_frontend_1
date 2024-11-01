package com.example.android_dz_1.model

import com.google.gson.annotations.SerializedName

data class UserDTO(
    @SerializedName("guid")
    val guid: String? = null,
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("email")
    val email: String? = null
)
