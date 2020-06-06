package com.example.mytask

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.mytask.databinding.ActivityMain2Binding
import com.squareup.picasso.Picasso

class Main2Activity : AppCompatActivity() {

    private lateinit var binding: ActivityMain2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        binding= DataBindingUtil.setContentView(this,R.layout.activity_main2)

        binding.tt.text="Title: "+intent.getStringExtra("t")
        binding.rr.text="Date: "+intent.getStringExtra("r")
        binding.dd.text="Description: \n"+intent.getStringExtra("d")


        Picasso.get().load("https://image.tmdb.org/t/p/w500/"+intent.getStringExtra("i")).into(binding.dimg)
    }
}
