package com.ikea.shoppable.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity
class Info(
    @ColumnInfo(name = "material")
    val material: String?,
    @ColumnInfo(name = "color")
    val color: String,
    @ColumnInfo(name = "numberOfSeats")
    val numberOfSeats: Int?
) {
    override fun toString(): String {
        val tokens = arrayListOf<String>()
        if (color.isNotEmpty()) {
            tokens.add(" • $color")
        }
        if (material != null && material.isNotEmpty()) {
            tokens.add(material)
        }
        if (numberOfSeats != null && numberOfSeats > 0) {
            tokens.add("$numberOfSeats ${if (numberOfSeats > 1) "people" else "person"}")
        }
        return tokens.joinToString("\n • ")
    }
}