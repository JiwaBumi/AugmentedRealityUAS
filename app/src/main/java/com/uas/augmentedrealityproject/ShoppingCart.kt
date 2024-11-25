package com.uas.augmentedrealityproject

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.uas.augmentedrealityproject.viewmodel.CartViewModel
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

// Map to assign the names to product ID accordingly
val productNames = mapOf(
    1 to "Table",
    2 to "Chair",
    3 to "TV",
    4 to "Drawer",
    5 to "Bathub",
    6 to "Stool"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingCart(navController: NavController, cartViewModel: CartViewModel) {
    val cartItems by cartViewModel.cart.collectAsState()
    val selectedItems = remember { mutableStateListOf<Int>() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Shopping Cart") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("homepage") }) {
                        Icon(painter = painterResource(id = R.drawable.backreturn), contentDescription = "Back")
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

            // Proceed to Payment button
            Button(
                onClick = { proceedToPayment(selectedItems) },
                enabled = selectedItems.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Proceed to Payment")
            }
        }
    }
}

@Composable
fun CartItem(productId: Int, selectedItems: MutableList<Int>, onCheckedChange: (Boolean) -> Unit) {
    // Get the product name using the productId
    val productName = productNames[productId] ?: "Unknown Product"

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // Display the product name instead of the generic "Product X"
        Text(productName, style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.weight(1f))

        Checkbox(
            checked = selectedItems.contains(productId),
            onCheckedChange = onCheckedChange
        )
    }
}

fun proceedToPayment(selectedItems: List<Int>) {
    // LogCat that Intent has been made
    Log.d("ShoppingCart", "Created Intent(${selectedItems.joinToString(", ")})")

    // Example Intent for proceeding with selected product IDs
    val intent = Intent().apply {
        putExtra("selectedProductIds", selectedItems.toIntArray()) // Add selected product IDs to the Intent
    }

    // All other logic later below TBC
}
