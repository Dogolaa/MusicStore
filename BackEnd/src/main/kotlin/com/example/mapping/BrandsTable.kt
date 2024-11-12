package com.example.mapping

import com.example.model.Brand
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Brands : IntIdTable("brands") {
    val brand_name = varchar("brand_name", 100)
    val brand_ph_content = varchar("brand_ph_content", 45)
}

class BrandDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<BrandDAO>(Brands)

    var brand_name by Brands.brand_name
    var brand_ph_content by Brands.brand_ph_content
}

fun daoToModel(dao: BrandDAO) = Brand(
    id = dao.id.value,
    brand_name = dao.brand_name,
    brand_ph_content = dao.brand_ph_content
)