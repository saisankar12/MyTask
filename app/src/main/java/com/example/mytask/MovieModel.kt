package com.example.mytask

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

data class MovieModel(val m_poster:String, val m_title:String, val m_des:String, val m_res:String){
    companion object {

        @JvmStatic
        @BindingAdapter("imageUrl")
        fun loadImage(view: ImageView, url: String) { // This methods should not have any return type, = declaration would make it return that object declaration.
            Glide.with(view.context).load("http://image.tmdb.org/t/p/w500/${url}").into(view)
        }
    }
}