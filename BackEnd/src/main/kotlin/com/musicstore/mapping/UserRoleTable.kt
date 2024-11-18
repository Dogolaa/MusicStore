package com.musicstore.mapping

import com.musicstore.model.UserRole
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object UserRoleTable : IntIdTable("users_roles") {
    val id_user = reference("id_user", Users, onDelete = ReferenceOption.CASCADE)
    val id_role = reference("id_role", Roles, onDelete = ReferenceOption.CASCADE)
}

class UserRoleDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserRoleDAO>(UserRoleTable)

    var id_user by UserRoleTable.id_user
    var id_role by UserRoleTable.id_role
}

fun daoToModel(dao: UserRoleDAO) = UserRole(
    id = dao.id.value,
    id_user = dao.id_user.value,
    id_role = dao.id_role.value
)