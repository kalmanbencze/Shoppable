package com.ikea.shoppable.view.cart

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
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

    private val TAG: String = javaClass.simpleName
    private lateinit var total: TextView
    private lateinit var sendButton: Button
    private lateinit var clearButton: Button
    private val compositeDisposable = CompositeDisposable()
    private lateinit var cartItemList: RecyclerView

    @Inject
    lateinit var cartRepository: CartRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cartItemList = view.findViewById(R.id.rv_cart_items)
        total = view.findViewById(R.id.tv_total)
        sendButton = view.findViewById(R.id.btn_checkout)

//        clearButton.setOnClickListener {
//            compositeDisposable.add(cart.clearCart().subscribe({
//                Log.d(TAG, "onViewCreated: successfully cleared cart")
//            }, {
//                Log.e(TAG, "onViewCreated: ", it)
//            }))
//        }
        sendButton.setOnClickListener {
            compositeDisposable.add(
                cartRepository.clear()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        Log.d(TAG, "onViewCreated: successfully cleared cart")
                        Snackbar.make(view, getString(R.string.message_order_sent), Snackbar.LENGTH_LONG).show()
                        findNavController().popBackStack()
                    }, {
                        Log.e(TAG, "onViewCreated: ", it)
                    })
            )
        }
        compositeDisposable.add(
            cartRepository.getTotal()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    total.text = getString(R.string.label_total, it.toString())
                }, {

                })
        )
        cartItemList.layoutManager = LinearLayoutManager(context)
        val adapter = CartAdapter(object : CartAdapter.OnRemoveClickListener {
            override fun onDeletePressed(item: CartItemProduct) {
                compositeDisposable.add(
                    cartRepository.remove(item.product.id)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            Log.d(TAG, "onItemClicked: removed element from cart")
                        }, {
                            Log.e(TAG, "onItemClicked: ", it)
                        })
                )
            }

            override fun onItemSwiped(item: CartItemProduct) {
                compositeDisposable.add(
                    cartRepository.removeAll(item.product.id)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            Log.d(TAG, "onItemClicked: removed product from cart")
                        }, {
                            Log.e(TAG, "onItemClicked: ", it)
                        })
                )
            }

        })
        cartItemList.adapter = adapter
        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(adapter))
        itemTouchHelper.attachToRecyclerView(cartItemList)

        compositeDisposable.add(cartRepository.getItems().observeOn(AndroidSchedulers.mainThread()).subscribe({
            val new = arrayListOf<CartItemProduct>()
            new.addAll(it)
            adapter.items = new
        }, {
            Log.e(TAG, "onViewCreated: ", it)
        }))
    }

    override fun onStop() {
        compositeDisposable.clear()
        super.onStop()
    }
}