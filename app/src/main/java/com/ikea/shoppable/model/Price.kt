package com.ikea.shoppable.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.ikea.shoppable.view.common.formatTo2Decimals

@Entity
class Price(
    @ColumnInfo(name = "value")
    val value: Double,
    @ColumnInfo(name = "currency")
    val currency: String
) {
    override fun toString(): String {

        return "${value.formatTo2Decimals()} $currency"
    }
}