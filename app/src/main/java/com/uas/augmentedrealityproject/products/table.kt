package com.uas.augmentedrealityproject.products

import com.uas.augmentedrealityproject.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.uas.augmentedrealityproject.viewmodel.CartViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TableProduct(navController: NavController, cartViewModel: CartViewModel) {
    val productId = 1

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("homepage") }) {
                        Icon(
                            painter = painterResource(id = R.drawable.backreturn),
                            contentDescription = "Back"
                        )
                    }
                },
                title = { Text("Viewing Product") },
                actions = { /* PLACEHOLDER */ },
                modifier = Modifier.fillMaxWidth()
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()) // This makes the column scrollable
        ) {
            // Section 1: Product Details
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Placeholder Image
                Image(
                    painter = painterResource(id = R.drawable.mo_table),
                    contentDescription = "Product Image",
                    modifier = Modifier.size(120.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))

                // Product Info
                Column {
                    Text("Table", style = MaterialTheme.typography.headlineSmall)
                    Text("Very Sleek, Sturdy, and Cheap! \nRp.750.000", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(16.dp))

                    // Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            onClick = { cartViewModel.addToCart(productId) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "Add to Cart",
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 1, // Ensures the text does not wrap
                            )
                        }
                        Button(
                            onClick = { /* PLACEHOLDER ACTION */ },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("AR View")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Section 2: Description
            Text(
                text = "Description",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp)
            )
            Text(
                text = """
                    This sleek modern coffee table will make a fine addition to your living room! Even for only Rp. 750,000 we can still assure you of its quality!
                """.trimIndent(),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Section 3: Products You May Like
            Text(
                text = "Products You May Like",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(start = 16.dp)
            )

            // Recommendation  section
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(3) { index ->
                    Box(
                        modifier = Modifier
                            .width(200.dp)
                            .height(200.dp)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Product ${index + 1}", style = MaterialTheme.typography.headlineSmall)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TableProductPreview() {
    TableProduct(navController = rememberNavController(), cartViewModel = viewModel())
}