package com.example.mapping

import com.example.model.Role
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Roles : IntIdTable("roles") {
    val role_name = varchar("role_name", 100)
    val role_description = varchar("role_description", 100)
}

class RoleDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<RoleDAO>(Roles)

    var role_name by Roles.role_name
    var role_description by Roles.role_description
}

fun daoToModel(dao: RoleDAO) = Role(
    id = dao.id.value,
    role_name = dao.role_name,
    role_description = dao.role_description
)