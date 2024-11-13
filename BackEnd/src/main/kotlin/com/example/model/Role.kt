package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class Role(
    val id: Int,
    val role_name: String,
    val role_description: String
)