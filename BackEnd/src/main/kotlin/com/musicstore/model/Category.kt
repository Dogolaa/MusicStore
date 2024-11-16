package com.musicstore.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.statements.api.ExposedBlob

@Serializable
data class Category(
    val id: Int,
    val id_category_parent: Int? = null,
    val category_name: String,
    @Contextual
    val category_ph_content: ExposedBlob
)
