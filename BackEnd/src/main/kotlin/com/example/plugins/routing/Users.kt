package com.example.plugins.routing

import com.example.model.User
import com.example.repositories.user.UserRepository
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.JsonConvertException
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Routing.userRoutes(userRepository: UserRepository) {
    route("/users") {
        get {
            val users = userRepository.allUsers()

            if (users.isEmpty()) {
                call.respond(HttpStatusCode.NoContent, "No products found")
                return@get
            }

            call.respond(users)
        }

        post {
            try {
                val user = call.receive<User>()
                userRepository.addUser(user)
                call.respond(HttpStatusCode.Created)
            } catch (ex: IllegalStateException) {
                call.respond(HttpStatusCode.BadRequest)
            } catch (ex: JsonConvertException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
}