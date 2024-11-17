package com.musicstore.routes

import com.musicstore.repositories.role.RoleRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.*

fun Route.roleRoutes(roleRepository: RoleRepository) {
    route("/api/roles") {
        get("/user/{id}") {
            val id = call.parameters["id"]
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Missing id")
                return@get
            }
            val role = roleRepository.roleByUserId(id.toInt())
            if (role == null) {
                call.respond(HttpStatusCode.NotFound)
                return@get
            }
            call.respond(role)
        }
    }
}