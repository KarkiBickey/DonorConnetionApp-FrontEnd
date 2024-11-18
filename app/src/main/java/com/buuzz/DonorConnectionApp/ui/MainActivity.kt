package com.buuzz.DonorConnectionApp.ui

import android.content.Intent
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.buuzz.DonorConnectionApp.R
import com.buuzz.DonorConnectionApp.databinding.ActivityMainBinding
import com.buuzz.DonorConnectionApp.ui.base.BaseActivity
import com.buuzz.DonorConnectionApp.ui.post.create.CreatePostActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.bottomNavigationView.menu.clear()
        binding.bottomNavigationView.inflateMenu(R.menu.main_menu)
        supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.let {
            binding.bottomNavigationView.setupWithNavController(
                it.findNavController()
            )
        }
        binding.fab.setOnClickListener {
            startActivity(Intent(this, CreatePostActivity::class.java))
        }
        setContentView(binding.root)
    }
}