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

data class Product(val name: String, val description: String, val imageRes: Int)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Homepage(navController: NavController) {
    val auth = FirebaseAuth.getInstance()

    // Product list
    val products = listOf(
        Product("Bathub", "A luxurious bathub for relaxation.", R.drawable.mo_bathub),
        Product("Chair", "A comfortable chair for your home.", R.drawable.mo_chair),
        Product("Drawer", "A stylish drawer to keep your essentials.", R.drawable.mo_drawer),
        Product("Stool", "A sturdy stool for any occasion.", R.drawable.mo_stool),
        Product("Table", "A modern table for your living room.", R.drawable.mo_table),
        Product("TV", "A high-definition television.", R.drawable.mo_tv)
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
                            .width(400.dp)
                            .height(150.dp)
                            .padding(8.dp)
                            .clickable {
                                // Navigate to the respective screen based on the product name
                                when (product.name) {
                                    "Bathub" -> navController.navigate("bathub")
                                    "Chair" -> navController.navigate("chair")
                                    "Drawer" -> navController.navigate("drawer")
                                    "Stool" -> navController.navigate("stool")
                                    "TV" -> navController.navigate("tv")
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
                text = "View All Products",
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
                                    "Table" -> navController.navigate("table")
                                    "Chair" -> navController.navigate("chair")
                                    "Drawer" -> navController.navigate("drawer")
                                    "Stool" -> navController.navigate("stool")
                                    "TV" -> navController.navigate("tv")
                                    "Bathub" -> navController.navigate("bathub")
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
        Homepage(rememberNavController())
    }
}
