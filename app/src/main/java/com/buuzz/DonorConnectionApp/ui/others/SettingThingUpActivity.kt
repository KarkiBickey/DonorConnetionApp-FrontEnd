package com.buuzz.DonorConnectionApp.ui.others

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.buuzz.DonorConnectionApp.ui.MainActivity
import com.buuzz.DonorConnectionApp.R
import com.buuzz.DonorConnectionApp.databinding.ActivitySettingThingUpBinding
import com.buuzz.DonorConnectionApp.ui.base.BaseActivity
import com.buuzz.DonorConnectionApp.ui.registerlogin.RegisterLoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingThingUpActivity : BaseActivity() {
    private lateinit var binding: ActivitySettingThingUpBinding
    private val viewModel: SettingThingsUpViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivitySettingThingUpBinding.inflate(layoutInflater)
        observeFetchingFinishedOrNot()
        binding.settingThingsUpActLoadingProgressBar.setAnimation(R.raw.loading)
        viewModel.fetchEverythingNeeded()
        setContentView(binding.root)
    }

    private fun observeFetchingFinishedOrNot() {
        viewModel.fetchCompleteLiveData.observe(this) { fetchComplete ->
            if (fetchComplete) {
                lifecycleScope.launch {
                    if (viewModel.isUserLoggedIn) {
                        delay(5000)
                        startActivityFinishingCurrentOne(MainActivity::class.java)
                    } else {
                        startActivityFinishingCurrentOne(RegisterLoginActivity::class.java)
                    }
                }

            }
        }
    }

    private fun <T> startActivityFinishingCurrentOne(className: Class<T>) {
        startActivity(Intent(this, className))
        finishAffinity()
    }
}