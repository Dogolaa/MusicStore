package com.example.repositories.product

import com.example.model.Product

interface ProductRepository {
    suspend fun allProducts(ascending: Boolean = true, offset: Int = 0, limit: Int = 10): List<Product>
    suspend fun addProduct(product: Product)
    suspend fun productById(id: Int): Product
    suspend fun removeProductById(id: Int): Boolean
}