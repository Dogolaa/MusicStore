package com.musicstore.repositories.brand

import com.musicstore.model.Brand

interface BrandRepository {
    suspend fun allBrands(): List<Brand>
    suspend fun brandById(id: Int): Brand?
}