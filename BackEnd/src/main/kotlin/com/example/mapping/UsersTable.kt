package com.example.mapping

import com.example.model.User
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Users : IntIdTable("users") {
    val user_email = varchar("user_email", 100).uniqueIndex()
    val user_name = varchar("user_name", 100)
    val user_last_name = varchar("user_last_name", 100)
    val user_password = varchar("user_password", 80)
    val user_status = integer("user_status")
    val user_ph_content = varchar("user_ph_content", 100)
}

class UserDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserDAO>(Users)

    var user_email by Users.user_email
    var user_name by Users.user_name
    var user_last_name by Users.user_last_name
    var user_password by Users.user_password
    var user_status by Users.user_status
    var user_ph_content by Users.user_ph_content
}

fun daoToModel(dao: UserDAO) = User(
    id = dao.id.value,
    user_email = dao.user_email,
    user_name = dao.user_name,
    user_last_name = dao.user_last_name,
    user_password = dao.user_password,
    user_status = dao.user_status,
    user_ph_content = dao.user_ph_content
)