package com.ikea.shoppable.view.cart

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ikea.shoppable.R
import com.ikea.shoppable.model.CartItemProduct
import com.ikea.shoppable.persistence.CartRepository
import dagger.android.support.DaggerFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * a simple fragment holding the views for the list of products
 */
class CartFragment : DaggerFragment() {

    private val compositeDisposable = CompositeDisposable()
    private lateinit var productList: RecyclerView

    @Inject
    lateinit var cart: CartRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productList = view as RecyclerView
        productList.layoutManager = LinearLayoutManager(context)
        val adapter = CartAdapter(object : CartAdapter.OnRemoveClickListener {
            override fun onItemClicked(item: CartItemProduct) {
                compositeDisposable.add(
                    cart.removeFromCart(item.product.id)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            Log.d(javaClass.simpleName, "onItemClicked: removed element from cart")
                        }, {
                            Log.e(javaClass.simpleName, "onItemClicked: ", it)
                        })
                )
            }

        })
        productList.adapter = adapter

        compositeDisposable.add(cart.getItems().observeOn(AndroidSchedulers.mainThread()).subscribe({
            adapter.items = it
        }, {
            Log.e(javaClass.simpleName, "onViewCreated: ", it)
        }))
    }

    override fun onStop() {
        compositeDisposable.clear()
        super.onStop()
    }
}