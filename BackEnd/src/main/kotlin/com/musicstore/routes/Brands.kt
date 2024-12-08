package com.musicstore.routes

import com.musicstore.model.Brand
import com.musicstore.model.request.UpdateBrand
import com.musicstore.plugins.RoleValidation
import com.musicstore.repositories.brand.BrandRepository
import io.ktor.http.*
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.brandRoute(brandRepository: BrandRepository) {
    authenticate("auth-jwt") {
        install(RoleValidation) {
            roles = listOf("SHIPPER")
        }
        route("/api/brands") {
            get {
                val brands = brandRepository.allBrands()

                if (brands.isEmpty()) {
                    call.respond(HttpStatusCode.NoContent, "No brands found")
                    return@get
                }

                call.respond(brands)
            }

            get("/{id}") {
                val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)

                val brand = brandRepository.brandById(id.toInt()) ?: return@get call.respond(HttpStatusCode.NotFound)

                call.respond(brand)
            }

            post {
                val brand = call.receive<Brand>()
                brandRepository.addBrand(brand)
                call.respond(HttpStatusCode.OK)
            }

            put("/{id}") {
                val brand = call.receive<UpdateBrand>()
                val id = call.parameters["id"] ?: return@put call.respond(HttpStatusCode.BadRequest)
                brandRepository.brandById(id.toInt()) ?: return@put call.respond(HttpStatusCode.NotFound)

                brandRepository.updateBrandById(id.toInt(), brand)
                call.respond(HttpStatusCode.OK)
            }

            delete("/{id}") {
                try {
                    val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
                    brandRepository.brandById(id.toInt()) ?: return@delete call.respond(HttpStatusCode.NotFound)
                    brandRepository.removeBrandById(id.toInt())
                    call.respond(HttpStatusCode.OK)
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, "Error: ${e.message}")
                }
            }

            post("/{brandId}/categories/{categoryId}") {
                val brandId = call.parameters["brandId"] ?: return@post call.respond(HttpStatusCode.BadRequest)
                val categoryId = call.parameters["categoryId"] ?: return@post call.respond(HttpStatusCode.BadRequest)

                brandRepository.addCategoryToBrand(brandId.toInt(), categoryId.toInt())
                call.respond(HttpStatusCode.OK)
            }

            delete("/{brandId}/categories/{categoryId}") {
                val brandId = call.parameters["brandId"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
                val categoryId = call.parameters["categoryId"] ?: return@delete call.respond(HttpStatusCode.BadRequest)

                brandRepository.removeCategoryFromBrand(brandId.toInt(), categoryId.toInt())
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}