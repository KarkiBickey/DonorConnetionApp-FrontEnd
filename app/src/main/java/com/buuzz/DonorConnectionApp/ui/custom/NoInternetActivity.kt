package com.buuzz.DonorConnectionApp.ui.custom

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.buuzz.DonorConnectionApp.databinding.NoInternetLytBinding
import com.buuzz.DonorConnectionApp.utils.apihelper.InternetConnectionChecker

class NoInternetActivity : AppCompatActivity() {
    private lateinit var binding: NoInternetLytBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = NoInternetLytBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.swipeRefreshLayout.setOnRefreshListener {
            if (InternetConnectionChecker.isOnline(this)) {
                binding.swipeRefreshLayout.isRefreshing = false
                this.finish()
            } else {
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
    }
}