package com.dashxdemo.app.feature.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dashxdemo.app.R
import com.dashxdemo.app.binding.viewBinding
import com.dashxdemo.app.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private val binding: ActivityLoginBinding by viewBinding()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}