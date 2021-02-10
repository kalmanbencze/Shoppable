package com.ikea.shoppable.view.list

import androidx.lifecycle.ViewModel
import com.ikea.shoppable.model.Product
import com.ikea.shoppable.persistence.CartRepository
import com.ikea.shoppable.persistence.ProductRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class ListViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val productRepository: ProductRepository
) : ViewModel() {
    lateinit var id: String

    fun getProducts(): Observable<List<Product>> {
        return productRepository.getProducts()
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun addProduct(product: Product): Completable {
        return cartRepository.add(product.id, 1)
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun removeProduct(id: String): Completable {
        return cartRepository.remove(id)
            .observeOn(AndroidSchedulers.mainThread())
    }
}