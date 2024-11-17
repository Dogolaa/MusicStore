package com.musicstore.mapping

import com.musicstore.model.ProductCategory
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object ProductCategoryTable : IntIdTable("product_category") {
    val id_category = reference("id_category", CategoryTable)
    val id_product = reference("id_product", ProductTable)
}

class ProductsCategoriesDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ProductDAO>(ProductTable)

    var id_category by ProductCategoryTable.id_category
    var id_product by ProductCategoryTable.id_product
}

fun daoToModel(dao: ProductsCategoriesDAO) = ProductCategory(
    id = dao.id.value,
    id_category = dao.id_category.value,
    id_product = dao.id_product.value
)