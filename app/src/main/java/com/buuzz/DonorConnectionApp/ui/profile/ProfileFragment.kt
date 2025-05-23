package com.buuzz.DonorConnectionApp.ui.profile

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.buuzz.DonorConnectionApp.R
import com.buuzz.DonorConnectionApp.data.model.response.UserDetails
import com.buuzz.DonorConnectionApp.databinding.FragmentProfileBinding
import com.buuzz.DonorConnectionApp.ui.base.BaseFragment
import com.buuzz.DonorConnectionApp.ui.profile.ProfileItemProvider.getProfileMoreItems
import com.buuzz.DonorConnectionApp.utils.apihelper.safeapicall.ApiCallListener
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : BaseFragment() {
    private lateinit var binding: FragmentProfileBinding

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        setView()
        setUpMoreView()
        return binding.root
    }

    private fun setView() {
        viewModel.getUserDetails {
            val user = Gson().fromJson(it, UserDetails::class.java)
            user?.let {
                binding.apply {
                    username.text = it.name
                    userNumber.text = it.phone_number
                    Glide.with(binding.root.context).load(it.image)
                        .placeholder(R.drawable.person_placeholder)
                        .into(binding.userImage)
                }
            }

        }
    }

    private fun setUpMoreView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        val moreAdapter =
            ProfileAdapter(
                getProfileMoreItems(requireContext()),
                activity ?: Activity(),
                onItemsClicked = { onItemsClicked(it) })
        binding.recyclerView.adapter = moreAdapter
        binding.moreLyt.isVisible = false
    }

    private fun onItemsClicked(title: String) {
        lifecycleScope.launch {
            when (title) {
                getString(R.string.logout) -> {
                    logOut()
                }
//                getString(R.string.about_us) -> {
//                    aboutUs()
//                }

                else -> {}
            }
        }
    }

    private fun logOut() {
        viewModel.logOut(object : ApiCallListener {
            override fun onSuccess(response: String?) {
                showTopSnackBar(binding.root, response ?: "Logged Out Successfully!!")
                requireActivity().finish()
            }

            override fun onError(errorMessage: String?) {
                showTopSnackBar(binding.root, errorMessage ?: "Error Logging Out !!")
                requireActivity().finish()
            }

        })
    }
    //    private fun aboutUs(){
//        findNavController().navigate(R.layout.activity_about_us)
//    }

}