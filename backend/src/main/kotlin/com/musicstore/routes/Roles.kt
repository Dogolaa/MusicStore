package com.musicstore.routes

import com.musicstore.repositories.role.RoleRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.*

fun Route.roleRoute(roleRepository: RoleRepository) {
    route("/api/roles") {
        get("/user/{id}") {
            val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            val role = roleRepository.roleByUserId(id.toInt()) ?: return@get call.respond(HttpStatusCode.NotFound)
            call.respond(role)
        }
    }
}