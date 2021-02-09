package com.ikea.shoppable.persistence

import com.ikea.shoppable.model.CartItemProduct
import io.reactivex.Completable
import io.reactivex.Observable

interface CartRepository {
    fun addToCart(productId: String, count: Int): Completable
    fun removeFromCart(productId: String): Completable
    fun clearCart(): Completable
    fun getItems(): Observable<List<CartItemProduct>>
    fun getSize(): Observable<Int>
}