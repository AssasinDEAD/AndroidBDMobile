package com.example.mobilebd

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mobilebd.ui.theme.MobilebdTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userPreferences = UserPreferences(this)

        setContent {
            MobilebdTheme {
                val navController = rememberNavController()
                val isRegistered = remember { mutableStateOf(userPreferences.isUserRegistered()) }

                NavHost(
                    navController = navController,
                    startDestination = if (isRegistered.value) "login" else "registration"
                ) {
                    composable("login") {
                        LoginScreen(
                            onLoginSuccess = {
                                navController.navigate("greeting") {
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                            onRegisterClick = {
                                navController.navigate("registration")
                            }
                        )
                    }
                    composable("registration") {
                        RegistrationScreen(
                            onRegister = { username, email, password ->
                                userPreferences.saveUser(username, email, password)
                                isRegistered.value = true
                                navController.navigate("login") {
                                    popUpTo("registration") { inclusive = true }
                                }
                            },
                            onLoginClick = {
                                navController.navigate("login")
                            }
                        )
                    }
                    composable("greeting") {
                        val username = userPreferences.getUsername() ?: "User"
                        GreetingScreen(
                            name = username,
                            onContinue = {
                                navController.navigate("movies") {
                                    popUpTo("greeting") { inclusive = true }
                                }
                            }
                        )
                    }
                    composable("movies") {
                        MovieScreen()
                    }
                }
            }
        }
    }
}
