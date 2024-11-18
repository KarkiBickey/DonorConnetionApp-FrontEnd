package com.buuzz.DonorConnectionApp.ui.registerlogin

import android.os.Bundle
import com.buuzz.DonorConnectionApp.databinding.ActivityRegisterLoginBinding
import com.buuzz.DonorConnectionApp.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterLoginActivity : BaseActivity() {
    private lateinit var binding: ActivityRegisterLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}