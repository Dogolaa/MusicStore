package com.musicstore.repositories.user

import com.musicstore.model.User

interface UserRepository {
    suspend fun allUsers(): List<User>
    suspend fun userById(id: Int): User?
    suspend fun addUser(user: User)
    suspend fun removeUser(id: Int): Boolean
    suspend fun findByEmail(email: String): User?
}