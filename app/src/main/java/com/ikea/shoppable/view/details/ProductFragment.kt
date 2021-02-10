package com.ikea.shoppable.view.details

import android.animation.Animator
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ikea.shoppable.R
import com.ikea.shoppable.persistence.CartRepository
import com.ikea.shoppable.persistence.ProductRepository
import com.ikea.shoppable.view.common.EmptyAnimatorListener
import com.ikea.shoppable.view.common.dpToPx
import com.ikea.shoppable.view.common.loadUrl
import dagger.android.support.DaggerFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


/**
 * a simple fragment visualizing the
 */
class ProductFragment : DaggerFragment() {

    private var offset: Float = 0f
    private lateinit var countLabel: TextView
    private lateinit var addToCart: FloatingActionButton

    //    private lateinit var increase: FloatingActionButton
//    private lateinit var decrease: FloatingActionButton
    private val TAG: String = javaClass.simpleName
    private val compositeDisposable = CompositeDisposable()
    private lateinit var name: TextView
    private lateinit var info: TextView
    private lateinit var type: TextView
    private lateinit var price: TextView
    private lateinit var photo: ImageView

    @Inject
    lateinit var cart: CartRepository

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
        info = view.findViewById(R.id.tv_info)
        type = view.findViewById(R.id.tv_type)
        price = view.findViewById(R.id.tv_price)
        addToCart = view.findViewById(R.id.btn_add_to_cart)
        countLabel = view.findViewById(R.id.tv_count)


        offset = dpToPx(100f)
        initialisePositionsForButtons()
        val argument = arguments?.getString(KEY_ID)
        argument?.let { id ->
            compositeDisposable.add(
                repository.getProduct(id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { it ->
                        (activity as AppCompatActivity?)?.supportActionBar?.title = it.name
                        name.text = it.name
                        photo.loadUrl(it.imageUrl)
                        @SuppressLint("SetTextI18n")
                        info.text = "${it.info}"
                        type.text = it.type.name
                        price.text = it.price.toString()
                    }
            )
            addToCart.setOnClickListener {
                compositeDisposable.add(
                    cart.add(id, 1)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe()
                )
            }
            cart.getItem(id).observeOn(AndroidSchedulers.mainThread()).subscribe {
                if (it.items.isNotEmpty()) {
                    showCountControls()
                    countLabel.text = "${it.items.size}"
                } else {
                    hideCountControls()
                }
            }
        }

    }

    private fun initialisePositionsForButtons() {
        countLabel.animate()
            .scaleX(0f)
            .scaleY(0f)
            .setDuration(0)
            .setListener(object : EmptyAnimatorListener() {
                override fun onAnimationEnd(animation: Animator?) {
                    countLabel.visibility = View.VISIBLE
                }
            })
            .start()
    }

    private fun showCountControls() {
        countLabel.animate()
            .scaleX(1f)
            .scaleY(1f)
            .setInterpolator(DecelerateInterpolator())
            .setDuration(300)
            .start()
    }

    private fun hideCountControls() {
        countLabel.animate()
            .scaleX(0f)
            .scaleY(0f)
            .setDuration(300)
            .start()
    }

    companion object {
        const val KEY_ID = "PRODUCT_ID"
    }
}