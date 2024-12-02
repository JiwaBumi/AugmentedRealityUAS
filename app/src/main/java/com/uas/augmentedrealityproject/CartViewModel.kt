package com.uas.augmentedrealityproject.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CartViewModel : ViewModel() {
    private val _cart = MutableStateFlow<List<Int>>(emptyList()) // Holds product IDs
    val cart: StateFlow<List<Int>> = _cart // Expose as a StateFlow

    // Add product to the cart
    fun addToCart(productId: Int) {
        val currentCart = _cart.value.toMutableList()
        if (!currentCart.contains(productId)) {
            currentCart.add(productId) // Add product ID to cart
            _cart.value = currentCart // Update the cart
        }
    }

    // Remove product from the cart
    fun removeFromCart(productId: Int) {
        val currentCart = _cart.value.toMutableList()
        currentCart.remove(productId) // Remove product ID from cart
        _cart.value = currentCart
    }

    // Clear all items in the cart
    fun clearCart() {
        _cart.value = emptyList() // Set it to Empty List
    }
}
