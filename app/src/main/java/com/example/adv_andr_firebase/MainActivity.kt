package com.example.adv_andr_firebase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.adv_andr_firebase.LoginC
import com.example.adv_andr_firebase.ProfileC
import com.example.adv_andr_firebase.SignUpC

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "login") {
                composable("login") { LoginC(navController) }
                composable("SignUp") { SignUpC(navController) }
                composable("profile") { ProfileC(navController) }
            }
        }
    }
}
