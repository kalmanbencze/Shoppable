package com.ikea.shoppable.persistence.db.util

import android.content.Context
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import com.ikea.shoppable.model.Product
import java.io.InputStream
import java.io.InputStreamReader

object DBInputParser {
    fun readProducts(context: Context, filePath: String): Array<Product> {

        val gson = Gson()
        val inputStream: InputStream = context.resources.assets.open(filePath)
        val reader = JsonReader(InputStreamReader(inputStream))
        val input = gson.fromJson<InputModel>(reader, InputModel::class.java)
        return input.products
    }
}