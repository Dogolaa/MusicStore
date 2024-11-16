package com.musicstore.repositories.brand

import com.musicstore.mapping.BrandDAO
import com.musicstore.mapping.Brands
import com.musicstore.mapping.daoToModel
import com.musicstore.model.Brand
import com.musicstore.plugins.suspendTransaction

class PostgresBrandRepository : BrandRepository {
    override suspend fun allBrands(): List<Brand> = suspendTransaction {
        BrandDAO.all().map(::daoToModel)
    }

    override suspend fun brandById(id: Int): Brand? = suspendTransaction {
        BrandDAO.find { Brands.id eq id }.limit(1).map(::daoToModel).firstOrNull()
    }
}