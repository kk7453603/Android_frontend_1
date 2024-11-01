package com.example.android_dz_1.model

import com.google.gson.annotations.SerializedName

data class UserDTO(
    @SerializedName("email")
    val email: String?,
    @SerializedName("password")
    val password: String?,
    @SerializedName("guid")
    val guid: String?,
    @SerializedName("username")
    val username: String?
)
