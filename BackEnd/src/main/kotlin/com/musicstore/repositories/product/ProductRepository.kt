package com.musicstore.repositories.product

import com.musicstore.model.Product
import com.musicstore.model.request.ProductPaginatedResponse
import com.musicstore.model.request.UpdateProduct

interface ProductRepository {
    suspend fun allProducts(
        ascending: Boolean = true,
        page: Int = 1,
        pageSize: Int = 10
    ): ProductPaginatedResponse<Product>

    suspend fun addProduct(product: Product)
    suspend fun productById(id: Int): Product?
    suspend fun updateProductById(id: Int, product: UpdateProduct)
    suspend fun removeProductById(id: Int): Boolean
}