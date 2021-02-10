package com.ikea.shoppable.view.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.ikea.shoppable.R
import com.ikea.shoppable.model.Product
import com.ikea.shoppable.view.common.ViewModelProviderFactory
import com.ikea.shoppable.view.details.ProductFragment
import dagger.android.support.DaggerFragment
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * a simple fragment holding the views for the list of products
 */
class ListFragment : DaggerFragment() {

    private var adapter: ListAdapter? = null
    private lateinit var viewModel: ListViewModel
    private var savedView: View? = null

    private val compositeDisposable = CompositeDisposable()
    private var productList: RecyclerView? = null

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (savedView == null) {
            savedView = inflater.inflate(R.layout.fragment_list, container, false)
        }
        viewModel = ViewModelProvider(this, providerFactory)[ListViewModel::class.java]
        return savedView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (productList == null) {
            initialiseList()
        }
        compositeDisposable.add(viewModel.getProducts().subscribe { adapter?.items = it })
    }

    private fun initialiseList() {
        productList = view as RecyclerView
        productList!!.layoutManager = LinearLayoutManager(context)
        adapter = ListAdapter(object : ListAdapter.OnItemClickListener {
            override fun onItemClicked(item: Product) {
                openProductScreen(item)
            }
        }, object : ListAdapter.OnAddClickListener {
            override fun onAddClicked(item: Product) {
                compositeDisposable.add(viewModel.addProduct(item).subscribe { addSuccessful(item) })
            }
        })
        productList!!.adapter = adapter
    }

    private fun openProductScreen(item: Product) {
        val args = Bundle()
        args.putString(ProductFragment.KEY_ID, item.id)
        findNavController().navigate(R.id.action_open_product, args)
    }

    private fun addSuccessful(product: Product) {
        Snackbar.make(requireView(), getString(R.string.label_add_successful), Snackbar.LENGTH_SHORT)
            .setDuration(1000)
            .setAction(getString(R.string.action_undo)) {
                compositeDisposable.add(viewModel.removeProduct(product.id).subscribe())
            }.show()
    }

    override fun onStop() {
        compositeDisposable.clear()
        super.onStop()
    }
}