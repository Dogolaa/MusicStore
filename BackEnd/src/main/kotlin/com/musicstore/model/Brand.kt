package com.musicstore.model

import kotlinx.serialization.Serializable

@Serializable
data class Brand (
    val id: Int,
    val brand_name: String,
    val brand_ph_content: String
)