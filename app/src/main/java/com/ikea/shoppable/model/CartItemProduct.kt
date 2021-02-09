package com.ikea.shoppable.model

import androidx.room.Embedded
import androidx.room.Relation

data class CartItemProduct(
    @Embedded
    val product: Product,
    @Relation(
        parentColumn = "id",
        entityColumn = "productId"
    )
    val items: List<CartItem>
)