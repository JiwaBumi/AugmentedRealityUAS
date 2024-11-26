package com.uas.augmentedrealityproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import com.google.firebase.auth.FirebaseAuth
import com.uas.augmentedrealityproject.ui.theme.AugmentedRealityProjectTheme
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uas.augmentedrealityproject.products.BathubProduct
import com.uas.augmentedrealityproject.products.ChairProduct
import com.uas.augmentedrealityproject.products.DrawerProduct
import com.uas.augmentedrealityproject.products.StoolProduct
import com.uas.augmentedrealityproject.products.TVProduct
import com.uas.augmentedrealityproject.products.TableProduct
import com.uas.augmentedrealityproject.viewmodel.CartViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AugmentedRealityProjectTheme {
                val navController = rememberNavController()
                val cartViewModel: CartViewModel = viewModel()

                NavHost(
                    navController = navController,
                    startDestination = "login"
                ) {
                    composable("login") { LoginScreen(navController) }
                    composable("homepage") { Homepage(navController) }
                    composable("shoppingcart") { ShoppingCart(navController, cartViewModel) }
                    composable("table") { TableProduct(navController, cartViewModel) }
                    composable("chair") { ChairProduct(navController, cartViewModel) }
                    composable("bathub") { BathubProduct(navController, cartViewModel) }
                    composable("stool") { StoolProduct(navController, cartViewModel) }
                    composable("tv") { TVProduct(navController, cartViewModel) }
                    composable("drawer") { DrawerProduct(navController, cartViewModel) }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController) {
    val auth = FirebaseAuth.getInstance()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isRegistering by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    fun showSnackbar(message: String) {
        CoroutineScope(Dispatchers.Main).launch {
            snackbarHostState.showSnackbar(message)
        }
    }


    fun handleRegister() {
        if (password != confirmPassword) {
            errorMessage = "Passwords do not match!"
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    email = ""
                    password = ""
                    confirmPassword = ""
                    errorMessage = ""
                    isRegistering = false
                    showSnackbar("Registration Successful!")
                } else {
                    errorMessage = "Registration failed: ${task.exception?.message}"
                }
            }
    }

    fun handleLogin() {
        // Show the initial Snackbar message while logging in
        showSnackbar("Logging You In...")

        // Run Firebase operation in a background thread
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Directly sign in the user without storing the result
                auth.signInWithEmailAndPassword(email, password).await()
                withContext(Dispatchers.Main) {
                    // Navigate to homepage after successful login
                    navController.navigate("homepage") {
                        popUpTo("login") { inclusive = true }
                    }
                    // After login, show the success message
                    showSnackbar("Logged In Successfully!")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    errorMessage = "Login failed: ${e.message}"
                }
            }
        }
    }





    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.our_logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(300.dp) // Increased size for the logo
                    .padding(bottom = 32.dp)
            )


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
fun LoginScreenPreview() {
    AugmentedRealityProjectTheme {
        LoginScreen(rememberNavController())
    }
}
