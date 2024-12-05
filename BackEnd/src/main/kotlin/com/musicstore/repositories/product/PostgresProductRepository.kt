package com.musicstore.repositories.product

import com.musicstore.mapping.BrandCategoryTable
import com.musicstore.mapping.BrandTable
import com.musicstore.mapping.CategoryTable
import com.musicstore.mapping.ProductDAO
import com.musicstore.mapping.ProductTable
import com.musicstore.mapping.daoToModel
import com.musicstore.mapping.mapRowToProduct
import com.musicstore.model.Product
import com.musicstore.model.request.PaginatedResponse
import com.musicstore.model.request.UpdateProduct
import com.musicstore.plugins.suspendTransaction
import com.musicstore.repositories.brand.BrandRepository
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.selectAll

class PostgresProductRepository(
    private val brandRepository: BrandRepository
) : (ProductRepository) {

    override suspend fun allProducts(
        ascending: Boolean,
        page: Int,
        pageSize: Int,
        nameProduct: String?,
        shortDesc: String?,
        fullDesc: String?,
        brandId: Int?,
        categoryId: Int?
    ): PaginatedResponse<Product> = suspendTransaction {
        val sortOrder = if (ascending) SortOrder.ASC else SortOrder.DESC

        val baseQuery = ProductTable
            .innerJoin(BrandTable)
            .let { joinQuery ->
                if (categoryId != null) {
                    joinQuery.innerJoin(BrandCategoryTable).innerJoin(CategoryTable)
                } else {
                    joinQuery
                }
            }
            .selectAll()
            .apply {
                if (nameProduct != null) andWhere { ProductTable.product_name regexp "(?i).*${nameProduct}.*" }
                if (shortDesc != null) andWhere { ProductTable.product_short_desc regexp "(?i).*%$shortDesc%" }
                if (fullDesc != null) andWhere { ProductTable.product_long_desc regexp "(?i).*$fullDesc%" }
                if (brandId != null) andWhere { ProductTable.id_brand eq brandId }
                if (categoryId != null) andWhere { BrandCategoryTable.id_category eq categoryId }
            }

        val totalElements = baseQuery.count().toInt()

        if (totalElements == 0) {
            return@suspendTransaction PaginatedResponse(
                totalElements = 0,
                totalPages = 0,
                page = 0,
                pageSize = 0,
                items = emptyList()
            )
        }

        val totalPages = (totalElements + pageSize - 1) / pageSize
        val page = page.coerceAtMost(totalPages)
        val offset = (page - 1) * pageSize
        val actualPageSize = pageSize.coerceAtMost(totalElements - offset)

        val paginatedQuery = baseQuery
            .orderBy(ProductTable.product_name to sortOrder)
            .limit(actualPageSize, offset.toLong())
            .map(::mapRowToProduct)

        PaginatedResponse(
            totalElements = totalElements,
            totalPages = totalPages,
            page = page,
            pageSize = actualPageSize,
            items = paginatedQuery
        )
    }


    override suspend fun addProduct(product: Product): Unit = suspendTransaction {
        runBlocking {
            brandRepository.brandById(product.id_brand)
                ?: throw Exception("Esta marca com ID ${product.id_brand} não existe")
        }

        ProductDAO.new {
            id_brand = EntityID(product.id_brand, BrandTable)
            product_name = product.product_name
            product_main_photo = product.product_main_photo.toString()
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
        ProductDAO.find { (ProductTable.id eq id) }.limit(1).map(::daoToModel).firstOrNull()
    }

    override suspend fun updateProductById(id: Int, product: UpdateProduct): Unit = suspendTransaction {
        runBlocking {
            if (product.id_brand != null) {
                brandRepository.brandById(product.id_brand)
                    ?: throw Exception("Esta marca com ID ${product.id_brand} não existe")
            }
        }

        ProductDAO.findByIdAndUpdate(id) { entity ->
            product.id_brand?.let { entity.id_brand = EntityID(it, BrandTable) }
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
        val rowsDeleted = ProductTable.deleteWhere {
            ProductTable.id eq id
        }
        rowsDeleted == 1
    }
}