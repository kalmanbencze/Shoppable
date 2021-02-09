package com.ikea.shoppable.view.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.ikea.shoppable.R
import com.ikea.shoppable.model.Product
import com.ikea.shoppable.persistence.CartRepository
import com.ikea.shoppable.persistence.ProductRepository
import com.ikea.shoppable.view.details.ProductFragment
import dagger.android.support.DaggerFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * a simple fragment holding the views for the list of products
 */
class ListFragment : DaggerFragment() {

    private var savedView: View? = null
    private val TAG: String = javaClass.simpleName

    private val compositeDisposable = CompositeDisposable()
    private var productList: RecyclerView? = null

    @Inject
    lateinit var repository: ProductRepository

    @Inject
    lateinit var cart: CartRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //todo use viewmodel
        if (savedView == null) {
            savedView = inflater.inflate(R.layout.fragment_list, container, false)
        }
        return savedView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (productList == null) {
            productList = view as RecyclerView
            productList!!.layoutManager = LinearLayoutManager(context)
            val adapter = ProductsListAdapter(object : ProductsListAdapter.OnItemClickListener {
                override fun onItemClicked(item: Product) {

                    val args = Bundle()
                    args.putString(ProductFragment.KEY_ID, item.id)
                    findNavController().navigate(R.id.action_open_product, args)
                }

            }, object : ProductsListAdapter.OnAddClickListener {
                override fun onAddClicked(item: Product) {
                    compositeDisposable.add(
                        cart.add(item.id, 1)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                Snackbar.make(view, getString(R.string.label_add_successful), Snackbar.LENGTH_SHORT)
                                    .setDuration(1000)
                                    .setAction(getString(R.string.action_undo)) {
                                        cart.remove(item.id).subscribe({
                                            Log.d(TAG, "onAddClicked: undone add to cart.")
                                        }, {
                                            Log.e(TAG, "onAddClicked: ", it)
                                        })
                                    }.show()
                            }, {
                                Log.e(TAG, "onAddClicked: ", it)
                            })
                    )
                }

            })
            productList!!.adapter = adapter

            compositeDisposable.add(repository.getProducts().observeOn(AndroidSchedulers.mainThread()).subscribe({
                adapter.items = it
            }, {
                Log.e(TAG, "onViewCreated: ", it)
            }))
        }
    }

    override fun onStop() {
        compositeDisposable.clear()
        super.onStop()
    }
}