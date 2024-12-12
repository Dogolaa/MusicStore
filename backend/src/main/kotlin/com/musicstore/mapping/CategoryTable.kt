package com.musicstore.mapping

import com.musicstore.model.Category
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object CategoryTable : IntIdTable("categories") {
    val id_category_parent = optReference("id_category_parent", CategoryTable)
    val category_name = varchar("category_name", 255)
    val category_ph_content = varchar("category_ph_content", 100)
}

class CategoryDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CategoryDAO>(CategoryTable)

    var id_category_parent by optionalReferencedOn(CategoryTable.id_category_parent)
    var category_name by CategoryTable.category_name
    var category_ph_content by CategoryTable.category_ph_content
}

fun daoToModel(dao: CategoryDAO) = Category(
    id = dao.id.value,
    id_category_parent = dao.id_category_parent?.id?.value,
    category_name = dao.category_name,
    category_ph_content = dao.category_ph_content
)