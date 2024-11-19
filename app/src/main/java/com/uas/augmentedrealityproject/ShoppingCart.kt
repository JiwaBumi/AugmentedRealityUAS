package com.uas.augmentedrealityproject

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.uas.augmentedrealityproject.ui.theme.AugmentedRealityProjectTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingCart(navController: NavController) {
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
                title = { Text("Your Current Cart") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Cart empty message
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Your Cart is Empty! \nBuy Something!",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .align(Alignment.Center)
                )
            }

            // Proceed to Payment
            Button(
                onClick = { /* PLACEHOLDER ACTION */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Text("Proceed to Payment")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShoppingCartPreview() {
    AugmentedRealityProjectTheme {
        ShoppingCart(navController = rememberNavController())
    }
}
