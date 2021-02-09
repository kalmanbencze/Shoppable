package com.ikea.shoppable.persistence

import com.ikea.shoppable.model.CartItem
import com.ikea.shoppable.model.CartItemProduct
import com.ikea.shoppable.model.Product
import com.ikea.shoppable.persistence.db.CartDao
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class CartRepositoryImpl(val cartDao: CartDao) : CartRepository {
    override fun addToCart(productId: String, count: Int): Completable {
        return cartDao.insert(CartItem(productId, count))
            .subscribeOn(Schedulers.io())
    }

    override fun removeFromCart(productId: String): Completable {
        return cartDao.remove(productId)
            .subscribeOn(Schedulers.io())
    }

    override fun clearCart(): Completable {
        return cartDao.deleteAll()
            .subscribeOn(Schedulers.io())
    }

    override fun getItems(): Observable<List<CartItemProduct>> {
        return cartDao.getAll()
            .map { list ->
                list.sortedWith(Comparator { o1, o2 ->
                    if (o1.items.isEmpty()) {
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
}