package com.uas.augmentedrealityproject

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uas.augmentedrealityproject.ui.theme.AugmentedRealityProjectTheme
import com.uas.augmentedrealityproject.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Payment(navController: NavController, selectedItems: List<Int>, cartViewModel: CartViewModel) {
    // Billing details state
    var name by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }
    var selectedMonth by remember { mutableStateOf("") }
    var selectedYear by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }

    // Dropdown options
    val months = (1..12).map { it.toString().padStart(2, '0') }
    val years = (2024..2035).map { it.toString() }

    // Validation Flags
    val isFormValid = name.isNotBlank() &&
            address.isNotBlank() &&
            cardNumber.length in 8..19 &&
            selectedMonth.isNotEmpty() &&
            selectedYear.isNotEmpty() &&
            cvv.matches(Regex("^\\d{3}$"))
    val isCardNumberValid = cardNumber.length in 8..19
    val isCVVValid = cvv.matches(Regex("^\\d{3}$"))

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Payment") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("shoppingcart") }) {
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )

            // Warning for Name
            if (name.isBlank()) {
                Text(
                    text = "Name must not be empty.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            TextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Address") },
                modifier = Modifier.fillMaxWidth()
            )

            // Warning for Address
            if (address.isBlank()) {
                Text(
                    text = "Address must not be empty.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            TextField(
                value = cardNumber,
                onValueChange = { if (it.length <= 19) cardNumber = it },
                label = { Text("Credit Card Number") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // Warning for Card Number
            if (!isCardNumberValid) {
                Text(
                    text = "Credit card number must be between 8 and 19 digits.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
                // Dropdown for Month
                DropdownSelector(
                    label = "Month",
                    options = months,
                    selectedOption = selectedMonth,
                    onOptionSelected = { selectedMonth = it },
                    modifier = Modifier.weight(1f)
                )

                // Dropdown for Year
                DropdownSelector(
                    label = "Year",
                    options = years,
                    selectedOption = selectedYear,
                    onOptionSelected = { selectedYear = it },
                    modifier = Modifier.weight(1f)
                )
            }

            TextField(
                value = cvv,
                onValueChange = { if (it.length <= 3) cvv = it },
                label = { Text("CVV") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // Warning for CVV
            if (!isCVVValid) {
                Text(
                    text = "CVV must be 3 digits.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Action buttons
            Button(
                onClick = {
                    // Clear cart after successful purchase
                    cartViewModel.clearCart()
                    navController.navigate("billing/${selectedItems.joinToString(",")}") {
                        popUpTo("payment") { inclusive = true }
                    }
                },
                enabled = isFormValid, // Enable the button only when ALL forms are valid
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Purchase")
            }

            Button(
                onClick = { navController.navigate("shoppingcart") },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancel")
            }
        }
    }
}



@Composable
fun DropdownSelector(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        TextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PaymentPreview() {
    AugmentedRealityProjectTheme {
        val navController = rememberNavController()
        val cartViewModel: CartViewModel = viewModel()
        Payment(navController = navController, selectedItems = listOf(1, 2, 3), cartViewModel = cartViewModel)
    }
}

