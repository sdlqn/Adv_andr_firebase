package com.example.adv_andr_firebase

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions

import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.adv_andr_firebase.ui.theme.Green40
import com.example.adv_andr_firebase.ui.theme.Purple40
import kotlinx.coroutines.tasks.await



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginC(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showErr by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf("") }

    val pswColor = if (password.isNotBlank()) { Green40 } else { Purple40 }
    val emailColor = if (email.contains("@") && email.isNotBlank()) {
        Green40
    } else {
        Purple40
    }

    TopAppBar(
        title = { Text("Login") }
    )


    if (showErr) {
        Snackbar(
            modifier = Modifier.padding(16.dp),
            action = {
                TextButton(onClick = { showErr = false }) {
                    Text(stringResource(R.string.ok))
                }
            }
        ) {
            Text(errorMsg)
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            placeholder = { Text(stringResource(R.string.Email_placeholder)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = emailColor,
            )

        )

        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            placeholder = { Text(stringResource(R.string.Password_placeholder)) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = pswColor,
                )

        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (email.isBlank() || password.isBlank()) {
                    errorMsg = "Please enter email and password"
                    showErr = true
                } else {
                    FirebaseUtils.firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener {
                            navController.navigate("profile")
                        }
                        .addOnFailureListener {
                            errorMsg = "Please double check your email and password"
                            showErr = true

                        }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = { navController.navigate("signup") }) {
            Text("Sign Up")
        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpC(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showErr by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf("") }


    val pswColor = if (password.length >= 8) { Green40} else { Purple40 }
    val emailColor = if (email.contains("@") && email.isNotBlank()) {
        Green40
    } else {
        Purple40
    }



    TopAppBar(
        title = { Text("SignUp") },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }
    )

    if (showErr) {
        Snackbar(
            modifier = Modifier.padding(16.dp),
            action = {
                TextButton(onClick = { showErr = false }) {
                    Text(stringResource(R.string.ok))
                }
            }
        ) {
            Text(errorMsg)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        TextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            placeholder = { Text(stringResource(R.string.Email_placeholder)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = emailColor,
            )

        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            placeholder = { Text(stringResource(R.string.Password_placeholder)) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = pswColor,
            )
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (email.isBlank() || password.isBlank()) {
                        errorMsg = "Please enter email and password"
                        showErr = true
                } else {
                    FirebaseUtils.firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener {
                            navController.navigate("profile")
                        }
                        .addOnFailureListener {
                            errorMsg = "Sign Up failed. Please try again"
                            showErr = true
                        }
                }

            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.Sign_up))
        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileC(navController: NavController) {
    val currentUser = FirebaseUtils.firebaseAuth.currentUser
    var userData by remember { mutableStateOf(mapOf<String, Any>()) }


    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            val document =
                FirebaseUtils.firestore.collection("users").document(currentUser.uid).get().await()
            if (document.exists()) {
                userData = document.data!!
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(
            title = { Text(stringResource(R.string.Profile)) },
            actions = {
                IconButton(onClick = {
                    FirebaseUtils.firebaseAuth.signOut()
                    navController.navigate("login")
                }) {
                    Icon(
                        imageVector = Icons.Filled.ExitToApp,
                        contentDescription = "Logout"
                    )
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Email: ${currentUser?.email ?: "-"}")
            Spacer(modifier = Modifier.height(8.dp))
            Text("User ID: ${currentUser?.uid ?: "-"}")
        }
    }
}