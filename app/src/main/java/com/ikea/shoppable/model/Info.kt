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
)