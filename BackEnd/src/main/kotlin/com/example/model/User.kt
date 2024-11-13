package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int? = null,
    val user_email: String,
    val user_name: String,
    val user_last_name: String,
    val user_password: String,
    val user_status: Int,
    val user_ph_content: String
)