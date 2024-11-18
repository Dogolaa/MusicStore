package com.musicstore.routes

import com.musicstore.repositories.brand.BrandRepository
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.brandRoute(brandRepository: BrandRepository) {
    route("/api/brands") {
        get {
            val brands = brandRepository.allBrands()

            if (brands.isEmpty()) {
                call.respond(HttpStatusCode.NoContent, "No brands found")
                return@get
            }

            call.respond(brands)
        }
    }
}