package com.ikea.shoppable.persistence.db

import androidx.room.TypeConverter
import com.ikea.shoppable.model.Product

class Converters {

    @TypeConverter
    fun fromString(type: Product.Companion.Type): String {
        return type.name.toLowerCase()
    }

    @TypeConverter
    fun fromType(type: String): Product.Companion.Type {
        return Product.Companion.Type.valueOf(type.toUpperCase())
    }
}