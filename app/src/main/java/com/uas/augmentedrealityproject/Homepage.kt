package com.uas.augmentedrealityproject

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Homepage(navController: NavController) {
    val auth = FirebaseAuth.getInstance()

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    // Logout button
                    IconButton(onClick = {
                        // Sign out the user
                        auth.signOut()
                        // Navigate back to the MainActivity (login screen)
                        navController.navigate("login") {
                            // Pop all previous destinations from the stack so the user can't go back to the homepage
                            popUpTo("login") { inclusive = true }
                        }
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.door_out), // Your door_out asset
                            contentDescription = "Logout"
                        )
                    }
                },
                title = {},
                actions = {
                    // Shopping cart button
                    IconButton(onClick = { navController.navigate("shoppingcart") }) {
                        Icon(
                            painter = painterResource(id = R.drawable.shopping_cart), // Your shopping cart asset
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
                items(3) { index ->
                    Box(
                        modifier = Modifier
                            .width(400.dp)  // Modify carousel item width here
                            .height(150.dp) // Same but for height
                            .padding(8.dp)
                            .clickable {
                                // Navigate to PlaceholderProduct when clicked
                                navController.navigate("exampleproduct")
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Carousel Item ${index + 1}", style = MaterialTheme.typography.headlineSmall)
                    }
                }
            }

            // Label above product list
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
                items(10) { index ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate("exampleproduct")
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.placeholder_image),
                            contentDescription = "Product Image",
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("Product Title $index")
                            Text("Lorem ipsum dolor sit amet, consectetur adipiscing elit.")
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
