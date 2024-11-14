package com.example.plugins.routing

import com.example.repositories.product.ProductRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.route

fun Routing.productRoutes(productRepository: ProductRepository) {
    route("/products") {
        get {
            val ascending = call.request.queryParameters["ascending"]?.toBoolean() != false
            val page = call.request.queryParameters["page"]?.toIntOrNull()?.coerceAtLeast(1) ?: 1

            val pageSize = 10
            val offset = (page - 1) * pageSize

            val products = productRepository.allProducts(ascending, offset, pageSize)

            if (products.isEmpty()) {
                call.respond(HttpStatusCode.NoContent, "No products found")
                return@get
            }

            call.respond(products)
        }
    }
}