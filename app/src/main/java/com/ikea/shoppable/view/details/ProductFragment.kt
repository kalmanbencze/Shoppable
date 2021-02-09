package com.ikea.shoppable.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.ikea.shoppable.R
import com.ikea.shoppable.persistence.ProductRepository
import com.ikea.shoppable.view.common.loadUrl
import dagger.android.support.DaggerFragment
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * a simple fragment visualizing the
 */
class ProductFragment : DaggerFragment() {

    private val compositeDisposable = CompositeDisposable()
    private lateinit var name: TextView
    private lateinit var info: TextView
    private lateinit var photo: ImageView

    @Inject
    lateinit var repository: ProductRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        photo = view.findViewById(R.id.iv_photo)
        name = view.findViewById(R.id.tv_name)
        name = view.findViewById(R.id.tv_info)

        val id = arguments?.getString(KEY_ID)
        id?.let {
            compositeDisposable.add(repository.getProduct(id).subscribe({
                name.text = it.name
                photo.loadUrl(it.imageUrl)
                info.text = (it.info.toString())
            }, {

            }))
        }

    }

    companion object {
        const val KEY_ID = "PRODUCT_ID"
    }
}