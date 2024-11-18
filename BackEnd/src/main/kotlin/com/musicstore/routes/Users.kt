package com.musicstore.routes

import com.musicstore.model.User
import com.musicstore.repositories.user.UserRepository
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userRoute(userRepository: UserRepository) {
    route("/api/users") {
        get {
            val users = userRepository.allUsers()

            if (users.isEmpty()) {
                call.respond(HttpStatusCode.NoContent, "No users found")
                return@get
            }

            call.respond(users)
        }

        post {
            try {
                val user = call.receive<User>()
                userRepository.addUser(user)
                call.respond(HttpStatusCode.Created)
            } catch (_: IllegalStateException) {
                call.respond(HttpStatusCode.BadRequest)
            } catch (_: JsonConvertException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
}