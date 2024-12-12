package com.musicstore.repositories.brand

import com.musicstore.model.Brand
import com.musicstore.model.request.UpdateBrand

interface BrandRepository {
    suspend fun allBrands(): List<Brand>
    suspend fun addBrand(brand: Brand)
    suspend fun brandById(id: Int): Brand?
    suspend fun updateBrandById(id: Int, brand: UpdateBrand)
    suspend fun removeBrandById(id: Int): Boolean

    suspend fun addCategoryToBrand(brandId: Int, categoryId: Int)
    suspend fun removeCategoryFromBrand(brandId: Int, categoryId: Int)
}