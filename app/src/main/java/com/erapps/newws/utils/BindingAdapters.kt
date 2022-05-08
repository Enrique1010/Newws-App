package com.erapps.newws.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import com.erapps.newws.R

class BindingAdaptersUtils {
    companion object{
        @BindingAdapter("setImageUrl")
        @JvmStatic
        fun setImage(view: ImageView, url: String?){
            view.load(url) {
                error(R.drawable.ic_error_placeholder)
                crossfade(true)
                placeholder(R.drawable.ic_error_placeholder)
            }
        }
    }
}