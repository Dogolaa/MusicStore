package com.musicstore.repositories.role

import com.musicstore.mapping.RoleTable
import com.musicstore.mapping.UserRoleTable
import com.musicstore.plugins.suspendTransaction

class PostgresRoleRepository : RoleRepository {
    override suspend fun roleByUserId(id: Int): List<String> = suspendTransaction {
        (UserRoleTable innerJoin RoleTable)
            .select(RoleTable.role_name)
            .where { UserRoleTable.id_user eq id }
            .map { it[RoleTable.role_name] }
    }
}