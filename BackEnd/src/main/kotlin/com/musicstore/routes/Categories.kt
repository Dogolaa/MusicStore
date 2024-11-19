package com.musicstore.routes

import com.musicstore.model.Category
import com.musicstore.repositories.category.CategoryRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.response.respond
import io.ktor.server.routing.delete
import io.ktor.server.routing.post
import io.ktor.server.routing.put

fun Route.categoryRoute(categoryRepository: CategoryRepository) {
    route("/api/categories") {
        get {
            val categories = categoryRepository.allCategories()

            if (categories.isEmpty()) {
                call.respond(HttpStatusCode.NoContent, "No categories found")
                return@get
            }

            call.respond(categories)
        }

        get("/{id}") {
            val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)

            val category =
                categoryRepository.categoryById(id.toInt()) ?: return@get call.respond(HttpStatusCode.NotFound)

            call.respond(category)
        }

        post() {
            val category = call.receive<Category>()
            categoryRepository.addCategory(category)
            call.respond(HttpStatusCode.OK)
        }

        put("/{id}") {
            val category = call.receive<Category>()
            val id = call.parameters["id"] ?: return@put call.respond(HttpStatusCode.BadRequest)
            categoryRepository.categoryById(id.toInt()) ?: return@put call.respond(HttpStatusCode.NotFound)

            categoryRepository.updateCategoryById(id.toInt(), category)
            call.respond(HttpStatusCode.OK)
        }

        delete("/{id}") {
            try {
                val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
                categoryRepository.categoryById(id.toInt()) ?: return@delete call.respond(HttpStatusCode.NotFound)
                categoryRepository.removeCategoryById(id.toInt())
                call.respond(HttpStatusCode.OK)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Error: ${e.message}")
            }
        }
    }
}