package com.ikea.shoppable.persistence

import com.ikea.shoppable.model.CartItemProduct
import com.ikea.shoppable.model.Price
import io.reactivex.Completable
import io.reactivex.Observable

interface CartRepository {
    fun add(productId: String, count: Int): Completable
    fun remove(productId: String): Completable
    fun removeAll(productId: String): Completable
    fun clear(): Completable
    fun getItems(): Observable<List<CartItemProduct>>
    fun getSize(): Observable<Int>
    fun getProductCount(id: String): Observable<Int>
    fun getTotal(): Observable<Price>
    fun getItem(id: String): Observable<CartItemProduct>
}