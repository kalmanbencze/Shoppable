package com.ikea.shoppable.view

import androidx.lifecycle.ViewModel
import com.ikea.shoppable.persistence.CartRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class AppBarViewModel @Inject constructor(private val cartRepository: CartRepository) : ViewModel() {

    fun getCartSize(): Observable<Int> {
        return cartRepository.getSize().observeOn(AndroidSchedulers.mainThread())
    }
}