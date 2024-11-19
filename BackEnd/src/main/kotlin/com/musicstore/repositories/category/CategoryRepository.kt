package com.musicstore.repositories.category

import com.musicstore.model.Category

interface CategoryRepository {
    suspend fun allCategories(): List<Category>
    suspend fun categoryById(id: Int): Category?
    suspend fun addCategory(category: Category)
    suspend fun updateCategoryById(id: Int, category: Category)
    suspend fun removeCategoryById(id: Int): Boolean
}