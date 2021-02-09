package com.ikea.shoppable.persistence.db

import androidx.room.TypeConverter
import com.ikea.shoppable.model.Product
import java.util.*

class Converters {

    @TypeConverter
    fun fromString(type: Product.Companion.Type): String {
        return type.name.toLowerCase(Locale.getDefault())
    }

    @TypeConverter
    fun fromType(type: String): Product.Companion.Type {
        return Product.Companion.Type.valueOf(type.toUpperCase(Locale.getDefault()))
    }
}