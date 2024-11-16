package com.musicstore.model

import kotlinx.datetime.*
import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Int? = null,
    val id_brand: Int,
    val product_name: String,
    val product_main_photo: String,
    val product_short_desc: String,
    val product_long_desc: String,
    val product_price: Float,
    val product_discount: Float,
    val product_status: Int,
    val product_has_stocks: Int,
    val product_width: Float,
    val product_lenght: Float,
    val product_height: Float,
    val product_cost: Float,
    val product_creation_time: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    val product_update_time: LocalDateTime? = null
)