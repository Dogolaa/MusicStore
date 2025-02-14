package com.musicstore.mapping

import com.musicstore.model.Role
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object RoleTable : IntIdTable("roles") {
    val role_name = varchar("role_name", 100).check("valid_role_names") {
        it inList listOf("ADMIN", "SELLER", "SHIPPER", "CUSTOMER")
    }
    val role_description = varchar("role_description", 100)
}

class RoleDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<RoleDAO>(RoleTable)

    var role_name by RoleTable.role_name
    var role_description by RoleTable.role_description
}

fun daoToModel(dao: RoleDAO) = Role(
    id = dao.id.value,
    role_name = dao.role_name,
    role_description = dao.role_description
)