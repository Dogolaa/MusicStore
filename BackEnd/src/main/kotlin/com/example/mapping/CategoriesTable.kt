package com.example.mapping

import com.example.model.Category
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Categories : IntIdTable("categories") {
    val id_category_parent = reference("id_category_parent", Categories)
    val category_name = varchar("name", 255)
    val category_ph_content = blob("category_ph_content")
}

class CategoryDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CategoryDAO>(Categories)

    var id_category_parent by CategoryDAO referencedOn Categories.id_category_parent
    var category_name by Categories.category_name
    var category_ph_content by Categories.category_ph_content
}

fun daoToModel(dao: CategoryDAO) = Category(
    id = dao.id.value,
    // TODO resolve auto referenciamento
    category_name = dao.category_name,
    category_ph_content = dao.category_ph_content.toString()
)