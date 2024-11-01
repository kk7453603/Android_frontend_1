package com.example.android_dz_1.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.auth0.android.jwt.JWT
import com.example.android_dz_1.model.UserDTO
import com.example.android_dz_1.network.ApiClient
import com.example.android_dz_1.network.UserService
import com.example.android_dz_1.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val userService = ApiClient.getClient(null).create(UserService::class.java)

    val registerResult = MutableLiveData<Result<String>>()
    val loginResult = MutableLiveData<Result<String>>()

    fun registerUser(user: UserDTO) {
        registerResult.value = Result.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = userService.createUser(user)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    val guid = responseBody?.content as? String
                    if (!guid.isNullOrEmpty()) {
                        registerResult.postValue(Result.Success(guid))
                    } else {
                        registerResult.postValue(Result.Failure(Exception("GUID не получен")))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    registerResult.postValue(Result.Failure(Exception("Ошибка регистрации: $errorBody")))
                }
            } catch (e: Exception) {
                registerResult.postValue(Result.Failure(e))
            }
        }
    }

    fun loginUser(user: UserDTO) {
        loginResult.value = Result.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = userService.loginUser(user)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    val token = responseBody?.content as? String
                    val guid = extractGuidFromToken(token)
                    if (!token.isNullOrEmpty() && !guid.isNullOrEmpty()) {
                        loginResult.postValue(Result.Success(token))
                    } else {
                        loginResult.postValue(Result.Failure(Exception("Токен или GUID не получены")))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    loginResult.postValue(Result.Failure(Exception("Ошибка авторизации: $errorBody")))
                }
            } catch (e: Exception) {
                loginResult.postValue(Result.Failure(e))
            }
        }
    }

    private fun extractGuidFromToken(token: String?): String? {
        return token?.let {
            try {
                val jwt = JWT(it)
                jwt.getClaim("guid").asString()
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Ошибка при извлечении GUID из токена", e)
                null
            }
        }
    }
}
