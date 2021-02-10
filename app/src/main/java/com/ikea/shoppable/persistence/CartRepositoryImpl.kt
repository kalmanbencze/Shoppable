package com.ikea.shoppable.persistence

import com.ikea.shoppable.model.CartItem
import com.ikea.shoppable.model.CartItemProduct
import com.ikea.shoppable.model.Price
import com.ikea.shoppable.persistence.db.CartDao
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class CartRepositoryImpl(private val cartDao: CartDao) : CartRepository {
    override fun add(productId: String, count: Int): Completable {
        return cartDao.insert(CartItem(productId, count))
            .subscribeOn(Schedulers.io())
    }

    override fun remove(productId: String): Completable {
        return cartDao.remove(productId)
            .subscribeOn(Schedulers.io())
    }

    override fun removeAll(productId: String): Completable {
        return cartDao.removeAll(productId)
            .subscribeOn(Schedulers.io())
    }

    override fun clear(): Completable {
        return cartDao.deleteAll()
            .subscribeOn(Schedulers.io())
    }

    override fun getItems(): Observable<List<CartItemProduct>> {
        return cartDao.getAll()
            .subscribeOn(Schedulers.io())
            .map { list -> list.filter { it.items.isNotEmpty() } }
    }

    override fun getSize(): Observable<Int> {
        return cartDao.getSize()
            .subscribeOn(Schedulers.io())
    }

    override fun getProductCount(id: String): Observable<Int> {
        return cartDao.getProductCount(id)
            .subscribeOn(Schedulers.io())
    }

    override fun getTotal(): Observable<Price> {
        return cartDao.getTotalValue()
            .subscribeOn(Schedulers.io())
    }

    override fun getItem(id: String): Observable<CartItemProduct> {
        return cartDao.getByProductId(id)
            .subscribeOn(Schedulers.io())
    }
}