package com.musicstore.model.request

import kotlinx.serialization.Serializable

@Serializable
data class UpdateBrand(
    val brand_name: String? = null,
    val brand_ph_content: String? = null
)
