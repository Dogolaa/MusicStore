package com.musicstore.plugins

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.auth.AuthenticationChecked
import io.ktor.server.auth.jwt.*
import io.ktor.server.auth.principal
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route

// Para adicionar a roleValidation, use este escopo por fora da requisição:
//authenticate {
//    requireRole("ADMIN")
//    get(...){}
//}

fun Route.requireRole(requiredRole: String) {
    install(createRouteScopedPlugin(name = "RoleValidation") {
        on(AuthenticationChecked) { call ->
            val principal = call.principal<JWTPrincipal>()

            if (principal == null) {
                call.respondText(
                    "A valid JWT token must be provided in the Authorization header",
                    status = HttpStatusCode.BadRequest
                )
                return@on
            }

            val userRole = principal.payload.getClaim("user_role").asList(String::class.java)

            if (!userRole.any { it.equals(requiredRole, ignoreCase = true) }) {
                call.respondText(
                    "According to your JWT token, you don't have the required role",
                    status = HttpStatusCode.Forbidden
                )
                return@on
            }
        }
    })
}
