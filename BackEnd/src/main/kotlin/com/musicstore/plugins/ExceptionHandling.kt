package com.musicstore.plugins

import com.musicstore.exceptions.InsufficientPermissionException
import com.musicstore.model.request.ErrorResponse
import io.ktor.http.*
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
                    cause.message ?: "Not Found"
                )
            )
        }

        exception<com.musicstore.exceptions.MissingHeaderException> { call, cause ->
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(
                    HttpStatusCode.BadRequest.value,
                    cause.message ?: "Missing Header"
                )
            )
        }

        exception<InsufficientPermissionException> { call, cause ->
            call.respond(
                HttpStatusCode.Forbidden,
                ErrorResponse(
                    HttpStatusCode.Forbidden.value,
                    cause.message ?: "Forbidden",
                )
            )
        }
        exception<Throwable> { call, cause ->
            call.respond(
                HttpStatusCode.InternalServerError,
                ErrorResponse(
                    HttpStatusCode.InternalServerError.value,
                    cause.message ?: "Internal Server Error"
                )
            )
        }
    }
}