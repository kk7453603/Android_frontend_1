package com.example.android_dz_1.model

import com.google.gson.annotations.SerializedName

data class ResponseModel<T>(
    @SerializedName("error")
    val error: String?,
    @SerializedName("content")
    val content: T?
)
