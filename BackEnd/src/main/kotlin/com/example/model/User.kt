package com.example.model

data class User(
    val id: Int,
    val user_email: String,
    val user_name: String,
    val user_last_name: String,
    val user_password: String,
    val user_status: Int,
    val user_ph_content: String
)