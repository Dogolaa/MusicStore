package com.example.repositories.brand

import com.example.model.Brand

interface BrandRepository {
    suspend fun allBrands(): List<Brand>
    suspend fun brandById(id: Int): Brand?
}