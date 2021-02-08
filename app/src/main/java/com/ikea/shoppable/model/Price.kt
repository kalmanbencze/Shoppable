package com.ikea.shoppable.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Price(
    @ColumnInfo(name = "value")
    val value: Double,
    @ColumnInfo(name = "currency")
    val currency: String
) {
    override fun toString(): String {

        return "$value $currency"
    }
}