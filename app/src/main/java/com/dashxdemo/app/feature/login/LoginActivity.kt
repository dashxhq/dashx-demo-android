package com.dashxdemo.app.feature.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dashxdemo.app.R
import com.dashxdemo.app.databinding.ActivityLoginBinding
import com.dashxdemo.app.feature.home.HomeActivity
import com.dashxdemo.app.pref.AppPref

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!AppPref(this).getUserToken().isNullOrEmpty()) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
