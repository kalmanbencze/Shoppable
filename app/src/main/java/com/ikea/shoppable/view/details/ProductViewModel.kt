package com.ikea.shoppable.view.details

import androidx.lifecycle.ViewModel
import com.ikea.shoppable.model.Product
import com.ikea.shoppable.persistence.CartRepository
import com.ikea.shoppable.persistence.ProductRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.*
import javax.inject.Inject

class ProductViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val productRepository: ProductRepository
) : ViewModel() {
    lateinit var id: String

    private fun getProduct(): Observable<Product> {
        return productRepository.getProduct(id).observeOn(AndroidSchedulers.mainThread())
    }

    fun getName(): Observable<String> {
        return getProduct().map { it.name }
    }

    fun getInfo(): Observable<String> {
        return getProduct().map { "${it.info}" }
    }

    fun getType(): Observable<String> {
        return getProduct().map { it.type.name.toLowerCase(Locale.getDefault()) }
    }

    fun getPrice(): Observable<String> {
        return getProduct().map { it.price.toString() }
    }

    fun getPhoto(): Observable<String> {
        return getProduct().map { it.imageUrl }
    }

    fun getNumberOfProductInCart(): Observable<Int> {
        return cartRepository.getProductCount(id)
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun addToCart(id: String): Completable {
        return cartRepository.add(id, 1)
            .observeOn(AndroidSchedulers.mainThread())
    }

}