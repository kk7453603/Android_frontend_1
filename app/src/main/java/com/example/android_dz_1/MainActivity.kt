package com.example.android_dz_1

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.*
import com.example.android_dz_1.ui.LoginScreen
import com.example.android_dz_1.ui.ChatScreen
import com.example.android_dz_1.ui.MessageScreen
import com.example.android_dz_1.viewmodel.ChatViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatApp()
        }
    }
}

@Composable
fun ChatApp() {
    val navController = rememberNavController()
    val context = LocalContext.current

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(onLoginSuccess = { token, guid ->
                val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                Log.d("MainActivity", "Сохранён токен: $token")
                with(sharedPreferences.edit()) {
                    putString("token", token)
                    putString("guid", guid)
                    apply()
                }
                navController.navigate("chat") {
                    popUpTo("login") { inclusive = true }
                }
            })
        }
        composable("chat") {
            val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val token = sharedPreferences.getString("token", null) ?: ""
            val chatViewModelFactory = ChatViewModelFactory(token)
            ChatScreen(
                chatViewModelFactory = chatViewModelFactory,
                onChatSelected = { chatroom ->
                    navController.navigate("message/${chatroom.chatroomId}")
                }
            )
        }
        composable("message/{chatroomId}") { backStackEntry ->
            val chatroomId = backStackEntry.arguments?.getString("chatroomId") ?: return@composable
            val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val token = sharedPreferences.getString("token", null) ?: ""
            MessageScreen(
                authToken = token,
                chatroomId = chatroomId
            )
        }
    }
}
