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
    
    on(AuthenticationChecked) { call ->
        val token = call.principal<JWTPrincipal>()
        val userRole = token!!.payload.getClaim("user_role").asList(String::class.java)

        if (!userRole.any { it in roles })
            if (!userRole.any { it == "ADMIN" })
                throw InsufficientPermissionException("You don't have the right role for this method. Your role: $userRole. Required role: $roles")
    }

}

class PluginConfiguration {
    lateinit var roles: List<String>
}