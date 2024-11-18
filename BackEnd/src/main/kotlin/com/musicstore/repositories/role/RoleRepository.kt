package com.musicstore.repositories.role

interface RoleRepository {
    suspend fun roleByUserId(id: Int): List<String>?
}