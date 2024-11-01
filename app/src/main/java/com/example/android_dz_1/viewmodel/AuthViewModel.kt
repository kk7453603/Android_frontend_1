package com.example.android_dz_1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.android_dz_1.model.UserDTO
import com.example.android_dz_1.network.ApiClient
import com.example.android_dz_1.network.UserService
import kotlinx.coroutines.Dispatchers

class AuthViewModel : ViewModel() {

    private val userService = ApiClient.getClient(null).create(UserService::class.java)

    fun loginUser(user: UserDTO) = liveData(Dispatchers.IO) {
        try {
            val response = userService.loginUser(user)
            if (response.isSuccessful) {
                val token = response.body()?.content
                emit(Result.success(token))
            } else {
                emit(Result.failure(Exception("Ошибка авторизации")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}
