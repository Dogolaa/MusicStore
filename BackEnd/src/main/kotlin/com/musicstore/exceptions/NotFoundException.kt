package com.musicstore.exceptions

class NotFoundException(
    message: String,
    val details: String? = null
) : RuntimeException(message)