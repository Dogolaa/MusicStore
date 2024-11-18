package com.musicstore.model.request

import kotlinx.serialization.Serializable

@Serializable
data class ProductPaginatedResponse<T>(
    val totalElements: Int,
    val totalPages: Int,
    val page: Int,
    val pageSize: Int,
    val items: List<T>
)
