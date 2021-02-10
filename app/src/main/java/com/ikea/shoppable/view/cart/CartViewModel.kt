package com.ikea.shoppable.view.cart

import androidx.lifecycle.ViewModel
import com.ikea.shoppable.model.CartItemProduct
import com.ikea.shoppable.persistence.CartRepository
import com.ikea.shoppable.persistence.ProductRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val productRepository: ProductRepository
) : ViewModel() {


    fun getItems(): Observable<List<CartItemProduct>> {
        return cartRepository.getItems()
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun addToCart(id: String, count: Int): Completable {
        return cartRepository.add(id, 1)
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun checkout(): Completable {
        return cartRepository.clear().observeOn(AndroidSchedulers.mainThread())
    }

    fun delete(id: String): Completable {
        return cartRepository.remove(id)
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun deleteAll(id: String): Completable {
        return cartRepository.removeAll(id)
            .observeOn(AndroidSchedulers.mainThread())

    }

    fun getTotal(): Observable<String> {
        return cartRepository.getTotal()
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.toString() }
    }

}