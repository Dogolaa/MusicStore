package com.musicstore.model

import kotlinx.serialization.Serializable

@Serializable
data class BrandCategory (
    val id: Int,
    val id_brand: Int,
    val id_category: Int,
)