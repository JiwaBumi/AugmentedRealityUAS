package com.uas.augmentedrealityproject

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.uas.augmentedrealityproject.viewmodel.CartViewModel

// Map to assign the names to product ID accordingly
val cartProductNames = mapOf(
    1 to "Table",
    2 to "Hanging Pod Chair",
    3 to "Lamp",
    4 to "Stool",
    5 to "Grass Lamp",
    6 to "Coffee Table"
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingCart(navController: NavController, cartViewModel: CartViewModel) {
    val cartItems by cartViewModel.cart.collectAsState()
    val selectedItems = remember { mutableStateListOf<Int>() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Cart") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("homepage") }) {
                        Icon(
                            painter = painterResource(id = R.drawable.backreturn),
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (cartItems.isEmpty()) {
                Text(
                    text = "Your Cart is Empty!",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                LazyColumn {
                    items(cartItems.size) { index ->
                        val productId = cartItems[index]
                        CartItem(
                            productId = productId,
                            selectedItems = selectedItems,
                            onCheckedChange = { isChecked ->
                                if (isChecked) selectedItems.add(productId)
                                else selectedItems.remove(productId)
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Row for action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Proceed to Payment button
                Button(
                    onClick = {
                        if (selectedItems.isNotEmpty()) {
                            val selectedItemsString = selectedItems.joinToString(",")
                            navController.navigate("payment/$selectedItemsString")
                        }
                    },
                    enabled = selectedItems.isNotEmpty(),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) {
                    Text("Proceed to Payment")
                }


                // Remove from Cart button
                Button(
                    onClick = {
                        selectedItems.forEach { productId ->
                            cartViewModel.removeFromCart(productId)
                        }
                        selectedItems.clear() // Clear the selection after removing
                    },
                    enabled = selectedItems.isNotEmpty(),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Remove from Cart")
                }
            }
        }
    }
}

@Composable
fun CartItem(productId: Int, selectedItems: MutableList<Int>, onCheckedChange: (Boolean) -> Unit) {
    // Get the product name using the productId
    val productName = cartProductNames[productId] ?: "Unknown Product"

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // Display the product names
        Text(productName, style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.weight(1f))

        Checkbox(
            checked = selectedItems.contains(productId),
            onCheckedChange = onCheckedChange
        )
    }
}


@Preview(showBackground = true)
@Composable
fun ShoppingCartPreview() {
    // A placeholder NavController preview
    ShoppingCart(navController = rememberNavController(), cartViewModel = CartViewModel())
}
