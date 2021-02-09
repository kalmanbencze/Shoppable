package com.ikea.shoppable.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "Cart",
    foreignKeys = [ForeignKey(
        entity = Product::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("productId"),
        onDelete = ForeignKey.CASCADE
    )]
)
class CartItem(
    @ColumnInfo(name = "productId")
    val productId: String,
    @ColumnInfo(name = "count")
    val count: Int,
    @ColumnInfo(name = "date")
    val date: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
)