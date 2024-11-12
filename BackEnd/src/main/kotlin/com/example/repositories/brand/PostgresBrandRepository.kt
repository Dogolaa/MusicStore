package com.example.repositories.brand

import com.example.mapping.BrandDAO
import com.example.mapping.Brands
import com.example.mapping.daoToModel
import com.example.model.Brand
import com.example.plugins.suspendTransaction

class PostgresBrandRepository : BrandRepository {
    override suspend fun allBrands(): List<Brand> = suspendTransaction {
        BrandDAO.all().map(::daoToModel)
    }

    override suspend fun brandById(id: Int): Brand? = suspendTransaction {
        BrandDAO.find { Brands.id eq id }.limit(1).map(::daoToModel).firstOrNull()
    }
}