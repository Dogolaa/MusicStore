package com.musicstore.repositories.brand

import com.musicstore.mapping.BrandCategoryDAO
import com.musicstore.mapping.BrandCategoryTable
import com.musicstore.mapping.BrandDAO
import com.musicstore.mapping.BrandTable
import com.musicstore.mapping.CategoryTable
import com.musicstore.mapping.daoToModel
import com.musicstore.model.Brand
import com.musicstore.model.request.UpdateBrand
import com.musicstore.plugins.suspendTransaction
import com.musicstore.repositories.category.CategoryRepository
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere

class PostgresBrandRepository(
    private val categoryRepository: CategoryRepository
) : BrandRepository {
    override suspend fun allBrands(): List<Brand> = suspendTransaction {
        BrandDAO.all().map(::daoToModel)
    }

    override suspend fun addBrand(brand: Brand): Unit = suspendTransaction {
        BrandDAO.new {
            brand_name = brand.brand_name
            brand_ph_content = brand.brand_ph_content
        }
    }

    override suspend fun brandById(id: Int): Brand? = suspendTransaction {
        BrandDAO.find { BrandTable.id eq id }.limit(1).map(::daoToModel).firstOrNull()
    }

    override suspend fun updateBrandById(id: Int, brand: UpdateBrand): Unit = suspendTransaction {
        BrandDAO.findByIdAndUpdate(id) { entity ->
            brand.brand_name?.let { entity.brand_name = it }
            brand.brand_ph_content?.let { entity.brand_ph_content = it }
        }
    }

    override suspend fun removeBrandById(id: Int): Boolean = suspendTransaction {
        try {
            val rowsDeleted = BrandTable.deleteWhere {
                BrandTable.id eq id
            }
            rowsDeleted == 1
        } catch (e: ExposedSQLException) {
            if (e.message?.contains("violates foreign key constraint") == true) {
                throw Exception("Cannot delete brand. It is referenced by existing products")
            } else {
                throw e
            }
        }
    }

    override suspend fun addCategoryToBrand(brandId: Int, categoryId: Int): Unit = suspendTransaction {
        runBlocking {
            categoryRepository.categoryById(categoryId) ?: throw IllegalArgumentException("Category not found")
            brandById(brandId) ?: throw IllegalArgumentException("Brand not found")
        }

        BrandCategoryDAO.new {
            id_brand = EntityID(brandId, BrandTable)
            id_category = EntityID(categoryId, CategoryTable)
        }
    }

    override suspend fun removeCategoryFromBrand(brandId: Int, categoryId: Int): Unit = suspendTransaction {
        runBlocking {
            categoryRepository.categoryById(categoryId) ?: throw IllegalArgumentException("Category not found")
            brandById(brandId) ?: throw IllegalArgumentException("Brand not found")
        }

        BrandCategoryDAO.find {
            BrandCategoryTable.id_brand eq brandId and (BrandCategoryTable.id_category eq categoryId)
        }.firstOrNull()?.delete()

    }
}