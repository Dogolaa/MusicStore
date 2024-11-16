package com.musicstore.model

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val id: Int,
    val id_category_parent: Int? = null,
    val category_name: String,
    val category_ph_content: String
)
