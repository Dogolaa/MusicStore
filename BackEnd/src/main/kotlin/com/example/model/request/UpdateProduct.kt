package com.example.model.request

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class UpdateProduct(
    val id_brand: Int? = null,
    val product_name: String? = null,
    val product_main_photo: String? = null,
    val product_short_desc: String? = null,
    val product_long_desc: String? = null,
    val product_price: Float? = null,
    val product_discount: Float? = null,
    val product_status: Int? = null,
    val product_has_stocks: Int? = null,
    val product_width: Float? = null,
    val product_lenght: Float? = null,
    val product_height: Float? = null,
    val product_cost: Float? = null,
    val product_update_time: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
)
