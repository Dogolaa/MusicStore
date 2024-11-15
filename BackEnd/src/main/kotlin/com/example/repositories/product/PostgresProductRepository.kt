package com.example.repositories.product

import com.example.mapping.Brands
import com.example.mapping.ProductDAO
import com.example.mapping.Products
import com.example.mapping.daoToModel
import com.example.model.Product
import com.example.plugins.suspendTransaction
import com.example.repositories.brand.BrandRepository
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere

class PostgresProductRepository(
    private val BrandRepository: BrandRepository
) : (ProductRepository) {
    override suspend fun allProducts(ascending: Boolean, offset: Int, limit: Int): List<Product> =
        suspendTransaction {
            val sortOrder = if (ascending) SortOrder.ASC else SortOrder.DESC

            ProductDAO
                .all()
                .orderBy(Products.product_name to sortOrder)
                .limit(limit, offset = offset.toLong())
                .map(::daoToModel)
        }

    override suspend fun addProduct(product: Product): Unit = suspendTransaction {
        runBlocking {
            BrandRepository.brandById(product.id_brand) ?: throw Exception("Esta marca com ID ${product.id_brand} não existe")
        }

        ProductDAO.new {
            id_brand = EntityID(product.id_brand, Brands)
            product_name = product.product_name
            product_main_photo = product.product_main_photo
            product_short_desc = product.product_short_desc
            product_long_desc = product.product_long_desc
            product_price = product.product_price
            product_discount = product.product_discount
            product_status = product.product_status
            product_has_stocks = product.product_has_stocks
            product_width = product.product_width
            product_lenght = product.product_lenght
            product_height = product.product_height
            product_cost = product.product_cost
            product_creation_time = product.product_creation_time
            product_update_time = product.product_update_time
        }
    }

    override suspend fun productById(id: Int): Product {
        TODO("Not yet implemented")
    }

    override suspend fun removeProductById(id: Int): Boolean = suspendTransaction {
        val rowsDeleted = Products.deleteWhere {
            Products.id eq id
        }
        rowsDeleted == 1
    }
}