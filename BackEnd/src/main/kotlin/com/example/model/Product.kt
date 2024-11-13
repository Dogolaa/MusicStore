package com.example.model

data class Product(
    // TODO Verificar tipos e concertar o último elemento (datetime)
    val id: Int,
    val id_brand: Int,
    val product_name: String,
    val product_main_photo: String,
    val product_short_desc: String,
    val product_long_desc: String,
    val product_price: Float,
    val product_discount: Float,
    val product_status: String,
    val product_has_stocks: String,
    val product_width: Float,
    val product_lenght: Float,
    val product_height: Float,
    val product_cost: Float,
    val product_creation_time: String,
    val product_update_time: String
)