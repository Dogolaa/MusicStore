package com.musicstore.mapping

import com.musicstore.model.BrandCategory
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object BrandsCategories : IntIdTable("brands_categories") {
    val id_brand = reference("id_brand", Brands)
    val id_category = reference("id_category", Categories)
}

class BrandCategoryDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<BrandCategoryDAO>(BrandsCategories)

    var id_brand by BrandsCategories.id_brand
    var id_category by BrandsCategories.id_category
}

fun daoToModel(dao: BrandCategoryDAO) = BrandCategory(
    id = dao.id.value,
    id_brand = dao.id_brand.value,
    id_category = dao.id_category.value,
)