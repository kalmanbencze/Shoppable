package com.ikea.shoppable.persistence

import com.ikea.shoppable.model.Product
import com.ikea.shoppable.persistence.db.ProductDao
import io.reactivex.Completable
import io.reactivex.Observable

class ProductCacheImplementation(val productDao: ProductDao) : ProductCache {
    override fun getProducts(): Observable<List<Product>> {
        return productDao.getAll()
    }

    override fun getProductCount(): Observable<Int> {
        return productDao.getSize()
    }

    override fun cacheProducts(products: List<Product>): Completable {
        return productDao.insert(products)
    }

    override fun clearCache() {
        productDao.deleteAll()
    }
}