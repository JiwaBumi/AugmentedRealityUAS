package com.uas.augmentedrealityproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import com.uas.augmentedrealityproject.ui.theme.AugmentedRealityProjectTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.compose.*
import androidx.navigation.compose.rememberNavController
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.uas.augmentedrealityproject.ui.theme.AugmentedRealityProjectTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainActivity() {
    val auth = FirebaseAuth.getInstance()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isRegistering by remember { mutableStateOf(false) }

    fun handleRegister() {
        if (password != confirmPassword) {
            errorMessage = "Passwords do not match!"
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Registration successful
                    email = ""
                    password = ""
                    confirmPassword = ""
                    errorMessage = ""
                    isRegistering = false
                    // Optionally navigate to Homepage
                } else {
                    // Registration failed
                    errorMessage = "Registration failed: ${task.exception?.message}"
                }
            }
    }

    fun handleLogin() {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Login successful, navigate to Homepage
                } else {
                    errorMessage = "Login failed: ${task.exception?.message}"
                }
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Login / Register") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (isRegistering) {
                TextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Button(
                onClick = {
                    if (isRegistering) {
                        handleRegister()
                    } else {
                        handleLogin()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isRegistering) "Register" else "Login")
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = { isRegistering = !isRegistering }
            ) {
                Text(if (isRegistering) "Already have an account? Login" else "Don't have an account? Register")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AugmentedRealityProjectTheme {
        MainActivity()
    }
}
