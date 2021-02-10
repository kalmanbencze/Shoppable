package com.ikea.shoppable.view.details

import android.animation.Animator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewPropertyAnimator
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ikea.shoppable.R
import com.ikea.shoppable.view.common.EmptyAnimatorListener
import com.ikea.shoppable.view.common.ViewModelProviderFactory
import com.ikea.shoppable.view.common.loadUrl
import dagger.android.support.DaggerFragment
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


/**
 * a simple fragment visualizing the
 */
class ProductFragment : DaggerFragment() {

    private lateinit var countBadge: TextView
    private lateinit var addToCart: FloatingActionButton

    private val compositeDisposable = CompositeDisposable()
    private lateinit var name: TextView
    private lateinit var info: TextView
    private lateinit var type: TextView
    private lateinit var price: TextView
    private lateinit var photo: ImageView

    private lateinit var viewModel: ProductViewModel

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_product, container, false)
        viewModel = ViewModelProvider(this, providerFactory)[ProductViewModel::class.java]
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        photo = view.findViewById(R.id.iv_photo)
        name = view.findViewById(R.id.tv_name)
        info = view.findViewById(R.id.tv_info)
        type = view.findViewById(R.id.tv_type)
        price = view.findViewById(R.id.tv_price)
        addToCart = view.findViewById(R.id.fab_add_to_cart)
        countBadge = view.findViewById(R.id.tv_count)
        hideBadgeInstant()

        val argument = arguments?.getString(KEY_ID)
        argument?.let { id ->
            connectViews(id)
            enableActions(id)
        }

    }

    private fun enableActions(id: String) {
        addToCart.setOnClickListener {
            compositeDisposable.add(viewModel.addToCart(id, 1).subscribe())
        }
    }

    private fun connectViews(id: String) {
        viewModel.id = id
        compositeDisposable.addAll(
            viewModel.getName().subscribe {
                name.text = it
                (activity as AppCompatActivity?)?.supportActionBar?.title = it
            },
            viewModel.getInfo().subscribe { info.text = it },
            viewModel.getType().subscribe { type.text = getString(R.string.label_category, it) },
            viewModel.getPrice().subscribe { price.text = it },
            viewModel.getPhoto().subscribe { photo.loadUrl(it) },
            viewModel.getNumberOfProductInCart().subscribe {
                if (it > 0) {
                    countBadge.text = "$it"
                    showBadge()
                } else {
                    hideBadge()
                }
            }
        )
    }

    private fun hideBadgeInstant() = animateBadge(0f, 0)
        .setListener(object : EmptyAnimatorListener() {
            override fun onAnimationEnd(animation: Animator?) {
                countBadge.visibility = View.VISIBLE
            }
        })

    private fun showBadge() = animateBadge(1f, 300).start()

    private fun hideBadge() = animateBadge(0f, 300).start()

    private fun animateBadge(scale: Float, duration: Long): ViewPropertyAnimator {
        return countBadge.animate()
            .scaleX(scale)
            .scaleY(scale)
            .setInterpolator(DecelerateInterpolator())
            .setDuration(duration)
    }

    companion object {
        const val KEY_ID = "PRODUCT_ID"
    }
}