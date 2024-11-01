package com.example.android_dz_1.viewmodel

import android.util.Log
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
                val responseBody = response.body()
                val token = responseBody?.content
                Log.d("AuthViewModel", "Получен токен: $token")
                if (!token.isNullOrEmpty()) {
                    emit(Result.success(token))
                } else {
                    emit(Result.failure(Exception("Токен не получен")))
                }
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("AuthViewModel", "Ошибка авторизации: $errorBody")
                emit(Result.failure(Exception("Ошибка авторизации: ${response.code()}")))
            }
        } catch (e: Exception) {
            Log.e("AuthViewModel", "Исключение при авторизации", e)
            emit(Result.failure(e))
        }
    }
}
