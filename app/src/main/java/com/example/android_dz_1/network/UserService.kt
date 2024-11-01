package com.example.android_dz_1.network

import com.example.android_dz_1.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {

    @POST("/user/login")
    suspend fun loginUser(@Body user: UserDTO): Response<ResponseModel<String>>
}
