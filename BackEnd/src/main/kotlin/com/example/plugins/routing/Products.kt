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
            val brands = productRepository.allProducts()

            if (brands.isEmpty()) {
                call.respond(HttpStatusCode.NoContent, "No products found")
                return@get
            }

            call.respond(brands)
        }
    }
}