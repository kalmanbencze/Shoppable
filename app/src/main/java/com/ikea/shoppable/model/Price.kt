package com.ikea.shoppable.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ikea.shoppable.view.common.format

@Entity
class Price(
    @ColumnInfo(name = "value")
    val value: Double,
    @ColumnInfo(name = "currency")
    val currency: String
) {
    override fun toString(): String {

        return "${value.format(2)} $currency"
    }
}