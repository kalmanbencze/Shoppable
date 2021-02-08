package com.ikea.shoppable.persistence

import com.ikea.shoppable.model.Product
import io.reactivex.Completable
import io.reactivex.Observable

interface ProductRepository {
    fun getProducts(): Observable<List<Product>>
    fun getProductCount(): Observable<Int>
    fun cacheProducts(products: List<Product>): Completable
    fun clearCache(): Completable
}