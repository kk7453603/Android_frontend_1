package com.example.android_dz_1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.android_dz_1.ui.theme.Android_DZ_1Theme
import com.example.android_dz_1.ui.LoginScreen
import com.example.android_dz_1.ui.RegisterScreen
import com.example.android_dz_1.ui.ChatScreen
import androidx.navigation.compose.composable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Android_DZ_1Theme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = "login") {
                    composable("login") {
                        LoginScreen(
                            onLoginSuccess = { token ->
                                // Сохраните токен и перейдите к экрану чата
                                navController.navigate("chat")
                            },
                            onRegisterClick = {
                                navController.navigate("register")
                            }
                        )
                    }
                    composable("register") {
                        RegisterScreen(
                            onRegisterSuccess = { guid ->
                                // Сохраните GUID и перейдите к экрану входа
                                navController.navigate("login")
                            },
                            onBackToLoginClick = {
                                navController.navigate("login")
                            }
                        )
                    }
                    composable("chat") {
                        ChatScreen()
                    }
                }
            }
        }
    }
}
