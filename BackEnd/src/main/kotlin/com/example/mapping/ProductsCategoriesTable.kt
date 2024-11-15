package com.example.mapping

import com.example.model.ProductCategory
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object ProductsCategories : IntIdTable("product_category") {
    val id_category = reference("id_category", Categories)
    val id_product = reference("id_product", Products)
}

class ProductsCategoriesDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ProductDAO>(Products)

    var id_category by ProductsCategories.id_category
    var id_product by ProductsCategories.id_product
}

fun daoToModel(dao: ProductsCategoriesDAO) = ProductCategory(
    id = dao.id.value,
    id_category = dao.id_category.value,
    id_product = dao.id_product.value
)