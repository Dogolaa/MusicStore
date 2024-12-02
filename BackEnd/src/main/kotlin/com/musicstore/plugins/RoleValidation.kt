package com.musicstore.plugins

import com.musicstore.exceptions.InsufficientPermissionException
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

val RoleValidation = createRouteScopedPlugin(
    name = "RoleValidationPlugin",
    createConfiguration = ::PluginConfiguration
) {
    val roles = pluginConfig.roles
    println("ROLES: $roles")

    on(AuthenticationChecked) { call ->
        val token = call.principal<JWTPrincipal>()
        val userRole = token!!.payload.getClaim("user_role").asList(String::class.java)
        println("ROLE IN TOKEN: $userRole")

        if (!userRole.any { it in roles })
            throw InsufficientPermissionException("You don't have the right role for this method. Your role: $userRole. Required role: $roles")
    }

}

class PluginConfiguration {
    lateinit var roles: List<String>
}