package com.musicstore.repositories.category

import com.musicstore.mapping.CategoryDAO
import com.musicstore.mapping.CategoryTable
import com.musicstore.mapping.daoToModel
import com.musicstore.model.Category
import com.musicstore.plugins.suspendTransaction
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class PostgresCategoryRepository : CategoryRepository {
    override suspend fun allCategories(): List<Category> = suspendTransaction {
        CategoryDAO.all().map(::daoToModel)
    }

    override suspend fun categoryById(id: Int): Category? = suspendTransaction {
        CategoryDAO.find { CategoryTable.id eq id }.limit(1).map(::daoToModel).firstOrNull()
    }

    override suspend fun addCategory(category: Category): Unit = suspendTransaction {
        val parentCategoryDAO = category.id_category_parent?.let {
            CategoryDAO.findById(it) ?: throw IllegalArgumentException("Parent category not found")
        }
        

        CategoryDAO.new {
            id_category_parent = parentCategoryDAO
            category_name = category.category_name
            category_ph_content = category.category_ph_content
        }
    }

    override suspend fun updateCategoryById(id: Int, category: Category): Unit = suspendTransaction {
        CategoryDAO.findByIdAndUpdate(id) { entity ->
            category.id_category_parent?.let { entity.id_category_parent = CategoryDAO.findById(it) }
            category.category_name.let { entity.category_name = it }
            category.category_ph_content.let { entity.category_ph_content = it }
        }
    }

    override suspend fun removeCategoryById(id: Int): Boolean = suspendTransaction {
        val rowsDeleted = CategoryTable.deleteWhere {
            CategoryTable.id eq id
        }
        rowsDeleted == 1
    }
}