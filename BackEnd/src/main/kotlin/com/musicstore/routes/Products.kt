package com.musicstore.routes

import com.musicstore.model.Product
import com.musicstore.model.request.UpdateProduct
import com.musicstore.repositories.product.ProductRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*

fun Route.productRoute(productRepository: ProductRepository) {

    route("/api/products") {
        get {
            val ascending = call.request.queryParameters["asc"]?.toBoolean() != false
            val page = call.request.queryParameters["page"]?.toIntOrNull()?.coerceAtLeast(1) ?: 1

            val paginatedProducts = productRepository.allProducts(
                ascending = ascending,
                page = page,
            )

            call.respond(paginatedProducts)
        }

        get("/{id}") {
            val id = call.parameters["id"]
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Missing id")
                return@get
            }
            val product = productRepository.productById(id.toInt())
            if (product == null) {
                call.respond(HttpStatusCode.NotFound)
                return@get
            }
            call.respond(product)
        }

        post {
            try {
                val product = call.receive<Product>()
                productRepository.addProduct(product)
                call.respond(HttpStatusCode.Created, "Produto adicionado com sucesso!")
            } catch (ex: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    "Erro inesperado: ${ex.message ?: "Entre em contato com o suporte."}"
                )
            }
        }

        put("/{id}") {
            val id = call.parameters["id"]
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@put
            }

            val product = call.receive<UpdateProduct>()
            productRepository.updateProductById(id.toInt(), product)
            call.respond(HttpStatusCode.OK)
        }

        delete("/{id}") {
            val id = call.parameters["id"]
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }
            if (productRepository.removeProductById(id.toInt())) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

    }
}