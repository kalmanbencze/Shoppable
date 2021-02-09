package com.ikea.shoppable.view.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.ikea.shoppable.R
import com.ikea.shoppable.model.Product
import com.ikea.shoppable.view.common.AutoUpdatableAdapter
import com.ikea.shoppable.view.common.inflate
import com.ikea.shoppable.view.common.loadUrlToCircle
import kotlin.properties.Delegates

class ProductsListAdapter(
    private val itemClickListener: OnItemClickListener,
    private val addClickListener: OnAddClickListener
) :
    RecyclerView.Adapter<ProductsListAdapter.ProductViewHolder>(), AutoUpdatableAdapter {

    var items: List<Product> by Delegates.observable(emptyList()) { _, old, new ->
        autoNotify(old, new) { o, n -> o.id == n.id }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(parent.inflate(R.layout.item_product))
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(items[position], itemClickListener, addClickListener)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    interface OnItemClickListener {
        fun onItemClicked(item: Product)
    }

    interface OnAddClickListener {
        fun onAddClicked(item: Product)
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(product: Product, itemClickListener: OnItemClickListener, addClickListener: OnAddClickListener) =
            with(itemView) {
                itemView.findViewById<View>(R.id.cl_row_container)
                    .setOnClickListener { itemClickListener.onItemClicked(product) }

                itemView.findViewById<ImageView>(R.id.civ_photo).loadUrlToCircle(product.imageUrl)

                itemView.findViewById<TextView>(R.id.tv_name).text = product.name
                itemView.findViewById<TextView>(R.id.tv_description).text = product.info.toString()
                itemView.findViewById<TextView>(R.id.tv_price).text = product.price.toString()
                itemView.findViewById<ImageView>(R.id.iv_add_to_cart).setOnClickListener {
                    addClickListener.onAddClicked(product)
                }
        }
    }
}