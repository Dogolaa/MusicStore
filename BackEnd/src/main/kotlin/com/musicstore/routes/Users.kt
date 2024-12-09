package com.musicstore.routes

import com.musicstore.exceptions.NotFoundException
import com.musicstore.model.User
import com.musicstore.plugins.RoleValidation
import com.musicstore.repositories.user.UserRepository
import io.ktor.http.*
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.server.auth.authenticate
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.utils.io.readRemaining
import kotlinx.io.readByteArray
import java.io.File

fun Route.userRoute(userRepository: UserRepository) {
    authenticate("auth-jwt") {
        install(RoleValidation) {
            roles = listOf("ADMIN")
        }
        route("/api/admin/users") {
            get {
                val page = call.request.queryParameters["page"]?.toIntOrNull()?.coerceAtLeast(1) ?: 1
                val size = call.request.queryParameters["size"]?.toIntOrNull()
                val admin = call.request.queryParameters["admin"]?.toBooleanStrictOrNull()

                val users = userRepository.allUsers(page, size, admin)

                call.respond(users)
            }

            get("/{id}") {
                val id = call.parameters["id"]

                val user = userRepository.userById(id!!.toInt()) ?: throw NotFoundException(
                    "User with ID $id not found"
                )

                call.respond(user)
            }

            get("/email/{email}") {
                val email = call.parameters["email"]

                val user = userRepository.findByEmail(email!!) ?: throw NotFoundException(
                    "User with email $email not found"
                )

                call.respond(user)
            }

            // TODO: Implementar PUT
            put("/{id]") {
                val id = call.parameters["id"]

//            val user = userRepository.userById(id!!.toInt()) ?: throw NotFoundException(
//                "User not found",
//                "User with ID $id not found"
//            )

                val user = call.receive<User>()
                userRepository.updateUserById(id!!.toInt())


            }

            post {
                try {
                    var userJson = ""
                    var fileName = ""

                    val multipartData = call.receiveMultipart()
                    multipartData.forEachPart { part ->
                        when (part) {
                            is PartData.FormItem -> {
                                if (part.name == "user") {
                                    userJson = part.value
                                }
                            }

                            is PartData.FileItem -> {
                                if (part.name == "image") {
                                    val originalFileName = part.originalFileName ?: "unknown.jpg"
                                    val fileExtension = originalFileName.substringAfterLast(".", "jpg")
                                    fileName =
                                        "${System.currentTimeMillis()}-${java.util.UUID.randomUUID()}.$fileExtension"

                                    val fileBytes = part.provider().readRemaining().readByteArray()
                                    File("images/users/$fileName").writeBytes(fileBytes)
                                }
                            }

                            else -> {}
                        }
                        part.dispose()
                    }

                    val user = kotlinx.serialization.json.Json.decodeFromString(User.serializer(), userJson)
                    val userWithImage = user.copy(user_ph_content = fileName)

                    userRepository.addUser(userWithImage)
                    call.respond(HttpStatusCode.Created, "Usuário adicionado com sucesso!")
                } catch (ex: Exception) {
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        "Erro inesperado: ${ex.message ?: "Entre em contato com o suporte."}"
                    )
                }
            }

            put("/{id}/access") {
                val id = call.parameters["id"]

                val user = userRepository.userById(id!!.toInt()) ?: throw NotFoundException(
                    "User with ID $id not found"
                )

                userRepository.changeUserAccess(user)

                val updatedUser = userRepository.userById(id.toInt())

                call.respond(updatedUser as User)
            }

            delete("/{id}") {
                val id = call.parameters["id"]
                val removed = userRepository.removeUser(id!!.toInt())

                if (removed) call.respond(HttpStatusCode.OK)
                else throw NotFoundException("User with ID $id not found")
            }
        }
    }

}