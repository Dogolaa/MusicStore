package com.musicstore.routes

import com.musicstore.model.Product
import com.musicstore.model.request.UpdateProduct
import com.musicstore.repositories.product.ProductRepository
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.server.request.receive
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import io.ktor.utils.io.readRemaining
import kotlinx.io.readByteArray
import java.io.File

fun Route.productRoute(productRepository: ProductRepository) {

    route("/api/products") {
        get {
            val ascending = call.request.queryParameters["asc"]?.toBoolean() != false
            val page = call.request.queryParameters["page"]?.toIntOrNull()?.coerceAtLeast(1) ?: 1
            val nameProduct = call.request.queryParameters["nameProduct"]
            val shortDesc = call.request.queryParameters["shortDesc"]
            val fullDesc = call.request.queryParameters["fullDesc"]
            val brandId = call.request.queryParameters["brand"]?.toIntOrNull()
            val categoryId = call.request.queryParameters["category"]?.toIntOrNull()

            val paginatedProducts = productRepository.allProducts(
                ascending = ascending,
                page = page,
                nameProduct = nameProduct,
                shortDesc = shortDesc,
                fullDesc = fullDesc,
                brandId = brandId,
                categoryId = categoryId
            )

            call.respond(paginatedProducts)
        }

        get("/{id}") {
            val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            val product = productRepository.productById(id.toInt()) ?: return@get call.respond(HttpStatusCode.NotFound)
            call.respond(product)
        }

        post {
            try {
                var productJson = ""
                var fileName = ""

                val multipartData = call.receiveMultipart()
                multipartData.forEachPart { part ->
                    when (part) {
                        is PartData.FormItem -> {
                            if (part.name == "product") {
                                productJson = part.value
                            }
                        }

                        is PartData.FileItem -> {
                            if (part.name == "image") {
                                val originalFileName = part.originalFileName ?: "unknown.jpg"
                                val fileExtension = originalFileName.substringAfterLast(".", "jpg")
                                fileName = "${System.currentTimeMillis()}-${java.util.UUID.randomUUID()}.$fileExtension"

                                val fileBytes = part.provider().readRemaining().readByteArray()
                                File("images/products/$fileName").writeBytes(fileBytes)
                            }
                        }

                        else -> {}
                    }
                    part.dispose()
                }

                val product = kotlinx.serialization.json.Json.decodeFromString(Product.serializer(), productJson)
                val productWithImage = product.copy(product_main_photo = fileName)

                productRepository.addProduct(productWithImage)
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