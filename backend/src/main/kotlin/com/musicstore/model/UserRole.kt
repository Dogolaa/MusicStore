package com.musicstore.model

import kotlinx.serialization.Serializable

@Serializable
data class UserRole(
    val id: Int,
    val id_user: Int,
    val id_role: Int
)