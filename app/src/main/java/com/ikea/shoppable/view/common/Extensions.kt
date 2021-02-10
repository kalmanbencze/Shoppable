package com.ikea.shoppable.view.common

import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.ikea.shoppable.R
import java.text.DecimalFormat


fun Double.formatTo2Decimals(): String {
    var dec = DecimalFormat("#,###.00")
    if (toInt().toDouble() == this) {
        dec = DecimalFormat("#,###")
    }
    return dec.format(this)
}


fun ImageView.loadUrlToCircle(url: String) {
    Glide.with(context).load(url)
        .error(R.drawable.broken_image_black)
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).apply(
            RequestOptions.circleCropTransform()
        ).into(this)
}

fun ImageView.loadUrl(url: String) {
    Glide.with(context).load(url)
        .error(R.drawable.broken_image_black)
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).into(this)
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}


fun View.dpToPx(dp: Float): Float {
    return dp * resources.displayMetrics.density
}

fun Fragment.dpToPx(dp: Float): Float {
    return dp * resources.displayMetrics.density
}

fun View.pxToDp(px: Int): Int {
    val displayMetrics = resources.displayMetrics
    return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
}