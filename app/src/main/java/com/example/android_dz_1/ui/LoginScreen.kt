package com.example.android_dz_1.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.android_dz_1.model.UserDTO
import com.example.android_dz_1.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: (String, String) -> Unit,
    authViewModel: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Вход", style = MaterialTheme.typography.h4)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Пароль") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        if (errorMessage != null) {
            Text(errorMessage!!, color = MaterialTheme.colors.error)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                isLoading = true
                val userDTO = UserDTO(
                    email = email,
                    password = password,
                    guid = null,
                    username = null
                )
                authViewModel.loginUser(userDTO).observeForever { result ->
                    isLoading = false
                    result.onSuccess { token ->
                        if (token != null) {
                            val guid = userDTO.guid ?: ""
                            onLoginSuccess(token, guid)
                        } else {
                            errorMessage = "Неверный ответ сервера"
                        }
                    }.onFailure { exception ->
                        errorMessage = exception.message
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = MaterialTheme.colors.onPrimary)
            } else {
                Text("Войти")
            }
        }
    }
}
