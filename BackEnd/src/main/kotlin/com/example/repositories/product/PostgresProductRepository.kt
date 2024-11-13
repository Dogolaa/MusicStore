package com.example.repositories.product

import com.example.mapping.ProductDAO
import com.example.mapping.daoToModel
import com.example.model.Product
import com.example.plugins.suspendTransaction

class PostgresProductRepository : ProductRepository {
    override suspend fun allProducts(): List<Product> = suspendTransaction {
        ProductDAO.all().map(::daoToModel)
    }

    override suspend fun productById(id: Int): Product {
        TODO("Not yet implemented")
    }
}