package com.buuzz.DonorConnectionApp.ui.registerlogin

import androidx.annotation.Keep

sealed class SignUpValidationEvent {
    @Keep
    data class Success(val response: String?) : SignUpValidationEvent()

    @Keep
    data class Failure(val errorMessage: String) : SignUpValidationEvent()

    @Keep
    data class Loading(val message: String) : SignUpValidationEvent()
    object UpdateData : SignUpValidationEvent()
    object ShowValidationError : SignUpValidationEvent()
}