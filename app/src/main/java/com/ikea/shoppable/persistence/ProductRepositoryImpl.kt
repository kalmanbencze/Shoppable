package com.ikea.shoppable.persistence

import com.ikea.shoppable.model.Product
import com.ikea.shoppable.persistence.db.ProductDao
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class ProductRepositoryImpl(val productDao: ProductDao) : ProductRepository {
    override fun getProducts(): Observable<List<Product>> {
        return productDao.getAll()
            .subscribeOn(Schedulers.io())
    }

    override fun getProductCount(): Observable<Int> {
        return productDao.getSize()
            .subscribeOn(Schedulers.io())
    }

    override fun cacheProducts(products: List<Product>): Completable {
        return productDao.insert(products)
            .subscribeOn(Schedulers.io())
    }

    override fun clearCache(): Completable {
        return productDao.deleteAll()
            .subscribeOn(Schedulers.io())
    }

    override fun getProduct(id: String): Single<Product> {
        return productDao.getById(id)
            .subscribeOn(Schedulers.io())
    }
}