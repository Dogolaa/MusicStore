package com.musicstore.mapping

import com.musicstore.model.Brand
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object BrandTable : IntIdTable("brands") {
    val brand_name = varchar("brand_name", 100)
    val brand_ph_content = varchar("brand_ph_content", 45)
}

class BrandDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<BrandDAO>(BrandTable)

    var brand_name by BrandTable.brand_name
    var brand_ph_content by BrandTable.brand_ph_content
}

fun daoToModel(dao: BrandDAO) = Brand(
    id = dao.id.value,
    brand_name = dao.brand_name,
    brand_ph_content = dao.brand_ph_content
)