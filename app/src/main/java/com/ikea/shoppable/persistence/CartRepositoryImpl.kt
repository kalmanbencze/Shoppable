package com.ikea.shoppable.persistence

import com.ikea.shoppable.model.CartItem
import com.ikea.shoppable.model.CartItemProduct
import com.ikea.shoppable.model.Price
import com.ikea.shoppable.persistence.db.CartDao
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class CartRepositoryImpl(val cartDao: CartDao) : CartRepository {
    override fun add(productId: String, count: Int): Completable {
        return cartDao.insert(CartItem(productId, count))
            .subscribeOn(Schedulers.io())
    }

    override fun remove(productId: String): Completable {
        return cartDao.remove(productId)
            .subscribeOn(Schedulers.io())
    }

    override fun clear(): Completable {
        return cartDao.deleteAll()
            .subscribeOn(Schedulers.io())
    }

    override fun getItems(): Observable<List<CartItemProduct>> {
        return cartDao.getAll()
            .map { list ->
                //sorting the list of entries based on the latest addition in their "cart item" list attached
                list.sortedWith(Comparator { o1, o2 ->
                    if (o1.items.isEmpty() || o2.items.isEmpty()) {
                        return@Comparator -1
                    }
                    return@Comparator (o1.items.maxOf { it.date } - o2.items.maxOf { it.date }).toInt()
                })
            }
            .subscribeOn(Schedulers.io())
            .map { list -> list.filter { it.items.isNotEmpty() } }
    }

    override fun getSize(): Observable<Int> {
        return cartDao.getSize()
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