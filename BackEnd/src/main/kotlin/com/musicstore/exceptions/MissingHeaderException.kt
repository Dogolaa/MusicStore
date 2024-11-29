package com.musicstore.exceptions

class MissingHeaderException(
    message: String,
    val details: String? = null
) : RuntimeException(message)