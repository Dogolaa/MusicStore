package com.musicstore.repositories.user

import com.musicstore.model.User
import com.musicstore.model.request.PaginatedResponse

interface UserRepository {
    suspend fun allUsers(
        page: Int = 1,
        size: Int? = 10,
        admin: Boolean? = false
    ): PaginatedResponse<User>

    suspend fun userById(id: Int): User?
    suspend fun addUser(user: User)
    suspend fun updateUserById(id: Int)
    suspend fun changeUserAccess(user: User)
    suspend fun removeUser(id: Int): Boolean
    suspend fun findByEmail(email: String): User?
}