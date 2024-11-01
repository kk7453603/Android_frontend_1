package com.example.android_dz_1.model

import com.google.gson.annotations.SerializedName

data class UpdatePasswordDTO(
    @SerializedName("guid")
    val guid: String?,
    @SerializedName("old_password")
    val oldPassword: String?,
    @SerializedName("password")
    val password: String?
)
