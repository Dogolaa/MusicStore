package com.example.repositories.product

import com.example.mapping.Brands
import com.example.mapping.ProductDAO
import com.example.mapping.Products
import com.example.mapping.daoToModel
import com.example.model.Product
import com.example.model.request.UpdateProduct
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

    override suspend fun productById(id: Int): Product? = suspendTransaction {
        ProductDAO.find { (Products.id eq id) }.limit(1).map(::daoToModel).firstOrNull()
    }

    override suspend fun updateProductById(id: Int, product: UpdateProduct): Unit = suspendTransaction {
        runBlocking {
            if(product.id_brand != null){
                BrandRepository.brandById(product.id_brand) ?: throw Exception("Esta marca com ID ${product.id_brand} não existe")
            }
        }

        ProductDAO.findByIdAndUpdate(id){ entity ->
            product.id_brand?.let { entity.id_brand = EntityID(it, Brands) }
            product.product_name?.let { entity.product_name = it }
            product.product_main_photo?.let { entity.product_main_photo = it }
            product.product_short_desc?.let { entity.product_short_desc = it }
            product.product_long_desc?.let { entity.product_long_desc = it }
            product.product_price?.let { entity.product_price = it }
            product.product_discount?.let { entity.product_discount = it }
            product.product_status?.let { entity.product_status = it }
            product.product_has_stocks?.let { entity.product_has_stocks = it }
            product.product_width?.let { entity.product_width = it }
            product.product_lenght?.let { entity.product_lenght = it }
            product.product_height?.let { entity.product_height = it }
            product.product_cost?.let { entity.product_cost = it }
            product.product_update_time.let { entity.product_update_time = it }
        }
    }

    override suspend fun removeProductById(id: Int): Boolean = suspendTransaction {
        val rowsDeleted = Products.deleteWhere {
            Products.id eq id
        }
        rowsDeleted == 1
    }
}