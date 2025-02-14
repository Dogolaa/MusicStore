package com.musicstore.model.request

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val status: Int,
    val message: String,
)
