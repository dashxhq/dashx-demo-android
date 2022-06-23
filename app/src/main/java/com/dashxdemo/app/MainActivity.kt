package com.dashxdemo.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dashxdemo.app.binding.viewBinding
import com.dashxdemo.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}