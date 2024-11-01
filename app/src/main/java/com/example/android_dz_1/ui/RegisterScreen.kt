package com.example.android_dz_1.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.android_dz_1.model.UserDTO
import com.example.android_dz_1.util.Result
import com.example.android_dz_1.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(
    onRegisterSuccess: (String) -> Unit,
    onBackToLoginClick: () -> Unit
) {
    val authViewModel: AuthViewModel = viewModel()
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }

    val registerResult by authViewModel.registerResult.observeAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Имя пользователя") },
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
                authViewModel.registerUser(UserDTO(email = email, password = password, username = username))
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Зарегистрироваться")
        }
        TextButton(
            onClick = onBackToLoginClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Уже есть аккаунт? Войти")
        }

        registerResult?.let { result ->
            when (result) {
                is Result.Success<*> -> {
                    val guid = result.data
                    onRegisterSuccess(guid)
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
