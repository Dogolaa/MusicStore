package com.musicstore.plugins

import com.musicstore.model.request.ErrorResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureExceptionHandling() {
    install(StatusPages) {
        exception<NotFoundException> { call, cause ->
            call.respond(
                HttpStatusCode.NotFound,
                ErrorResponse(
                    HttpStatusCode.NotFound.value,
                    cause.message?: "Resource not found",
                    null
                )
            )
        }

        exception<NotFoundException> { call, cause ->
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(
                    HttpStatusCode.BadRequest.value,
                    cause.message?: "Bad Request",
                    null
                )
            )
        }
    }
}