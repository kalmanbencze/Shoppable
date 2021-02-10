package com.ikea.shoppable.view.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.ikea.shoppable.R
import com.ikea.shoppable.model.CartItemProduct
import com.ikea.shoppable.view.common.ViewModelProviderFactory
import dagger.android.support.DaggerFragment
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


/**
 * a simple fragment holding the views for the list of products
 */
class CartFragment : DaggerFragment() {

    private lateinit var adapter: CartAdapter
    private lateinit var viewModel: CartViewModel
    private lateinit var total: TextView
    private lateinit var sendButton: Button
    private val compositeDisposable = CompositeDisposable()
    private lateinit var cartItemList: RecyclerView

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        viewModel = ViewModelProvider(this, providerFactory)[CartViewModel::class.java]
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cartItemList = view.findViewById(R.id.rv_cart_items)
        total = view.findViewById(R.id.tv_total)
        sendButton = view.findViewById(R.id.btn_checkout)

        cartItemList.layoutManager = LinearLayoutManager(context)
        adapter = CartAdapter(object : CartAdapter.OnRemoveClickListener {
            override fun onDeletePressed(item: CartItemProduct) {
                compositeDisposable.add(viewModel.delete(item.product.id).subscribe())
            }

            override fun onItemSwiped(item: CartItemProduct) {
                compositeDisposable.add(viewModel.deleteAll(item.product.id).subscribe())
            }

        })
        cartItemList.adapter = adapter
        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(adapter))
        itemTouchHelper.attachToRecyclerView(cartItemList)

        connectViews()
        setupActions()
    }

    private fun connectViews() {
        compositeDisposable.add(viewModel.getTotal().subscribe { total.text = getString(R.string.label_total, it) })
        compositeDisposable.add(viewModel.getItems().subscribe { adapter.items = it.toMutableList() })
    }

    private fun setupActions() {
        sendButton.setOnClickListener {
            compositeDisposable.add(viewModel.checkout().subscribe {
                Snackbar.make(requireView(), getString(R.string.message_order_sent), Snackbar.LENGTH_LONG).show()
                findNavController().popBackStack()
            })
        }
    }

    override fun onStop() {
        compositeDisposable.clear()
        super.onStop()
    }
}