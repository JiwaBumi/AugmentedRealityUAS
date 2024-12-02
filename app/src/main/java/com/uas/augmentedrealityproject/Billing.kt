package com.uas.augmentedrealityproject

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.text.NumberFormat
import java.util.*

// Map for product prices
val productPrices = mapOf(
    1 to 750000,
    2 to 1000000,
    3 to 6500000,
    4 to 3000000,
    5 to 5000000,
    6 to 980000
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Billing(navController: NavController, purchasedItems: List<Int>) {
    val totalAmount = purchasedItems.sumOf { productPrices[it] ?: 0 }

    // Function to format numbers with commas
    fun formatPrice(price: Int): String {
        val numberFormat = NumberFormat.getInstance(Locale("id", "ID")) // Indonesian Currency Format
        return "Rp.${numberFormat.format(price)}"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Receipt") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Thank you for your purchase!",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(purchasedItems.size) { index ->
                    val productId = purchasedItems[index]
                    val productName = productNames[productId] ?: "Unknown Product"
                    val productPrice = productPrices[productId]?.let { formatPrice(it) } ?: "Rp.0"

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(productName, style = MaterialTheme.typography.bodyLarge)
                        Text(productPrice, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Total: ${formatPrice(totalAmount)}",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // Return Button
            Button(
                onClick = { navController.navigate("homepage") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Return")
            }
        }
    }
}
