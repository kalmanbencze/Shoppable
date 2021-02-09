package com.ikea.shoppable.view.cart

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ikea.shoppable.R
import com.ikea.shoppable.model.CartItemProduct
import com.ikea.shoppable.view.common.AutoUpdatableAdapter
import com.ikea.shoppable.view.common.formatTo2Decimals
import com.ikea.shoppable.view.common.inflate
import com.ikea.shoppable.view.common.loadUrlToCircle
import kotlin.properties.Delegates

class CartAdapter(private val removeClickListener: OnRemoveClickListener) :
    RecyclerView.Adapter<CartAdapter.ProductViewHolder>(), AutoUpdatableAdapter {

    var items: List<CartItemProduct> by Delegates.observable(emptyList()) { _, old, new ->
        autoNotify(old, new) { o, n -> o.product.id == n.product.id }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(parent.inflate(R.layout.item_cart))
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(items[position], removeClickListener)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    interface OnRemoveClickListener {
        fun onItemClicked(item: CartItemProduct)
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        fun bind(item: CartItemProduct, removeClickListener: OnRemoveClickListener) = with(itemView) {
            itemView.findViewById<View>(R.id.iv_remove_from_cart)
                .setOnClickListener { removeClickListener.onItemClicked(item) }

            itemView.findViewById<ImageView>(R.id.civ_photo).loadUrlToCircle(item.product.imageUrl)

            itemView.findViewById<TextView>(R.id.tv_name).text = item.product.name
            itemView.findViewById<TextView>(R.id.tv_count).text = "${item.items.size}x"
            itemView.findViewById<TextView>(R.id.tv_price).text = "${formatPrice(item)} ${item.product.price.currency}"


        }

        private fun formatPrice(item: CartItemProduct): String {
            return (item.product.price.value * item.items.size).formatTo2Decimals()
        }
    }
}
