package com.buuzz.DonorConnectionApp.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buuzz.DonorConnectionApp.data.respository.UserRepository
import com.buuzz.DonorConnectionApp.utils.apihelper.safeapicall.ApiCallListener
import com.buuzz.DonorConnectionApp.utils.apihelper.safeapicall.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    fun logOut(callback: ApiCallListener) {
        viewModelScope.launch {
            when (val response = userRepository.logOut()) {
                is Resource.Failure -> {
                    callback.onError(response.errorMsg)
                    userRepository.deleteAllData()
                }

                is Resource.Success -> {
                    userRepository.deleteAllData()
                    callback.onSuccess(response.value.message)
                }
            }
        }
    }


    fun getUserDetails(onSuccess :(String) ->Unit){
        viewModelScope.launch {
            onSuccess(userRepository.getUserDetails())
        }
    }
}