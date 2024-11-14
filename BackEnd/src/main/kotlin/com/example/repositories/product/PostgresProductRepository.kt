package com.example.repositories.product

import com.example.mapping.ProductDAO
import com.example.mapping.Products
import com.example.mapping.daoToModel
import com.example.model.Product
import com.example.plugins.suspendTransaction
import org.jetbrains.exposed.sql.SortOrder

class PostgresProductRepository : ProductRepository {
    override suspend fun allProducts(ascending: Boolean, offset: Int, limit: Int): List<Product> =
        suspendTransaction {
            val sortOrder = if (ascending) SortOrder.ASC else SortOrder.DESC

            ProductDAO
                .all()
                .orderBy(Products.product_name to sortOrder)
                .limit(limit, offset = offset.toLong())
                .map(::daoToModel)
        }

    override suspend fun productById(id: Int): Product {
        TODO("Not yet implemented")
    }
}