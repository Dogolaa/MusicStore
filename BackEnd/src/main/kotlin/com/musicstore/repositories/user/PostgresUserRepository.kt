package com.musicstore.repositories.user

import com.musicstore.mapping.UserDAO
import com.musicstore.mapping.UserTable
import com.musicstore.mapping.daoToModel
import com.musicstore.model.User
import com.musicstore.plugins.hashPassword
import com.musicstore.plugins.suspendTransaction
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class PostgresUserRepository : UserRepository {
    override suspend fun allUsers(): List<User> = suspendTransaction {
        UserDAO.all().map(::daoToModel)
    }

    override suspend fun userById(id: Int): User? = suspendTransaction {
        UserDAO.find { (UserTable.id eq id) }.limit(1).map(::daoToModel).firstOrNull()
    }

    override suspend fun addUser(user: User): Unit = suspendTransaction {
        UserDAO.new {
            user_email = user.user_email
            user_name = user.user_name
            user_last_name = user.user_last_name
            user_password = user.user_password.hashPassword()
            user_status = user.user_status
            user_ph_content = user.user_ph_content
        }
    }

    override suspend fun removeUser(id: Int): Boolean = suspendTransaction {
        val rowsDeleted = UserTable.deleteWhere {
            UserTable.id eq id
        }
        
        rowsDeleted == 1
    }

    override suspend fun findByEmail(email: String): User? = suspendTransaction {
        UserDAO.find { (UserTable.user_email eq email) }.limit(1).map(::daoToModel).firstOrNull()
    }
}