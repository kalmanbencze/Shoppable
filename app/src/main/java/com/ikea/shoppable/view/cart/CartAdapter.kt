package com.ikea.shoppable.view.cart

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.BaseTransientBottomBar.BaseCallback
import com.google.android.material.snackbar.Snackbar
import com.ikea.shoppable.R
import com.ikea.shoppable.model.CartItemProduct
import com.ikea.shoppable.view.common.AutoUpdatableAdapter
import com.ikea.shoppable.view.common.formatTo2Decimals
import com.ikea.shoppable.view.common.inflate
import com.ikea.shoppable.view.common.loadUrlToCircle
import kotlin.properties.Delegates


class CartAdapter(private val removeClickListener: OnRemoveClickListener) :
    RecyclerView.Adapter<CartAdapter.ProductViewHolder>(), AutoUpdatableAdapter {

    private var recentlyDeletedItemPosition: Int = -1
    private var recentlyDeletedItem: CartItemProduct? = null
    var items: ArrayList<CartItemProduct> by Delegates.observable(arrayListOf()) { _, old, new ->
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

    fun deleteItem(viewHolder: RecyclerView.ViewHolder, position: Int) {
        recentlyDeletedItem = items[position]
        recentlyDeletedItemPosition = position
        items.removeAt(position)
        notifyItemRemoved(position)
        showUndoSnackbar(viewHolder)
    }


    private fun showUndoSnackbar(viewHolder: RecyclerView.ViewHolder) {
        val snackbar: Snackbar = Snackbar.make(
            viewHolder.itemView, R.string.item_deleted_message,
            Snackbar.LENGTH_LONG
        )

        snackbar.setAction(R.string.action_undo) { undoDelete() }
        snackbar.addCallback(object : Snackbar.Callback() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                super.onDismissed(transientBottomBar, event)
                //we ignore in case it was undone
                if (event != BaseCallback.DISMISS_EVENT_ACTION) {
                    recentlyDeletedItem?.let {
                        removeClickListener.onItemSwiped(it)
                    }
                    recentlyDeletedItem = null
                    recentlyDeletedItemPosition = -1
                }
            }
        })
        snackbar.show()
    }

    private fun undoDelete() {
        recentlyDeletedItem?.let {
            items.add(
                recentlyDeletedItemPosition,
                it
            )
            notifyItemInserted(recentlyDeletedItemPosition)
        }
    }

    interface OnRemoveClickListener {
        fun onDeletePressed(item: CartItemProduct)
        fun onItemSwiped(item: CartItemProduct)
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        fun bind(item: CartItemProduct, removeClickListener: OnRemoveClickListener) = with(itemView) {
            itemView.findViewById<View>(R.id.iv_remove_from_cart)
                .setOnClickListener { removeClickListener.onDeletePressed(item) }

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
