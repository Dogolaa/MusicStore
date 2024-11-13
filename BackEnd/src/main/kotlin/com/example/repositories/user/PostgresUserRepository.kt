package com.example.repositories.user

import com.example.mapping.UserDAO
import com.example.mapping.Users
import com.example.mapping.daoToModel
import com.example.model.User
import com.example.plugins.hashPassword
import com.example.plugins.suspendTransaction

class PostgresUserRepository : UserRepository {
    override suspend fun allUsers(): List<User> = suspendTransaction {
        UserDAO.all().map(::daoToModel)
    }

    override suspend fun userById(id: Int): User? = suspendTransaction {
        UserDAO.find { (Users.id eq id) }.limit(1).map(::daoToModel).firstOrNull()
    }

    override suspend fun addUser(user: User): Unit = suspendTransaction {
        var senhaHash = user.user_password.hashPassword()

        UserDAO.new {
            user_email = user.user_email
            user_name = user.user_name
            user_last_name = user.user_last_name
            user_password = senhaHash
            user_status = user.user_status
            user_ph_content = user.user_ph_content
        }
    }

    override suspend fun removeUser(id: Int): Boolean {
        TODO("Not yet implemented")
    }
}