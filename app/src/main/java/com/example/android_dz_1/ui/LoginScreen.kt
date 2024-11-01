package com.example.android_dz_1.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.android_dz_1.model.UserDTO
import com.example.android_dz_1.util.Result
import com.example.android_dz_1.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: (String) -> Unit,
    onRegisterClick: () -> Unit
) {
    val authViewModel: AuthViewModel = viewModel()
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val loginResult by authViewModel.loginResult.observeAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Пароль") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                authViewModel.loginUser(UserDTO(email = email, password = password))
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Войти")
        }
        TextButton(
            onClick = onRegisterClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Регистрация")
        }

        loginResult?.let { result ->
            when (result) {
                is Result.Success -> {
                    val token = result.data
                    onLoginSuccess(token)
                }
                is Result.Failure -> {
                    val errorMessage = result.exception.message ?: "Неизвестная ошибка"
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }
                is Result.Loading -> {
                    // Отображение индикатора загрузки (если нужно)
                }
            }
        }
    }
}
