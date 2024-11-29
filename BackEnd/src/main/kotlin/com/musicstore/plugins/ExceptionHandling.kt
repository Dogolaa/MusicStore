package com.musicstore.plugins

import com.musicstore.model.request.ErrorResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureExceptionHandling() {
    install(StatusPages) {
        exception<com.musicstore.exceptions.NotFoundException> { call, cause ->
            call.respond(
                HttpStatusCode.NotFound,
                ErrorResponse(
                    HttpStatusCode.NotFound.value,
                    cause.message ?: "Bad Request",
                    cause.details
                )
            )
        }

        exception<com.musicstore.exceptions.MissingHeaderException> { call, cause ->
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(
                    HttpStatusCode.BadRequest.value,
                    cause.message ?: "Bad Request",
                    cause.details
                )
            )
        }

        exception<Throwable> { call, cause ->
            call.respond(
                HttpStatusCode.InternalServerError,
                ErrorResponse(
                    HttpStatusCode.InternalServerError.value,
                    cause.message ?: "Internal Server Error",
                    null
                )
            )
        }
    }
}