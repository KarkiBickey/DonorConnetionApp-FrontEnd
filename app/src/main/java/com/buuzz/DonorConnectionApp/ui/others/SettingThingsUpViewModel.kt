package com.buuzz.DonorConnectionApp.ui.others

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buuzz.DonorConnectionApp.data.local.DataStoreHelper
import com.buuzz.DonorConnectionApp.data.respository.ContentRepository
import com.buuzz.DonorConnectionApp.data.respository.UserRepository
import com.buuzz.DonorConnectionApp.utils.helpers.AppData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.LinkedList
import java.util.Queue
import javax.inject.Inject

@HiltViewModel
class SettingThingsUpViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val dataStoreHelper: DataStoreHelper,
    private val contentRepository: ContentRepository
) : ViewModel() {
    private val _fetchCompleteLiveData = MutableLiveData<Boolean>()
    val fetchCompleteLiveData: LiveData<Boolean> get() = _fetchCompleteLiveData
    var isUserLoggedIn = false


    private val operationQueue = getRequiredOperationQueue()
    private var anyOperationFailed = false


    fun fetchEverythingNeeded() {
        viewModelScope.launch {
            while (!operationQueue.isEmpty()) {
                operationQueue.remove().invoke()
                if (anyOperationFailed) {
                    break
                }
            }
            if (!anyOperationFailed) {
                _fetchCompleteLiveData.value = true
            }
        }
    }

    private fun getRequiredOperationQueue(): Queue<suspend () -> Unit> {
        return LinkedList<suspend () -> Unit>().apply {
            add { fetchUserDetails() }
            add { checkUserLoggedIn() }
            add { getInitContents() }
        }
    }

    private suspend fun getInitContents() {
        contentRepository.getInitContents()
    }

    private suspend fun checkUserLoggedIn() {
        isUserLoggedIn = dataStoreHelper.readBooleanFromDatastore(AppData.IS_USER_LOGGED_IN)
    }

    private suspend fun fetchUserDetails() {
        userRepository.fetchUserDetails()
    }

}