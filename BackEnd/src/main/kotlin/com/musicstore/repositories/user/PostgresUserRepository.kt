package com.musicstore.repositories.user

import com.musicstore.mapping.RoleTable
import com.musicstore.mapping.UserDAO
import com.musicstore.mapping.UserRoleTable
import com.musicstore.mapping.UserTable
import com.musicstore.mapping.daoToModel
import com.musicstore.mapping.mapRowToUser
import com.musicstore.model.User
import com.musicstore.model.request.PaginatedResponse
import com.musicstore.plugins.hashPassword
import com.musicstore.plugins.suspendTransaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.selectAll

class PostgresUserRepository : UserRepository {
    override suspend fun allUsers(
        page: Int,
        size: Int?,
        admin: Boolean?
    ): PaginatedResponse<User> = suspendTransaction {
        val baseQuery = UserTable
            .innerJoin(UserRoleTable)
            .innerJoin(RoleTable)
            .selectAll()
            .apply {
                if (admin == true) {
                    andWhere {
                        RoleTable.id eq 1
                    }
                }
            }

        val totalElements = baseQuery.count().toInt()

        if (totalElements == 0) {
            return@suspendTransaction PaginatedResponse(
                totalElements = 0,
                totalPages = 0,
                page = 0,
                pageSize = 0,
                items = emptyList()
            )
        }
        val pageSize = 10
        val totalPages = (totalElements + pageSize - 1) / pageSize
        val page = page.coerceAtMost(totalPages)
        val offset = (page - 1) * pageSize
        val actualPageSize = pageSize.coerceAtMost(totalElements - offset)

        val paginatedQuery = baseQuery
            .limit(actualPageSize, offset.toLong())
            .map(::mapRowToUser)

        PaginatedResponse(
            totalElements = totalElements,
            totalPages = totalPages,
            page = page,
            pageSize = actualPageSize,
            items = paginatedQuery
        )
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

    override suspend fun updateUserById(id: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun changeUserAccess(user: User): Unit = suspendTransaction {
        UserDAO.findByIdAndUpdate(user.id!!) { userToUpdate ->
            userToUpdate.user_status = if (userToUpdate.user_status == 1) 0 else 1
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