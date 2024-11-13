package com.example.repositories.product

import com.example.model.Product

interface ProductRepository {
    suspend fun allProducts(): List<Product>
    suspend fun productById(id: Int): Product
}