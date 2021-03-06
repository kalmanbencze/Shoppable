package com.ikea.shoppable.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "Products")
class Product(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "name")
    val name: String,
    @Embedded
    val price: Price,
    @Embedded
    val info: Info,
    @ColumnInfo(name = "type")
    val type: Type,
    @ColumnInfo(name = "imageUrl")
    val imageUrl: String
) {

    companion object {
        enum class Type {
            @SerializedName("chair")
            CHAIR,

            @SerializedName("couch")
            COUCH
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is Product && other.id == id
    }

    override fun hashCode(): Int {
        return id.hashCode() * name.hashCode() * price.hashCode() * type.hashCode()
    }
}