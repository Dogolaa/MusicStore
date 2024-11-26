package com.musicstore.mapping

import com.musicstore.model.Product
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object ProductTable : IntIdTable("products") {
    val id_brand = reference("id_brand", BrandTable)
    val product_name = varchar("product_name", 100)
    val product_main_photo = varchar("product_main_photo", 100)
    val product_short_desc = varchar("product_short_desc", 100)
    val product_long_desc = varchar("product_long_desc", 500)
    val product_price = float("product_price")
    val product_discount = float("product_discount")
    val product_status = integer("product_status")
    val product_has_stocks = integer("product_has_stocks")
    val product_width = float("product_width")
    val product_lenght = float("product_lenght")
    val product_height = float("product_height")
    val product_cost = float("product_cost")
    val product_creation_time = datetime("product_creation_time")
    val product_update_time = datetime("product_update_time").nullable()
}

class ProductDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ProductDAO>(ProductTable)

    var id_brand by ProductTable.id_brand
    var product_name by ProductTable.product_name
    var product_main_photo by ProductTable.product_main_photo
    var product_short_desc by ProductTable.product_short_desc
    var product_long_desc by ProductTable.product_long_desc
    var product_price by ProductTable.product_price
    var product_discount by ProductTable.product_discount
    var product_status by ProductTable.product_status
    var product_has_stocks by ProductTable.product_has_stocks
    var product_width by ProductTable.product_width
    var product_lenght by ProductTable.product_lenght
    var product_height by ProductTable.product_height
    var product_cost by ProductTable.product_cost
    var product_creation_time by ProductTable.product_creation_time
    var product_update_time by ProductTable.product_update_time
}

fun daoToModel(dao: ProductDAO) = Product(
    id = dao.id.value,
    id_brand = dao.id_brand.value,
    product_name = dao.product_name,
    product_main_photo = dao.product_main_photo,
    product_short_desc = dao.product_short_desc,
    product_long_desc = dao.product_long_desc,
    product_price = dao.product_price,
    product_discount = dao.product_discount,
    product_status = dao.product_status,
    product_has_stocks = dao.product_has_stocks,
    product_width = dao.product_width,
    product_lenght = dao.product_lenght,
    product_height = dao.product_height,
    product_cost = dao.product_cost,
    product_creation_time = dao.product_creation_time,
    product_update_time = dao.product_update_time,
)

fun mapRowToModel(row: ResultRow): Product {
    return Product(
        id = row[ProductTable.id].value,
        id_brand = row[ProductTable.id_brand].value,
        product_name = row[ProductTable.product_name],
        product_main_photo = row[ProductTable.product_main_photo],
        product_short_desc = row[ProductTable.product_short_desc],
        product_long_desc = row[ProductTable.product_long_desc],
        product_price = row[ProductTable.product_price],
        product_discount = row[ProductTable.product_discount],
        product_status = row[ProductTable.product_status],
        product_has_stocks = row[ProductTable.product_has_stocks],
        product_width = row[ProductTable.product_width],
        product_lenght = row[ProductTable.product_lenght],
        product_height = row[ProductTable.product_height],
        product_cost = row[ProductTable.product_cost],
        product_creation_time = row[ProductTable.product_creation_time],
        product_update_time = row[ProductTable.product_update_time]
    )
}