package com.uas.augmentedrealityproject

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.uas.augmentedrealityproject.ui.theme.AugmentedRealityProjectTheme
import com.uas.augmentedrealityproject.viewmodel.CartViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue


data class Product(val name: String, val description: String, val imageRes: Int)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Homepage(navController: NavController, cartViewModel: CartViewModel) {
    val auth = FirebaseAuth.getInstance()
    val selectedItems = cartViewModel.cart.collectAsState().value // Get current cart items

    // Product list
    val products = listOf(
        Product("Grass Lamp", "Bring nature indoors with this eco-inspired lamp!", R.drawable.mo_grasslamp),
        Product("Hanging Pod Chair", "A Reliable Comfort!", R.drawable.mo_chair),
        Product("Lamp", "Sleek and functional lighting!", R.drawable.mo_lamp),
        Product("Stool", "Sleek And Fancy Way To Sit!", R.drawable.mo_stool),
        Product("Table", "Versatile, sleek, and robust! Ideal for small spaces!", R.drawable.mo_table),
        Product("Coffee Table", "A stylish wooden coffee table that complements any living room", R.drawable.mo_coffee)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = {
                        auth.signOut()
                        navController.navigate("login") {
                            popUpTo("login") { inclusive = true }
                        }
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.door_out),
                            contentDescription = "Logout"
                        )
                    }
                },
                title = {},
                actions = {
                    // Shopping cart button
                    IconButton(onClick = { navController.navigate("shoppingcart") }) {
                        Icon(
                            painter = painterResource(id = R.drawable.shopping_cart),
                            contentDescription = "Shopping Cart"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            Text(
                text = "Hot Products Right Now!",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp)
            )

            // Carousel section
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(products.take(3)) { product ->
                    Box(
                        modifier = Modifier
                            .width(200.dp)
                            .height(250.dp)
                            .padding(8.dp)
                            .clickable {
                                // Navigate to the respective screen based on the product name
                                when (product.name) {
                                    "Lamp" -> navController.navigate("lamp")
                                    "Hanging Pod Chair" -> navController.navigate("chair")
                                    "Coffee Table" -> navController.navigate("coffee")
                                    "Stool" -> navController.navigate("stool")
                                    "Grass Lamp" -> navController.navigate("grasslamp")
                                    "Table" -> navController.navigate("table")
                                    else -> navController.navigate("homepage") // Fallback
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = product.imageRes),
                            contentDescription = product.name,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            Text(
                text = "All Of Our Products",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(start = 16.dp, top = 24.dp)
            )

            // Product list section
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(products) { product ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                // Navigate to the respective screen based on product name
                                when (product.name) {
                                    "Lamp" -> navController.navigate("lamp")
                                    "Grass Lamp" -> navController.navigate("grasslamp")
                                    "Hanging Pod Chair" -> navController.navigate("chair")
                                    "Stool" -> navController.navigate("stool")
                                    "Coffee Table" -> navController.navigate("coffee")
                                    "Table" -> navController.navigate("table")
                                }
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = product.imageRes),
                            contentDescription = product.name,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(product.name, style = MaterialTheme.typography.titleMedium)
                            Text(product.description, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}




@Preview(showBackground = true)
@Composable
fun HomepagePreview() {
    AugmentedRealityProjectTheme {
        Homepage(navController = rememberNavController(), cartViewModel = CartViewModel())
    }
}
