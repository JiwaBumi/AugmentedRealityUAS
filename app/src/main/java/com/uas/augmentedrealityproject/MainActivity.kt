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
import com.uas.augmentedrealityproject.products.ChairProduct
import com.uas.augmentedrealityproject.products.CoffeeProduct
import com.uas.augmentedrealityproject.products.GrasslampProduct
import com.uas.augmentedrealityproject.products.LampProduct
import com.uas.augmentedrealityproject.products.StoolProduct
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
                    composable("homepage") { Homepage(navController, cartViewModel) }
                    composable("shoppingcart") { ShoppingCart(navController, cartViewModel) }
                    composable("table") { TableProduct(navController, cartViewModel) }
                    composable("chair") { ChairProduct(navController, cartViewModel) }
                    composable("grasslamp") { GrasslampProduct(navController, cartViewModel) }
                    composable("stool") { StoolProduct(navController, cartViewModel) }
                    composable("coffee") { CoffeeProduct(navController, cartViewModel) }
                    composable("lamp") { LampProduct(navController, cartViewModel) }
                    composable("payment/{selectedItems}") { backStackEntry ->
                        val selectedItems = backStackEntry.arguments?.getString("selectedItems")
                            ?.split(",")?.map { it.toInt() } ?: emptyList()
                        Payment(navController, selectedItems, cartViewModel)
                    }
                    composable("billing/{selectedItems}") { backStackEntry ->
                        val selectedItems = backStackEntry.arguments?.getString("selectedItems")
                            ?.split(",")?.map { it.toInt() } ?: emptyList()
                        Billing(navController, selectedItems)
                    }

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
    var isLoading by remember { mutableStateOf(false) }

    fun handleRegister() {
        CoroutineScope(Dispatchers.IO).launch {
            if (password != confirmPassword) {
                withContext(Dispatchers.Main) {
                    errorMessage = "Passwords do not match!"
                }
                return@launch
            }

            try {
                auth.createUserWithEmailAndPassword(email, password).await()
                withContext(Dispatchers.Main) {
                    email = ""
                    password = ""
                    confirmPassword = ""
                    errorMessage = ""
                    isRegistering = false
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    errorMessage = "Registration failed: ${e.message}"
                }
            }
        }
    }

    fun handleLogin() {
        isLoading = true
        CoroutineScope(Dispatchers.IO).launch {
            try {
                auth.signInWithEmailAndPassword(email, password).await()
                withContext(Dispatchers.Main) {
                    navController.navigate("homepage") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    errorMessage = "Login failed: ${e.message}"
                }
            } finally {
                withContext(Dispatchers.Main) {
                    isLoading = false
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") }
            )
        }
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
                    .size(300.dp)
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
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading // Disable the button while loading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text(if (isRegistering) "Register" else "Login")
                }
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
