package com.example.mapping

import com.example.model.UserRole
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object UsersRoles : IntIdTable("users_roles") {
    val id_user = reference("id_user", Users)
    val id_role = reference("id_role", Roles)
}

class UserRoleDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserRoleDAO>(UsersRoles)

    var id_user by UsersRoles.id_user
    var id_role by UsersRoles.id_role
}

fun daoToModel(dao: UserRoleDAO) = UserRole(
    id = dao.id.value,
    id_user = dao.id_user.value,
    id_role = dao.id_role.value
)