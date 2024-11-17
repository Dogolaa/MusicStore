package com.musicstore.mapping

import com.musicstore.model.BrandCategory
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object BrandCategoryTable : IntIdTable("brands_categories") {
    val id_brand = reference("id_brand", BrandTable)
    val id_category = reference("id_category", CategoryTable)
}

class BrandCategoryDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<BrandCategoryDAO>(BrandCategoryTable)

    var id_brand by BrandCategoryTable.id_brand
    var id_category by BrandCategoryTable.id_category
}

fun daoToModel(dao: BrandCategoryDAO) = BrandCategory(
    id = dao.id.value,
    id_brand = dao.id_brand.value,
    id_category = dao.id_category.value,
)