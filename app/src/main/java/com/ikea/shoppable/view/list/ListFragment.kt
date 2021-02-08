package com.ikea.shoppable.view.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ikea.shoppable.R
import com.ikea.shoppable.model.Product
import com.ikea.shoppable.persistence.ProductRepository
import com.ikea.shoppable.view.common.ProductsListAdapter
import com.ikea.shoppable.view.details.ProductFragment
import dagger.android.support.DaggerFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * a simple fragment holding the views for the list of products
 */
class ListFragment : DaggerFragment() {

    private val compositeDisposable = CompositeDisposable()
    private lateinit var productList: RecyclerView

    @Inject
    lateinit var repository: ProductRepository

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
        val adapter = ProductsListAdapter(object : ProductsListAdapter.OnItemClickListener {
            override fun onItemClicked(item: Product) {

                val args = Bundle()
                args.putString(ProductFragment.KEY_ID, item.id)
                findNavController().navigate(R.id.action_open_product, args)
            }

        })
        productList.adapter = adapter

        compositeDisposable.add(repository.getProducts().observeOn(AndroidSchedulers.mainThread()).subscribe({
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