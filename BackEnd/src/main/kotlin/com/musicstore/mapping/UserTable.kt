package com.musicstore.mapping

import com.musicstore.model.User
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.charLength

object UserTable : IntIdTable("users") {
    val user_email = varchar("user_email", 128).uniqueIndex()
    val user_name = varchar("user_name", 60).check { it.charLength() greaterEq 2 }
    val user_last_name = varchar("user_last_name", 60).check { it.charLength() greaterEq 2 }
    val user_password = varchar("user_password", 80)
    val user_status = integer("user_status")
    val user_ph_content = varchar("user_ph_content", 100)
}

class UserDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserDAO>(UserTable)

    var user_email by UserTable.user_email
    var user_name by UserTable.user_name
    var user_last_name by UserTable.user_last_name
    var user_password by UserTable.user_password
    var user_status by UserTable.user_status
    var user_ph_content by UserTable.user_ph_content
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

fun mapRowToUser(row: ResultRow): User {
    return User(
        id = row[UserTable.id].value,
        user_email = row[UserTable.user_email],
        user_name = row[UserTable.user_name],
        user_last_name = row[UserTable.user_last_name],
        user_password = row[UserTable.user_password],
        user_status = row[UserTable.user_status],
        user_ph_content = row[UserTable.user_ph_content]
    )
}