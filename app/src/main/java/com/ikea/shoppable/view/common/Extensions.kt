package com.ikea.shoppable.view.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.ikea.shoppable.R


fun Double.format(digits: Int) = "%.${digits}f".format(this)


fun ImageView.loadUrlToCircle(url: String) {
    Glide.with(context).load(url).placeholder(R.drawable.broken_image_black)
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).apply(
            RequestOptions.circleCropTransform()
        ).into(this)
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}